package sk.stuba.fei.uim.dp.attendanceapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "participant",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id"))
    private List<Activity> activities = new ArrayList<>();

    public Card(User user, String name, String serialNumber){
        this.user = user;
        this.name = name;
        this.serialNumber = serialNumber;
    }
}