package com.siti.wisdomhydrologic.analysis.listener;

import com.rabbitmq.client.Channel;
import com.siti.wisdomhydrologic.analysis.pipeline.PipelineValve;
import com.siti.wisdomhydrologic.analysis.pipeline.valve.*;
import com.siti.wisdomhydrologic.config.ColorsExecutor;
import com.siti.wisdomhydrologic.config.RabbitMQConfig;
import com.siti.wisdomhydrologic.analysis.mapper.*;
import com.siti.wisdomhydrologic.analysis.vo.RealVo;
import com.siti.wisdomhydrologic.util.DateTransform;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Created by DC on 2019/6/12.
 *
 * @data ${DATA}-15:23
 */
@Component
@Transactional
public class RealListener {

    private final String TABLENAME = "history_real_sensor_data_";

    @Resource
    RealMapper realMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AtomicInteger maxBatch = new AtomicInteger(0);
    private AtomicBoolean flag = new AtomicBoolean(false);
    private AtomicInteger sumSize = new AtomicInteger(0);
    private BlockingQueue<List<RealVo>> receiver;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REAL, containerFactory = "firstRabbitListenerConnectionFactory")
    @RabbitHandler
    public void realProcess(List<RealVo> realVos, Channel channel, Message message) {
        //logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "线程ID:" + Thread.currentThread().getId() + realVos.get(0).toString());
        try {
            if (realVos.size() > 0) {
                calPackage(realVos);
            } else {
              //  channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                logger.error("real queue:{} 数据为空！", LocalDateTime.now().toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            //----------------------后面可以优化---------------------
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 判断是否丢包记录日志
     */
    private void calPackage(List<RealVo> RealVoList) throws Exception {
        RealVo vo = RealVoList.get(0);
        //-------------------------触发一次-----------------------
        if (flag.compareAndSet(false, true)) {
            PipelineValve finalValvo = new PipelineValve();
            finalValvo.setHandler(new RealWaterlevelValve());
            finalValvo.setHandler(new RealTidelValve());
            finalValvo.setHandler(new RealWindSpeedValve());
            finalValvo.setHandler(new RealWindDirectionValve());
            finalValvo.setHandler(new RealAirPressValve());
            finalValvo.setHandler(new RealAirTemperatureValve());
            finalValvo.setHandler(new RealFlowVelocityValve());
            // special
            finalValvo.setHandler(new RealRainfallValve());
            //------------------------初始化消费端-----------------------
            new Thread(() -> {
                multiProcess(finalValvo);
            }).start();
            //---------------------------初始化容器--------------
            receiver = new LinkedBlockingQueue(40);
            maxBatch.set(vo.getMaxBatch());
            sumSize.set(vo.getSumSize());
            logger.info("RealListener ColorsExecurots Initial...");
        }
        int currentsize = vo.getCurrentSize();
        int currentbatch = vo.getCurrentBatch();
        int stastus = vo.getStatus();
        if (stastus == 1) {
            if (sumSize.get() == currentsize && maxBatch.get() == currentbatch) {
                logger.info("real消息成功消费完成无丢包！");
            }
        }
        //----------------------------往容器中放入信息（会阻塞）-----------------
        logger.info("put receiver size {}",receiver.size());
        receiver.put(RealVoList);
        logger.info("real_queue消费者获取day数据...总包数:{},当前包数:{},总条数:{},条数;{},状态:{}", maxBatch.get(), currentbatch, sumSize.get(), currentsize, vo.getStatus());
    }

    /**
     * 触发消费任务
     */
    private void multiProcess(PipelineValve finalValvo) {
        ColorsExecutor colors = new ColorsExecutor();
        colors.init();
        ThreadPoolExecutor es = colors.getCustomThreadPoolExecutor();
        Runnable fetchTask = () -> {
            logger.info("receiver size{}",receiver.size());
            List<RealVo> voList = receiver.poll();
            if (voList != null) {
                //------------------------real数据入库-------------------
                splitList(voList, 1000);
                logger.info("voList is not empty, in doInterceptor！");
                finalValvo.doInterceptor(voList);
            }else{
                logger.info("voList is empty！");
            }
        };
        while (true) {
            es.execute(fetchTask);
            if(es.getQueue().size()==20){
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (receiver.isEmpty()&&es.getQueue().size()<1) {
                logger.info("receiver is empty！");
                flag.compareAndSet(true, false);
                break;
            }
        }
    }
//    private void multiProcess(PipelineValve finalValvo) {
//        ColorsExecutor colors = new ColorsExecutor();
//        colors.init();
//        ThreadPoolExecutor es = colors.getCustomThreadPoolExecutor();
//        Runnable fetchTask = () -> {
//            List<RealVo> voList = receiver.poll();
//            if (voList != null) {
//                logger.info("voList is not empty, in doInterceptor！");
//                finalValvo.doInterceptor(voList);
//            }
//            else{
//                logger.info("voList is empty！");
//            }
//        };
//        while (true) {
//            if (es.getQueue().size() < 20) {
//                es.execute(fetchTask);
//            }
//            if (receiver.isEmpty()) {
//                logger.info("receiver is empty，es shutdown！");
//                es.shutdown();
//                flag.compareAndSet(true, false);
//                break;
//            }
//        }
//    }

    //--------------------入库-------------------
    public boolean splitList(List<RealVo> arrayList, int size) {
        try {
            int all = arrayList.size();
            int cycle = all % size == 0 ? all / size : (all / size + 1);
            IntStream.range(0, cycle).forEach(e -> {
                //判断数据归属于哪一个月

                String date = DateTransform.Date2String(arrayList.get(0).getTime(), "yyyyMM");

                String realtime = DateTransform.Date2String(arrayList.get(0).getTime(), "yyyy-MM-dd HH:mm:ss");
                String table = TABLENAME + date;

                /**
                 * 生成并插入历史表
                 * */
                realMapper.buildTable(table);
                realMapper.insertHistroy(arrayList.subList(e * size, (e + 1) * size > all ? all : size * (e + 1)), table);

                /**
                 * 更新实时表数据,更新一周内数据
                 * */
                Calendar cal = Calendar.getInstance();
                cal.setTime(DateTransform.String2Date(realtime, "yyyy-MM-dd HH:mm:ss"));
                cal.add(Calendar.DAY_OF_MONTH, -7);
                String oldWeekTime = DateTransform.Date2String(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
                /**
                 * 删除7天前的数据
                 * */
                realMapper.deleteOldTime(oldWeekTime);
                /**
                 * 插入实时数据
                 * */
                realMapper.insertReal(arrayList.subList(e * size, (e + 1) * size > all ? all : size * (e + 1)));
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}




