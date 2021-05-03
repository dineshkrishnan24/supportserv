package com.growfin.supportserv.controllers;

import com.growfin.supportserv.constants.dto.AgentCriteriaDTO;
import com.growfin.supportserv.constants.vo.AgentVO;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;
import com.growfin.supportserv.service.AgentService;
import com.growfin.supportserv.utils.CriteriaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.growfin.supportserv.constants.fields.AgentDBFields.AGENT_CODE;
import static com.growfin.supportserv.constants.fields.GetListFields.PAGE;
import static com.growfin.supportserv.constants.fields.GetListFields.PAGE_SIZE;
import static com.growfin.supportserv.constants.fields.AgentDBFields.ID;

@RestController
@RequestMapping("/v1/customer/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping
    private ResponseEntity<AgentVO> createAgent(@RequestBody @Valid AgentVO agentVO) {
        AgentVO respVO = agentService.createAgent(agentVO);
        return ResponseEntity.ok(respVO);
    }

    @GetMapping
    public ResponseEntity<ResourceCollectionVO<AgentVO>> getAllAgent(@RequestParam(required = false) MultiValueMap<String, String> queryParam) {
        AgentCriteriaDTO agentCriteriaDTO = getCriteriaFromQuery(queryParam);
        ResourceCollectionVO<AgentVO> response = agentService.getAllAgent(agentCriteriaDTO);
        return ResponseEntity.ok(response);
    }

    private AgentCriteriaDTO getCriteriaFromQuery(MultiValueMap<String, String> queryParam) {

        AgentCriteriaDTO agentCriteriaDTO = new AgentCriteriaDTO();
        if (CriteriaUtils.isQueryParamAvailable(queryParam, ID.getValue())) {
            agentCriteriaDTO.setIds(queryParam.get(ID.getValue()));
        }
        if(CriteriaUtils.isQueryParamAvailable(queryParam, AGENT_CODE.getValue())) {
            agentCriteriaDTO.setAgentCodes(queryParam.get(AGENT_CODE.getValue()));
        }
        if(CriteriaUtils.isQueryParamAvailable(queryParam, PAGE.getValue()) && CriteriaUtils.isQueryParamAvailable(queryParam, PAGE_SIZE.getValue()) ) {
            agentCriteriaDTO.setPage(Integer.parseInt(queryParam.get(PAGE.getValue()).get(0)));
            agentCriteriaDTO.setPageSize(Integer.parseInt(queryParam.get(PAGE_SIZE.getValue()).get(0)));
        }
        return agentCriteriaDTO;

    }

}
