package sk.stuba.fei.uim.dp.attendanceapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "group_id")
    private Integer groupId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "activity")
    private List<Participant> participants = new ArrayList<>();

    public Activity(User user, String name, String location, LocalDateTime time){
        this.createdBy = user;
        this.name = name;
        this.location = location;
        this.time = time;

    }

    public String getFormattedCreatedBy(){
        if(this.createdBy == null){
            return "-";
        }
        return this.createdBy.getId() + " " + this.createdBy.getFullName();
    }

    public String getFormattedStartTime(){
        if(this.startTime == null){
            return "-";
        }
        return this.startTime.toString();
    }

    public String getFormattedEndTime(){
        if(this.endTime == null){
            return "-";
        }
        return this.endTime.toString();
    }

}