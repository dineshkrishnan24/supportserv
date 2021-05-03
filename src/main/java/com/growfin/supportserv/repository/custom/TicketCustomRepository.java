package com.growfin.supportserv.repository.custom;

import com.growfin.supportserv.constants.dto.TicketCriteriaDTO;
import com.growfin.supportserv.models.Ticket;
import org.springframework.data.domain.Page;

public interface TicketCustomRepository {

    Page<Ticket> getAllTicket(TicketCriteriaDTO ticketCriteriaDTO);

}
