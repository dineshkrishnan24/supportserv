package com.growfin.supportserv.constants.vo;

import com.growfin.supportserv.constants.enums.Priority;
import com.growfin.supportserv.constants.enums.Status;
import com.growfin.supportserv.constants.enums.Type;
import com.growfin.supportserv.models.Agent;
import com.growfin.supportserv.models.User;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TicketVO {

    private String id;

    @NotNull
    private Type type;

    private String code;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String userId;

    private String agentId;

    @NotNull
    private Priority priority;

    @NotNull
    private Status status;

}
