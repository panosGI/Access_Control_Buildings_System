package com.aueb.team04.ft.representation;

import jakarta.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDateTime;
import java.util.Set;

public class AccessLogRepresentation {
    public Long id;
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime timestamp;
    public boolean accessStatus;
    public Set<AlertRepresentation> alerts;
    public Long accessCardId;
    public Long accessPointId;
}