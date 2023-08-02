package com.example.core.model.serverLog;

import com.example.core.model.user.User;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
@Table(name = "log_tb")
public class ServerLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String requestIp;

    @CreatedDate
    private Timestamp signInDate;
}