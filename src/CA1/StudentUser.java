/*
 * Name : LIN HTET
 * ADMIN NO : p2340304
 * CLASS : DIT/FT/2A/03
 * */

package CA1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static java.lang.StringTemplate.STR;

import java.util.ArrayList;

public class StudentUser {
    private final StudentManagement sm;

    public StudentUser(StudentManagement sm) {
        this.sm = sm;
    }

    public void displayAllStudData() {
        String[] column_names = {"Attribute", "Value"};
        DefaultTableModel model = new DefaultTableModel(column_names, 0);

        int count = 1;
        for (Student student : sm.getStudents()) {
            model.addRow(new Object[]{"Student No.", count++});
            model.addRow(new Object[]{"Name", student.getName()});
            model.addRow(new Object[]{"Admin No", student.getAdminNumber()});
            model.addRow(new Object[]{"Class", student.getStudentClass()});

            int module_no = 1;
            for (Module module : student.getModules()) {
                model.addRow(new Object[]{STR."Module \{module_no++}", STR."\{module.getModuleCodes()}/\{module.getModuleNames()}/\{module.getCreditUnits()}: \{module.getGrade()}"});
            }

            model.addRow(new Object[]{"", ""});
        }

        new InterfaceUtil(model,"All Student Report");
    }

    public void searchStudentByClass() {
        String student_class = sm.validateClassInput();
        if(student_class == null) return;

        double total_gpa = 0.0;

        ArrayList<Student> studentsInClass = sm.findStudentsByClass(student_class);
        if (studentsInClass.isEmpty()) {
            DialogUtil.showMessage("No students found from class");
            return;
        }

        for(Student student : studentsInClass){
            total_gpa += student.getGPA();
        }

        double avg_gpa = total_gpa/studentsInClass.size();

        String message = STR."Number of Student(s) in \{student_class}:\{studentsInClass.size()}\nAverage GPA : \{String.format("%.2f", avg_gpa)}";

        DialogUtil.showMessage(message);
    }

    public void searchStudentByName() {
        String stud_name = sm.validateStudentName();
        if(stud_name == null) return;


        ArrayList<Student> studentsByName = sm.findStudentsByName(stud_name);
        if (studentsByName.isEmpty()) {
            DialogUtil.showErrorMessage(STR."Cannot find the student\"\{stud_name}\"!!");
            return;
        }

        String[] column_names = {"Attribute", "Value"};
        DefaultTableModel model = new DefaultTableModel(column_names, 0);

         //the condition to check input student name, 
         //Duplicate student name is detected
         //Confirm dialog will display
        if (sm.isNotUnique(studentsByName)) {
            int dialog_result = DialogUtil.showConfirm(STR."\{studentsByName.size()} students found with this name \"\{stud_name}\". Want to filter by admin number?", "Filter by Admin Number? (Y/N)");
            if (dialog_result == JOptionPane.NO_OPTION) {
                for (Student student : studentsByName) {
                    formatStudentDetails(student,model);
                }

            } else {
                String adm_no = DialogUtil.getInput("Enter the admin number to filter:");
                if (adm_no == null || adm_no.trim().isEmpty()) {
                    DialogUtil.showMessage("Invalid input. Admin number cannot be empty.");
                    return;
                }

                if(adm_no.matches("^p\\d{7}$")){
                    Student student = sm.findStudentByAdminNumber(adm_no);
                    if (student != null) {
                        formatStudentDetails(student,model);
                    } else {
                        DialogUtil.showMessage(STR."No student found with admin number \{adm_no}");
                        return;
                    }
                }
                else{
                    DialogUtil.showMessage("Invalid admin number. Admin number should start with 'p' followed 7 digits. (e.g. pxxxxxxx)");
                    return;
                }
            }
        }
        else {
            for (Student student : studentsByName) {
                formatStudentDetails(student,model);
            }
        }
        new InterfaceUtil(model, "Student Info");
    }

    //Method to display data as turbulated format
    private void formatStudentDetails(Student student, DefaultTableModel model) {
        model.addRow(new Object[]{"Name", student.getName()});
        model.addRow(new Object[]{"Admin No", student.getAdminNumber()});
        model.addRow(new Object[]{"Class", student.getStudentClass()});

        int module_no = 1;
        for (Module module : student.getModules()) {
            model.addRow(new Object[]{STR."Module \{module_no++}", STR."\{module.getModuleCodes()}/\{module.getModuleNames()}/\{module.getCreditUnits()}: \{module.getGrade()}"});
        }
        model.addRow(new Object[]{"GPA",String.format("%.2f",student.getGPA())});

        model.addRow(new Object[]{"", ""});
    }

    public void displayMostDifficultModules() {
        ArrayList<Module> modules = sm.findMostDifficultModules();
        if (modules.isEmpty()) {
            DialogUtil.showMessage("No modules found.");
        } else {
            StringBuilder message = new StringBuilder("Most Difficult Modules:\n\n");
            for (Module module : modules) {
                message.append("Module code: ").append(module.getModuleCodes()).append("\n");
                message.append("Module name: ").append(module.getModuleNames()).append("\n");
                message.append("Module credit: ").append(module.getCreditUnits()).append("\n\n");
            }
            DialogUtil.showMessage(message.toString());
        }
    }

    public void displayEasiestModules() {
        ArrayList<Module> modules = sm.findEasiestModules();
        if (modules.isEmpty()) {
            DialogUtil.showMessage("No modules found.");
        } else {
            StringBuilder message = new StringBuilder("Easiest Modules:\n\n");
            for (Module module : modules) {
                message.append("Module code: ").append(module.getModuleCodes()).append("\n");
                message.append("Module name: ").append(module.getModuleNames()).append("\n");
                message.append("Module credit: ").append(module.getCreditUnits()).append("\n\n");
            }
            DialogUtil.showMessage(message.toString());
        }
    }

}

