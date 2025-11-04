import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AttendanceRecord {
    private String studentId;
    private LocalDate date;
    private boolean isPresent;
    private String subject;

    public AttendanceRecord(String studentId, LocalDate date, boolean isPresent, String subject) {
        this.studentId = studentId;
        this.date = date;
        this.isPresent = isPresent;
        this.subject = subject;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return String.format("Student: %-10s | Date: %s | Subject: %-15s | Status: %s",
                           studentId, date.format(formatter), subject,
                           isPresent ? "Present" : "Absent");
    }
}
