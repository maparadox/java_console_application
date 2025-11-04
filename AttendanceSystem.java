import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AttendanceSystem {
    private AttendanceManager manager;
    private Scanner scanner;
    private DateTimeFormatter dateFormatter;

    public AttendanceSystem() {
        this.manager = new AttendanceManager();
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public void run() {
        System.out.println("\n========================================");
        System.out.println("    ATTENDANCE MANAGEMENT SYSTEM");
        System.out.println("========================================\n");

        System.out.print("Load sample data? (y/n): ");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("y")) {
            manager.loadSampleData();
            System.out.println("\n✓ Sample data loaded successfully!");
        }

        boolean running = true;
        while (running) {
            displayMainMenu();
            int option = getIntInput("Enter your choice: ");

            switch (option) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addSubject();
                    break;
                case 3:
                    markAttendance();
                    break;
                case 4:
                    viewStudentAttendance();
                    break;
                case 5:
                    viewAttendanceByDate();
                    break;
                case 6:
                    viewAttendanceBySubject();
                    break;
                case 7:
                    viewAttendancePercentage();
                    break;
                case 8:
                    viewSubjectWiseAttendance();
                    break;
                case 9:
                    viewAllStudents();
                    break;
                case 10:
                    viewLowAttendanceStudents();
                    break;
                case 11:
                    viewAttendanceReport();
                    break;
                case 0:
                    System.out.println("\nThank you for using the Attendance Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("\n✗ Invalid choice! Please try again.");
            }

            if (running && option != 0) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n========================================");
        System.out.println("              MAIN MENU");
        System.out.println("========================================");
        System.out.println("1.  Add New Student");
        System.out.println("2.  Add New Subject");
        System.out.println("3.  Mark Attendance");
        System.out.println("4.  View Student Attendance");
        System.out.println("5.  View Attendance by Date");
        System.out.println("6.  View Attendance by Subject");
        System.out.println("7.  View Student Attendance Percentage");
        System.out.println("8.  View Subject-wise Attendance");
        System.out.println("9.  View All Students");
        System.out.println("10. View Low Attendance Students");
        System.out.println("11. Generate Complete Attendance Report");
        System.out.println("0.  Exit");
        System.out.println("========================================");
    }

    private void addStudent() {
        System.out.println("\n--- Add New Student ---");
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter Department: ");
        String department = scanner.nextLine().trim();

        try {
            Student student = new Student(id, name, email, department);
            manager.addStudent(student);
            System.out.println("\n✓ Student added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
    }

    private void addSubject() {
        System.out.println("\n--- Add New Subject ---");
        System.out.print("Enter Subject Name: ");
        String subject = scanner.nextLine().trim();

        manager.addSubject(subject);
        System.out.println("\n✓ Subject added successfully!");
    }

    private void markAttendance() {
        System.out.println("\n--- Mark Attendance ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = manager.getStudent(studentId);
        if (student == null) {
            System.out.println("\n✗ Student not found!");
            return;
        }

        System.out.println("Student: " + student.getName());

        System.out.print("Enter Date (dd-MM-yyyy) or press Enter for today: ");
        String dateStr = scanner.nextLine().trim();
        LocalDate date = LocalDate.now();

        if (!dateStr.isEmpty()) {
            try {
                date = LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("\n✗ Invalid date format!");
                return;
            }
        }

        List<String> subjects = manager.getAllSubjects();
        if (subjects.isEmpty()) {
            System.out.println("\n✗ No subjects available! Please add subjects first.");
            return;
        }

        System.out.println("\nAvailable Subjects:");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i));
        }

        int subjectIndex = getIntInput("Select Subject (number): ") - 1;
        if (subjectIndex < 0 || subjectIndex >= subjects.size()) {
            System.out.println("\n✗ Invalid subject selection!");
            return;
        }

        String subject = subjects.get(subjectIndex);

        System.out.print("Is student present? (y/n): ");
        String presenceStr = scanner.nextLine().trim().toLowerCase();
        boolean isPresent = presenceStr.equals("y");

        try {
            manager.markAttendance(studentId, date, isPresent, subject);
            System.out.println("\n✓ Attendance marked successfully!");
            System.out.println("  Student: " + student.getName());
            System.out.println("  Subject: " + subject);
            System.out.println("  Date: " + date.format(dateFormatter));
            System.out.println("  Status: " + (isPresent ? "Present" : "Absent"));
        } catch (IllegalArgumentException e) {
            System.out.println("\n✗ Error: " + e.getMessage());
        }
    }

    private void viewStudentAttendance() {
        System.out.println("\n--- View Student Attendance ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = manager.getStudent(studentId);
        if (student == null) {
            System.out.println("\n✗ Student not found!");
            return;
        }

        List<AttendanceRecord> records = manager.getAttendanceByStudent(studentId);
        if (records.isEmpty()) {
            System.out.println("\n✗ No attendance records found for this student!");
            return;
        }

        System.out.println("\n" + student);
        System.out.println("\nAttendance Records:");
        System.out.println("----------------------------------------------------------");
        for (AttendanceRecord record : records) {
            System.out.println(record);
        }
        System.out.println("----------------------------------------------------------");
        System.out.println("Total Records: " + records.size());
    }

    private void viewAttendanceByDate() {
        System.out.println("\n--- View Attendance by Date ---");
        System.out.print("Enter Date (dd-MM-yyyy): ");
        String dateStr = scanner.nextLine().trim();

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("\n✗ Invalid date format!");
            return;
        }

        List<AttendanceRecord> records = manager.getAttendanceByDate(date);
        if (records.isEmpty()) {
            System.out.println("\n✗ No attendance records found for this date!");
            return;
        }

        System.out.println("\nAttendance Records for " + date.format(dateFormatter) + ":");
        System.out.println("----------------------------------------------------------");
        for (AttendanceRecord record : records) {
            System.out.println(record);
        }
        System.out.println("----------------------------------------------------------");
        System.out.println("Total Records: " + records.size());
    }

    private void viewAttendanceBySubject() {
        System.out.println("\n--- View Attendance by Subject ---");

        List<String> subjects = manager.getAllSubjects();
        if (subjects.isEmpty()) {
            System.out.println("\n✗ No subjects available!");
            return;
        }

        System.out.println("\nAvailable Subjects:");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i));
        }

        int subjectIndex = getIntInput("Select Subject (number): ") - 1;
        if (subjectIndex < 0 || subjectIndex >= subjects.size()) {
            System.out.println("\n✗ Invalid subject selection!");
            return;
        }

        String subject = subjects.get(subjectIndex);
        List<AttendanceRecord> records = manager.getAttendanceBySubject(subject);

        if (records.isEmpty()) {
            System.out.println("\n✗ No attendance records found for this subject!");
            return;
        }

        System.out.println("\nAttendance Records for " + subject + ":");
        System.out.println("----------------------------------------------------------");
        for (AttendanceRecord record : records) {
            System.out.println(record);
        }
        System.out.println("----------------------------------------------------------");
        System.out.println("Total Records: " + records.size());
    }

    private void viewAttendancePercentage() {
        System.out.println("\n--- View Student Attendance Percentage ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = manager.getStudent(studentId);
        if (student == null) {
            System.out.println("\n✗ Student not found!");
            return;
        }

        double percentage = manager.calculateAttendancePercentage(studentId);
        if (percentage == 0.0) {
            System.out.println("\n✗ No attendance records found for this student!");
            return;
        }

        System.out.println("\n" + student);
        System.out.println("\nOverall Attendance: " + String.format("%.2f%%", percentage));

        if (percentage >= 75) {
            System.out.println("Status: ✓ GOOD ATTENDANCE");
        } else if (percentage >= 65) {
            System.out.println("Status: ⚠ WARNING - Below 75%");
        } else {
            System.out.println("Status: ✗ CRITICAL - Below 65%");
        }
    }

    private void viewSubjectWiseAttendance() {
        System.out.println("\n--- View Subject-wise Attendance ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();

        Student student = manager.getStudent(studentId);
        if (student == null) {
            System.out.println("\n✗ Student not found!");
            return;
        }

        Map<String, Double> percentages = manager.calculateAttendancePercentageBySubject(studentId);
        if (percentages.isEmpty()) {
            System.out.println("\n✗ No attendance records found for this student!");
            return;
        }

        System.out.println("\n" + student);
        System.out.println("\nSubject-wise Attendance:");
        System.out.println("----------------------------------------------------------");

        for (Map.Entry<String, Double> entry : percentages.entrySet()) {
            String status;
            double pct = entry.getValue();
            if (pct >= 75) {
                status = "✓";
            } else if (pct >= 65) {
                status = "⚠";
            } else {
                status = "✗";
            }
            System.out.printf("%-25s : %6.2f%% %s%n", entry.getKey(), pct, status);
        }
        System.out.println("----------------------------------------------------------");
    }

    private void viewAllStudents() {
        System.out.println("\n--- All Students (Sorted by Name) ---");
        List<Student> students = manager.getStudentsSortedByName();

        if (students.isEmpty()) {
            System.out.println("\n✗ No students found!");
            return;
        }

        System.out.println();
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println("\nTotal Students: " + students.size());
    }

    private void viewLowAttendanceStudents() {
        System.out.println("\n--- Low Attendance Students ---");
        double threshold = getDoubleInput("Enter threshold percentage (e.g., 75): ");

        List<Student> students = manager.getLowAttendanceStudents(threshold);

        if (students.isEmpty()) {
            System.out.println("\n✓ No students found below " + threshold + "% attendance!");
            return;
        }

        System.out.println("\nStudents with attendance below " + threshold + "%:");
        System.out.println("----------------------------------------------------------");
        for (Student student : students) {
            double percentage = manager.calculateAttendancePercentage(student.getStudentId());
            System.out.printf("%s | Attendance: %.2f%%%n", student, percentage);
        }
        System.out.println("----------------------------------------------------------");
        System.out.println("Total Students: " + students.size());
    }

    private void viewAttendanceReport() {
        System.out.println("\n========================================");
        System.out.println("    COMPLETE ATTENDANCE REPORT");
        System.out.println("========================================");

        List<Student> students = manager.getStudentsSortedByName();

        if (students.isEmpty()) {
            System.out.println("\n✗ No students found!");
            return;
        }

        for (Student student : students) {
            System.out.println("\n" + student);
            double overallPercentage = manager.calculateAttendancePercentage(student.getStudentId());

            if (overallPercentage == 0.0) {
                System.out.println("  Overall Attendance: No records");
                continue;
            }

            System.out.printf("  Overall Attendance: %.2f%%%n", overallPercentage);

            Map<String, Double> subjectPercentages = manager.calculateAttendancePercentageBySubject(student.getStudentId());
            System.out.println("  Subject-wise Breakdown:");
            for (Map.Entry<String, Double> entry : subjectPercentages.entrySet()) {
                System.out.printf("    - %-25s : %.2f%%%n", entry.getKey(), entry.getValue());
            }
        }

        System.out.println("\n========================================");
        System.out.println("Total Students: " + students.size());
        System.out.println("========================================");
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input! " + prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print("Invalid input! " + prompt);
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    public static void main(String[] args) {
        AttendanceSystem system = new AttendanceSystem();
        system.run();
    }
}
