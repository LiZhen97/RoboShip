package com.project.ddm.repository;

import com.project.ddm.model.Order;
import com.project.ddm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // find orders of a user
    List<Order> findByUser(User user);
    // use its save method
}
