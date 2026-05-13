package fr.istic.taa.jaxrs.dto.notification;

public class CreateNotificationDto {

    private Long userId;
    private Long organizerId;
    private String message;

    public CreateNotificationDto() {
    }

    public CreateNotificationDto(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public CreateNotificationDto(Long userId, Long organizerId, String message) {
        this.userId = userId;
        this.organizerId = organizerId;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
