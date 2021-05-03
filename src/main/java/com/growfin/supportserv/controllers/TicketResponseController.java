package com.growfin.supportserv.controllers;

import com.growfin.supportserv.constants.vo.AgentVO;
import com.growfin.supportserv.constants.vo.TicketResponseVO;
import com.growfin.supportserv.service.TicketResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/customer/ticket-responses")
public class TicketResponseController {


    @Autowired
    private TicketResponseService ticketResponseService;

    @PostMapping
    private ResponseEntity<TicketResponseVO> createTicketResp(@RequestBody @Valid TicketResponseVO ticketResponseVO) {
        TicketResponseVO respVO = ticketResponseService.createTicketResp(ticketResponseVO);
        return ResponseEntity.ok(respVO);
    }

}
