package com.packet.publisher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("Event")
public class Event {

    @Id
    private String eventId;
    private String category;
    private String subCategory;
    private String name;
    private Long startTime;
    private boolean displayed;
    private boolean suspended;
    private List<Market> markets;

}
