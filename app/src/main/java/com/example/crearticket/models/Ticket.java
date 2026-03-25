package com.example.crearticket.models;

import java.util.Date;

public class Ticket {
    private String id;
    private String clientId;
    private String clientName;
    private String typeWork; // ajuste, reparación, confección, modificación, otro
    private String typeGarment; // pantalón, camisa, vestido, saco, falda, otro
    private String description;
    private String measures;
    private Date deliveryDate;
    private Date createdAt;
    private String status; // Nuevo, Pendiente, Falta material, Cancelado, Terminado
    private String notes;

    public Ticket() {
        // Required for Firebase
    }

    public Ticket(String id, String clientId, String clientName, String typeWork, String typeGarment, String description, String measures, Date deliveryDate, Date createdAt, String status, String notes) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.typeWork = typeWork;
        this.typeGarment = typeGarment;
        this.description = description;
        this.measures = measures;
        this.deliveryDate = deliveryDate;
        this.createdAt = createdAt;
        this.status = status;
        this.notes = notes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getTypeWork() { return typeWork; }
    public void setTypeWork(String typeWork) { this.typeWork = typeWork; }

    public String getTypeGarment() { return typeGarment; }
    public void setTypeGarment(String typeGarment) { this.typeGarment = typeGarment; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMeasures() { return measures; }
    public void setMeasures(String measures) { this.measures = measures; }

    public Date getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}