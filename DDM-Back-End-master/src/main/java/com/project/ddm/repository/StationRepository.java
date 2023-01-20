package com.project.ddm.repository;

import com.project.ddm.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findAll();

    // need to find station by station id
    Station findStationById(Long stationId);
}