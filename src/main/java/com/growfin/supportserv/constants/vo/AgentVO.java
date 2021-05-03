package com.growfin.supportserv.constants.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AgentVO {

    private String id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String address;

    @NotNull
    @NotEmpty
    private String emailId;

    @NotNull
    @NotEmpty
    private String agentCode;

}
