package com.example.VisitorManagementSystem.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VisitorManagementSystem.entity.Flat;

@Repository
public interface FlatRepo extends JpaRepository<Flat, Long> {
    Flat findByNumber(String number);
}
