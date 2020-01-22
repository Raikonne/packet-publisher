package com.packet.publisher.processor;

import com.packet.publisher.enumeration.JsonPacketProperties;
import com.packet.publisher.enumeration.OperationType;
import com.packet.publisher.model.Event;
import com.packet.publisher.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Component
public class PacketEventProcessor {

    private final EventRepository eventRepository;

    public PacketEventProcessor(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void handleEvents(String operation, JSONObject body) {
        if (operation.equals(OperationType.CREATE.getValue())) {
            Optional<Event> optionalEvent = eventRepository.findById(body.getString(JsonPacketProperties.EVENT_ID.getValue()));
            if (optionalEvent.isEmpty()) {
                Event event = buildEvent(body);
                eventRepository.insert(event);
                log.info("Stored new Event {}", event.toString());
                return;
            }
            log.error("Unable to store new event as there is already one under this name");
            return;
        }
        if (operation.equals(OperationType.UPDATE.getValue())) {
            Optional<Event> optionalEvent = eventRepository.findById(body.getString(JsonPacketProperties.EVENT_ID.getValue()));
            if (optionalEvent.isEmpty()) {
                log.error("Unable to update record that doesn't exists");
                return;
            }
            updateAndSaveEvent(optionalEvent.get(), body);
            return;
        }
        log.error("Illegal operation type {}", operation);
    }

    private Event buildEvent(JSONObject body) {
        return new Event(
                body.getString(JsonPacketProperties.EVENT_ID.getValue()),
                body.getString(JsonPacketProperties.CATEGORY.getValue()),
                body.getString(JsonPacketProperties.SUB_CATEGORY.getValue()),
                body.getString(JsonPacketProperties.NAME.getValue()),
                body.getLong(JsonPacketProperties.START_TIME.getValue()),
                body.getBoolean(JsonPacketProperties.DISPLAYED.getValue()),
                body.getBoolean(JsonPacketProperties.SUSPENDED.getValue()),
                new ArrayList<>()
        );
    }

    private void updateAndSaveEvent(Event event, JSONObject body) {
        event.setCategory(body.getString(JsonPacketProperties.CATEGORY.getValue()));
        event.setSubCategory(body.getString(JsonPacketProperties.SUB_CATEGORY.getValue()));
        event.setName(body.getString(JsonPacketProperties.NAME.getValue()));
        event.setStartTime(body.getLong(JsonPacketProperties.START_TIME.getValue()));
        event.setDisplayed(body.getBoolean(JsonPacketProperties.DISPLAYED.getValue()));
        event.setSuspended(body.getBoolean(JsonPacketProperties.SUSPENDED.getValue()));
        log.info("Found an existing event, about to update with new values {}", event.toString());
        eventRepository.save(event);
    }
}
