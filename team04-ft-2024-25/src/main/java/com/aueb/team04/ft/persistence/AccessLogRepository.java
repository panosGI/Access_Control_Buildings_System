package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessLog;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class AccessLogRepository implements PanacheRepositoryBase<AccessLog, Long> {

    public List<AccessLog> findAllAccessLogs() {
        return listAll();
    }

    public AccessLog findByID(Long id) {
        return find("id", id).firstResult();
    }

    public List<AccessLog> findByAccessCardId(Long accessCardId) {
        return list("accessCard.id", accessCardId);
    }

    public void deleteAccessLogById(Long id) {
        AccessLog accessLog = findByID(id);
            accessLog.getAlerts().clear();
            delete(accessLog);
    }

    public void saveAccessLog(AccessLog accessLog) {
        persist(accessLog);
    }

    public List<AccessLog> findByAccessPointId (Long accessPointId) {
        return list("accessPoint.id", accessPointId);
    }

}