package com.growfin.supportserv.repository;

import com.growfin.supportserv.models.TicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketResponseRepository extends JpaRepository<TicketResponse, Long> {
}
