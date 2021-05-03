package com.growfin.supportserv.constants.dto;

import com.growfin.supportserv.constants.enums.Status;
import lombok.Data;

import java.util.List;

@Data
public class TicketCriteriaDTO {

    private int page = 1;

    private int pageSize = 10;

    private List<String> ids;

    private List<String> agentIds;

    private List<String> userIds;

    private Status status;


}
