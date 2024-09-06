package com.example.VisitorManagementSystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.VisitorManagementSystem.entity.Flat;
import com.example.VisitorManagementSystem.entity.Visit;
import com.example.VisitorManagementSystem.enums.VisitStatus;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitRepo extends JpaRepository<Visit, Long> {

    List<Visit> findByStatusAndFlat(VisitStatus visitStatus, Flat flat);

    List<Visit> findByStatusAndCreatedDateLessThanEqual(VisitStatus visitStatus, Date date);
}
