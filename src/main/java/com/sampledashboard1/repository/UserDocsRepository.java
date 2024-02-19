package com.sampledashboard1.repository;

import com.sampledashboard1.model.UserDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocsRepository extends JpaRepository<UserDoc, Long> {

    UserDoc findByUserId(Long id);
   // DELETE FROM table_name WHERE condition;
   @Modifying
  /* @Query("""
            delete from UserDoc ud where ud.user.id=:userId
            """)*/
   void deleteUserDocByUserId(Long userId);

}
