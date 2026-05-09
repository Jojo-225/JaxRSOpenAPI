package fr.istic.taa.jaxrs.dto.notification;

public class CreateNotificationDto {

    private Long userId;
    private String message;

    public CreateNotificationDto() {
    }

    public CreateNotificationDto(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
