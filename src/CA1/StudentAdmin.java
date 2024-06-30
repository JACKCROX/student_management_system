/*
 * Name : NAY HTET AUNG
 * ADMIN NO : p2340391
 * CLASS : DIT/FT/2A/03
 * */

package CA1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.*;
import java.util.regex.Pattern;


public class StudentAdmin {
    private final StudentManagement sm;

    public StudentAdmin(StudentManagement sm) {
        this.sm = sm;
    }

    public void addStudent() {
        String name = getValidStringInput("Enter the student name:");
        if (name == null) return;
        String adminNumber = getUniqueAdminNumber();
        if (adminNumber == null) return;
        String studentClass = getValidStudentClass();
        if (studentClass == null) return;
        int numModules = getValidIntegerInput("Enter the number of modules:", 0, 10);
        if (numModules == -1) return;

        ArrayList<Module> modules = new ArrayList<>();
        for (int i = 0; i < numModules; i++) {
            Module module = createModule(modules);
            if (module == null) return;
            modules.add(module);
        }

        sm.addStudent(new Student(name, adminNumber, studentClass, modules));
        DialogUtil.showMessage("Student created successfully!");
    }

    public void deleteStudent() {
        String adminNumber = getValidStringInput("Enter the admin number of the student to delete:");
        if (adminNumber == null) return;
        if (sm.deleteStudent(adminNumber)) {
            DialogUtil.showMessage("Student deleted successfully!");
        } else {
            DialogUtil.showMessage("Student not found.");
        }
    }

    public void addModulesForStudent() {
        String adminNumber = getValidStringInput("Enter the admin number of the student:");
        if (adminNumber == null) return;
        Student student = sm.findStudentByAdminNumber(adminNumber);

        if (student == null) {
            DialogUtil.showMessage("Student not found.");
            return;
        }

        boolean addMore = true;
        while (addMore) {
            Module module = createModule(student.getModules());
            if (module == null) return;
            student.getModules().add(module);
            student.updateGPA();
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to add more modules?", "Warning", JOptionPane.YES_NO_OPTION);
            addMore = dialogResult == JOptionPane.YES_OPTION;
        }
    }
    public void forecastGPA(){
        String adminNumber = getValidStringInput("Enter the admin number of the student:");
        if (adminNumber == null) return;
        Student student = sm.findStudentByAdminNumber(adminNumber);

        if (student == null) {
            DialogUtil.showMessage("Student not found.");
            return;
        }
        double targetGPA = getValidDoubleInput("Enter the target GPA:", 0, 4);
        if(targetGPA == -1) return;
        int additionalModules = getValidIntegerInput("Enter the number of additional modules :", 0, 20);
        if(additionalModules == -1) return;

        int[] creditUnits = new int[additionalModules];
        int[] marks = new int[additionalModules];

        for (int i = 0; i < additionalModules; i++) {
            creditUnits[i] = getValidIntegerInput("Enter the credit units for module " + (i + 1) + ":", 0, 20);
            if (creditUnits[i] == -1) return;
            marks[i] = getValidIntegerInput("Enter marks for module " + (i + 1) + ":", 0, 100);
            if (marks[i] == -1) return;
        }
        boolean canAchieve = sm.canAchieveTargetGPA(student, targetGPA, additionalModules, creditUnits, marks);
        String message = "With the given future grades, " + (canAchieve ? "you can" : "you cannot") + " achieve the target GPA of " + targetGPA;
        DialogUtil.showMessage(message);
    }

    private Module createModule(ArrayList<Module> existingModules) {
        String moduleCode = getUniqueModuleCode(existingModules);

        while (moduleCode == null || !moduleCode.matches("ST\\d{5}")) {
            DialogUtil.showMessage("Invalid module code. Module code should be in the format ST#####.");
            moduleCode = getUniqueModuleCode(existingModules);
        }

        Module existingModule = sm.findModuleByCode(moduleCode);

        if (existingModule != null) {
            DialogUtil.showMessage("This module code already exists in the system. Automatically added with module name: " + existingModule.getModuleNames() + " and credit units: " + existingModule.getCreditUnits());
            int marks = getValidIntegerInput("Enter marks:", 0, 100);
            if (marks == -1) return null;
            return new Module(moduleCode, existingModule.getModuleNames(), existingModule.getCreditUnits(), marks);
        } else {
            String moduleName = getUniqueModuleName();
            if (moduleName == null) return null;
            int creditUnits = getValidIntegerInput("Enter credit units:", 0, Integer.MAX_VALUE);
            if (creditUnits == -1) return null;
            int marks = getValidIntegerInput("Enter marks:", 0, 100);
            if (marks == -1) return null;
            return new Module(moduleCode, moduleName, creditUnits, marks);
        }
    }

    private String getUniqueModuleName() {
        while (true) {
            String moduleName = getValidStringInput("Enter module name:");
            if (moduleName == null) return null;
            if (!moduleNameExists(moduleName)) {
                return moduleName;
            }
            DialogUtil.showMessage("Module name already exists in the system. Please enter a different module name.");
        }
    }

    private boolean moduleNameExists(String moduleName) {
        for (Student s : sm.getStudents()) {
            for (Module module : s.getModules()) {
                if (module.getModuleNames().equalsIgnoreCase(moduleName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getUniqueModuleCode(ArrayList<Module> modules) {
        while (true) {
            String moduleCode = getValidStringInput("Enter module code:");
            if (moduleCode == null) return null;
            if (!moduleCodeExists(moduleCode, modules)) {
                return moduleCode;
            }

            DialogUtil.showMessage("Module code already added. Please enter a different module code.");
        }
    }

    private boolean moduleCodeExists(String moduleCode, ArrayList<Module> modules) {
        for (Module module : modules) {
            if (module.getModuleCodes().equals(moduleCode)) {
                return true;
            }
        }
        return false;
    }

    private String getValidStringInput(String message) {
        while (true) {
            String input = DialogUtil.getInput(message);
            if (input == null) {
                return null;
            }
            if (!input.trim().isEmpty()) {
                return input;
            }
            DialogUtil.showMessage("Invalid input. Please enter a non-empty string.");
        }
    }

    private String getValidAdminNumber() {
        while (true) {
            String adminNumber = DialogUtil.getInput("Enter the admin number (start with p):");
            if (adminNumber == null) {
                return null;
            }
            if (adminNumber.matches("^p\\d{7}$")) {
                return adminNumber;
            }
            DialogUtil.showMessage("Invalid admin number. Admin number should start with 'p' and contain 7 digits.");
        }
    }

    private String getUniqueAdminNumber() {
        while (true) {
            String adminNumber = getValidAdminNumber();
            if (adminNumber == null) return null;
            if (sm.findStudentByAdminNumber(adminNumber) == null) {
                return adminNumber;
            }
            DialogUtil.showMessage("Admin number already exists. Please enter a different admin number.");
        }
    }

    private String getValidStudentClass() {
        while (true) {
            String studentClass = DialogUtil.getInput("Enter the class (format: DIT/FT/2A/01):");

            if (studentClass != null && Pattern.matches("^[A-Za-z0-9]{2,4}/[A-Za-z]{2}/\\d[A-Za-z]/\\d{2}$", studentClass)) {
                return studentClass;
            }
            DialogUtil.showMessage("Invalid class format. Please use the format DIT/FT/2A/01.");
        }
    }

    private int getValidIntegerInput(String message, int minValue, int maxValue) {
        while (true) {
            String inputStr = DialogUtil.getInput(message);
            if (inputStr == null) {
                return -1;
            }
            try {
                int input = Integer.parseInt(inputStr);
                if (input >= minValue && input <= maxValue) {
                    return input;
                } else {
                    DialogUtil.showMessage("Invalid input. Please enter a value between " + minValue + " and " + maxValue + ".");
                }
            } catch (NumberFormatException e) {
                DialogUtil.showMessage("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private double getValidDoubleInput(String message, double minValue, double maxValue) {
        while (true) {
            String inputStr = DialogUtil.getInput(message);
            if (inputStr == null) {
                return -1;
            }
            try {
                double input = Double.parseDouble(inputStr);
                if (input >= minValue && input <= maxValue) {
                    return input;
                } else {
                    DialogUtil.showMessage("Invalid input. Please enter a value between " + minValue + " and " + maxValue + ".");
                }
            } catch (NumberFormatException e) {
                DialogUtil.showMessage("Invalid input. Please enter a valid double.");
            }
        }
    }
    public void displayStatistics() {
        String[] columnNames = {"Statistic", "Value"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        model.addRow(new Object[]{"Median GPA", String.format("%.2f", sm.calculateMedianGPA())});
        model.addRow(new Object[]{"Highest GPA", String.format("%.2f", sm.calculateHighestGPA())});
        model.addRow(new Object[]{"Lowest GPA", String.format("%.2f", sm.calculateLowestGPA())});
        model.addRow(new Object[]{"No of students above GPA 3.0", sm.countStudentsAboveGPAThreshold(3.0)});

        new InterfaceUtil(model,"Student Performance Statistics");
    }




}