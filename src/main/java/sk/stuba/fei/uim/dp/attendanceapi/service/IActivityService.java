package sk.stuba.fei.uim.dp.attendanceapi.service;

import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;
import sk.stuba.fei.uim.dp.attendanceapi.request.ActivityRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.EditActivityRequest;
import sk.stuba.fei.uim.dp.attendanceapi.request.SerialNumberRequest;

public interface IActivityService {
    void createActivity(ActivityRequest request);
    Activity getById(Integer id);
    void startActivity(Integer id);
    void endActivity(Integer id);
    Activity update(Integer id, EditActivityRequest request);
    void deleteActivity(Integer id);
    User addParticipant(Integer id, SerialNumberRequest request);
}
