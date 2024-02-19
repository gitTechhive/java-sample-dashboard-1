package com.sampledashboard1.repository;

import com.sampledashboard1.filter.DropdownResponse;
import com.sampledashboard1.model.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatesRepository extends JpaRepository<States, Long> {

    @Query("select new com.sampledashboard1.filter.DropdownResponse(s.name,s.id) from States as s where s.country.id=:id  order by s.name")
    List<DropdownResponse> getStatesDropdown(Long id);
}
