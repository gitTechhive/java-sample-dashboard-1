package com.sampledashboard1.repository;

import com.sampledashboard1.filter.DropdownResponse;
import com.sampledashboard1.model.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitiesRepository extends JpaRepository<Cities, Long> {

    @Query("select new com.sampledashboard1.filter.DropdownResponse(c.name,c.id) from Cities as c where c.state.id=:id  order by c.name")
    List<DropdownResponse> getCitiesDropdown(Long id);
}
