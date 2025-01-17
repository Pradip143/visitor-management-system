package com.example.VisitorManagementSystem.controller;


import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.VisitorManagementSystem.dto.AllVisitsByPageResponseBody;
import com.example.VisitorManagementSystem.dto.VisitDto;
import com.example.VisitorManagementSystem.entity.User;
import com.example.VisitorManagementSystem.enums.VisitStatus;
import com.example.VisitorManagementSystem.service.ResidentService;

import java.util.List;

@RestController
@RequestMapping("/resident")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @PostMapping("/actOnVisit/{id}")
    public ResponseEntity<String> actOnVisit(@PathVariable Long id, @RequestParam VisitStatus visitStatus) throws BadRequestException {
        return ResponseEntity.ok(residentService.updateVisit(visitStatus,id));
    }

    @GetMapping("/pendingVisits")
    public ResponseEntity<List<VisitDto>> getPendingVisits(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(residentService.getPendingVisits(user.getId()));
    }


    @GetMapping("/allVisitsByPage")
    public ResponseEntity<AllVisitsByPageResponseBody> getPendingVisits(@AuthenticationPrincipal User user, @RequestParam Integer pageNo, @RequestParam Integer pageSize){
        Pageable pageable = Pageable.ofSize(pageSize)
                .withPage(pageNo);
        return ResponseEntity.ok(residentService.getAllVisits(user.getId(),pageable));
    }


}
