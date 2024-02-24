package sk.stuba.fei.uim.dp.attendanceapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "card")
    private List<Participant> activities;

    public Card(User user, String name, String serialNumber){
        this.user = user;
        this.name = name;
        this.serialNumber = serialNumber;
    }

    public String getFormattedName(){
        if (this.name == null){
            return "-";
        }
        return this.name;
    }

    public String getFormattedOwner(){
        if (this.user == null){
            return "-";
        }
        return this.user.getId() + " " + this.user.getFullName();
    }
}