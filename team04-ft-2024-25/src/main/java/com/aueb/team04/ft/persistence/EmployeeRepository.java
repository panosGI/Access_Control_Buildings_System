package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class EmployeeRepository implements PanacheRepositoryBase<Employee, Long> {
    public Employee findByID(Long id) {
        return find("id", id).firstResult();
    }

    public List<Employee> listAllEmployees() {
        return listAll();
    }

    public void save(Employee employee) {
        persist(employee);
    }

    public boolean existsByEmail(String email) {
        return find("email", email).count() > 0;
    }

    public Employee findByUsername(String username) {
        return find("username", username).firstResult();
    }
    public boolean existsByUsername(String username) {
        return find("username", username).count() > 0;
    }

    public void deleteEmployeeById(Long employeeID) {
        delete("id", employeeID);
    }
}

