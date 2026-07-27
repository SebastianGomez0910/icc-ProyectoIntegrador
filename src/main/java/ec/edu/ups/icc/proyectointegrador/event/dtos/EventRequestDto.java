package ec.edu.ups.icc.proyectointegrador.event.dtos;

import java.time.Instant;

public class EventRequestDto {
    
    private String title;
    private String description;
    private String modality;
    private String location;
    private String virtualUrl;
    private Integer capacity;
    private Instant registrationStartAt;
    private Instant registrationEndAt;
    private Instant startAt;
    private Instant endAt;
    private Long categoryId;

    public EventRequestDto(String title, String description, String modality, String location, String virtualUrl,
            Integer capacity, Instant registrationStartAt, Instant registrationEndAt, Instant startAt, Instant endAt,
            Long categoryId) {
        this.title = title;
        this.description = description;
        this.modality = modality;
        this.location = location;
        this.virtualUrl = virtualUrl;
        this.capacity = capacity;
        this.registrationStartAt = registrationStartAt;
        this.registrationEndAt = registrationEndAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.categoryId = categoryId;
    }

    public EventRequestDto() {
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getModality() {
        return modality;
    }
    public void setModality(String modality) {
        this.modality = modality;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getVirtualUrl() {
        return virtualUrl;
    }
    public void setVirtualUrl(String virtualUrl) {
        this.virtualUrl = virtualUrl;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    public Instant getRegistrationStartAt() {
        return registrationStartAt;
    }
    public void setRegistrationStartAt(Instant registrationStartAt) {
        this.registrationStartAt = registrationStartAt;
    }
    public Instant getRegistrationEndAt() {
        return registrationEndAt;
    }
    public void setRegistrationEndAt(Instant registrationEndAt) {
        this.registrationEndAt = registrationEndAt;
    }
    public Instant getStartAt() {
        return startAt;
    }
    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }
    public Instant getEndAt() {
        return endAt;
    }
    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Getters and setters
    
}
