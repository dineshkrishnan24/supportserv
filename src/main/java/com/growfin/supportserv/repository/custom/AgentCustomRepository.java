package com.growfin.supportserv.repository.custom;

import com.growfin.supportserv.constants.dto.AgentCriteriaDTO;
import com.growfin.supportserv.models.Agent;
import org.springframework.data.domain.Page;

public interface AgentCustomRepository {

    Page<Agent> getAllAgent(AgentCriteriaDTO agentCriteriaDTO);

}
