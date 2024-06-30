/*
 * Name : LIN HTET & NAY HTET AUNG
 * ADMIN NO : p2340304 & p2340391
 * CLASS : DIT/FT/2A/03
 * */

package CA1;

import javax.swing.*;

// Utility class for common dialog operations
class DialogUtil {
    static String getInput(String message) {
        return JOptionPane.showInputDialog(message);
    }

    static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    static void showErrorMessage(String message){JOptionPane.showMessageDialog(null,message,"Info",JOptionPane.ERROR_MESSAGE);}

    static String getChoice(String message, String[] options, String title) {
        return (String) JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    static int showConfirm(String message, String title) {
       return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);

    }
}
