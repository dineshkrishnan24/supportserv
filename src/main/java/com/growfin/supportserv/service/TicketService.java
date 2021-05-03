package com.growfin.supportserv.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.growfin.supportserv.constants.dto.TicketCriteriaDTO;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;
import com.growfin.supportserv.constants.vo.TicketVO;

import javax.servlet.http.HttpServletRequest;

public interface TicketService {

    TicketVO createTicket(TicketVO ticketVO, HttpServletRequest httpServletRequest);

    ResourceCollectionVO<TicketVO> getAllTicket(TicketCriteriaDTO ticketCriteriaDTO);

    TicketVO getTicketById(String id);

    TicketVO updateTicket(String id, TicketVO ticketVO);

    TicketVO patchTicket(String id, JsonPatch reqBody);

    void deleteTicketById(String id);
}
