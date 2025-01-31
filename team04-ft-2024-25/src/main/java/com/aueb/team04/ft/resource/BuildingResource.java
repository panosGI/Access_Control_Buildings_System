package com.aueb.team04.ft.resource;

import com.aueb.team04.ft.domain.Admin;
import com.aueb.team04.ft.domain.Building;
import com.aueb.team04.ft.domain.Employee;
import com.aueb.team04.ft.persistence.*;
import com.aueb.team04.ft.representation.BuildingMapper;
import com.aueb.team04.ft.representation.BuildingRepresentation;
import com.google.errorprone.annotations.ThreadSafe;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

@ThreadSafe
@RequestScoped
@Path(AccessControlURI.BUILDING)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BuildingResource {
    @Inject
    BuildingRepository buildingRepository;

    @Inject
    BuildingMapper buildingMapper;

    @Context
    SecurityContext securityContext;

    @Context
    UriInfo uriInfo;

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    AccessPointResource accessPointResource;

    @GET
    @RolesAllowed("ADMIN")
    public Response getBuildings() {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can view all buildings").build();
        }
        List<Building> buildings = buildingRepository.findAllBuildings();
        return Response.ok(buildings).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public Response getBuilding(@PathParam("id") Long id) {
        Building building = buildingRepository.findByID(id);
        if (building == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Building not found").build();
        }

        BuildingRepresentation buildingRepresentation = buildingMapper.toRepresentation(building);

        // Get the user's role using the security context
        if(securityContext.isUserInRole("ADMIN")) {
            // If the user is an admin, return the building representation
            return Response.ok(buildingRepresentation).build();
        } else {
            // If the user is an employee, check if the employee is allowed to access the building
            String employeeUsername = securityContext.getUserPrincipal().getName();
            Employee employee = employeeRepository.findByUsername(employeeUsername);

            boolean hasAccess = employee.getAccessCard().getAccessPermissions().stream()
                    .anyMatch(accessPermission -> accessPermission.getBuilding().getId().equals(id));

            if (hasAccess) {
                return Response.ok(buildingRepresentation).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("You do not have access to this building").build();
            }
        }
    }

    @POST
    @RolesAllowed("ADMIN")
    @Transactional
    public Response createBuilding(CreateBuildingRequest createBuildingRequest) {
        // Validate admin role via security context
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can create buildings").build();
        }

        // Validate the building representation
        BuildingRepresentation buildingRepresentation = createBuildingRequest.getBuilding();
        if (buildingRepresentation == null || buildingRepresentation.name == null || buildingRepresentation.address == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request payload").build();
        }

        // Use the mapper to convert the representation to a domain model
        Building building = buildingMapper.toModel(buildingRepresentation);

        // Persist the building entity
        buildingRepository.save(building);

        // Create response object
        BuildingRepresentation createdBuilding = buildingMapper.toRepresentation(building);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(building.getId().toString()).build())
                .entity(createdBuilding) // Return the created building representation
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response updateBuilding(@PathParam("id") Long id, BuildingRepresentation buildingRepresentation) {
        // Validate admin role via security context
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can update buildings").build();
        }

        // Validate the building representation
        if (buildingRepresentation == null || buildingRepresentation.name == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request payload").build();
        }

        // Find the building entity
        Building building = buildingRepository.findByID(id);
        if (building == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Building not found").build();
        }

        // Update the building entity
        building.setName(buildingRepresentation.name);
        building.setAddress(buildingRepresentation.address);
        building.setBelongsTo(buildingRepresentation.belongsTo);

        // Persist the building entity
        buildingRepository.save(building);

        // Create response object
        BuildingRepresentation updatedBuilding = buildingMapper.toRepresentation(building);

        // return the updated building and the path to the updated building
        return Response.created(uriInfo.getAbsolutePathBuilder().path("").build())
                .entity(updatedBuilding)
                .build();
    }

    @Path("/{buildingId}/access_point")
    public AccessPointResource getAccessPoints(@PathParam("buildingId") Long buildingId, @Context SecurityContext securityContext) {
        if (buildingRepository.findByID(buildingId) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Building not found").build().readEntity(AccessPointResource.class);
        }
        accessPointResource.setBuildingId(buildingId);
        accessPointResource.setSecurityContext(securityContext);
        return accessPointResource;
    }

    // This class is used to create a request object for the createBuilding method
    public static class CreateBuildingRequest {
        private Admin admin;
        private BuildingRepresentation building;

        // Getters and setters
        public Admin getAdmin() {
            return admin;
        }

        public void setAdmin(Admin admin) {
            this.admin = admin;
        }

        public BuildingRepresentation getBuilding() {
            return building;
        }

        public void setBuilding(BuildingRepresentation building) {
            this.building = building;
        }
    }
}
