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
}
