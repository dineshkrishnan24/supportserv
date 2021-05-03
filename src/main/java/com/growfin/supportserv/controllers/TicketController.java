package com.growfin.supportserv.controllers;

import com.github.fge.jsonpatch.JsonPatch;
import com.growfin.supportserv.constants.dto.AgentCriteriaDTO;
import com.growfin.supportserv.constants.dto.TicketCriteriaDTO;
import com.growfin.supportserv.constants.enums.Status;
import com.growfin.supportserv.constants.vo.AgentVO;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;
import com.growfin.supportserv.constants.vo.TicketVO;
import com.growfin.supportserv.exceptions.ResourceNotFoundException;
import com.growfin.supportserv.exceptions.ServiceException;
import com.growfin.supportserv.models.Ticket;
import com.growfin.supportserv.service.TicketService;
import com.growfin.supportserv.utils.CriteriaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Objects;

import static com.growfin.supportserv.constants.fields.TicketDBFields.*;
import static com.growfin.supportserv.constants.fields.GetListFields.PAGE;
import static com.growfin.supportserv.constants.fields.GetListFields.PAGE_SIZE;

@RestController
@RequestMapping("/v1/customer/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    private ResponseEntity<TicketVO> createTicket(@RequestBody @Valid TicketVO ticketVO, HttpServletRequest httpServletRequest) {
        TicketVO respVO = ticketService.createTicket(ticketVO, httpServletRequest);
        return ResponseEntity.ok(respVO);
    }

    @GetMapping
    public ResponseEntity<ResourceCollectionVO<TicketVO>> getAllTicket(@RequestParam(required = false) MultiValueMap<String, String> queryParam) {
        TicketCriteriaDTO ticketCriteriaDTO = getCriteriaFromQuery(queryParam);
        ResourceCollectionVO<TicketVO> response = ticketService.getAllTicket(ticketCriteriaDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<TicketVO> getTicketById(@PathVariable("id") String id) {
        TicketVO ticketVO = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticketVO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketVO> updateTicket(@PathVariable("id") String id, @RequestBody TicketVO ticketVO) {
        TicketVO respVO = ticketService.updateTicket(id, ticketVO);
        return ResponseEntity.ok(respVO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity patchTicket(@PathVariable("id") String id, @RequestBody JsonPatch reqBody) throws ResourceNotFoundException {
        TicketVO ticketVO = ticketService.patchTicket(id,reqBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity deleteTicketById(@PathVariable("id") String id) {
        ticketService.deleteTicketById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private TicketCriteriaDTO getCriteriaFromQuery(MultiValueMap<String, String> queryParam) {

        TicketCriteriaDTO ticketCriteriaDTO = new TicketCriteriaDTO();
        if (CriteriaUtils.isQueryParamAvailable(queryParam, ID.getValue())) {
            ticketCriteriaDTO.setIds(queryParam.get(ID.getValue()));
        }
        if(CriteriaUtils.isQueryParamAvailable(queryParam, AGENT_ID.getValue())) {
            ticketCriteriaDTO.setAgentIds(queryParam.get(AGENT_ID.getValue()));
        }
        if(CriteriaUtils.isQueryParamAvailable(queryParam, USER_ID.getValue())) {
            ticketCriteriaDTO.setUserIds(queryParam.get(USER_ID.getValue()));
        }
        if(CriteriaUtils.isQueryParamAvailable(queryParam, STATUS.getValue())) {
            try {
                ticketCriteriaDTO.setStatus(Status.valueOf(queryParam.getFirst(STATUS.getValue())));
            } catch (Exception e) {
                throw new ServiceException("Invalid status", HttpStatus.BAD_REQUEST);
            }
        }
        if(CriteriaUtils.isQueryParamAvailable(queryParam, PAGE.getValue()) && CriteriaUtils.isQueryParamAvailable(queryParam, PAGE_SIZE.getValue()) ) {
            ticketCriteriaDTO.setPage(Integer.parseInt(queryParam.get(PAGE.getValue()).get(0)));
            ticketCriteriaDTO.setPageSize(Integer.parseInt(queryParam.get(PAGE_SIZE.getValue()).get(0)));
        }
        return ticketCriteriaDTO;

    }


}
