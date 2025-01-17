package com.example.VisitorManagementSystem.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.VisitorManagementSystem.dto.AddressDto;
import com.example.VisitorManagementSystem.dto.VisitDto;
import com.example.VisitorManagementSystem.dto.VisitorDTO;
import com.example.VisitorManagementSystem.entity.*;
import com.example.VisitorManagementSystem.enums.Role;
import com.example.VisitorManagementSystem.enums.UserStatus;
import com.example.VisitorManagementSystem.enums.VisitStatus;
import com.example.VisitorManagementSystem.exception.BadRequestException;
import com.example.VisitorManagementSystem.exception.NotFoundException;
import com.example.VisitorManagementSystem.repo.FlatRepo;
import com.example.VisitorManagementSystem.repo.VisitRepo;
import com.example.VisitorManagementSystem.repo.VisitorRepo;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class GateKeeperService {

//    @Autowired
//    private EntityManager entityManager;


    @Autowired
    private RedisTemplate<String,VisitorDTO> redisTemplate;

    @Autowired
    private VisitorRepo visitorRepo;

    @Autowired
    private FlatRepo flatRepo;

    @Autowired
    private VisitRepo visitRepo;

    public Long createVisitor(VisitorDTO visitorDTO){
        Visitor visitor = Visitor.builder()
                .email(visitorDTO.getEmail())
                .name(visitorDTO.getName())
                .phone(visitorDTO.getPhone())
                .idNumber(visitorDTO.getIdNumber())
                .build();
        if(visitorDTO.getAddress() != null){
            AddressDto addressDto = visitorDTO.getAddress();
            Address address = Address.builder()
                    .line1(addressDto.getLine1())
                    .line2(addressDto.getLine1())
                    .pincode(addressDto.getPincode())
                    .city(addressDto.getCity())
                    .country(addressDto.getCountry())
                    .build();
            visitor.setAddress(address);
        }
//        entityManager.persist(visitor);
        visitor = visitorRepo.save(visitor);
        return visitor.getId();
    }

    public VisitorDTO getByIdNumber(String idNumber){
        String key = "visitor:"+idNumber;
        VisitorDTO visitorDTO = redisTemplate.opsForValue().get(key);
        if(visitorDTO != null){
            return visitorDTO;
        }
        Visitor visitor = visitorRepo.findByIdNumber(idNumber);
        if(visitor != null){
            visitorDTO = VisitorDTO.builder()
                    .email(visitor.getEmail())
                    .name(visitor.getName())
                    .phone(visitor.getPhone())
                    .idNumber(visitor.getIdNumber())
                    .build();
            redisTemplate.opsForValue().set(key,visitorDTO,60, TimeUnit.MINUTES);
        }
        else {
            throw new NotFoundException();
        }
        return visitorDTO;
    }


    public Long createVisit(VisitDto visitDto){
        Visitor visitor = visitorRepo.findByIdNumber(visitDto.getIdNumber());
        if(visitor == null){
            throw new BadRequestException("Visitor does not exist");
        }
        Flat flat = flatRepo.findByNumber(visitDto.getFlatNumber());
        if(flat == null){
            throw new BadRequestException("Flat does not exist");
        }
        User gatekeeper = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Visit visit = Visit.builder()
                .imageUrl(visitDto.getUrlOfImage())
                .noOfPeople(visitDto.getNoOfPeople())
                .purpose(visitDto.getPurpose())
                .visitor(visitor)
                .flat(flat)
                .status(VisitStatus.WAITING)
                .createdBy(gatekeeper)
                .build();
        visit = visitRepo.save(visit);
        return visit.getId();
    }

    public String markEntry(Long id){
        Visit visit = visitRepo.findById(id).get();
        if(visit == null){
            throw new BadRequestException("Visit does not exist");
        }
        User gatekeeper = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!gatekeeper.equals(visit.getCreatedBy())){
            throw new BadRequestException("This GateKeeper is different");
        }
        if(visit.getStatus().equals(VisitStatus.APPROVED)){
            visit.setInTime(new Date());
            visitRepo.save(visit);
            return "Done";
        }
        else {
            throw new BadRequestException("Can not mark this");
        }
    }

    public String markExit(Long id){
        Visit visit = visitRepo.findById(id).get();
        if(visit == null){
            throw new BadRequestException("Visit does not exist");
        }
        if(visit.getStatus().equals(VisitStatus.APPROVED) && visit.getInTime() != null){
            visit.setOutTime(new Date());
            visit.setStatus(VisitStatus.COMPLETED);
            visitRepo.save(visit);
            return "Done";
        }
        else {
            throw new BadRequestException("Can not mark this");
        }
    }
}
