package com.demo.service;

import com.demo.entity.Admin;
import com.demo.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{

    final static Logger logger = Logger.getLogger(AdminServiceImpl.class);
    private final AdminRepository adminRepository;

    @Override
    public List<Admin> findAll() {
        try {
            logger.info("Attempt to get admin list");
            List<Admin> customers = adminRepository.findAll();
            logger.info("Attempt to get admin list - success");
            return customers;
        } catch (Exception e){
            logger.error("Attempt to get admin list - false\n" + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void create(Admin admin) {
        try {
            logger.info("Attempt to create an admin");
            adminRepository.save(admin);
            logger.info(String.format("Admin created with ID: %d", admin.getId()));
        } catch (Exception e){
            logger.error("Attempt to create an admin - false\n" + e.getMessage());
        }
    }

    @Override
    public Admin update(Admin admin) {
        try {
            logger.info("Attempt to update admin password");
            Admin adminToSave = adminRepository.findByName(admin.getName());
            adminToSave.setPassword(admin.getPassword());
            admin = adminRepository.save(adminToSave);
            logger.info("Attempt to update admin password - success");
            return admin;
        } catch (Exception e){
            logger.error("Attempt to update admin password - false\n" + e.getMessage());
            return null;
        }
    }
}
