package org.example.canteenreservationsystem.entity;

public enum Status {
    ACTIVE,
    CANCELLED;

    public String title() {
        String lower = this.name().toLowerCase();
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }
}
