package com.aueb.team04.ft.domain;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "access_log")
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    @Column(name="LogTimestamp")
    private LocalDateTime timestamp;

    @Column(name="AccessStatus")
    private boolean accessStatus;

    @JsonbTransient
    @OneToMany(mappedBy = "accessLog", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Alert> alerts = new HashSet<>();

    @JsonbTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "accessCard_id")
    private AccessCard accessCard;

    @JsonbTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accessPoint_id")
    private AccessPoint accessPoint;

    public AccessLog() {}

    public AccessLog(LocalDateTime timestamp, boolean accessStatus){
        this.timestamp = timestamp;
        this.accessStatus = accessStatus;
    }

    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }

    public void setAccessStatus(Boolean accessStatus){
        this.accessStatus = accessStatus;
    }

    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }

    public boolean getAccessStatus(){
        return this.accessStatus;
    }

    public void setAccessCard(AccessCard accessCard){
        this.accessCard = accessCard;
    }

    public AccessCard getAccessCard(){
        return this.accessCard;
    }

    public long getId(){
        return this.id;
    }

    public void setAccessPoint(AccessPoint accessPoint){
        this.accessPoint = accessPoint;
    }

    public AccessPoint getAccessPoint(){
        return this.accessPoint;
    }

    public void addAlert(Alert alert){
        this.alerts.add(alert);
    }

    public Set<Alert> getAlerts(){
        return this.alerts;
    }

    public void triggers(String alertType, Integer Severity){
        Alert alert = new Alert(alertType, Severity);
        addAlert(alert);
        System.out.println(alert);
    }
}