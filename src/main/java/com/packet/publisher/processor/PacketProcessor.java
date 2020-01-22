package com.packet.publisher.processor;

import com.packet.publisher.enumeration.JsonPacketProperties;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PacketProcessor {

    private final PacketEventProcessor packetEventProcessor;
    private final PacketMarketProcessor packetMarketProcessor;

    public PacketProcessor(PacketEventProcessor packetEventProcessor, PacketMarketProcessor packetMarketProcessor) {
        this.packetEventProcessor = packetEventProcessor;
        this.packetMarketProcessor = packetMarketProcessor;
    }

    public void processMessageContent(String message) {
        JSONObject jsonMessage = new JSONObject(message);
        String operation = jsonMessage.getString(JsonPacketProperties.OPERATION.getValue());
        String type = jsonMessage.getString(JsonPacketProperties.TYPE.getValue());
        JSONObject body = jsonMessage.getJSONObject(JsonPacketProperties.BODY.getValue());

        if (type.equals(JsonPacketProperties.EVENT.getValue())) {
            packetEventProcessor.handleEvents(operation, body);
        }

        if (type.equals(JsonPacketProperties.MARKET.getValue())) {
            packetMarketProcessor.handleMarkets(operation, body);
        }

        if (type.equals(JsonPacketProperties.OUTCOME.getValue())) {
            //TODO: handle outcomes.
            log.info("Not processing outcomes just yet");
        }
    }
}
