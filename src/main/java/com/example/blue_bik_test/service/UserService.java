package com.example.blue_bik_test.service;

import com.example.blue_bik_test.entity.User;
import com.example.blue_bik_test.repository.UserRepository;
import com.example.proto.*;
import com.google.protobuf.Int64Value;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private final UserRepository repository;

    @Override
    public void getUsers(GetUser keyword, StreamObserver<Users> response) {
        List<User> users = Objects.isNull(keyword) ? repository.findAll() : repository.findAllByUsername(keyword.getKeyword());
        List<UserResponse> responses = users.stream().map(u ->
                UserResponse.newBuilder()
                        .setFullName(u.getFullName())
                        .setAddress(u.getAddress())
                        .setGender(u.getGender())
                        .setPhoneNumber(u.getPhoneNumber())
                        .setEmail(u.getEmail())
                        .setAge(u.getAge())
                        .build()).collect(Collectors.toList());
        response.onNext(Users.newBuilder().addAllUserResponse(responses).build());
        response.onCompleted();
    }

    @Override
    public void createUser(UserRequest request, StreamObserver<UserResponse> responses) {
        try {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .fullName(request.getFullName())
                    .address(request.getAddress())
                    .age((int) request.getAge())
                    .gender(request.getGender())
                    .phoneNumber(request.getPhoneNumber())
                    .email(request.getEmail())
                    .build();
            User saveUser = repository.save(user);
            UserResponse response = UserResponse.newBuilder()
                    .setFullName(saveUser.getFullName())
                    .setAddress(saveUser.getAddress())
                    .setGender(saveUser.getGender())
                    .setPhoneNumber(saveUser.getPhoneNumber())
                    .setEmail(saveUser.getEmail())
                    .setAge(saveUser.getAge())
                    .build();
            responses.onNext(response);
            responses.onCompleted();
        } catch (Exception e) {
            log.error("Create user failed with error: {}", e.getMessage());
            responses.onError(new Throwable("Create user failed with error: " + e.getMessage()));
            responses.onCompleted();
        }
    }

    @Override
    public void createUsers(UserRequests requests, StreamObserver<Users> responses) {
        try {
            List<User> users = requests.getUserRequestList().stream().map(request ->
                            User.builder()
                                    .firstName(request.getFirstName())
                                    .lastName(request.getLastName())
                                    .fullName(request.getFirstName().concat(" ").concat(request.getLastName()))
                                    .address(request.getAddress())
                                    .age((int) request.getAge())
                                    .gender(request.getGender())
                                    .phoneNumber(request.getPhoneNumber())
                                    .email(request.getEmail())
                                    .build())
                    .collect(Collectors.toList());
            List<User> saveUsers = repository.saveAll(users);
            List<UserResponse> userResponses = saveUsers.stream().map(res ->
                            UserResponse.newBuilder()
                                    .setFullName(res.getFullName())
                                    .setAddress(res.getAddress())
                                    .setGender(res.getGender())
                                    .setPhoneNumber(res.getPhoneNumber())
                                    .setEmail(res.getEmail())
                                    .setAge(res.getAge())
                                    .build())
                    .collect(Collectors.toList());
            responses.onNext(Users.newBuilder().addAllUserResponse(userResponses).build());
            responses.onCompleted();
        } catch (Exception e) {
            log.error("Create users failed with error: {}", e.getMessage());
            responses.onError(new Throwable("Create users failed with error: " + e.getMessage()));
            responses.onCompleted();
        }
    }

    @Override
    public void updateUser(UserRequest request, StreamObserver<UserResponse> response) {
        try {
            User user = repository.findAllById(request.getId());
            if (Objects.isNull(user)) {
                log.error("User not exist!");
                response.onError(new Throwable("User not exist!"));
                response.onCompleted();
            } else {
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setFullName(request.getFirstName().concat(" ").concat(request.getLastName()));
                user.setAge((int) request.getAge());
                user.setAddress(request.getAddress());
                user.setGender(request.getGender());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setEmail(request.getEmail());
                repository.save(user);

                UserResponse userResponse = UserResponse.newBuilder()
                        .setFullName(user.getFullName())
                        .setAddress(user.getAddress())
                        .setGender(user.getGender())
                        .setPhoneNumber(user.getPhoneNumber())
                        .setEmail(user.getEmail())
                        .setAge(user.getAge())
                        .build();
                response.onNext(userResponse);
                response.onCompleted();
            }
        } catch (Exception e) {
            log.error("Update user failed with error: {}", e.getMessage());
            response.onError(new Throwable("Update user failed with error: " + e.getMessage()));
            response.onCompleted();
        }
    }

    @Override
    public void updateUserInBulk(UserRequests requests, StreamObserver<Users> responses) {
        try {
            List<User> users = new ArrayList<>();
            for (UserRequest request : requests.getUserRequestList()) {
                User user = repository.findAllById(request.getId());
                if (Objects.isNull(user)) {
                    log.error("User not exist!");
                    responses.onError(new Throwable("User not exist!"));
                    responses.onCompleted();
                } else {
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setFullName(request.getFirstName().concat(" ").concat(request.getLastName()));
                    user.setAge((int) request.getAge());
                    user.setAddress(request.getAddress());
                    user.setGender(request.getGender());
                    user.setPhoneNumber(request.getPhoneNumber());
                    user.setEmail(request.getEmail());
                    users.add(user);
                }
            }
            List<User> saveUsers = repository.saveAll(users);
            List<UserResponse> userResponses = saveUsers.stream().map(res ->
                            UserResponse.newBuilder()
                                    .setFullName(res.getFullName())
                                    .setAddress(res.getAddress())
                                    .setGender(res.getGender())
                                    .setPhoneNumber(res.getPhoneNumber())
                                    .setEmail(res.getEmail())
                                    .setAge(res.getAge())
                                    .build())
                    .collect(Collectors.toList());
            responses.onNext(Users.newBuilder().addAllUserResponse(userResponses).build());
            responses.onCompleted();
        } catch (Exception e) {
            log.error("Update users failed with error: {}", e.getMessage());
            responses.onError(new Throwable("Update users failed with error: " + e.getMessage()));
            responses.onCompleted();
        }
    }

    @Override
    public void deleteUser(Int64Value id, StreamObserver<deleteResponse> response) {
        try {
            repository.deleteById(id.getValue());
            response.onNext(deleteResponse.newBuilder().setMessage("Delete user successfully!").build());
            response.onCompleted();
        } catch (Exception e) {
            log.error("Delete user failed with error: {}", e.getMessage());
            response.onError(new Throwable("Delete user failed with error: " + e.getMessage()));
            response.onCompleted();
        }
    }

    @Override
    public void deleteUserInBulk(listIds ids, StreamObserver<deleteResponse> responses) {
        try {
            repository.deleteAllById(ids.getIdsList());
            responses.onNext(deleteResponse.newBuilder().setMessage("Delete users successfully!").build());
            responses.onCompleted();
        } catch (Exception e) {
            log.error("Delete users failed with error: {}", e.getMessage());
            responses.onError(new Throwable("Delete users failed with error: " + e.getMessage()));
            responses.onCompleted();
        }
    }
}
