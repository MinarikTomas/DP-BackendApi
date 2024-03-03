package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Participant;
import sk.stuba.fei.uim.dp.attendanceapi.repository.ParticipantRepository;

import java.util.List;

@Service
public class ParticipantService {
    private ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> getAllByActivityId(Integer id){
        return participantRepository.findByActivity_Id(id);
    }

    public void save(Participant participant){
        this.participantRepository.save(participant);
    }

    public void delete(Participant participant){
        this.participantRepository.delete(participant);
    }
}
