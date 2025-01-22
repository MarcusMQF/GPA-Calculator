import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

public class Calculator {
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";
    private static final String PURPLE = "\u001B[35m";
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        boolean continueCalculating = true;
        
        while (continueCalculating) {
            printHeader("WELCOME TO GPA CALCULATOR");
            List<Subject> subjects = Subject.readSubjects("subjects.txt");
            displaySubjects(subjects);
            
            List<Subject> enrolledSubjects = selectSubjects(subjects);
            HashMap<String, String> grades = inputGrades(enrolledSubjects);
            displayResults(enrolledSubjects, grades);
            
            // Ask user if they want to calculate again
            while (true) {
                System.out.print(YELLOW + "\nDo you want to calculate another GPA? (Y/N): " + RESET);
                String response = scanner.nextLine().trim().toUpperCase();
                
                if (response.equals("Y")) {
                    System.out.println("\n" + GREEN + "Starting new calculation..." + RESET);
                    break;
                } else if (response.equals("N")) {
                    continueCalculating = false;
                    System.out.println("\n" + GREEN + "Thank you for using GPA Calculator!" + RESET);
                    break;
                } else {
                    System.out.println(RED + "[ERROR] Please enter Y or N" + RESET);
                }
            }
        }
        scanner.close();
    }

    private static void printHeader(String title) {
        int totalWidth = 70;  // Consistent width for all headers
        String border = "+" + "=".repeat(totalWidth) + "+";
        
        System.out.println();
        System.out.println(PURPLE + border);
        System.out.println("|" + centerText(title, totalWidth) + "|");
        System.out.println(border + RESET);
    }

    private static void displaySubjects(List<Subject> subjects) {
        printHeader("AVAILABLE SUBJECTS");
        
        // Table header
        System.out.println();
        System.out.println(BLUE + "+============+==========================================+============+");
        System.out.println("| Code       | Subject Name                             | Credits    |");
        System.out.println("+============+==========================================+============+" + RESET);
        
        // Table content
        for(Subject subject : subjects) {
            System.out.printf(BLUE + "| %-10s | %-40s | %-10d |%n", 
                subject.getCode(), 
                subject.getSubjectName(), 
                subject.getCreditHours());
            System.out.println(BLUE + "+------------+------------------------------------------+------------+" + RESET);
        }
    }

    private static List<Subject> selectSubjects(List<Subject> subjects) {
        List<Subject> enrolled = new ArrayList<>();
        while(true) {
            System.out.print(YELLOW + "\nEnter subject code to enroll (or 'done' to finish): " + RESET);
            String input = scanner.nextLine().trim().toUpperCase();
            
            if(input.equalsIgnoreCase("done")) {
                if (enrolled.isEmpty()) {
                    System.out.println(RED + "[ERROR] Please enroll in at least one subject!" + RESET);
                    continue;
                }
                break;
            }
            
            // Check if subject is already enrolled
            boolean isDuplicate = enrolled.stream()
                .anyMatch(s -> s.getCode().equalsIgnoreCase(input));
            
            if (isDuplicate) {
                System.out.println(RED + "[ERROR] You have already enrolled in this subject!" + RESET);
                continue;
            }
            
            Subject selected = subjects.stream()
                .filter(s -> s.getCode().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
                
            if(selected != null) {
                enrolled.add(selected);
                System.out.println(GREEN + "Successfully Added: " + selected.getSubjectName() + RESET);
            } else {
                System.out.println(RED + "[ERROR] Invalid subject code. Please try again." + RESET);
            }
        }
        return enrolled;
    }

    private static HashMap<String, String> inputGrades(List<Subject> subjects) {
        printHeader("ENTER GRADES");
        
        HashMap<String, String> grades = new HashMap<>();
        
        for (Subject subject : subjects) {
            while (true) {
                System.out.print(YELLOW + String.format("\nEnter grade for %s (%s): ", 
                    subject.getSubjectName(), subject.getCode()) + RESET);
                String grade = scanner.nextLine().trim().toUpperCase();
                if (isValidGrade(grade)) {
                    grades.put(subject.getCode(), grade);
                    break;
                }
                System.out.println(RED + "[ERROR] Invalid grade! Please enter a valid grade (A+, A, A-, B+, B, B-, C+, C, C-, D+, D, F)" + RESET);
            }
        }
        return grades;
    }

    private static void displayResults(List<Subject> subjects, HashMap<String, String> grades) {
        printHeader("SEMESTER RESULTS DASHBOARD");
        
        // Table header with wider columns
        System.out.println();
        System.out.println(BLUE + "+============+==========================================+============+============+===============+============+");
        System.out.println("| Code       | Subject Name                             | Credits    | Grade      | Grade Point   | Points     |");
        System.out.println("+============+==========================================+============+============+===============+============+" + RESET);
        
        double totalPoints = 0;
        int totalCredits = 0;
        
        // Table content
        for (Subject subject : subjects) {
            String grade = grades.get(subject.getCode());
            double gradePoint = getGradePoint(grade);
            double points = getGradePoint(grade) * subject.getCreditHours();
            totalPoints += points;
            totalCredits += subject.getCreditHours();
            
            System.out.printf(BLUE + "| %-10s | %-40s | %-10d | %-10s | %-10.2f    | %-10.2f |%n",
                subject.getCode(), 
                subject.getSubjectName(),
                subject.getCreditHours(),
                grade,
                gradePoint,
                points);
            System.out.println(BLUE + "+------------+------------------------------------------+------------+------------+---------------+------------+" + RESET);
        }
        
        // Summary section
        double gpa = totalPoints / totalCredits;
        System.out.println();
        System.out.println(GREEN + "+====================================+");
        System.out.println("|           FINAL SUMMARY            |");
        System.out.println("+==========================+=========+");
        System.out.printf("| Total Credits            | %-7d |%n", totalCredits);
        System.out.printf("| Total Points             | %-7.2f |%n", totalPoints);
        System.out.printf("| Semester GPA             | %-7.2f |%n", gpa);
        System.out.println("+==========================+=========+" + RESET);
    }

    private static boolean isValidGrade(String grade) {
        return grade.matches("^(A\\+|A|A-|B\\+|B|B-|C\\+|C|C-|D\\+|D|F)$");
    }

    private static double getGradePoint(String grade) {
        switch (grade) {
            case "A+": case "A": return 4.00;
            case "A-": return 3.70;
            case "B+": return 3.30;
            case "B": return 3.00;
            case "B-": return 2.70;
            case "C+": return 2.30;
            case "C": return 2.00;
            case "C-": return 1.70;
            case "D+": return 1.30;
            case "D": return 1.00;
            default: return 0.00;
        }
    }

    private static String centerText(String text, int width) {
        int padding = width - text.length();
        int leftPadding = padding / 2;
        int rightPadding = padding - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }
}