package com.packet.publisher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
public class Market {

    @Id
    private String marketId;
    private String eventId;
    private String name;
    private boolean displayed;
    private boolean suspended;
    private List<Outcome> outcomes;
}
