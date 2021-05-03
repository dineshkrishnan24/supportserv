package com.growfin.supportserv.constants.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class RespValidationErrorVO extends ResponseSubErrorVO {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    RespValidationErrorVO(String object, String message) {
        this.object = object;
        this.message = message;
    }

}
