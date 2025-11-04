import java.time.LocalDate;
import java.util.*;

public class AttendanceManager {
    private HashMap<String, Student> students;
    private ArrayList<AttendanceRecord> attendanceRecords;
    private LinkedHashSet<String> subjects;

    public AttendanceManager() {
        this.students = new HashMap<>();
        this.attendanceRecords = new ArrayList<>();
        this.subjects = new LinkedHashSet<>();
    }

    public void addStudent(Student student) {
        if (students.containsKey(student.getStudentId())) {
            throw new IllegalArgumentException("Student with ID " + student.getStudentId() + " already exists");
        }
        students.put(student.getStudentId(), student);
    }

    public Student getStudent(String studentId) {
        return students.get(studentId);
    }

    public void addSubject(String subject) {
        subjects.add(subject);
    }

    public List<String> getAllSubjects() {
        return new ArrayList<>(subjects);
    }

    public void markAttendance(String studentId, LocalDate date, boolean isPresent, String subject) {
        if (!students.containsKey(studentId)) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        if (!subjects.contains(subject)) {
            throw new IllegalArgumentException("Subject not found: " + subject);
        }

        AttendanceRecord record = new AttendanceRecord(studentId, date, isPresent, subject);
        attendanceRecords.add(record);
    }

    public List<AttendanceRecord> getAttendanceByStudent(String studentId) {
        List<AttendanceRecord> result = new ArrayList<>();
        for (AttendanceRecord record : attendanceRecords) {
            if (record.getStudentId().equals(studentId)) {
                result.add(record);
            }
        }
        sortRecordsByDate(result);
        return result;
    }

    public List<AttendanceRecord> getAttendanceByDate(LocalDate date) {
        List<AttendanceRecord> result = new ArrayList<>();
        for (AttendanceRecord record : attendanceRecords) {
            if (record.getDate().equals(date)) {
                result.add(record);
            }
        }
        return result;
    }

    public List<AttendanceRecord> getAttendanceBySubject(String subject) {
        List<AttendanceRecord> result = new ArrayList<>();
        for (AttendanceRecord record : attendanceRecords) {
            if (record.getSubject().equals(subject)) {
                result.add(record);
            }
        }
        sortRecordsByDate(result);
        return result;
    }

    public double calculateAttendancePercentage(String studentId) {
        List<AttendanceRecord> records = getAttendanceByStudent(studentId);
        if (records.isEmpty()) {
            return 0.0;
        }

        int presentCount = 0;
        for (AttendanceRecord record : records) {
            if (record.isPresent()) {
                presentCount++;
            }
        }

        return (presentCount * 100.0) / records.size();
    }

    public Map<String, Double> calculateAttendancePercentageBySubject(String studentId) {
        Map<String, Integer> totalCount = new HashMap<>();
        Map<String, Integer> presentCount = new HashMap<>();

        for (AttendanceRecord record : attendanceRecords) {
            if (record.getStudentId().equals(studentId)) {
                String subject = record.getSubject();
                totalCount.put(subject, totalCount.getOrDefault(subject, 0) + 1);
                if (record.isPresent()) {
                    presentCount.put(subject, presentCount.getOrDefault(subject, 0) + 1);
                }
            }
        }

        Map<String, Double> percentages = new HashMap<>();
        for (String subject : totalCount.keySet()) {
            int total = totalCount.get(subject);
            int present = presentCount.getOrDefault(subject, 0);
            percentages.put(subject, (present * 100.0) / total);
        }

        return percentages;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public List<Student> getStudentsSortedByName() {
        List<Student> studentList = getAllStudents();
        quickSort(studentList, 0, studentList.size() - 1);
        return studentList;
    }

    private void quickSort(List<Student> students, int low, int high) {
        if (low < high) {
            int pi = partition(students, low, high);
            quickSort(students, low, pi - 1);
            quickSort(students, pi + 1, high);
        }
    }

    private int partition(List<Student> students, int low, int high) {
        String pivot = students.get(high).getName();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (students.get(j).getName().compareToIgnoreCase(pivot) < 0) {
                i++;
                Student temp = students.get(i);
                students.set(i, students.get(j));
                students.set(j, temp);
            }
        }

        Student temp = students.get(i + 1);
        students.set(i + 1, students.get(high));
        students.set(high, temp);

        return i + 1;
    }

    private void sortRecordsByDate(List<AttendanceRecord> records) {
        for (int i = 0; i < records.size() - 1; i++) {
            for (int j = 0; j < records.size() - i - 1; j++) {
                if (records.get(j).getDate().isAfter(records.get(j + 1).getDate())) {
                    AttendanceRecord temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }
    }

    public List<Student> getLowAttendanceStudents(double threshold) {
        List<Student> lowAttendanceStudents = new ArrayList<>();

        for (Student student : students.values()) {
            double percentage = calculateAttendancePercentage(student.getStudentId());
            if (percentage < threshold && percentage > 0) {
                lowAttendanceStudents.add(student);
            }
        }

        return lowAttendanceStudents;
    }

    public void loadSampleData() {
        addStudent(new Student("S001", "Alice Johnson", "alice@example.com", "Computer Science"));
        addStudent(new Student("S002", "Bob Smith", "bob@example.com", "Electronics"));
        addStudent(new Student("S003", "Charlie Brown", "charlie@example.com", "Computer Science"));
        addStudent(new Student("S004", "Diana Prince", "diana@example.com", "Mechanical"));
        addStudent(new Student("S005", "Eve Williams", "eve@example.com", "Computer Science"));

        addSubject("Data Structures");
        addSubject("Algorithms");
        addSubject("Database Systems");
        addSubject("Operating Systems");
        addSubject("Computer Networks");

        LocalDate today = LocalDate.now();
        String[] studentIds = {"S001", "S002", "S003", "S004", "S005"};
        String[] subjectList = {"Data Structures", "Algorithms", "Database Systems", "Operating Systems"};

        Random random = new Random();
        for (int day = 0; day < 10; day++) {
            LocalDate date = today.minusDays(day);
            for (String studentId : studentIds) {
                for (String subject : subjectList) {
                    boolean isPresent = random.nextInt(100) < 75;
                    markAttendance(studentId, date, isPresent, subject);
                }
            }
        }
    }
}
