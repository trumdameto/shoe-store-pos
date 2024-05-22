package repository;

import java.awt.Component;
import javax.swing.JOptionPane;
import view.DoiTra;

public class JOPane {

    public static void showMessageDialog(Component parent, String content) {
        JOptionPane.showMessageDialog(parent, content, "Meo Meo Store", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showConfirmDialog(Component parent, String content) {
        int choose = JOptionPane.showConfirmDialog(parent, content, "Meo Meo Store",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return choose == JOptionPane.YES_OPTION;
    }

    public static void showErrorDialog(Component parent, String content, String title) {
        JOptionPane.showMessageDialog(parent, content, title, JOptionPane.ERROR_MESSAGE);
    }

    public static int showOptionDialog(Component parent, String content, String[] options, String initialValue) {
        return JOptionPane.showOptionDialog(parent, content, "Meo Meo Store", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, initialValue);
    }

    public static String showInputDialog(Component parent, String content) {
        return JOptionPane.showInputDialog(parent, content, "Meo Meo Store", JOptionPane.QUESTION_MESSAGE);
    }

}
