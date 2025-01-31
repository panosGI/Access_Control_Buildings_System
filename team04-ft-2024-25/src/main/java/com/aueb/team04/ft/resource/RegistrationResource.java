package com.aueb.team04.ft.resource;

import com.aueb.team04.ft.domain.Employee;
import com.aueb.team04.ft.persistence.EmployeeRepository;
import com.aueb.team04.ft.representation.EmployeeMapper;
import com.aueb.team04.ft.representation.EmployeeRepresentation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;

@RequestScoped
@Path(AccessControlURI.REGISTRATION)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationResource {

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    EmployeeMapper employeeMapper;

    @POST
    @Transactional
    public Response submitEmployeeInfo(EmployeeRepresentation employeeRepresentation) {
        if (employeeRepresentation.username == null || employeeRepresentation.password == null || employeeRepresentation.email == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username, password, and email are required")
                    .build();
        }

        if (employeeRepository.existsByUsername(employeeRepresentation.username)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username is already in use")
                    .build();
        }

        if (employeeRepository.existsByEmail(employeeRepresentation.email)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email is already in use")
                    .build();
        }

        Employee employee = employeeMapper.toModel(employeeRepresentation);
        employeeRepository.save(employee);

        return Response.status(Response.Status.CREATED)
                .entity(employeeRepresentation)
                .build();
    }
}