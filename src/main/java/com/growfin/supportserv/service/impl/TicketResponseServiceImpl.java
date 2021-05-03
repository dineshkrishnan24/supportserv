package com.growfin.supportserv.service.impl;

import com.growfin.supportserv.constants.vo.TicketResponseVO;
import com.growfin.supportserv.exceptions.ServiceException;
import com.growfin.supportserv.models.Agent;
import com.growfin.supportserv.models.Ticket;
import com.growfin.supportserv.models.TicketResponse;
import com.growfin.supportserv.repository.AgentRepository;
import com.growfin.supportserv.repository.TicketRepository;
import com.growfin.supportserv.repository.TicketResponseRepository;
import com.growfin.supportserv.service.TicketResponseService;
import com.growfin.supportserv.utils.ConversionUtils;
import com.growfin.supportserv.utils.CryptUtils;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class TicketResponseServiceImpl implements TicketResponseService {
    
    @Autowired
    private TicketResponseRepository ticketResponseRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ConversionUtils conversionUtils;

    @Override
    public TicketResponseVO createTicketResp(TicketResponseVO ticketResponseVO) {
        TicketResponse ticketResponse = new TicketResponse();
        ticketResponse.setResponse(ticketResponseVO.getResponse());
        Ticket ticket = Optional.ofNullable(CryptUtils.decryptLongData(ticketResponseVO.getTicketId()))
                .flatMap(ticketId-> ticketRepository.findById(ticketId))
                .orElseThrow(()->new ServiceException("Invalid Ticket", HttpStatus.BAD_REQUEST));
        Agent agent = Optional.ofNullable(CryptUtils.decryptLongData(ticketResponseVO.getAgentId()))
                .flatMap(agentId-> agentRepository.findById(agentId))
                .orElseThrow(()->new ServiceException("Invalid Agent", HttpStatus.BAD_REQUEST));
        if(!ticket.getAgent().getId().equals(agent.getId())) {
            throw new ServiceException("Another Agent not allowed",HttpStatus.FORBIDDEN);
        }
        ticketResponse.setTicket(ticket);
        ticketResponse.setAgent(agent);
        ticketResponse = ticketResponseRepository.save(ticketResponse);
        sendMailToCustomer(ticketResponse);
        ticketResponseVO = conversionUtils.convertTicketRespEntityToVO(ticketResponse);
        return ticketResponseVO;
    }

    private void sendMailToCustomer(TicketResponse ticketResponse) {
        try {
            Mail mail = createMail(ticketResponse);
            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException e) {
            log.error("Exception while sending mail to customer",e);
        }

    }

    private Mail createMail(TicketResponse ticketResponse) {
        Ticket ticket = ticketResponse.getTicket();
        Agent agent = ticketResponse.getAgent();
        Email from = new Email("yogesh@sinecycle.com");
        String subject = "Your";
        Email to = new Email("dineshkrishnanj@gmail.com");
        Content content = new Content("text/plain", ticketResponse.getResponse());
        Mail mail = new Mail(from, subject, to, content);

        return mail;
    }


}
