package com.example.VisitorManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.VisitorManagementSystem.dto.AddressDto;
import com.example.VisitorManagementSystem.dto.UserDto;
import com.example.VisitorManagementSystem.entity.Address;
import com.example.VisitorManagementSystem.entity.Flat;
import com.example.VisitorManagementSystem.entity.User;
import com.example.VisitorManagementSystem.enums.Role;
import com.example.VisitorManagementSystem.enums.UserStatus;
import com.example.VisitorManagementSystem.exception.BadRequestException;
import com.example.VisitorManagementSystem.repo.FlatRepo;
import com.example.VisitorManagementSystem.repo.UserRepo;

@Service
public class AdminService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FlatRepo flatRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Long createUser( UserDto userDto) throws BadRequestException {
        Flat flat = null;
        if(Role.RESIDENT.name().equals(userDto.getRole()) && userDto.getFlatNo()==null){
            throw  new BadRequestException("RESIDENT must have a flatNumber");
        }
        else {
            flat = flatRepo.findByNumber(userDto.getFlatNo());
        }
        String password = "12345";
        if(userDto.getPassword()!=null){
            password = userDto.getPassword();
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phone(userDto.getPhone())
                .role(Role.valueOf(userDto.getRole()))
                .status(UserStatus.ACTIVE)
                .idNumber(userDto.getIdNumber())
                .password(passwordEncoder.encode(password))
                .flat(flat)
                .build();
        if(userDto.getAddress() != null){
            AddressDto addressDto = userDto.getAddress();
            Address address = Address.builder()
                    .line1(addressDto.getLine1())
                    .line2(addressDto.getLine1())
                    .pincode(addressDto.getPincode())
                    .city(addressDto.getCity())
                    .country(addressDto.getCountry())
                    .build();
            user.setAddress(address);
        }
        user = userRepo.save(user);
        return user.getId();
    }


}
