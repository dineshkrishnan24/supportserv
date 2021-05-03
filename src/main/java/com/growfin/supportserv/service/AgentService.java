package com.growfin.supportserv.service;

import com.growfin.supportserv.constants.dto.AgentCriteriaDTO;
import com.growfin.supportserv.constants.vo.AgentVO;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;

public interface AgentService {

    AgentVO createAgent(AgentVO agentVO);

    ResourceCollectionVO<AgentVO> getAllAgent(AgentCriteriaDTO agentCriteriaDTO);

}
