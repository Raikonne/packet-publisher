package com.packet.publisher.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperationType {
    CREATE("create"),
    UPDATE("update");

    private final String value;

    public String getValue() {
        return value;
    }
}
