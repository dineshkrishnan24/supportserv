package com.growfin.supportserv.repository;

import com.growfin.supportserv.constants.enums.AssignedStatus;
import com.growfin.supportserv.models.Agent;
import com.growfin.supportserv.repository.custom.AgentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Long>, AgentCustomRepository {

    List<Agent> findByAssignedStatusOrderByIdAsc(AssignedStatus assignedStatus);

    @Modifying
    @Query("update Agent set assignedStatus = '0'")
    void updateAllAssignedStatus();

}
