package com.sampledashboard1.repository;

import com.sampledashboard1.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    @Query("""
            select l from Login l 
            join Users u on u.login.id=l.id
            where l.email=:email and (:userId is null or u.id <> :userId)
            """)
    List<Login> getByEmailAndUserId(String email, Long userId);
    Optional<Login> findByEmail(String email);
    @Query("""
            select new Login(l.id,l.email,l.password,l.isActive,u.firstName,u.id) from Login l
            left join Users u on u.login.id=l.id
            where l.id=:id
            """)
    Optional<Login> getByLoginId(Long id);
    Optional<Login> findById(Long id);
}
