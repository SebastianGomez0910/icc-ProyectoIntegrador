package ec.edu.ups.icc.proyectointegrador.event.dtos;

import java.time.Instant;

public class SessionResponseDto {
    
    private Long id;
    private String title;
    private Instant startAt;
    private Instant endAt;
    private String location;
    private String virtualUrl;
    
    public SessionResponseDto() {
    }

    public SessionResponseDto(Long id, String title, Instant startAt, Instant endAt, String location,
            String virtualUrl) {
        this.id = id;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.location = location;
        this.virtualUrl = virtualUrl;
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
