package com.growfin.supportserv.repository.custom;

import com.growfin.supportserv.constants.dto.UserCriteriaDTO;
import com.growfin.supportserv.models.User;
import org.springframework.data.domain.Page;


public interface UserCustomRepository {

    Page<User> getAllUser(UserCriteriaDTO userCriteriaDTO);

}
