package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessLog;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class AccessLogRepositoryTest {
    @Inject
    AccessLogRepository accessLogRepository;

    @Test
    public void testListAllAccessLogs() {
        List<AccessLog> accessLogs = accessLogRepository.findAllAccessLogs();
        assertNotNull(accessLogs);
    }

    @Test
    public void testFindByAccessCardId() {
        List<AccessLog> accessLogs = accessLogRepository.findByAccessCardId(1111L);
        assertNotNull(accessLogs);
    }

    @Test
    public void findByAccessCardId() {
        List<AccessLog> accessLogs = accessLogRepository.findByAccessCardId(1111L);
        assertNotNull(accessLogs);
    }

    @Test
    @Transactional
    public void deleteAccessLogById() {
        AccessLog accessLog = new AccessLog();
        accessLogRepository.saveAccessLog(accessLog);

        accessLogRepository.deleteAccessLogById(accessLog.getId());

        // Flush and clear the persistence context to ensure the delete is committed
        accessLogRepository.getEntityManager().flush();
        accessLogRepository.getEntityManager().clear();

        assertNull(accessLogRepository.findByID(accessLog.getId()));
    }

    @Test
    @Transactional
    public void saveAccessLogTest() {
        AccessLog accessLog = new AccessLog();
        accessLogRepository.saveAccessLog(accessLog);

        Assertions.assertNotNull(accessLogRepository.findByID(accessLog.getId()));
    }

    @Test
    public void findByAccessPointId() {
        List<AccessLog> accessLogs = accessLogRepository.findByAccessPointId(1111L);
        assertNotNull(accessLogs);
    }
}
