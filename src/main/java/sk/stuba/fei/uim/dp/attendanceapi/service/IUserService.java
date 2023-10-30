package sk.stuba.fei.uim.dp.attendanceapi.service;

import sk.stuba.fei.uim.dp.attendanceapi.dto.SignupDto;
import sk.stuba.fei.uim.dp.attendanceapi.entity.User;

public interface IUserService {
    public void create(SignupDto signupDto);
}
