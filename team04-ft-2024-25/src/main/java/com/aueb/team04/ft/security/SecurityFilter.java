package com.aueb.team04.ft.security;

import com.aueb.team04.ft.resource.AccessControlURI;
import com.aueb.team04.ft.service.AuthenticationService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    @Inject
    AuthenticationService authenticationService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        // Skip authentication for registration or public endpoints
        if (path.equals(AccessControlURI.REGISTRATION)) {
            return;
        }

        // First, check for credentials in headers
        String username = requestContext.getHeaderString("X-Username");
        String password = requestContext.getHeaderString("X-Password");

        // If not found in headers, attempt to extract from JSON body
        if (username == null || password == null) {
            InputStream originalStream = requestContext.getEntityStream();
            byte[] bodyBytes = originalStream.readAllBytes();
            requestContext.setEntityStream(new ByteArrayInputStream(bodyBytes));

            try (JsonReader jsonReader = Json.createReader(new ByteArrayInputStream(bodyBytes))) {
                JsonObject jsonObject = jsonReader.readObject();
                JsonObject userObject = jsonObject.getJsonObject("employee");
                if (userObject == null) {
                    userObject = jsonObject.getJsonObject("admin");
                }

                if (userObject != null) {
                    username = userObject.getString("username", null);
                    password = userObject.getString("password", null);
                }
            }
        }

        // If still missing credentials, return 401
        if (username == null || password == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Missing username or password").build());
            return;
        }

        // Authenticate user
        AuthenticationService.AuthenticatedUser authenticatedUser = authenticationService.authenticate(username, password);

        // Debugging: Print out the user type and username
        System.out.println("Authenticated user: " + username + " with role: " + authenticatedUser.getUserType());

        // Ensure authenticated user type is valid
        if (authenticatedUser.getUserType() != AuthenticationService.UserType.NONE) {
            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            String finalUsername = username;
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                            return finalUsername;
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String role) {
                    return role.equalsIgnoreCase(authenticatedUser.getUserType().name());
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "CUSTOM";
                }
            });
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Authentication failed").build());
        }
    }
}