package com.growfin.supportserv.repository.impl;

import com.growfin.supportserv.constants.dto.TicketCriteriaDTO;
import com.growfin.supportserv.models.Ticket;
import com.growfin.supportserv.repository.custom.TicketCustomRepository;
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
public class TicketRepositoryImpl implements TicketCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Ticket> getAllTicket(TicketCriteriaDTO ticketCriteriaDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = criteriaBuilder.createQuery(Ticket.class);
        Root<Ticket> ticketRoot = query.from(Ticket.class);
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(ticketCriteriaDTO.getIds()) && ticketCriteriaDTO.getIds().size()>0) {
            predicates.add(ticketRoot.get("id").in(ticketCriteriaDTO.getIds()));
        }
        if(Objects.nonNull(ticketCriteriaDTO.getAgentIds())) {
            predicates.add(ticketRoot.join("agent").get("id").in(ticketCriteriaDTO.getAgentIds()));
        }
        if(Objects.nonNull(ticketCriteriaDTO.getUserIds())) {
            predicates.add(ticketRoot.join("user").get("id").in(ticketCriteriaDTO.getUserIds()));
        }
        if(Objects.nonNull(ticketCriteriaDTO.getStatus())) {
            predicates.add(ticketRoot.get("status").in(ticketCriteriaDTO.getStatus()));
        }
        Predicate[] preArray = new Predicate[predicates.size()];
        predicates.toArray(preArray);
        query.select(ticketRoot).where(preArray);
        TypedQuery<Ticket> typesQuery = entityManager.createQuery(query);
        int totalRows = typesQuery.getResultList().size();
        Pageable pageable = PageRequest.of((ticketCriteriaDTO.getPage()-1), ticketCriteriaDTO.getPageSize());
        typesQuery.setFirstResult((ticketCriteriaDTO.getPage()-1) * ticketCriteriaDTO.getPageSize());
        typesQuery.setMaxResults(ticketCriteriaDTO.getPageSize());
        return PageableExecutionUtils.getPage(typesQuery.getResultList(),pageable,()->totalRows);
    }


}
