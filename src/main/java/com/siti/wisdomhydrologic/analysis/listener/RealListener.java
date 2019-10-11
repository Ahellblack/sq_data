package com.siti.wisdomhydrologic.analysis.listener;

import com.rabbitmq.client.Channel;
import com.siti.wisdomhydrologic.analysis.pipeline.PipelineValve;
import com.siti.wisdomhydrologic.analysis.pipeline.valve.*;
import com.siti.wisdomhydrologic.config.ColorsExecutor;
import com.siti.wisdomhydrologic.config.RabbitMQConfig;
import com.siti.wisdomhydrologic.analysis.entity.Real;
import com.siti.wisdomhydrologic.analysis.mapper.*;
import com.siti.wisdomhydrologic.analysis.vo.RealVo;
import com.siti.wisdomhydrologic.util.LocalDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by DC on 2019/6/12.
 *
 * @data ${DATA}-15:23
 */
@Component
@Transactional
public class RealListener {

    @Resource
    RealMapper realMapper;
    @Resource
    AbnormalDetailMapper abnormalDetailMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AtomicInteger maxBatch = new AtomicInteger(0);
    private AtomicBoolean flag = new AtomicBoolean(false);
    private AtomicInteger sumSize = new AtomicInteger(0);
    private BlockingQueue<List<RealVo>> receiver;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REAL)
    @RabbitHandler
    public void realProcess(List<RealVo> RealVo, Channel channel, Message message) {
        try {
            if (RealVo.size() > 0) {
                calPackage(RealVo);
            } else {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            //----------------------后面可以优化---------------------
//            try {
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
        }
    }

    /**
     * 判断是否丢包记录日志
     */
    private void calPackage(List<RealVo> RealVoList) throws Exception {
        RealVo vo = RealVoList.get(0);
        //------------------------real数据入库-------------------
        splitList(RealVoList, 1000);
        //-------------------------触发一次-----------------------
        if (flag.compareAndSet(false, true)) {
            PipelineValve finalValvo=new PipelineValve();
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
            receiver = new LinkedBlockingQueue(5);
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
        receiver.put(RealVoList);
        logger.info("real_queue消费者获取day数据...总包数:{},当前包数:{},总条数:{},条数;{},状态:{}", maxBatch.get(),
                currentbatch, sumSize.get(), currentsize, vo.getStatus());
    }

    /**
     * 触发消费任务
     */
    private void multiProcess(PipelineValve finalValvo) {
        ColorsExecutor colors = new ColorsExecutor();
        colors.init();
        ThreadPoolExecutor es = colors.getCustomThreadPoolExecutor();
        Runnable fetchTask = () -> {
            List<RealVo> voList = receiver.poll();
            if (voList != null) {
                finalValvo.doInterceptor(voList);
            }
        };
        while (true) {
            if (es.getQueue().size() < 3) {
                es.execute(fetchTask);
            }
            if (receiver.isEmpty()) {
                es.shutdown();
                flag.compareAndSet(true, false);
                break;
            }
        }
    }
    //--------------------入库-------------------
    public boolean splitList(List arrayList, int size) {
        try {
            int all = arrayList.size();
            int cycle = all % size == 0 ? all / size : (all / size + 1);
            IntStream.range(0, cycle).forEach(e -> {
                realMapper.insertReal(arrayList.subList(e * size, (e + 1) * size > all ? all : size * (e + 1)));
            });
        }catch (Exception e){
            return false;
        }
        return true;
    }
}




