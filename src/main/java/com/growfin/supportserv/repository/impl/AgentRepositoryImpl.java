package com.growfin.supportserv.repository.impl;

import com.growfin.supportserv.constants.dto.AgentCriteriaDTO;
import com.growfin.supportserv.models.Agent;
import com.growfin.supportserv.repository.custom.AgentCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class AgentRepositoryImpl implements AgentCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Agent> getAllAgent(AgentCriteriaDTO agentCriteriaDTO) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Agent> query = criteriaBuilder.createQuery(Agent.class);
        Root<Agent> agentRoot = query.from(Agent.class);
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(agentCriteriaDTO.getIds()) && agentCriteriaDTO.getIds().size()>0) {
            predicates.add(agentRoot.get("id").in(agentCriteriaDTO.getIds()));
        }
        if(Objects.nonNull(agentCriteriaDTO.getAgentCodes())) {
            predicates.add(agentRoot.get("agentCodes").in(agentCriteriaDTO.getAgentCodes()));
        }
        Predicate[] preArray = new Predicate[predicates.size()];
        predicates.toArray(preArray);
        query.select(agentRoot).where(preArray);
        TypedQuery<Agent> typesQuery = entityManager.createQuery(query);
        int totalRows = typesQuery.getResultList().size();
        Pageable pageable = PageRequest.of((agentCriteriaDTO.getPage()-1), agentCriteriaDTO.getPageSize());
        typesQuery.setFirstResult((agentCriteriaDTO.getPage()-1) * agentCriteriaDTO.getPageSize());
        typesQuery.setMaxResults(agentCriteriaDTO.getPageSize());
        return PageableExecutionUtils.getPage(typesQuery.getResultList(),pageable,()->totalRows);
    }
}
