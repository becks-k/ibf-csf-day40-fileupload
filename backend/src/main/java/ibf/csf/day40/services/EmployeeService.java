package ibf.csf.day40.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibf.csf.day40.models.Employee;
import ibf.csf.day40.repositories.EmployeeRepo;
import ibf.csf.day40.repositories.S3Repo;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepo empRepo;

    @Autowired
    private S3Repo s3Repo;

    public Optional<List<Employee>> getAllEmployees() {
        return empRepo.getAllEmployees();
    }

    public Optional<Employee> getEmployeeById1(int id) {
        return empRepo.getEmployeeById1(id);
    }

    public Optional<Employee> getEmployeeById2(int id) {
        return empRepo.getEmployeeById2(id);
    }

    public int saveEmployee(Employee emp, MultipartFile file) throws IOException {
        String url = s3Repo.saveToS3(file, emp.getFirstName(), emp.getLastName());
        emp.setProfileUrl(url);
        return empRepo.saveEmployee(emp);
    }

    public int updateEmployee(Employee emp, MultipartFile file) throws IOException {
        String url = s3Repo.saveToS3(file, emp.getFirstName(), emp.getLastName());
        emp.setProfileUrl(url);
        return empRepo.updateEmployee(emp);
    }

    public int deleteEmployee(int id) {
        return empRepo.deleteEmployee(id);
    }

    public JsonObject toJsonEmployee(Employee emp) {
        return Json.createObjectBuilder()
            .add("id", emp.getId())
            .add("firstName", emp.getFirstName())
            .add("lastName", emp.getLastName())
            .add("email", emp.getEmail())
            .add("profileUrl", emp.getProfileUrl())
            .build();
    }
}
