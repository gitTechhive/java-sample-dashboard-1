package com.sampledashboard1.repository;

import com.sampledashboard1.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository  extends JpaRepository<Users, Long> {
    @Query("""
            select u from Users u where u.mobileNo=:mobileNo and (:id is null or u.id <> :id)
            """)
    List<Users> getByMobileNoAndId(Long mobileNo, Long id);
    @Query("""
            select new Users(u.id,u.firstName,u.lastName) from Users u
            where u.login.id=:id
            """)
    Optional<Users> getUserByLoginId(Long id);
}
