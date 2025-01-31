package com.aueb.team04.ft.representation;

import com.aueb.team04.ft.domain.Employee;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class EmployeeMapper {
    public abstract EmployeeRepresentation toRepresentation(Employee employee);
    public abstract Employee toModel(EmployeeRepresentation employeeRepresentation);
    public abstract List<Employee> toRepresentationList(List<Employee> employees);
}
