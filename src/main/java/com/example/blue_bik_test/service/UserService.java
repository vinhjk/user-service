package com.example.blue_bik_test.service;

import com.example.blue_bik_test.request.UserRequest;
import com.example.blue_bik_test.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UserService {
    ResponseEntity<String> createUser(UserRequest request);
    ResponseEntity<String> createUsers(List<UserRequest> requests);
    ResponseEntity<String> updateUser(UserRequest request, Long id);
    ResponseEntity<String> updateUserInBulk(List<UserRequest> requests);
    ResponseEntity<String> deleteUser(Long id);
    ResponseEntity<String> deleteUserInBulk(Set<Long> ids);
    List<UserResponse> getUsers(String keyword, StreamObserver<>);
}
