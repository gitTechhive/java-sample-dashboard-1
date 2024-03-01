package com.sampledashboard1.repository;

import com.sampledashboard1.model.Charts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartsRepository extends JpaRepository<Charts, Long> {
}
