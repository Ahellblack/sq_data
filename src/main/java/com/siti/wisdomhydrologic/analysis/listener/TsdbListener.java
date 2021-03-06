package com.siti.wisdomhydrologic.analysis.listener;

import com.rabbitmq.client.Channel;
import com.siti.wisdomhydrologic.analysis.pipeline.PipelineValve;
import com.siti.wisdomhydrologic.config.ColorsExecutor;
import com.siti.wisdomhydrologic.config.RabbitMQConfig;
import com.siti.wisdomhydrologic.analysis.mapper.RainFallMapper;
import com.siti.wisdomhydrologic.analysis.mapper.TSDBMapper;
import com.siti.wisdomhydrologic.analysis.mapper.TideLevelMapper;
import com.siti.wisdomhydrologic.analysis.mapper.WaterLevelMapper;
import com.siti.wisdomhydrologic.analysis.service.impl.*;
import com.siti.wisdomhydrologic.analysis.vo.TSDBVo;
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
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
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
public class TsdbListener {

    @Resource
    RainFallMapper rainFallMapper;
    @Resource
    TideLevelMapper tideLevelMapper;
    @Resource
    WaterLevelMapper waterLevelMapper;
    @Resource
    private TSDBMapper tsdbMapper;

    @Resource
    private TSDBServiceImpl tsdbService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AtomicInteger maxBatch = new AtomicInteger(0);
    private AtomicBoolean flag = new AtomicBoolean(false);
    private AtomicInteger sumSize = new AtomicInteger(0);
    private BlockingQueue<List<TSDBVo>> receiver;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_TSDB, containerFactory = "firstRabbitListenerConnectionFactory")
    @RabbitHandler
    public void tsdbProcess(List<TSDBVo> vo, Channel channel, Message message) {
        try {
            if (vo.size() > 0) {
                calPackage(vo, channel, message);
            } else {
                //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                logger.error("tsdb queue:{} 数据为空！", LocalDateTime.now().toString());
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
    public void calPackage(List<TSDBVo> List, Channel channel, Message message) throws Exception {
        TSDBVo vo = List.get(0);
        if (flag.compareAndSet(false, true)) {
            PipelineValve finalValvo=new PipelineValve();
           /* finalValvo.setHandler(new TSDBWaterlevelValve());
            finalValvo.setHandler(new TSDBTidelValve());
            finalValvo.setHandler(new TSDBRainfallValve());
            finalValvo.setHandler(new TSDBAPValve());
            finalValvo.setHandler(new TSDBATValve());
            finalValvo.setHandler(new TSDBEValve());
            finalValvo.setHandler(new TSDBFVValve());
            finalValvo.setHandler(new TSDBFVYValve());
            finalValvo.setHandler(new TSDBWDValve());
            finalValvo.setHandler(new TSDBWSValve());*/
            new Thread(() -> {
                multiProcess(finalValvo);
            }).start();
            receiver = new LinkedBlockingQueue(5);
            maxBatch.set(vo.getMaxBatch());
            sumSize.set(vo.getSumSize());
            logger.info("ColorsExecurots Initial...");
        }
        int currentsize = vo.getCurrentSize();
        int currentbatch = vo.getCurrentBatch();
        int stastus = vo.getStatus();
        if (stastus == 1) {
            if (sumSize.get() == currentsize && maxBatch.get() == currentbatch) {
                logger.info("tsdb消息成功消费完成无丢包！");
            }
        }
        logger.info("List size="+List.size());
        receiver.put(List);
        splitList(List, 500);
        logger.info("tsdb_queue消费者获取数据...总包数:{},当前包数:{},总条数:{},条数;{},状态:{}", maxBatch.get(),
                currentbatch, sumSize.get(), currentsize, vo.getStatus());
    }

    /**
     * 触发一次消费任务
     */
    private void multiProcess(PipelineValve valvo) {
        ColorsExecutor colors = new ColorsExecutor();
        colors.init();
        ThreadPoolExecutor es = colors.getCustomThreadPoolExecutor();
        Runnable fetchTask = () -> {
            List<TSDBVo> voList = receiver.poll();
            if (voList != null) {
                valvo.doInterceptor(voList);
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
            tsdbService.insertTSDB(arrayList.subList(e * size, (e + 1) * size > all ? all : size * (e + 1)));
        });
    }
}




