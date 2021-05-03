package com.growfin.supportserv.service;

import com.growfin.supportserv.constants.dto.UserCriteriaDTO;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;
import com.growfin.supportserv.constants.vo.UserVO;

public interface UserService {

    UserVO createUser(UserVO userVO);

    ResourceCollectionVO<UserVO> getAllUser(UserCriteriaDTO userCriteriaDTO);
}
