package com.packet.publisher.consumer;

import com.packet.publisher.configuration.RabbitConfiguration;
import com.packet.publisher.processor.PacketProcessor;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PacketConsumer {

    private final PacketProcessor packetProcessor;

    public PacketConsumer(PacketProcessor packetProcessor) {
        this.packetProcessor = packetProcessor;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitConfiguration.incomingQueueName, durable = "true", autoDelete = "false"),
                    exchange = @Exchange(
                            value = RabbitConfiguration.incomingExchangeName, type = "topic"),
                    key = RabbitConfiguration.routingKey
            ))
    public void onNewMessage(Channel channel, Message message) {
        byte[] messageContent = message.getBody();
        String jsonMessage = new String(messageContent);
        log.trace(jsonMessage);
        packetProcessor.processMessageContent(jsonMessage);
    }
}
