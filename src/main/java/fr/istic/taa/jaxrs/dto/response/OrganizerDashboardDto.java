package fr.istic.taa.jaxrs.dto.response;

import java.util.ArrayList;
import java.util.List;

public class OrganizerDashboardDto {
    private OrganizerDashboardStatsDto stats;
    private List<ConcertResponseDto> upcomingConcerts = new ArrayList<>();
    private List<String> quickActions = new ArrayList<>();

    public OrganizerDashboardDto() {
    }

    public OrganizerDashboardDto(OrganizerDashboardStatsDto stats,
                                 List<ConcertResponseDto> upcomingConcerts,
                                 List<String> quickActions) {
        this.stats = stats;
        if (upcomingConcerts != null) {
            this.upcomingConcerts = upcomingConcerts;
        }
        if (quickActions != null) {
            this.quickActions = quickActions;
        }
    }

    public OrganizerDashboardStatsDto getStats() {
        return stats;
    }

    public void setStats(OrganizerDashboardStatsDto stats) {
        this.stats = stats;
    }

    public List<ConcertResponseDto> getUpcomingConcerts() {
        return upcomingConcerts;
    }

    public void setUpcomingConcerts(List<ConcertResponseDto> upcomingConcerts) {
        this.upcomingConcerts = upcomingConcerts;
    }

    public List<String> getQuickActions() {
        return quickActions;
    }

    public void setQuickActions(List<String> quickActions) {
        this.quickActions = quickActions;
    }
}

