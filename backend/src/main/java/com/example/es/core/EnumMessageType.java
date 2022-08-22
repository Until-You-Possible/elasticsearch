package com.example.es.core;

public enum EnumMessageType {

    ES_STATUS_DOC_DEL("Deleted"),
    BEEN_DELETED("doc has been deleted successfully"),
    NOT_FOUND_DOC("Not Found the relevant doc");

    private final String name;

    EnumMessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
