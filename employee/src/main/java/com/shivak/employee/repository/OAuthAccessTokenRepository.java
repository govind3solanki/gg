package com.shivak.employee.repository;

import com.shivak.employee.entity.OAuthAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthAccessTokenRepository extends JpaRepository<OAuthAccessToken,String> {
    OAuthAccessToken findByUserName(String username);
}
