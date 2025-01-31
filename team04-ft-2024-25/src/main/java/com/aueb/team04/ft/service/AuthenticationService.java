package com.aueb.team04.ft.service;

import com.aueb.team04.ft.domain.Admin;
import com.aueb.team04.ft.domain.Employee;
import com.aueb.team04.ft.persistence.AdminRepository;
import com.aueb.team04.ft.persistence.EmployeeRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class AuthenticationService {

    @Inject
    AdminRepository adminRepository;

    @Inject
    EmployeeRepository employeeRepository;

    public AuthenticatedUser authenticate(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return new AuthenticatedUser(admin, UserType.ADMIN);
        }

        Employee employee = employeeRepository.findByUsername(username);
        if (employee != null && employee.getPassword().equals(password)) {
            return new AuthenticatedUser(employee, UserType.EMPLOYEE);
        }

        return new AuthenticatedUser(null, UserType.NONE);
    }


    public static class AuthenticatedUser {
        private final Object user;
        private final UserType userType;

        public AuthenticatedUser(Object user, UserType userType) {
            this.user = user;
            this.userType = userType;
        }

        public Object getUser() {
            return user;
        }

        public UserType getUserType() {
            return userType;
        }
    }

    public enum UserType {
        ADMIN, EMPLOYEE, NONE
    }
}