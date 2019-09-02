/*
package com.siti.wisdomhydrologic.realmessageprocess.listener;

import com.rabbitmq.client.Channel;
import com.siti.wisdomhydrologic.config.RabbitMQConfig;
import com.siti.wisdomhydrologic.realmessageprocess.vo.RealVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

*/
/**
 * Created by DC on 2019/6/12.
 *
 * @data ${DATA}-15:23
 *//*

@Component
public class ExceptionListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(id = "exceptionConsumer", queues = RabbitMQConfig.QUEUE_REAL, containerFactory = "exceptionContainer")
    @RabbitHandler
    public void ExceptionProcess(String jsonStr, Channel channel, Message message) {
        try {

        } catch (Exception e) {
            */
/*try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }*//*

            logger.error(e.getMessage());
        }
    }

}




*/
