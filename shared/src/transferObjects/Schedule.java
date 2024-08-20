package transferObjects;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Schedule implements Serializable {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private User user;
    private int facilityId; // Add facilityId to link schedule with facility

    public Schedule(LocalDateTime startTime, LocalDateTime endTime, User user, int facilityId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.facilityId = facilityId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    @Override
    public String toString() {
        return "Schedule [startTime=" + startTime + ", endTime=" + endTime + ", user=" + user + ", facilityId=" + facilityId + "]";
    }
}
