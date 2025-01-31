package com.aueb.team04.ft.resource;

import com.aueb.team04.ft.domain.AccessCard;
import com.aueb.team04.ft.domain.AccessLog;
import com.aueb.team04.ft.persistence.AccessCardRepository;
import com.aueb.team04.ft.persistence.AccessLogRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@RequestScoped
@Path(AccessControlURI.ACCESS_CARD)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessCardResource {

    @Inject
    AccessCardRepository accessCardRepository;

    @Context
    SecurityContext securityContext;

    @Inject
    AccessLogRepository accessLogRepository;

    @GET
    @RolesAllowed({"ADMIN"})
    public Response getAccessCards() {
        if (securityContext.isUserInRole("ADMIN")) {
            // If the user is an admin, return the access point representation
            List<AccessCard> accessCards = accessCardRepository.listAllAccessCards();
            return Response.ok(accessCards).build();
        } else {
            // If the user is not an admin, return a 403 Forbidden response
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @GET
    @Path("/{id}/access_log")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response getAccessLogsByAccessCard(@PathParam("id") Long accessCardId) {
        List<AccessLog> accessLogs = accessLogRepository.findByAccessCardId(accessCardId);
        return Response.ok(accessLogs).build();
    }
}
