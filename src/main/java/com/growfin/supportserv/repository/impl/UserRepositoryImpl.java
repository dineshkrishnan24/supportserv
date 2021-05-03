package com.growfin.supportserv.repository.impl;

import com.growfin.supportserv.constants.dto.UserCriteriaDTO;
import com.growfin.supportserv.constants.vo.UserVO;
import com.growfin.supportserv.models.User;
import com.growfin.supportserv.repository.custom.UserCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepositoryImpl implements UserCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<User> getAllUser(UserCriteriaDTO userCriteriaDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(userCriteriaDTO.getIds()) && userCriteriaDTO.getIds().size()>0) {
            predicates.add(userRoot.get("id").in(userCriteriaDTO.getIds()));
        }
        if(Objects.nonNull(userCriteriaDTO.getEmailIds())) {
            predicates.add(userRoot.get("emailId").in(userCriteriaDTO.getEmailIds()));
        }
        Predicate[] preArray = new Predicate[predicates.size()];
        predicates.toArray(preArray);
        query.select(userRoot).where(preArray);
        TypedQuery<User> typesQuery = entityManager.createQuery(query);
        int totalRows = typesQuery.getResultList().size();
        Pageable pageable = PageRequest.of((userCriteriaDTO.getPage()-1), userCriteriaDTO.getPageSize());
        typesQuery.setFirstResult((userCriteriaDTO.getPage()-1) * userCriteriaDTO.getPageSize());
        typesQuery.setMaxResults(userCriteriaDTO.getPageSize());
        return PageableExecutionUtils.getPage(typesQuery.getResultList(),pageable,()->totalRows);
    }
}
