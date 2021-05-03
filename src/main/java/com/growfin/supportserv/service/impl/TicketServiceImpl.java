package com.growfin.supportserv.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.growfin.supportserv.constants.dto.TicketCriteriaDTO;
import com.growfin.supportserv.constants.enums.AssignedStatus;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;
import com.growfin.supportserv.constants.vo.TicketVO;
import com.growfin.supportserv.exceptions.ResourceNotFoundException;
import com.growfin.supportserv.exceptions.ServiceException;
import com.growfin.supportserv.models.Agent;
import com.growfin.supportserv.models.Ticket;
import com.growfin.supportserv.models.User;
import com.growfin.supportserv.repository.AgentRepository;
import com.growfin.supportserv.repository.TicketRepository;
import com.growfin.supportserv.repository.UserRepository;
import com.growfin.supportserv.service.TicketService;
import com.growfin.supportserv.utils.ConversionUtils;
import com.growfin.supportserv.utils.CryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ConversionUtils conversionUtils;

    @Override
    @Transactional
    public TicketVO createTicket(TicketVO ticketVO, HttpServletRequest httpServletRequest) {
        Ticket ticket = conversionUtils.convertTicketVOToEntity(ticketVO);
        User user = Optional.ofNullable(CryptUtils.decryptLongData(ticketVO.getUserId()))
                        .flatMap(userId-> userRepository.findById(userId))
                        .orElseThrow(()->new ServiceException("Invalid User", HttpStatus.BAD_REQUEST));
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        ticket.setCreatedBy(httpServletRequest.getRemoteAddr());
        ticket.setCode("TK"+ LocalDateTime.now().format(dateFormat));
        ticket.setUser(user);
        assignAgent(ticket);
        ticket = ticketRepository.save(ticket);
        ticketVO = conversionUtils.convertTicketEntityToVO(ticket);
        return ticketVO;
    }

    private Ticket assignAgent(Ticket ticket) {
        List<Agent> unAssignedAgents = agentRepository.findByAssignedStatusOrderByIdAsc(AssignedStatus.NOT_ASSIGNED);
        if(unAssignedAgents.size() == 0) {
            agentRepository.updateAllAssignedStatus();
            unAssignedAgents = agentRepository.findByAssignedStatusOrderByIdAsc(AssignedStatus.NOT_ASSIGNED);
        }
        if(!(unAssignedAgents.size()>0)) {
            throw new ServiceException("No Agent are there!!",HttpStatus.SERVICE_UNAVAILABLE);
        }
        Agent agent = unAssignedAgents.get(0);
        agent.setAssignedStatus(AssignedStatus.ASSIGNED);
        agentRepository.save(agent);
        ticket.setAgent(agent);
        return ticket;
    }

    @Override
    public ResourceCollectionVO<TicketVO> getAllTicket(TicketCriteriaDTO ticketCriteriaDTO) {
        ticketCriteriaDTO.setIds(conversionUtils.convertList(ticketCriteriaDTO.getIds(), CryptUtils::decryptData));
        ticketCriteriaDTO.setUserIds(conversionUtils.convertList(ticketCriteriaDTO.getUserIds(), CryptUtils::decryptData));
        ticketCriteriaDTO.setAgentIds(conversionUtils.convertList(ticketCriteriaDTO.getAgentIds(), CryptUtils::decryptData));
        Page<Ticket> ticketPage = ticketRepository.getAllTicket(ticketCriteriaDTO);
        List<TicketVO> ticketVOS = conversionUtils.convertList(ticketPage.getContent(), conversionUtils::convertTicketEntityToVO);
        ResourceCollectionVO<TicketVO> resourceCollectionVO = new ResourceCollectionVO<>();
        resourceCollectionVO.setItems(ticketVOS);
        resourceCollectionVO.setTotalItems(ticketPage.getTotalElements());
        resourceCollectionVO.setTotalPages(ticketPage.getTotalPages());
        resourceCollectionVO.setCurrentPage(ticketPage.getNumber()+1);
        return resourceCollectionVO;
    }

    @Override
    public TicketVO getTicketById(String id) {
        Ticket ticket = Optional.ofNullable(CryptUtils.decryptLongData(id))
                        .flatMap(ticketRepository::findById)
                        .orElseThrow(()->new ResourceNotFoundException(Ticket.class,"id",id));
        TicketVO ticketVO = conversionUtils.convertTicketEntityToVO(ticket);
        return ticketVO;
    }

    @Override
    public TicketVO updateTicket(String id, TicketVO ticketVO) {
        Ticket ticket = Optional.ofNullable(CryptUtils.decryptLongData(id))
                .flatMap(ticketRepository::findById)
                .orElseThrow(()->new ResourceNotFoundException(Ticket.class,"id",id));
        User user = Optional.ofNullable(CryptUtils.decryptLongData(ticketVO.getUserId()))
                .flatMap(userId-> userRepository.findById(userId))
                .orElseThrow(()->new ServiceException("Invalid User", HttpStatus.BAD_REQUEST));
        Agent agent = Optional.ofNullable(CryptUtils.decryptLongData(ticketVO.getAgentId()))
                .flatMap(agentId-> agentRepository.findById(agentId))
                .orElseThrow(()->new ServiceException("Invalid Agent", HttpStatus.BAD_REQUEST));
        Ticket finalTicket = conversionUtils.convertTicketVOToEntity(ticketVO);
        finalTicket.setUser(user);
        finalTicket.setAgent(agent);
        finalTicket = ticketRepository.save(finalTicket);
        ticketVO = conversionUtils.convertTicketEntityToVO(finalTicket);
        return ticketVO;
    }

    @Override
    public TicketVO patchTicket(String id, JsonPatch reqBody) {
        Ticket ticket = Optional.ofNullable(CryptUtils.decryptLongData(id))
                .flatMap(ticketRepository::findById)
                .orElseThrow(()->new ResourceNotFoundException(Ticket.class,"id",id));
        TicketVO ticketVO = conversionUtils.convertTicketEntityToVO(ticket);
        ObjectMapper objMapper = new ObjectMapper();
        try {
            JsonNode ticketVOJson = objMapper.readTree(objMapper.writeValueAsString(ticketVO));
            ticketVOJson = reqBody.apply(ticketVOJson);
            ticketVO = objMapper.readValue(ticketVOJson.toString(), TicketVO.class);
            Ticket updatedTicket = conversionUtils.convertTicketVOToEntity(ticketVO);
            User user = Optional.ofNullable(CryptUtils.decryptLongData(ticketVO.getUserId()))
                    .flatMap(userId-> userRepository.findById(userId))
                    .orElseThrow(()->new ServiceException("Invalid User", HttpStatus.BAD_REQUEST));
            Agent agent = Optional.ofNullable(CryptUtils.decryptLongData(ticketVO.getAgentId()))
                    .flatMap(agentId-> agentRepository.findById(agentId))
                    .orElseThrow(()->new ServiceException("Invalid Agent", HttpStatus.BAD_REQUEST));
            updatedTicket.setUser(user);
            updatedTicket.setAgent(agent);
            updatedTicket = ticketRepository.save(updatedTicket);
            ticketVO = conversionUtils.convertTicketEntityToVO(updatedTicket);
            return ticketVO;
        } catch (JsonProcessingException e) {
            throw new ServiceException("Error while processing", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonPatchException e) {
            throw new ServiceException("Invalid Request Body", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteTicketById(String id) {
        Ticket ticket = Optional.ofNullable(CryptUtils.decryptLongData(id))
                .flatMap(ticketRepository::findById)
                .orElseThrow(()->new ResourceNotFoundException(Ticket.class,"id",id));
        ticketRepository.delete(ticket);
    }


}
