package com.example.blue_bik_test.request;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String phoneNumber;
    private String email;
    private Integer age;
}
