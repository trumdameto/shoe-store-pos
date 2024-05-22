package repository;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Validate {
     public static void validateEmty(JTextField field,  String errorMessage){
        if (field.getText().equals("")) {
            JOptionPane.showMessageDialog(field, errorMessage);
            field.setBackground(new Color(247, 177, 187));
            field.requestFocus();
        }else{
            field.setBackground(new Color(151,240,181));
        }
    }
    
    
    public static void validateEmtyPass(JPasswordField field, String errorMessage){
        String password = new String(field.getPassword());
        if (password.equals("")) {
            JOptionPane.showMessageDialog(field, errorMessage);
            field.setBackground(new Color(247, 177, 187));
            field.requestFocus();
        }else{
            field.setBackground(new Color(151,240,181));
        }
    }
}
