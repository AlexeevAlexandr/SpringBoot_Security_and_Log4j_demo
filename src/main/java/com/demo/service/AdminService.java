package com.demo.service;

import com.demo.entity.Admin;

import java.util.List;

public interface AdminService {

    List<Admin> findAll();

    void create(Admin admin);

    Admin update(Admin admin);
}
