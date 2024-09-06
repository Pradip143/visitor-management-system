package com.example.VisitorManagementSystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.VisitorManagementSystem.controller.AdminController;
import com.example.VisitorManagementSystem.entity.Visit;
import com.example.VisitorManagementSystem.enums.VisitStatus;
import com.example.VisitorManagementSystem.repo.VisitRepo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration
public class VisitExpireScheduledTask {


    private static Logger LOGGER = LoggerFactory.getLogger(VisitExpireScheduledTask.class);

    @Autowired
    private VisitRepo visitRepo;

  //  @Scheduled(fixedDelay = 3000)
    public void markVisitExpire(){
        LOGGER.info("Marking visits as Expire");
        Date date = new Date();
        List<Visit> visitList = visitRepo.findByStatusAndCreatedDateLessThanEqual(VisitStatus.WAITING,date);
        for (Visit visit : visitList){
            visit.setStatus(VisitStatus.EXPIRE);
        }
        visitRepo.saveAll(visitList);
    }
}
