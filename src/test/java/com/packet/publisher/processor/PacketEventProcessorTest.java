package com.packet.publisher.processor;

import com.packet.publisher.model.Event;
import com.packet.publisher.repository.EventRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PacketEventProcessorTest {

    private PacketEventProcessor packetEventProcessor;

    @Mock
    EventRepository eventRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        packetEventProcessor = new PacketEventProcessor(eventRepository);
    }

    @Test
    public void createNewEventWhenOneDoesNotExist() {
        when(eventRepository.findById("febb5861-18e2-426d-a9c5-5cf8f897bf78")).thenReturn(Optional.empty());
        when(eventRepository.insert(any(Event.class))).thenReturn(any(Event.class));

        packetEventProcessor.handleEvents("create", buildTestJson());

        verify(eventRepository, times(1)).insert(any(Event.class));
        verify(eventRepository, times(1)).findById(anyString());
    }

    @Test
    public void doNotCreateNewEventWhenOneExists() {
        when(eventRepository.findById("febb5861-18e2-426d-a9c5-5cf8f897bf78")).thenReturn(Optional.of(new Event()));

        packetEventProcessor.handleEvents("create", buildTestJson());

        verify(eventRepository, times(0)).insert(any(Event.class));
        verify(eventRepository, times(1)).findById(anyString());
    }

    @Test
    public void updateEventWhenThereIsOneInTheDatabase() {
        when(eventRepository.findById("febb5861-18e2-426d-a9c5-5cf8f897bf78")).thenReturn(Optional.empty());

        packetEventProcessor.handleEvents("update", buildTestJson());

        verify(eventRepository, times(0)).save(any(Event.class));
        verify(eventRepository, times(1)).findById(anyString());
    }

    @Test
    public void doNotUpdateEventWhenThereIsNotOneInTheDatabase() {
        when(eventRepository.findById("febb5861-18e2-426d-a9c5-5cf8f897bf78")).thenReturn(Optional.of(new Event()));
        when(eventRepository.save(any(Event.class))).thenReturn(any(Event.class));

        packetEventProcessor.handleEvents("update", buildTestJson());

        verify(eventRepository, times(1)).save(any(Event.class));
        verify(eventRepository, times(1)).findById(anyString());
    }

    private JSONObject buildTestJson() {
        return new JSONObject()
                .put("displayed", false)
                .put("subCategory", "Sky Bet League Two")
                .put("name", "Colchester vs Notts County")
                .put("eventId", "febb5861-18e2-426d-a9c5-5cf8f897bf78")
                .put("suspended", true)
                .put("category", "Football")
                .put("startTime", "1579614502853");
    }
}
