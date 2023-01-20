package com.project.ddm.service;

import com.project.ddm.exception.OrderNotExistException;
import com.project.ddm.model.Order;
import com.project.ddm.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    private TrackRepository trackRepository;

    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public Order findByTrackId(int trackId) throws OrderNotExistException {
        Order order = trackRepository.findOrderByTrackId(trackId);
        if (order == null) {
            throw new OrderNotExistException("The order doesn't exist. Please enter correct tracking number.");
        }
        return order;
    }
}
