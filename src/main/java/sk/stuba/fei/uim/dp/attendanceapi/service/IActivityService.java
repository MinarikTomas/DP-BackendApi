package sk.stuba.fei.uim.dp.attendanceapi.service;

import sk.stuba.fei.uim.dp.attendanceapi.entity.Activity;
import sk.stuba.fei.uim.dp.attendanceapi.request.ActivityRequest;

public interface IActivityService {
    void createActivity(ActivityRequest request);
    Activity getById(Integer id);
    void startActivity(Integer id);
    void endActivity(Integer id);
    void deleteActivity(Integer id);

}
