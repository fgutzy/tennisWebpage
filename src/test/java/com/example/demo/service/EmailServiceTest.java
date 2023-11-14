package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    public void testEmailValidation1(){
        assertTrue(emailService.validEmail("ferdi@pm.me"));
    }

    @Test
    public void testEmailValidation2(){
        assertTrue(emailService.validEmail("ferdi@pm.me1.me"));
    }

    @Test
    public void testEmailValidation3(){
        assertFalse(emailService.validEmail("123@123"));
    }
}