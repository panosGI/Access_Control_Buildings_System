package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessRequest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class AccessRequestRepositoryTest {
    @Inject
    AccessRequestRepository accessRequestRepository;

    @Test
    public void findByAccessRequestId() {
        assertNotNull(accessRequestRepository.findByAccessRequestId(1111L));
    }

    @Test
    public void findAllAccessRequests() {
        assertNotNull(accessRequestRepository.findAllAccessRequests());
    }

    @Test
    @Transactional
    public void saveAccessRequest() {
        AccessRequest accessRequest = new AccessRequest();
        accessRequestRepository.saveAccessRequest(accessRequest);
        assertNotNull(accessRequestRepository.findByAccessRequestId(accessRequest.getRequestID()));
    }
}
