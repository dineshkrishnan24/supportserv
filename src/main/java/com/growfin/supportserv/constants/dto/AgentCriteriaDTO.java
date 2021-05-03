package com.growfin.supportserv.constants.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgentCriteriaDTO {

    private int page = 1;

    private int pageSize = 10;

    private List<String> ids;

    private List<String> agentCodes;


}
