package com.siti.wisdomhydrologic.datepull.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.siti.wisdomhydrologic.datepull.entity.DayEntity;
import com.siti.wisdomhydrologic.datepull.service.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by DC on 2019/7/8.
 *
 * @data ${DATA}-14:09
 */
@Component
public class ConcurrentDayExecutor implements Observer {

    private static final Logger logger= LoggerFactory.getLogger(ConcurrentDayExecutor.class);

    @Resource
    DayToDBImpl dayToDB;
    private ReentrantLock lock = new ReentrantLock();

    public static  BlockingQueue<DayEntity> dayQueue = new LinkedBlockingQueue<>();

    private int threadnum = 1;

    private ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(threadnum, threadnum * 2,
            120 * 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(threadnum * 3));

    @Override
    public void execute() {
        Runnable dispatchTask = () -> {
            //lock.lock();
            DayEntity entity=dayQueue.poll();
            logger.info("此数据1{}", JSON.toJSONString(entity));

            if(entity!=null){
                List<DayEntity> entityList = Lists.newArrayList();

                entityList.add(entity);
                dayToDB.batchInsert(entityList);

            }


            //entityList.add(entity);
           // System.out.println("entityList:"+entityList.size());
            //if(entityList.size()%100==0){
                //entityList.clear();
               // System.out.println("entityList:"+entityList.size());

           // }
            // DayToDBImpl.batchInsert(entityList);
            //DayVo task = (DayVo)messageQueue.poll();
           // lock.unlock();
        };
        while (true) {
            if (taskExecutor.getQueue().size() < threadnum * 2) {
                taskExecutor.execute(dispatchTask);
            }
            if (dayQueue.isEmpty()) {
                try {
                    Thread.sleep(1000);
                    System.out.println("睡了:");

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
                if (dayQueue.isEmpty()) {
                    taskExecutor.shutdown();
                    while (true) {
                        if (taskExecutor.isTerminated()) {
                            break;
                        }
                    }
                    System.out.println("完了:");

                    //consumer.atomicBoolean.set(false);
                    //System.out.println("完了:"+consumer.atomicBoolean.get());
                    break;
                }
            }
        }
    }
}
