package ibf.csf.day40.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import ibf.csf.day40.exceptions.RequestMessage;
import ibf.csf.day40.models.Employee;
import ibf.csf.day40.services.EmployeeService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping
public class EmployeeController {
    
    @org.springframework.beans.factory.annotation.Autowired
    private EmployeeService empSvc;
    
    @GetMapping("/employees")
    public ResponseEntity<String> getAllEmployees() {
        Optional<List<Employee>> opt = empSvc.getAllEmployees();
        if (opt.isEmpty()) {
            return ResponseEntity.status(404)
                .body(new RequestMessage("There are no employees in the database").toString());
        }
        JsonArrayBuilder jArray = Json.createArrayBuilder();
        opt.get().stream()
            .forEach(emp -> {
                JsonObject jObj = empSvc.toJsonEmployee(emp);
                jArray.add(jObj);
            });
        // System.out.println(">>>>>>>>>> List of emp: " + jArray.build().toString());
        return ResponseEntity.status(200).body(jArray.build().toString());
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<String> getEmployeeById(@PathVariable("id") String id) {
        Optional<Employee> opt = empSvc.getEmployeeById1(Integer.parseInt(id));
        if (opt.isEmpty()) {
            return ResponseEntity.status(404)
                .body(new RequestMessage(String.format("There is no employee with id %s", id)).toString()); 
        }
        Employee emp = opt.get();
        return ResponseEntity.status(200).body(empSvc.toJsonEmployee(emp).toString());
    }

    // file will be saved 
    @PostMapping(path="/save", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveEmployee(@RequestPart MultipartFile file, @RequestPart String firstName, @RequestPart String lastName, @RequestPart String email) throws IOException {
        Employee emp = new Employee();
        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        emp.setEmail(email);
        // save to employee repo
        int success = empSvc.saveEmployee(emp, file);
        if (success == 0) {
            return ResponseEntity.status(404)
                .body(new RequestMessage("Unsuccessful save").toString());
        }
        return ResponseEntity.status(200).body(new RequestMessage("Successfully saved").toString());
    }

    // file will be updated 
    @PostMapping(path="/update/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateEmployee(@PathVariable("id") String id, @RequestPart MultipartFile file, @RequestPart String firstName, @RequestPart String lastName, @RequestPart String email) throws IOException {
        // save file to s3, generate img url
        // save to employee object
        Employee emp = new Employee();
        emp.setId(Integer.parseInt(id));
        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        emp.setEmail(email);
        // save to employee repo
        int success = empSvc.updateEmployee(emp, file);
        if (success == 0) {
            return ResponseEntity.status(404)
                .body(new RequestMessage("Unsuccessful update").toString());
        }
        return ResponseEntity.status(200).body(new RequestMessage("Successfully updated").toString());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") int id) {
        // System.out.println(">>>>>>>>>>ID: " + id);
        // System.out.println(">>>>>>>>>> DELETED");
        int success = empSvc.deleteEmployee(id);
        if (success == 0) {
            return ResponseEntity.status(404)
                .body(new RequestMessage("Unsuccessful delete").toString());
        }
        return ResponseEntity.status(200).body(new RequestMessage("Successfully deleted").toString());
    }


}
