package com.aueb.team04.ft.resource;

import com.aueb.team04.ft.domain.*;
import com.aueb.team04.ft.persistence.*;
import com.aueb.team04.ft.representation.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.Inject;

import java.util.List;

@RequestScoped
@Path(AccessControlURI.ACCESS_REQUESTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessRequestResource {

    @Inject
    AccessRequestRepository accessRequestRepository;

    @Inject
    AccessRequestMapper accessRequestMapper;

    @Inject
    BuildingRepository buildingRepository;

    @Context
    UriInfo uriInfo;

    @Context
    SecurityContext securityContext;

    @Inject
    AccessPermissionRepository accessPermissionRepository;

    @Inject
    AccessCardRepository accessCardRepository;

    @Inject
    EmployeeRepository employeeRepository;

    @GET
    @RolesAllowed({"ADMIN"})
    public Response getAccessRequests() {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can view access requests").build();
        }
        List<AccessRequest> accessRequests = accessRequestRepository.findAllAccessRequests();
        List<AccessRequestRepresentation> accessRequestRepresentations = accessRequestMapper.toRepresentationList(accessRequests);
        return Response.ok(accessRequests).build();
    }

    @GET
    @RolesAllowed({"EMPLOYEE"})
    @Path("/{requestID}")
    public Response getAccessRequest(@PathParam("requestID") Long requestID) {
        AccessRequest accessRequest = accessRequestRepository.findByAccessRequestId(requestID);
        if (accessRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Access request not found").build();
        }

        AccessRequestRepresentation accessRequestRepresentation = accessRequestMapper.toRepresentation(accessRequest);
        return Response.ok(accessRequest).build();
    }

    @PUT
    @RolesAllowed({"ADMIN"})
    @Transactional
    @Path("/{requestID}/approve")
    public Response approveAccessRequest(@PathParam("requestID") Long requestID) {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can approve access requests").build();
        }

        // Find the access request by ID
        AccessRequest accessRequest = accessRequestRepository.findByAccessRequestId(requestID);

        // Check if the access request exists
        if (accessRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Access request not found").build();
        }

        // Approve the access request
        accessRequest.approve();

        // Save the access request
        accessRequestRepository.saveAccessRequest(accessRequest);

        // Create the access permissions for the employee
        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setAccessLevel(accessRequest.getRequestedAccessLevel());
        accessPermission.setBuilding(accessRequest.getBuilding());

        // Create a new access card for the employee
        AccessCard accessCard = new AccessCard();
        // Add the access permission to the access card
        accessCard.addPermission(accessPermission);

        // Find the employee that made the request
        Employee employee = (Employee) accessRequest.getUser();

        // Add the access card to the employee
        accessCard.setEmployeeOfCard(employee);

        // Save the entities: AccessPermission and AccessCard
        accessPermissionRepository.save(accessPermission);
        accessCardRepository.save(accessCard);

        return Response.ok("Access request approved successfully").build();
    }

    @PUT
    @RolesAllowed({"ADMIN"})
    @Transactional
    @Path("/{requestID}/reject")
    public Response rejectAccessRequest(@PathParam("requestID") Long requestID) {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can disapprove access requests").build();
        }

        // Find the access request by ID
        AccessRequest accessRequest = accessRequestRepository.findByAccessRequestId(requestID);

        // Check if the access request exists
        if (accessRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Access request not found").build();
        }

        // Disapprove the access request
        accessRequest.disapprove();

        // Save the access request
        accessRequestRepository.saveAccessRequest(accessRequest);

        return Response.ok("Access request disapproved successfully").build();
    }

    @POST
    @RolesAllowed({"EMPLOYEE"})
    @Transactional
    public Response submitAccessRequest(AccessRequestRepresentation accessRequestRepresentation) {
        if (!securityContext.isUserInRole("EMPLOYEE")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only EMPLOYEES can create access requests").build();
        }

        if (accessRequestRepresentation.buildingID == null || accessRequestRepresentation.requestedAccessLevel == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request payload").build();
        }

        // Check if the building exists
        Building building = buildingRepository.findByID(accessRequestRepresentation.buildingID);
        if (building == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Building not found").build();
        }

        // Convert representation to domain model
        AccessRequest accessRequest = accessRequestMapper.toModel(accessRequestRepresentation);

        // Set the building for the access request
        accessRequest.setBuilding(building);

        // Persist the access request
        accessRequestRepository.saveAccessRequest(accessRequest);

        // Return a success response with the created resource and its location
        return Response.created(uriInfo.getAbsolutePathBuilder()
                        .path(accessRequest.getRequestID().toString())
                        .build())
                .entity("Access request submitted successfully")
                .build();
    }
}
