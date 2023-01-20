package com.project.ddm.service;

import com.project.ddm.exception.OrderNotExistException;
import com.project.ddm.model.Order;
import com.project.ddm.model.User;
import com.project.ddm.repository.TrackRepository;
import com.project.ddm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private TrackRepository trackRepository;
    private UserRepository userRepository;

    @Autowired
    public TrackService(TrackRepository trackRepository,UserRepository userRepository) {

        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
    }

    public Order findByTrackId(Long trackId) throws OrderNotExistException {
        Order order = trackRepository.findOrderByTrackId(trackId);
        if (order == null) {
            throw new OrderNotExistException("The order doesn't exist. Please enter correct tracking number.");
        }
        return order;
    }

    public List<Order> findByUser(Long userId) throws OrderNotExistException{
        User user =  userRepository.findById(userId);
        List<Order> orders = user.getOrders();
        if (orders.isEmpty()) {
            throw new OrderNotExistException("History orders doesn't exist.");
        }
        return orders;
    }
}
