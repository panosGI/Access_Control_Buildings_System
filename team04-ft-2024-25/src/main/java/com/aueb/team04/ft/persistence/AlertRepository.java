package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.Alert;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class AlertRepository implements PanacheRepositoryBase<Alert, Long> {

    public void saveAlert(Alert alert) {
        persist(alert);
    }

    public List<Alert> findAllAlerts() {
        return listAll();
    }

    public void deleteAlertById(Long id) {
        Alert alert = findAlertById(id);
            delete(alert);

    }

    public Alert findAlertById(Long id) {
        return find("id", id).firstResult();
    }

    public List<Alert> findAlertBySeverity(int severity) {
        return list("severity", severity);
    }   
}
