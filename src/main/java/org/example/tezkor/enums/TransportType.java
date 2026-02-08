package org.example.tezkor.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransportType {
    PIYODA("Piyoda"),
    VELOSIPED("Velosiped"),
    ELEKTRO_SAMAKAT("Elektro Samakat"),
    ELEKTRO_SKUTER("Elektro Skuter"),
    MASHINA("Mashina"),
    MOTOTSIKL("Mototsikl");

    private final String displayName;

    TransportType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getValue() {
        return this.name(); // JSON → "PIYODA"
    }

    public String getDisplayName() {
        return displayName;
    }
}
