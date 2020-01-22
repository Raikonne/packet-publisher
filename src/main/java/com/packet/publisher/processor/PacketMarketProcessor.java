package com.packet.publisher.processor;

import com.packet.publisher.enumeration.JsonPacketProperties;
import com.packet.publisher.enumeration.OperationType;
import com.packet.publisher.model.Event;
import com.packet.publisher.model.Market;
import com.packet.publisher.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Slf4j
@Component
public class PacketMarketProcessor {

    private final EventRepository eventRepository;

    public PacketMarketProcessor(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void handleMarkets(String operation, JSONObject body) {
        String eventId = body.getString(JsonPacketProperties.EVENT_ID.getValue());
        if (operation.equals(OperationType.CREATE.getValue())) {
            checkIfEventExists(eventId).ifPresent(event -> {
                log.info("Event exists with id {}", eventId);
                Market market = buildMarket(body, eventId);
                List<Market> markets = event.getMarkets();
                if (markets.contains(market)) {
                    log.error("I found a existing market but operation is create");
                    return;
                }
                markets.add(market);
                eventRepository.save(event);
                log.info("Successfully added market {} and updated event", market);
            });
        }
        if (operation.equals(OperationType.UPDATE.getValue())) {
            checkIfEventExists(eventId).ifPresent(event -> {
                log.info("Event exists with id {}", eventId);
                List<Market> eventMarkets = event.getMarkets();
                ListIterator<Market> marketIterator = eventMarkets.listIterator();
                while (marketIterator.hasNext()) {
                    Market next = marketIterator.next();
                    if (next.getMarketId().equals(body.getString(JsonPacketProperties.MARKET_ID.getValue()))) {
                        marketIterator.set(buildMarket(body, eventId));
                        event.setMarkets(eventMarkets);
                        eventRepository.save(event);
                        log.error("Successfully updated existing market for operation update");
                        break;
                    }
                }
            });
        }
    }

    private Market buildMarket(JSONObject body, String eventId) {
        return new Market(
                body.getString(JsonPacketProperties.MARKET_ID.getValue()),
                eventId,
                body.getString(JsonPacketProperties.NAME.getValue()),
                body.getBoolean(JsonPacketProperties.DISPLAYED.getValue()),
                body.getBoolean(JsonPacketProperties.SUSPENDED.getValue()),
                new ArrayList<>()
        );
    }

    private Optional<Event> checkIfEventExists(String eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            log.error("Unable to update record that doesn't exists");
            return Optional.empty();
        }
        return optionalEvent;
    }
}
