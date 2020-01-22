package com.packet.publisher.consumer;

import com.packet.publisher.processor.PacketProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PacketConsumerTest {

    private PacketConsumer packetConsumer;

    @Mock
    private PacketProcessor packetProcessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        packetConsumer = new PacketConsumer(packetProcessor);
    }

    @Test
    public void testOnNewMessageIsPassedToTheProcessorForProcessing() {
        String messageString = "test message";
        Message message = new Message(messageString.getBytes(), new MessageProperties());
        packetConsumer.onNewMessage(null, message);
        verify(packetProcessor).processMessageContent(any());
    }
}
