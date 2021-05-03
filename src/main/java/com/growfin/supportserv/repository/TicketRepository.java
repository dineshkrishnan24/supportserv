package com.growfin.supportserv.repository;

import com.growfin.supportserv.models.Ticket;
import com.growfin.supportserv.repository.custom.TicketCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketCustomRepository {
}
