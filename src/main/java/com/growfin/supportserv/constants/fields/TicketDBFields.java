package com.growfin.supportserv.constants.fields;

public enum TicketDBFields {

    ID("id"),

    TYPE("type"),

    DESCRIPTION("description"),

    TITLE("title"),

    USER_ID("user_id"),

    AGENT_ID("agent_id"),

    PRIORITY("priority"),

    STATUS("status");

    private String value;

    public String getValue() {
        return value;
    }

    TicketDBFields(String value) {
        this.value = value;
    }

}
