package com.packet.publisher.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Outcome {

    @Id
    private String outcomeId;
    private String name;
    private String price;
    private boolean displayed;
    private boolean suspended;
}
