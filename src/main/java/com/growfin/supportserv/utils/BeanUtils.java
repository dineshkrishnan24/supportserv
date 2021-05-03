package com.growfin.supportserv.utils;

import com.growfin.supportserv.constants.vo.AgentVO;
import com.growfin.supportserv.constants.vo.TicketResponseVO;
import com.growfin.supportserv.constants.vo.TicketVO;
import com.growfin.supportserv.constants.vo.UserVO;
import com.growfin.supportserv.models.Agent;
import com.growfin.supportserv.models.Ticket;
import com.growfin.supportserv.models.TicketResponse;
import com.growfin.supportserv.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeanUtils {

    @Bean
    @Scope("prototype")
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.createTypeMap(User.class, UserVO.class);
        modelMapper.createTypeMap(UserVO.class, User.class);
        modelMapper.createTypeMap(Agent.class, AgentVO.class);
        modelMapper.createTypeMap(AgentVO.class, Agent.class);
        modelMapper.createTypeMap(Ticket.class, TicketVO.class);
        modelMapper.createTypeMap(TicketVO.class, Ticket.class);
        modelMapper.createTypeMap(TicketResponse.class, TicketResponseVO.class);
        modelMapper.createTypeMap(TicketResponseVO.class, TicketResponse.class);
        return modelMapper;
    }

}
