package com.demo.controller;

import com.demo.entity.Admin;
import com.demo.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public List<Admin> getAll(){
        return adminService.findAll();
    }

    @PutMapping
    public Admin update(@Valid @RequestBody Admin admin){
        return adminService.update(admin);
    }
}
