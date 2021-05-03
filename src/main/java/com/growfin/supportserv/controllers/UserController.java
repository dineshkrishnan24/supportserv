package com.growfin.supportserv.controllers;

import com.growfin.supportserv.constants.dto.UserCriteriaDTO;
import com.growfin.supportserv.service.UserService;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;
import com.growfin.supportserv.constants.vo.UserVO;
import com.growfin.supportserv.utils.CriteriaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.growfin.supportserv.constants.fields.GetListFields.PAGE;
import static com.growfin.supportserv.constants.fields.GetListFields.PAGE_SIZE;
import static com.growfin.supportserv.constants.fields.UserDBFields.*;

@RestController
@RequestMapping("/v1/customer/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    private ResponseEntity<UserVO> createUser(@RequestBody @Valid UserVO userVO) {
        UserVO respVO = userService.createUser(userVO);
        return ResponseEntity.ok(respVO);
    }

    @GetMapping
    public ResponseEntity<ResourceCollectionVO<UserVO>> getAllUser(@RequestParam(required = false) MultiValueMap<String, String> queryParam) {
        UserCriteriaDTO userCriteriaDTO = getCriteriaFromQuery(queryParam);
        ResourceCollectionVO<UserVO> response = userService.getAllUser(userCriteriaDTO);
        return ResponseEntity.ok(response);
    }

    private UserCriteriaDTO getCriteriaFromQuery(MultiValueMap<String, String> queryParam) {

        UserCriteriaDTO userCriteriaDTO = new UserCriteriaDTO();
        if (CriteriaUtils.isQueryParamAvailable(queryParam, ID.getValue())) {
            userCriteriaDTO.setIds(queryParam.get(ID.getValue()));
        }
        if(CriteriaUtils.isQueryParamAvailable(queryParam, EMAIL_ID.getValue())) {
            userCriteriaDTO.setEmailIds(queryParam.get(EMAIL_ID.getValue()));
        }
        if(CriteriaUtils.isQueryParamAvailable(queryParam, PAGE.getValue()) && CriteriaUtils.isQueryParamAvailable(queryParam, PAGE_SIZE.getValue()) ) {
            userCriteriaDTO.setPage(Integer.parseInt(queryParam.get(PAGE.getValue()).get(0)));
            userCriteriaDTO.setPageSize(Integer.parseInt(queryParam.get(PAGE_SIZE.getValue()).get(0)));
        }
        return userCriteriaDTO;

    }

}
