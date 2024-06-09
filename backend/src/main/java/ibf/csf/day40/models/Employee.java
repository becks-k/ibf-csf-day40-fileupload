package ibf.csf.day40.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String profileUrl;

}
