package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessCard;
import com.aueb.team04.ft.domain.Employee;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class EmployeeRepositoryTest {
    @Inject
    EmployeeRepository employeeRepository;

    @Test
    public void testFindByID() {
        assertNotNull(employeeRepository.findByID(1111L));
    }

    @Test
    public void testListAllEmployees() {
        assertNotNull(employeeRepository.listAllEmployees());
    }

    @Test
    @Transactional
    public void testSave() {
        Employee employee = new Employee();
        employeeRepository.save(employee);
        assertNotNull(employeeRepository.findByID(employee.getId()));
    }

    @Test
    public void testExistsByEmail() {
        assertNotNull(employeeRepository.existsByEmail("employee1111@email"));
    }

    @Test
    public void testExistsByEmailAndEmailNotExist() {
        assertFalse(employeeRepository.existsByEmail("emailNotExist"));
    }

    @Test
    public void testFindByUsername() {
        assertNotNull(employeeRepository.findByUsername("employee1111"));
    }

    @Test
    public void testExistsByUsername() {
        assertNotNull(employeeRepository.existsByUsername("employee1111"));
    }

    @Test
    public void testExistsByUsernameAndUsernameNotExist() {
        assertFalse(employeeRepository.existsByUsername("usernameNotExist"));
    }

    @Test
    @Transactional
    public void testDeleteEmployeeById() {
        AccessCard accessCard = employeeRepository.findByID(2222L).getAccessCard();
        accessCard.setEmployeeOfCard(null);

        employeeRepository.deleteEmployeeById(2222L);
        assertNull(employeeRepository.findByID(2222L));
    }
}
