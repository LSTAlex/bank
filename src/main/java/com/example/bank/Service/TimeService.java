package com.example.bank.Service;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Data
public class TimeService {
    private LocalDateTime localDateTime;

    @PostConstruct
    public void init(){
        localDateTime = LocalDateTime.now();
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void forwardTime(int Days) {
        localDateTime = localDateTime.plusDays(Days);
        System.out.println("Time: " + localDateTime);
    }
}
