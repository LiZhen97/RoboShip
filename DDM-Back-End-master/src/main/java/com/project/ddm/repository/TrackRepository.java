package com.project.ddm.repository;

import com.project.ddm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Order, Integer> {
    Order findOrderByTrackId(int trackId);
}
