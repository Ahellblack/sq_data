package com.siti.wisdomhydrologic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Nullable;


/**
 * Created by DC on 2019/6/12.
 *
 * @data ${DATA}-14:58
 */
@Configuration
public class RabbitMQConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String WH_EXCHANGE = "exchange_wh";

    public static final String QUEUE_REAL = "wh_real";

    public static final String QUEUE_DAY = "wh_day";

    public static final String QUEUE_HOUR = "wh_hour";

    public static final String QUEUE_TSDB = "wh_tsdb";
//
//    /**
//     * 针对消费者配置
//     * 1. 设置交换机类型
//     * 2. 将队列绑定到交换机
//     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
//     HeadersExchange ：通过添加属性key-value匹配
//     DirectExchange:按照routingkey分发到指定队列
//     TopicExchange:多关键字匹配
//     */
//    @Bean
//    public DirectExchange defaultExchange() {
//        return new DirectExchange(WH_EXCHANGE,true,false);
//    }
//
//    @Bean
//    public Queue queuReal() {
//        return new Queue(QUEUE_REAL, true); //队列持久
//    }
//
//    @Bean
//    public Queue queueTSDB() {
//        return new Queue(QUEUE_TSDB, true); //队列持久
//    }
//
//    @Bean
//    public Queue queueHour() {
//        return new Queue(QUEUE_HOUR, true); //队列持久
//    }
//
//    @Bean
//    public Queue queueDay()
//    {
//        return new Queue(QUEUE_DAY, true); //队列持久
//    }
//
//    @Bean
//    public Binding bindingReal() {
//        return BindingBuilder.bind(queuReal()).to(defaultExchange()).with(RabbitMQConfig.QUEUE_REAL);
//    }
//
//    @Bean
//    public Binding bindingDay() {
//        return BindingBuilder.bind(queueDay()).to(defaultExchange()).with(RabbitMQConfig.QUEUE_DAY);
//    }
//
//    @Bean
//    public Binding bindingTSDB() {
//        return BindingBuilder.bind(queueTSDB()).to(defaultExchange()).with(RabbitMQConfig.QUEUE_TSDB);
//    }
//
//    @Bean
//    public Binding bindingHour() {
//        return BindingBuilder.bind(queueHour()).to(defaultExchange()).with(RabbitMQConfig.QUEUE_HOUR);
//    }

    @Primary
    @Bean(name="firstConnectionFactory")
    public ConnectionFactory firstConnectionFactory(
            @Value("${spring.rabbitmq.first.host}") String host,
            @Value("${spring.rabbitmq.first.port}") int port,
            @Value("${spring.rabbitmq.first.username}") String username,
            @Value("${spring.rabbitmq.first.password}") String password,
            @Value("${spring.rabbitmq.first.virtual-host}") String virtualHost)  {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }

    @Bean(name="secondConnectionFactory")
    public ConnectionFactory secondConnectionFactory(
            @Value("${spring.rabbitmq.second.host}") String host,
            @Value("${spring.rabbitmq.second.port}") int port,
            @Value("${spring.rabbitmq.second.username}") String username,
            @Value("${spring.rabbitmq.second.password}") String password,
            @Value("${spring.rabbitmq.second.virtual-host}") String virtualHost)  {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }

    @Bean(name="firstRabbitListenerConnectionFactory")
    public SimpleRabbitListenerContainerFactory firstRabbitListenerContainerFactory(
            @Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Bean(name="secondRabbitListenerConnectionFactory")
    public SimpleRabbitListenerContainerFactory secondRabbitListenerContainerFactory(
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    @Bean(name="firstRabbitTemplate")
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate firstRabbitTemplate(@Qualifier("firstConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setEncoding("UTF-8");
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(msgReturnCallback);
        rabbitTemplate.setConfirmCallback(msgConfirmCallback);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        return rabbitTemplate;
    }

    @Bean(name="secondRabbitTemplate")
    public RabbitTemplate secondRabbitTemplate(@Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setEncoding("UTF-8");
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(msgReturnCallback);
        rabbitTemplate.setConfirmCallback(msgConfirmCallback);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        return rabbitTemplate;
    }

    /**
     * i->replyCode
     * s->replyText
     * s1->exchange
     * s2->routingKey
     * **/
    //消息从交换器发送到队列失败时触发
    RabbitTemplate.ReturnCallback msgReturnCallback=new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int i, String s, String s1, String s2) {
            logger.info("消息：{}，错误码：{}，失败原因：{}，交换器：{}，路由key：{}",message,i,s,s1,s2);
        }
    };

    //消息发送到交换器时触发
    RabbitTemplate.ConfirmCallback msgConfirmCallback=new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(@Nullable  CorrelationData correlationData, boolean b, String s) {
            if(b){
                logger.info("消息{}发送exchange成功",correlationData.getId());
            }else{
                logger.info("消息发送到exchange失败，原因：{}",s);
            }
        }
    };
}
