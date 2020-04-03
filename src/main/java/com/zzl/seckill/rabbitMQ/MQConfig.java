package com.zzl.seckill.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Zzl
 * @Date: 13:08 2020/4/3
 * @Version 1.0
 **/
@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String HEADER_QUEUE = "header.queue";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADERS_EXCHANGE = "headersExchange";

    //参考资料： https://www.jianshu.com/p/469f4608ce5d

    /**
     * Direct模式  交换机Exchange
     */
    @Bean
    public Queue queueMiaosha(){
        //队列需要名字
        return new Queue(MIAOSHA_QUEUE, true);
    }

    /**
     * Direct模式  交换机Exchange
     */
    @Bean
    public Queue queue(){
        //队列需要名字
        return new Queue(QUEUE, true);
    }

    /**
     * Topic模式  主题交换机<br>
     *
     * 队列<br> 交换机<br> routingKey 绑定 <br>
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1, true);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2, true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1(){
        //一个队列也可以绑定多个键
        //TODO: topic.Key1 可以接受 topic.Key2，未解决
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.Key1");
    }
    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }

    /**
     * Fanout模式   扇形交换机
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Binding fanoutBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /**
     * Header模式
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Queue headerQueue1(){
        return new Queue(HEADER_QUEUE, true);
    }
    @Bean
    public Binding headerBinding(){
        Map<String, Object> map = new HashMap<>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        //TODO: 出现ERROR
        return BindingBuilder.bind(headerQueue1()).to(headersExchange()).whereAll(map).match();
    }
}
