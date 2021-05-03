package com.growfin.supportserv.constants.fields;

public enum GetListFields {

    PAGE("page"),

    PAGE_SIZE("page_size");

    private String value;

    private GetListFields(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
