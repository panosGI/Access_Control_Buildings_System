package com.aueb.team04.ft.resource;

import com.aueb.team04.ft.domain.AccessCard;
import com.aueb.team04.ft.domain.Employee;
import com.aueb.team04.ft.persistence.AccessCardRepository;
import com.aueb.team04.ft.persistence.EmployeeRepository;
import com.aueb.team04.ft.representation.EmployeeMapper;
import com.aueb.team04.ft.representation.EmployeeRepresentation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

@RequestScoped
@Path(AccessControlURI.EMPLOYEE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {
    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    EmployeeMapper employeeMapper;

    @Context
    SecurityContext securityContext;

    @Inject
    AccessCardRepository accessCardRepository;

    @GET
    @RolesAllowed({"ADMIN"})
    public Response getEmployees() {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can view access requests").build();
        }
        List<Employee> employees = employeeRepository.listAllEmployees();
        List<Employee> employeeRepresentations = employeeMapper.toRepresentationList(employees);
        return Response.ok(employeeRepresentations).build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Path("/{employeeID}")
    public Response getEmployee(@PathParam("employeeID") Long employeeID) {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can view all active employees").build();
        }
        Employee employee = employeeRepository.findByID(employeeID);
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build();
        }
        EmployeeRepresentation employeeRepresentation = employeeMapper.toRepresentation(employee);
        return Response.ok(employeeRepresentation).build();
    }

    @DELETE
    @Transactional
    @RolesAllowed({"ADMIN"})
    @Path("/{employeeID}")
    public Response deleteEmployeeById(@PathParam("employeeID") Long employeeID) {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can delete employees").build();
        }
        Employee employee = employeeRepository.findByID(employeeID);
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build();
        }

        // Unassign the access card from the employee
        AccessCard accessCard = employee.getAccessCard();
        accessCard.setEmployeeOfCard(null);

        // Delete the access card and the employee
        accessCardRepository.deleteAccessCardById(accessCard.getId());
        employeeRepository.deleteEmployeeById(employeeID);

        return Response.ok("Employee deleted successfully").build();
    }
}