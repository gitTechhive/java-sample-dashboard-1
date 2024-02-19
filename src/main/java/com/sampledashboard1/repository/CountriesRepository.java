package com.sampledashboard1.repository;

import com.sampledashboard1.filter.DropdownResponse;
import com.sampledashboard1.model.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Long> {
    @Query("select new com.sampledashboard1.filter.DropdownResponse(c.name,c.id) from Countries as c  order by c.name")
    List<DropdownResponse> getCountriesDropdown();


    @Query("select new com.sampledashboard1.filter.DropdownResponse(CONCAT(c.phonecode ,' ', '(',c.name,')'),CONCAT(c.phonecode ,' ', '(',c.name,')')) from Countries as c  order by c.name")
    List<DropdownResponse> getCountriesDropdownFlag();
}
