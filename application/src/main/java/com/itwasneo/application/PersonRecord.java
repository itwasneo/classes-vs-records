package com.itwasneo.application;

public record PersonRecord(
        String name,
        int age) {
    public PersonRecord {
        if (age > 20) {
            throw new IllegalArgumentException();
        }
    }
}
