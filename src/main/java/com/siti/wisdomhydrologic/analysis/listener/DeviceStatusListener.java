package com.siti.wisdomhydrologic.analysis.listener;

import com.rabbitmq.client.Channel;
import com.siti.wisdomhydrologic.analysis.entity.DeviceUploadData;
import com.siti.wisdomhydrologic.analysis.mapper.DeviceUploadDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@Transactional
public class DeviceStatusListener{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    DeviceUploadDataMapper deviceUploadDataMapper;

    @RabbitListener(queues = "${spring.devstatusquene}", containerFactory = "secondRabbitListenerConnectionFactory")
    public void deviceStatusHandler(DeviceUploadData deviceUploadData, Channel channel, Message message){
        try {
            if( deviceUploadData != null){
                deviceUploadDataMapper.insert(deviceUploadData);
            }
            else {
                logger.error("deviceUploadData 为空!");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
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
}
