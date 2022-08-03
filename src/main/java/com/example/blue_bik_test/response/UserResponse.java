package com.example.blue_bik_test.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String fullName;
    private String address;
    private String gender;
    private String phoneNumber;
    private String email;
    private Integer age;
}
