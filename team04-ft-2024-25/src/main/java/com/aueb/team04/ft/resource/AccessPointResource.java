package com.aueb.team04.ft.resource;

import com.aueb.team04.ft.domain.AccessCard;
import com.aueb.team04.ft.domain.AccessLog;
import com.aueb.team04.ft.domain.AccessPoint;
import com.aueb.team04.ft.domain.Alert;
import com.aueb.team04.ft.domain.Employee;
import com.aueb.team04.ft.persistence.AccessLogRepository;
import com.aueb.team04.ft.persistence.AccessPointRepository;
import com.aueb.team04.ft.persistence.AlertRepository;
import com.aueb.team04.ft.persistence.EmployeeRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

@RequestScoped
@Path(AccessControlURI.ACCESS_POINT)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessPointResource {

    Long buildingId;

    @Inject
    EmployeeRepository employeeRepository;

    @Context
    SecurityContext securityContext;

    @Inject
    AccessPointRepository accessPointRepository;

    @Inject
    AccessLogRepository accessLogRepository;

    @Inject
    AccessLogResource accessLogResource;

    @Inject
    AlertRepository alertRepository;
    
        @GET
        @RolesAllowed({"ADMIN", "EMPLOYEE"})
        public Response getAccessPoints() {
            // Return only the access points of the building
            if (buildingId == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Building ID is required").build();
            }
    
            List<AccessPoint> accessPoints = accessPointRepository.getAccessPoints();
            accessPoints.removeIf(accessPoint -> !Objects.equals(accessPoint.getBuilding().getId(), buildingId));
    
            return Response.ok(accessPoints).build();
        }
    
        public void setBuildingId(Long buildingId) {
            this.buildingId = buildingId;
        }
        public void setSecurityContext(SecurityContext securityContext) {
            this.securityContext = securityContext;
        }
    
        @GET
        @Path("/{id}")
        @RolesAllowed({"ADMIN", "EMPLOYEE"})
        @Transactional
        public Response access(@PathParam("id") Long accessPointId) {
            if (buildingId == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Building ID is required").build();
            }
            AccessPoint accessPoint = accessPointRepository.getAccessPointById(accessPointId);
            if (accessPoint == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Access Point not found").build();
            }
    
            // AccessPointRepresentation accessPointRepresentation = accessPointMapper.toRepresentation(accessPoint);
    
            // Get the user's role using the security context
            if (securityContext.isUserInRole("ADMIN")) {
                // If the user is an admin, return the access point representation
                return Response.ok(accessPoint).build();
            } else {
                // If the user is an employee, check if the employee is allowed to access the access point
                String employeeUsername = securityContext.getUserPrincipal().getName();
                Employee employee = employeeRepository.findByUsername(employeeUsername);
                AccessCard accessCard = employee.getAccessCard();
                AccessLog accessLog = new AccessLog();
                
                if (accessCard.checkAccess(accessPoint) && accessCard.hasAccessToPrerequisites(accessPoint.getPrerequisites())) {
                    // If the employee is allowed to access the access point return success response
                    accessLog.setTimestamp(LocalDateTime.now());
                    accessLog.setAccessStatus(true);
                    accessLog.setAccessCard(accessCard);
                    accessLog.setAccessPoint(accessPoint);
                    accessLogRepository.saveAccessLog(accessLog);
                    return Response.ok("Access Granted!").build();
                } else {
                    //I'm allowed to be there but don't have Access, make AccessLog
                    if (accessCard.checkAccess(accessPoint)==false && accessCard.hasAccessToPrerequisites(accessPoint.getPrerequisites())==true){
                        accessLog.setTimestamp(LocalDateTime.now());
                        accessLog.setAccessStatus(false);
                        accessLog.setAccessCard(accessCard);
                        accessLog.setAccessPoint(accessPoint);
                        accessLogRepository.saveAccessLog(accessLog);
                        return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to pass this access point").build();
                        
                    //I'm not allowed to be there and don't have Access, make Log and Alert
                    } else if (accessCard.checkAccess(accessPoint)==false && accessCard.hasAccessToPrerequisites(accessPoint.getPrerequisites())==false)
                        accessLog.setTimestamp(LocalDateTime.now());
                        accessLog.setAccessStatus(false);
                        accessLog.setAccessCard(accessCard);
                        accessLog.setAccessPoint(accessPoint);
                        accessLogRepository.saveAccessLog(accessLog);
                        Alert alert = new Alert();
                        alert.setAlertType("Alert1");
                        alert.setSeverity(1);
                        alert.setAccessLog(accessLog);
                        alertRepository.saveAlert(alert);
                return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to be in this access point").build();
            }
        }
    }

    @POST
    @RolesAllowed("ADMIN")
    @Transactional
    public Response createAccessPoint(AccessPoint accessPoint) {
        accessPointRepository.saveAccessPoint(accessPoint);
        return Response.created(UriBuilder.fromResource(AccessPointResource.class).path(accessPoint.getId().toString()).build()).build();
    }

    @GET
    @Path("/{id}/access_log")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response getAccessLogsByAccessPoint(@PathParam("id") Long accessPointId) {
        List<AccessLog> accessLogs = accessLogRepository.findByAccessPointId(accessPointId);
        return Response.ok(accessLogs).build();
    }

    @POST
    @Path("/{target_id}/{source_id}")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response addPrerequisite(@PathParam("target_id") Long targetAccessPointId, @PathParam("source_id") Long sourceAccessPointId) {
        AccessPoint targetAccessPoint = accessPointRepository.getAccessPointById(targetAccessPointId);
        AccessPoint sourceAccessPoint = accessPointRepository.getAccessPointById(sourceAccessPointId);
        if (targetAccessPoint == null || sourceAccessPoint == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Access Point not found").build();
        }
        targetAccessPoint.getPrerequisites().add(sourceAccessPoint);
        accessPointRepository.saveAccessPoint(targetAccessPoint);
        return Response.ok(targetAccessPoint).build();
    }

    @GET
    @Path("/{id}/prerequisites")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response getPrerequisites(@PathParam("id") Long accessPointId) {
        AccessPoint accessPoint = accessPointRepository.getAccessPointById(accessPointId);
        if (accessPoint == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Access Point not found").build();
        }
        List<AccessPoint> prerequisites = new ArrayList<>(accessPoint.getPrerequisites());
        return Response.ok(prerequisites).build();
    }

}