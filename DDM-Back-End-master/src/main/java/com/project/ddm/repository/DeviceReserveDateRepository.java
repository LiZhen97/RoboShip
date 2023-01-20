package com.project.ddm.repository;

import com.project.ddm.model.DeviceReserveDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Set;

@Repository
public interface DeviceReserveDateRepository extends JpaRepository<DeviceReserveDate, Long> {

    @Query("select drd.id.device_id from DeviceReserveDate drd WHERE drd.id.device_id IN ?1 AND drd.id.time > ?2 GROUP BY drd.id.device_id")
    Set<Long> findByIdAndTimeAfter(List<Long> deviceIds, Time currentTime);
}
