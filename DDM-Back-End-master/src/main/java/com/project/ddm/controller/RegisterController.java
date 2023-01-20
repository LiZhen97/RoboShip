package com.project.ddm.controller;


import com.project.ddm.model.User;
import com.project.ddm.model.UserRole;
import com.project.ddm.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register/user")
    public void addUser(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_USER);
    }

    @PostMapping("/register/staff")
    public void addStaff(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_STAFF);
    }
}
