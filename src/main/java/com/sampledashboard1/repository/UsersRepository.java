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

    @Query("""
            select new Users(u.id,u.firstName,u.lastName,u.address,u.pinCode,u.mobileNo,u.bio,u.type,u.phonecode,c.id,
                               s.id,ci.id,l.email,ud.url) from Users u
            left join Countries c on c.id=u.country.id
            left join States s on s.id=u.state.id
            left join Cities ci on  ci.id=u.cities.id
            left join Login  l on l.id=u.login.id
            left join UserDoc ud on ud.user.id=u.id
            where u.login.id=:id
            """)
    Users getUsersData(Long id);
    Optional<Users> getUserByMobileNo(Long mobilNo);


}
