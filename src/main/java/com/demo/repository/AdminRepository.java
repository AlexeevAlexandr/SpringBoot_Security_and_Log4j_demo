package com.demo.repository;

import com.demo.entity.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminRepository extends CrudRepository<Admin, Integer> {

    @Override
    List<Admin> findAll();

    Admin findById(int id);
}
