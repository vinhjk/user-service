package com.example.blue_bik_test.service.impl;

import com.example.blue_bik_test.entity.User;
import com.example.blue_bik_test.repository.UserRepository;
import com.example.blue_bik_test.request.UserRequest;
import com.example.blue_bik_test.response.UserResponse;
import com.example.blue_bik_test.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.protobuf.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public ResponseEntity<String> createUser(UserRequest request) {
        try {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .fullName(request.getFirstName().concat(" ").concat(request.getLastName()))
                    .address(request.getAddress())
                    .age(request.getAge())
                    .gender(request.getGender())
                    .phoneNumber(request.getPhoneNumber())
                    .email(request.getEmail())
                    .build();
            repository.save(user);
        } catch (Exception e) {
            log.error("Create user failed with error: {}", e.getMessage());
            return new ResponseEntity<>("Create user failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Create user successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createUsers(List<UserRequest> requests) {
        try {
            List<User> users = requests.stream().map(request ->
                            User.builder()
                                    .firstName(request.getFirstName())
                                    .lastName(request.getLastName())
                                    .fullName(request.getFirstName().concat(" ").concat(request.getLastName()))
                                    .address(request.getAddress())
                                    .age(request.getAge())
                                    .gender(request.getGender())
                                    .phoneNumber(request.getPhoneNumber())
                                    .email(request.getEmail())
                                    .build())
                    .collect(Collectors.toList());
            repository.saveAll(users);
        } catch (Exception e) {
            log.error("Create users failed with error: {}", e.getMessage());
            return new ResponseEntity<>("Create users failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Create users successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateUser(UserRequest request, Long id) {
        try {
            User user = repository.findAllById(id);
            if (Objects.isNull(user)) {
                return new ResponseEntity<>("User not exist!", HttpStatus.NOT_FOUND);
            }
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setFullName(request.getFirstName().concat(" ").concat(request.getLastName()));
            user.setAge(request.getAge());
            user.setAddress(request.getAddress());
            user.setGender(request.getGender());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setEmail(request.getEmail());
            repository.save(user);
        } catch (Exception e) {
            log.error("Update user failed with error: {}", e.getMessage());
            return new ResponseEntity<>("Update user failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Update user successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateUserInBulk(List<UserRequest> requests) {
        try {
            List<User> users = new ArrayList<>();
            for (UserRequest request : requests) {
                User user = repository.findAllById(request.getId());
                if (Objects.isNull(user)) {
                    return new ResponseEntity<>("User with id " + request.getId() + " is not exist!", HttpStatus.NOT_FOUND);
                }
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setFullName(request.getFirstName().concat(" ").concat(request.getLastName()));
                user.setAge(request.getAge());
                user.setAddress(request.getAddress());
                user.setGender(request.getGender());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setEmail(request.getEmail());
                users.add(user);
            }
            repository.saveAll(users);
        } catch (Exception e) {
            log.error("Update users failed with error: {}", e.getMessage());
            return new ResponseEntity<>("Update users failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Update users successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e){
            log.error("Delete user failed with error: {}", e.getMessage());
            return new ResponseEntity<>("Delete user failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Delete user successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteUserInBulk(Set<Long> ids) {
        try {
            repository.deleteAllById(ids);
        } catch (Exception e){
            log.error("Delete users failed with error: {}", e.getMessage());
            return new ResponseEntity<>("Delete users failed!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Delete users successfully!", HttpStatus.OK);
    }


    @Override
    public List<UserResponse> getUsers(GetUser keyword) {
        ModelMapper modelMapper = new ModelMapper();
        List<User> users = repository.findAllByUsername(keyword);
        return Arrays.asList(modelMapper.map(users, UserResponse[].class));
    }
}
