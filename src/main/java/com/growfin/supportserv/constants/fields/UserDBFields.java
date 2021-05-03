package com.growfin.supportserv.constants.fields;

public enum UserDBFields {

    ID("id"),

    ADDRESS("address"),

    NAME("name"),

    EMAIL_ID("email_id");

    private String value;

    public String getValue() {
        return value;
    }

    UserDBFields(String value) {
        this.value = value;
    }

}
