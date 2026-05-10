package fr.istic.taa.jaxrs.dto.notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationPreferencesDto {
    private boolean notifyAllOrganizers;
    private List<Long> organizerIds = new ArrayList<>();

    public NotificationPreferencesDto() {
    }

    public NotificationPreferencesDto(boolean notifyAllOrganizers, List<Long> organizerIds) {
        this.notifyAllOrganizers = notifyAllOrganizers;
        if (organizerIds != null) {
            this.organizerIds = organizerIds;
        }
    }

    public boolean isNotifyAllOrganizers() {
        return notifyAllOrganizers;
    }

    public void setNotifyAllOrganizers(boolean notifyAllOrganizers) {
        this.notifyAllOrganizers = notifyAllOrganizers;
    }

    public List<Long> getOrganizerIds() {
        return organizerIds;
    }

    public void setOrganizerIds(List<Long> organizerIds) {
        this.organizerIds = organizerIds;
    }
}
