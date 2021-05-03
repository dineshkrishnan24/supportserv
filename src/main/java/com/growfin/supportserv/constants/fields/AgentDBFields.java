package com.growfin.supportserv.constants.fields;

public enum AgentDBFields {

    ID("id"),

    AGENT_CODE("agent_code");

    private String value;

    public String getValue() {
        return value;
    }

    AgentDBFields(String value) {
        this.value = value;
    }

}
