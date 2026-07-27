package ec.edu.ups.icc.proyectointegrador.event.dtos;

import java.time.Instant;
import java.util.List;

public class EventResponseDto {
 
    private Long id;
    private String title;
    private String description;
    private String modality;
    private String location;
    private String virtualUrl;
    private Integer capacity;
    private Integer availableCapacity;
    private Instant startAt;
    private Instant endAt;
    private String status;
    private String organizerName; 
    private String categoryName;

    private List<SessionResponseDto> sessions;

    public EventResponseDto() {
    }

    public EventResponseDto(Long id, String title, String description, String modality, String location,
            String virtualUrl, Integer capacity, Integer availableCapacity, Instant startAt, Instant endAt,
            String status, String organizerName, String categoryName, List<SessionResponseDto> sessions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.modality = modality;
        this.location = location;
        this.virtualUrl = virtualUrl;
        this.capacity = capacity;
        this.availableCapacity = availableCapacity;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.organizerName = organizerName;
        this.categoryName = categoryName;
        this.sessions = sessions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Integer availableCapacity) {
        this.availableCapacity = availableCapacity;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SessionResponseDto> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionResponseDto> sessions) {
        this.sessions = sessions;
    }
}
