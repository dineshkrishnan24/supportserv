package com.growfin.supportserv.repository;

import com.growfin.supportserv.models.User;
import com.growfin.supportserv.repository.custom.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

}
