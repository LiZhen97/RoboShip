package com.project.ddm.controller;

import com.project.ddm.model.Order;
import com.project.ddm.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackOrderController {

    private TrackService trackService;

    @Autowired
    public TrackOrderController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(value = "/track/{trackId}")
    public Order getOrder(@PathVariable int trackId) {
        return trackService.findByTrackId(trackId);
    }

}
