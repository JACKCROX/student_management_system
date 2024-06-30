/*
* Name : LIN HTET & NAY HTET AUNG
* ADMIN NO : p2340304 & p2340391
* CLASS : DIT/FT/2A/03
* */



package CA1;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class App {
    static StudentManagement sm = new StudentManagement();
    static StudentAdmin admin = new StudentAdmin(sm);
    static StudentUser studentUser = new StudentUser(sm);

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("programIntro.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip program_intro = AudioSystem.getClip();
        program_intro.open(audioStream);
        showMainMenu(program_intro);
    }

    public static void showMainMenu(Clip program_intro) {
        while (true) {
            program_intro.start();
            String[] options = {"1. Admin System", "2. Enquiry System", "3. Exit"};
            String choice = DialogUtil.getChoice("Choose an option:", options, "Main Menu");

            if (choice == null) return;

            switch (choice) {
                case "1. Admin System":
                    showAdminMenu(program_intro);
                    break;
                case "2. Enquiry System":
                    showStudentMenu(program_intro);
                    break;
                case "3. Exit":
                    DialogUtil.showMessage("Thank you for using the Student Management System!");
                    program_intro.stop();
                    System.exit(0);
            }
        }
    }

    private static void showAdminMenu(Clip program_intro) {
        while (true) {
            String[] options = {
                    "1. Create Student", "2. Delete Student", "3. Add Module for Student",

                    "4. Show Statistics",
                    "5. GPA Forecast", "6. Back to Main Menu", "7. Exit"
            };
            String choice = DialogUtil.getChoice("Choose an option:", options, "Admin Menu");

            if (choice == null) return;

            switch (AdminMenuOption.values()[Integer.parseInt(choice.split("\\.")[0]) - 1]) {
                case CREATE_STUDENT:
                    admin.addStudent();
                    break;
                case DELETE_STUDENT:
                    admin.deleteStudent();
                    break;
                case ADD_MODULE:
                    admin.addModulesForStudent();
                    break;
                case SHOW_STATISTICS:
                   admin.displayStatistics();
                   break;
                case GPA_FORECAST:
                    admin.forecastGPA();
                    break;
                case BACK_TO_MAIN:
                    return;
                case QUIT:
                    DialogUtil.showMessage("Thank you for using the Student Management System!");
                    program_intro.stop();
                    System.exit(0);
            }
        }
    }

    private static void showStudentMenu(Clip program_intro) {
        while (true) {
            String[] options = {
                    "1. Display All Students", "2. Search student by class",
                    "3. Search student by name", "4. Display Most Difficult Module", "5. Display Easiest Module",
                    "6. Back to Main Menu", "7. Quit"
            };
            String choice = DialogUtil.getChoice("Choose an option:", options, "Student Menu");

            if (choice == null) return;

            switch (StudentMenuOption.values()[Integer.parseInt(choice.split("\\.")[0]) - 1]) {
                case DISPLAY_ALL:
                    studentUser.displayAllStudData();
                    break;
                case SEARCH_BY_CLASS:
                    studentUser.searchStudentByClass();
                    break;
                case SEARCH_BY_NAME:
                    studentUser.searchStudentByName();
                    break;
                case DISPLAY_MOST_DIFFICULT:
                    studentUser.displayMostDifficultModules();
                    break;
                case DISPLAY_EASIEST:
                    studentUser.displayEasiestModules();
                    break;
                case BACK_TO_MAIN:
                    return;
                case QUIT:
                    DialogUtil.showMessage("Thank you for using the Student Management System!");
                    program_intro.stop();
                    System.exit(0);
            }
        }
    }
}


// Enums for Admin and Student menu options
enum AdminMenuOption {
    CREATE_STUDENT, DELETE_STUDENT, ADD_MODULE, SHOW_STATISTICS, GPA_FORECAST, BACK_TO_MAIN, QUIT
}

enum StudentMenuOption {
    DISPLAY_ALL, SEARCH_BY_CLASS, SEARCH_BY_NAME, DISPLAY_MOST_DIFFICULT, DISPLAY_EASIEST, BACK_TO_MAIN, QUIT
}
