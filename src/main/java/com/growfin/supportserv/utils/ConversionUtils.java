package com.growfin.supportserv.utils;

import com.growfin.supportserv.constants.vo.AgentVO;
import com.growfin.supportserv.constants.vo.TicketResponseVO;
import com.growfin.supportserv.constants.vo.TicketVO;
import com.growfin.supportserv.constants.vo.UserVO;
import com.growfin.supportserv.models.Agent;
import com.growfin.supportserv.models.Ticket;
import com.growfin.supportserv.models.TicketResponse;
import com.growfin.supportserv.models.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConversionUtils {

    @Autowired
    private ModelMapper modelMapper;

    public static Converter<String, String> encryptData = ctx -> CryptUtils.encryptData(ctx.getSource());

    public static Converter<String, String> decryptData = ctx -> CryptUtils.decryptData(ctx.getSource());

    public static Converter<Long, String> encryptLongData = ctx -> CryptUtils.encryptLongData(ctx.getSource());

    public static Converter<String, Long> decryptLongData = ctx -> CryptUtils.decryptLongData(ctx.getSource());


    public User convertUserVOToEntity(UserVO userVO) {
        TypeMap<UserVO, User> typeMap = modelMapper.getTypeMap(UserVO.class, User.class);
        typeMap.addMappings(propertyMapper -> propertyMapper.using(decryptLongData).map(UserVO::getId, User::setId));
        return typeMap.map(userVO);
    }

    public UserVO convertUserEntityToVO(User user) {
        TypeMap<User, UserVO> typeMap = modelMapper.getTypeMap(User.class, UserVO.class);
        typeMap.addMappings(propertyMapper -> propertyMapper.using(encryptLongData).map(User::getId, UserVO::setId));
        return typeMap.map(user);
    }

    public Agent convertAgentVOToEntity(AgentVO agentVO) {
        TypeMap<AgentVO, Agent> typeMap = modelMapper.getTypeMap(AgentVO.class, Agent.class);
        typeMap.addMappings(propertyMapper -> propertyMapper.using(decryptLongData).map(AgentVO::getId, Agent::setId));
        return typeMap.map(agentVO);
    }

    public AgentVO convertAgentEntityToVO(Agent agent) {
        TypeMap<Agent, AgentVO> typeMap = modelMapper.getTypeMap(Agent.class, AgentVO.class);
        typeMap.addMappings(propertyMapper -> propertyMapper.using(encryptLongData).map(Agent::getId, AgentVO::setId));
        return typeMap.map(agent);
    }

    public Ticket convertTicketVOToEntity(TicketVO ticketVO) {
        TypeMap<TicketVO, Ticket> typeMap = modelMapper.getTypeMap(TicketVO.class, Ticket.class);
        typeMap.addMappings(propertyMapper -> propertyMapper.using(decryptLongData).map(TicketVO::getId, Ticket::setId));
        typeMap.addMappings(propertyMapper -> propertyMapper.using(decryptLongData).map(TicketVO::getUserId, (ticket, o) -> ticket.getUser().setId((Long) o)));
        typeMap.addMappings(propertyMapper -> propertyMapper.using(decryptLongData).map(TicketVO::getAgentId, (ticket, o) -> ticket.getAgent().setId((Long) o)));
        return typeMap.map(ticketVO);
    }

    public TicketVO convertTicketEntityToVO(Ticket ticket) {
        TypeMap<Ticket, TicketVO> typeMap = modelMapper.getTypeMap(Ticket.class, TicketVO.class);
        typeMap.addMappings(propertyMapper -> propertyMapper.using(encryptLongData).map(Ticket::getId, TicketVO::setId));
        typeMap.addMappings(propertyMapper -> propertyMapper.using(encryptLongData).map(ticObj -> ticObj.getUser().getId(), TicketVO::setUserId));
        typeMap.addMappings(propertyMapper -> propertyMapper.using(encryptLongData).map(ticObj -> ticObj.getAgent().getId(), TicketVO::setAgentId));
        return typeMap.map(ticket);
    }

    public TicketResponseVO convertTicketRespEntityToVO(TicketResponse ticketResponse) {
        TypeMap<TicketResponse, TicketResponseVO> typeMap = modelMapper.getTypeMap(TicketResponse.class, TicketResponseVO.class);
        typeMap.addMappings(propertyMapper -> propertyMapper.using(encryptLongData).map(TicketResponse::getId, TicketResponseVO::setId));
        typeMap.addMappings(propertyMapper -> propertyMapper.using(encryptLongData).map(ticObj -> ticObj.getAgent().getId(), TicketResponseVO::setAgentId));
        typeMap.addMappings(propertyMapper -> propertyMapper.using(encryptLongData).map(ticObj -> ticObj.getTicket().getId(), TicketResponseVO::setTicketId));
        return typeMap.map(ticketResponse);
    }

    public <S, T> List<T> convertList(List<S> source, Function<S, T> mapperFunction) {
        return Optional.ofNullable(source).map(src ->
                src.stream()
                        .map(mapperFunction)
                        .collect(Collectors.toList())).orElse(null);
    }

}
