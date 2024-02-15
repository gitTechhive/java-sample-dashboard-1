package com.sampledashboard1.repository;

import com.sampledashboard1.model.AppConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppConstantRepository extends JpaRepository<AppConstant, Long> {

    @Query("""
            select a.value from AppConstant a where a.title=:title
            """)
    Optional<String> getValueByTitle(String title);
}
