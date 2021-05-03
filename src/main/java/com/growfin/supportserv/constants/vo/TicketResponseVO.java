package com.growfin.supportserv.constants.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseVO {

    private String id;

    @NonNull
    @NotEmpty
    private String ticketId;

    @NonNull
    @NotEmpty
    private String agentId;

    @NonNull
    @NotEmpty
    private String response;

}
