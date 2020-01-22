package com.packet.publisher.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    public static final String incomingExchangeName = "packet.single.json";
    public static final String incomingQueueName = "packet.single.json.json.adapter";
    public static final String routingKey = "packet";
    public static final String errorExchangeName = "packet.single.json.store.error";
    public static final String errorQueueName = "packet.single.json.packet.publisher.error";
    public static final String errorRoutingKey = "packet.store.error";

    @Value("${rabbitmq.host.1}")
    private String rabbitHost;

    @Value("${rabbitmq.port.1}")
    private int rabbitPort;

    @Value("${rabbitmq.virtualhost}")
    private String rabbitVirtualHost;

    @Value("${rabbitmq.username}")
    private String rabbitUsername;

    @Value("${rabbitmq.password}")
    private String rabbitPassword;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setVirtualHost(rabbitVirtualHost);
        factory.setUsername(rabbitUsername);
        factory.setPassword(rabbitPassword);
        factory.setPort(rabbitPort);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Queue jsonPacketQueue() {
        return new Queue(incomingQueueName);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(errorQueueName);
    }

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange(errorExchangeName);
    }

    @Bean
    public TopicExchange destinationExchange() {
        return new TopicExchange(incomingExchangeName);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(errorQueue())
                .to(errorExchange())
                .with(errorRoutingKey);
    }

    @Bean
    public Binding jsonBinding() {
        return BindingBuilder
                .bind(jsonPacketQueue())
                .to(destinationExchange())
                .with(routingKey);
    }

    @Bean
    public MessageProperties rabbitMessageProperties() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.setContentType("application/json");
        return messageProperties;
    }
}
