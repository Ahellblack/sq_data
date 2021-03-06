package com.siti.wisdomhydrologic.analysis.listener;

import com.rabbitmq.client.Channel;
import com.siti.wisdomhydrologic.analysis.entity.Real;
import com.siti.wisdomhydrologic.analysis.pipeline.valve.*;
import com.siti.wisdomhydrologic.config.ColorsExecutor;
import com.siti.wisdomhydrologic.config.RabbitMQConfig;
import com.siti.wisdomhydrologic.analysis.mapper.*;
import com.siti.wisdomhydrologic.analysis.pipeline.PipelineValve;
import com.siti.wisdomhydrologic.analysis.service.impl.DayDataServiceImpl;
import com.siti.wisdomhydrologic.analysis.vo.DayVo;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
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
public class HourListener {

    @Resource
    private DayDataServiceImpl dayDataService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AtomicInteger maxBatch = new AtomicInteger(0);
    private AtomicBoolean flag = new AtomicBoolean(false);
    private AtomicInteger sumSize = new AtomicInteger(0);
    private BlockingQueue<List<DayVo>> receiver;
    @RabbitListener(queues = RabbitMQConfig.QUEUE_HOUR, containerFactory = "firstRabbitListenerConnectionFactory")
    @RabbitHandler
    public void dayprocess(List<DayVo> vo, Channel channel, Message message) {
        try {
            if (vo.size() > 0) {
                calPackage(vo, channel, message);
            } else {
                //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                logger.error("hour queue:{} 数据为空！", LocalDateTime.now().toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
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
    private void calPackage(List<DayVo> List, Channel channel, Message message) throws Exception {
        DayVo vo = List.get(0);
        splitList(List, 100);
        if (flag.compareAndSet(false, true)) {
            PipelineValve finalValvo=new PipelineValve();
//            finalValvo.setHandler(new HourAirPressValve());
//            finalValvo.setHandler(new HourAirTempratrueValve());
//            finalValvo.setHandler(new HourFlowVelocitylValve());
//            finalValvo.setHandler(new HourlWaterLevelValve());
//            finalValvo.setHandler(new HourTideValve());
//            finalValvo.setHandler(new HourWindDirectionValve());
//            finalValvo.setHandler(new HourWindSpeedValve());

            //HourRegressionValve
            new Thread(() -> {
                multiProcess(finalValvo);
            }).start();
            receiver = new LinkedBlockingQueue(5);
            maxBatch.set(vo.getMaxBatch());
            sumSize.set(vo.getSumSize());
            logger.info("hour开始处理...");
        }
        int currentsize = vo.getCurrentSize();
        int currentbatch = vo.getCurrentBatch();
        int stastus = vo.getStatus();
        if (stastus == 1) {
            if (sumSize.get() == currentsize && maxBatch.get() == currentbatch) {
                logger.info("hour消息成功消费完成无丢包！");
            }
        }
        logger.info("List size="+List.size());
        receiver.put(List);
        logger.info("Hour消费者----总包数:{},当前包数:{},总条数:{},条数;{},状态:{}", maxBatch.get(),
                currentbatch, sumSize.get(), currentsize, vo.getStatus());
    }

    /**
     * 触发一次消费任务
     */
    private void multiProcess(PipelineValve finalValvo) {

        ColorsExecutor colors = new ColorsExecutor();
        colors.init();
        ThreadPoolExecutor es = colors.getCustomThreadPoolExecutor();
        Runnable fetchTask = () -> {
            List<DayVo> voList = receiver.poll();
            if (voList != null) {
              /*  //-------------------一天内的数据-----------------
                List<Real> realVos = abnormalDetailMapper.selectHourPeriod(LocalDateUtil
                        .dateToLocalDateTime(voList.get(0).getTime()).plusHours(-1)
                        .format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),LocalDateUtil
                        .dateToLocalDateTime(voList.get(0).getTime())
                        .format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                Map<String, Real> compareMap=new HashMap<>();
                if (realVos.size() > 0) {
                    compareMap = realVos.stream()
                            .collect( Collectors.toMap((real)->real.getTime().toString()+","+real.getSensorCode()
                                    ,account -> account));
                }*/
                finalValvo.doInterceptor(voList);
            }
        };
        while (true) {
            if (es.getQueue().size() < 20) {
                es.execute(fetchTask);
            }
            if (receiver.isEmpty()) {
                es.shutdown();
                flag.compareAndSet(true, false);
                break;
            }
        }
    }

    public void splitList(List arrayList, int size) {
        int all = arrayList.size();
        int cycle = all % size == 0 ? all / size : (all / size + 1);
        IntStream.range(0, cycle).forEach(e -> {
            dayDataService.addHourData(arrayList.subList(e * size, (e + 1) * size > all ? all : size * (e + 1)));
        });
    }
}




