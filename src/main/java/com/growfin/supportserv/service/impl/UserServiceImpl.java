package com.growfin.supportserv.service.impl;

import com.growfin.supportserv.constants.dto.UserCriteriaDTO;
import com.growfin.supportserv.constants.vo.ResourceCollectionVO;
import com.growfin.supportserv.constants.vo.UserVO;
import com.growfin.supportserv.models.User;
import com.growfin.supportserv.repository.UserRepository;
import com.growfin.supportserv.service.UserService;
import com.growfin.supportserv.utils.ConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversionUtils conversionUtils;

    @Override
    public UserVO createUser(UserVO userVO) {
        User user = conversionUtils.convertUserVOToEntity(userVO);
        user = userRepository.save(user);
        userVO = conversionUtils.convertUserEntityToVO(user);
        return userVO;
    }

    @Override
    public ResourceCollectionVO<UserVO> getAllUser(UserCriteriaDTO userCriteriaDTO) {
        Page<User> userPage = userRepository.getAllUser(userCriteriaDTO);
        List<UserVO> userVOS = conversionUtils.convertList(userPage.getContent(), conversionUtils::convertUserEntityToVO);
        ResourceCollectionVO<UserVO> resourceCollectionVO = new ResourceCollectionVO<>();
        resourceCollectionVO.setItems(userVOS);
        resourceCollectionVO.setTotalItems(userPage.getTotalElements());
        resourceCollectionVO.setTotalPages(userPage.getTotalPages());
        resourceCollectionVO.setCurrentPage(userPage.getNumber()+1);
        return resourceCollectionVO;
    }

}
