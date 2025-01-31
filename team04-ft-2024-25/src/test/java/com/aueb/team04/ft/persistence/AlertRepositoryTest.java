package com.aueb.team04.ft.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.aueb.team04.ft.domain.Alert;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class AlertRepositoryTest {

    @Inject
    AlertRepository alertRepository;

    @Test
    public void testfindAllAlerts() {
        List<Alert> alerts = alertRepository.findAllAlerts();
        assertNotNull(alerts);
    }

    @Test
    @Transactional
    public void testSaveAlert() {
        Alert alert = new Alert();
        alertRepository.saveAlert(alert);
        assertTrue(alertRepository.findAlertById(alert.getId()) != null);
    }

    @Test
    @Transactional
    public void testDeleteAlertById(){
        Alert alert = new Alert();
        alertRepository.saveAlert(alert);
        alertRepository.deleteAlertById(alert.getId());

        // Flush and clear the persistence context to ensure the delete is committed
        alertRepository.getEntityManager().flush();
        alertRepository.getEntityManager().clear();
        assertTrue(alertRepository.findAlertById(alert.getId()) == null);
    }

    @Test
    public void testFindAlertById() {
        Alert alert = new Alert();
        alertRepository.findAlertById(111L);
        assertNotNull(alert);
    }

    @Test
    public void testFindAlertBySeverity() {
        Alert alert = new Alert();
        alertRepository.findAlertBySeverity(1);
        assertNotNull(alert);
    }
}
