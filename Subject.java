import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Subject {
    private String code;
    private String subjectName;
    private int creditHours;

    public Subject(String code, String subjectName, int creditHours) {
        this.code = code;
        this.subjectName = subjectName;
        this.creditHours = creditHours;
    }

    public String getCode() {
        return code;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public static List<Subject> readSubjects(String filename) {
        List<Subject> subjects = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String code = parts[0];
                String subjectName = parts[1];
                int creditHours = Integer.parseInt(parts[2]);
                subjects.add(new Subject(code, subjectName, creditHours));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return subjects;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "code='" + code + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", creditHours=" + creditHours +
                '}';
    }
} 