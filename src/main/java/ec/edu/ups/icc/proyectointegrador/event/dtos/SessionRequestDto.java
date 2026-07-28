package ec.edu.ups.icc.proyectointegrador.event.dtos;

import java.time.Instant;

public class SessionRequestDto {
    
    private String title;
    private String description;
    private Instant startAt;
    private Instant endAt;
    private String location;
    private String virtualUrl;
    
    public SessionRequestDto() {
    }
    
    public SessionRequestDto(String title, String description, Instant startAt, Instant endAt, String location,
            String virtualUrl) {
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.location = location;
        this.virtualUrl = virtualUrl;
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
}
