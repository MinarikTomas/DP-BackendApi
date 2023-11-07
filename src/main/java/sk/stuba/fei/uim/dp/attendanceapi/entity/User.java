package sk.stuba.fei.uim.dp.attendanceapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "createdBy")
    private List<Activity> myActivities = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Card> cards = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "participant",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id"))
    private List<Activity> attendedActivities = new ArrayList<>();

    public User(String fullName, String email, String password){
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}