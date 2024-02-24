package sk.stuba.fei.uim.dp.attendanceapi.service;

import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.dp.attendanceapi.entity.Role;
import sk.stuba.fei.uim.dp.attendanceapi.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    RoleRepository repository;


    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> getAll(){
        return this.repository.findAll();
    }
    public void save(String role){
        this.repository.save(new Role(role));
    }
    public void delete(Role role){
        this.repository.delete(role);
    }
}
