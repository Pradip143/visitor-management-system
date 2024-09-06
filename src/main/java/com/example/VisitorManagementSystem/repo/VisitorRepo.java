package com.example.VisitorManagementSystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VisitorManagementSystem.entity.Visitor;

@Repository
public interface VisitorRepo extends JpaRepository<Visitor,Long> {

    Visitor findByIdNumber(String idNumber);
}
