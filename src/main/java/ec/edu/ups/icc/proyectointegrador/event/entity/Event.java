package ec.edu.ups.icc.proyectointegrador.event.entity;

import java.time.LocalDateTime;

import ec.edu.ups.icc.proyectointegrador.category.entity.Category;
import ec.edu.ups.icc.proyectointegrador.common.entity.BaseEntity;
import ec.edu.ups.icc.proyectointegrador.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name="events")
public class Event extends BaseEntity{
    
    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false, length=100)
    private String modality;

    @Column(length=100)
    private String location;

    @Column(name = "virtual_url", length = 500)
    private String virtualUrl;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "available_capacity", nullable = false)
    private Integer availableCapacity;

    @Column(name = "registration_start_at", nullable = false)
    private LocalDateTime registrationStartAt;

    @Column(name = "registration_end_at", nullable = false)
    private LocalDateTime registrationEndAt;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false, length = 20)
    private String status = "DRAFT";

    @Column(nullable = false)
    private Boolean deleted = false;

    // La anotación @Version le indica a Spring Data JPA que use esta columna para manejar concurrencia
    @Version
    @Column(nullable = false)
    private Long version = 0L;

    // Relación con el organizador (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    // Relación con la Categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Event() {
    }

    public Event(String title, String description, String modality, String location, String virtualUrl,
            Integer capacity, Integer availableCapacity, LocalDateTime registrationStartAt,
            LocalDateTime registrationEndAt, LocalDateTime startAt, LocalDateTime endAt, String status, Boolean deleted,
            Long version, User organizer, Category category) {
        this.title = title;
        this.description = description;
        this.modality = modality;
        this.location = location;
        this.virtualUrl = virtualUrl;
        this.capacity = capacity;
        this.availableCapacity = availableCapacity;
        this.registrationStartAt = registrationStartAt;
        this.registrationEndAt = registrationEndAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.deleted = deleted;
        this.version = version;
        this.organizer = organizer;
        this.category = category;
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

    public LocalDateTime getRegistrationStartAt() {
        return registrationStartAt;
    }

    public void setRegistrationStartAt(LocalDateTime registrationStartAt) {
        this.registrationStartAt = registrationStartAt;
    }

    public LocalDateTime getRegistrationEndAt() {
        return registrationEndAt;
    }

    public void setRegistrationEndAt(LocalDateTime registrationEndAt) {
        this.registrationEndAt = registrationEndAt;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
