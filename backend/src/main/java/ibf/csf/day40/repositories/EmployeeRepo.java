package ibf.csf.day40.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ibf.csf.day40.models.Employee;

@Repository
public class EmployeeRepo {
    
    @Autowired
    private JdbcTemplate template;

    
    private static final String GET_ALL_EMPLOYEES = """
            select * from employees
            """;

    private static final String GET_EMPLOYEE_BY_ID = """
            select * from employees where id = ?
            """;
    
    private static final String SAVE_EMPLOYEE = """
            insert into employees (first_name, last_name, email, profile_url) values (?, ?, ?, ?)
            """;
    
    private static final String UPDATE_EMPLOYEE = """
            update employees set first_name = ?, last_name = ?, email = ?, profile_url = ? where id = ?
            """;
    
    private static final String DELETE_EMPLOYEE = """
            delete from employees where id = ?
            """;

    public Optional<List<Employee>> getAllEmployees() {
        List<Employee> employees = new LinkedList<>();
        SqlRowSet rs = template.queryForRowSet(GET_ALL_EMPLOYEES);
        
        while (rs.next()) {
            employees.add(createEmployee(rs));
        }
        if (employees.isEmpty()) {
            return Optional.empty();
        }
        // System.out.println(">>>>>>>>>>>>>> Repo list of emp: " + employees.toString());
        return Optional.of(employees);
    }

    public Optional<Employee> getEmployeeById1(int id) {
        try {
            Employee emp = template.queryForObject(GET_EMPLOYEE_BY_ID, (rs, rowNum) -> {
                return new Employee(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("profile_url")
                );
            }, id);
            return Optional.ofNullable(emp);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Employee> getEmployeeById2(int id) {
        try {
            Employee emp = template.queryForObject(GET_EMPLOYEE_BY_ID, BeanPropertyRowMapper.newInstance(Employee.class), id);
            return Optional.ofNullable(emp);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }       
    }

    public int saveEmployee(Employee emp) {
        return template.update(SAVE_EMPLOYEE, emp.getFirstName(), emp.getLastName(), emp.getEmail(), emp.getProfileUrl());
    }

    public int updateEmployee(Employee emp) {
        return template.update(UPDATE_EMPLOYEE, emp.getFirstName(), emp.getLastName(), emp.getEmail(), emp.getProfileUrl(), emp.getId());
    }

    public int deleteEmployee(int id) {
        return template.update(DELETE_EMPLOYEE, id);
    }

    private Employee createEmployee(SqlRowSet rs) {
        Employee emp = new Employee();
        emp.setId(rs.getInt("id"));
        emp.setFirstName(rs.getString("first_name"));
        emp.setLastName(rs.getString("last_name"));
        emp.setEmail(rs.getString("email"));
        emp.setProfileUrl(rs.getString("profile_url"));
        return emp;
    }

}
