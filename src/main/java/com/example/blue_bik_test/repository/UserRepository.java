package com.example.blue_bik_test.repository;

import com.example.blue_bik_test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u FROM User u where u.fullName LIKE CONCAT('%',:username,'%')")
    List<User> findAllByUsername(String username);

    User findAllById(Long id);
}
