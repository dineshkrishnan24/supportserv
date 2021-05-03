package com.growfin.supportserv.service.impl;

import com.growfin.supportserv.constants.dto.AgentCriteriaDTO;
import com.growfin.supportserv.constants.enums.AssignedStatus;
import com.growfin.supportserv.constants.vo.AgentVO;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;
import com.growfin.supportserv.models.Agent;
import com.growfin.supportserv.models.User;
import com.growfin.supportserv.repository.AgentRepository;
import com.growfin.supportserv.repository.UserRepository;
import com.growfin.supportserv.service.AgentService;
import com.growfin.supportserv.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversionUtils conversionUtils;

    @Override
    public AgentVO createAgent(AgentVO agentVO) {
        Agent agent = conversionUtils.convertAgentVOToEntity(agentVO);
        User user = new User();
        user.setName(agentVO.getName());
        user.setAddress(agentVO.getAddress());
        user.setEmailId(agentVO.getEmailId());
        user = userRepository.save(user);
        agent.setUser(user);
        agent.setAssignedStatus(AssignedStatus.NOT_ASSIGNED);
        agent = agentRepository.save(agent);
        agentVO = conversionUtils.convertAgentEntityToVO(agent);
        setUserDetailsForAgent(agent, agentVO);
        return agentVO;
    }

    private void setUserDetailsForAgent(Agent agent, AgentVO agentVO) {
        User user = agent.getUser();
        agentVO.setAddress(user.getAddress());
        agentVO.setEmailId(user.getEmailId());
        agentVO.setName(user.getName());
    }

    @Override
    public ResourceCollectionVO<AgentVO> getAllAgent(AgentCriteriaDTO agentCriteriaDTO) {
        Page<Agent> agentPage = agentRepository.getAllAgent(agentCriteriaDTO);
        List<AgentVO> agentVOS = convertToAgentVO(agentPage.getContent());
        ResourceCollectionVO<AgentVO> resourceCollectionVO = new ResourceCollectionVO<>();
        resourceCollectionVO.setItems(agentVOS);
        resourceCollectionVO.setTotalItems(agentPage.getTotalElements());
        resourceCollectionVO.setTotalPages(agentPage.getTotalPages());
        resourceCollectionVO.setCurrentPage(agentPage.getNumber()+1);
        return resourceCollectionVO;
    }

    private List<AgentVO> convertToAgentVO(List<Agent> agentList) {
        List<AgentVO> agentVOS = new ArrayList<>();
        agentList.forEach(agent -> {
            AgentVO agentVO = conversionUtils.convertAgentEntityToVO(agent);
            setUserDetailsForAgent(agent, agentVO);
            agentVOS.add(agentVO);
        });
        return agentVOS;
    }
}
