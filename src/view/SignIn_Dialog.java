package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JLabel;
import main.main;
import model.NhanVien;
import repository.Account;
import repository.XImage;
import repository.Validate;
import repository.JOPane;
import util.Auth;

public class SignIn_Dialog extends javax.swing.JDialog {

    private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
    private Validate validate = new Validate();
    private Account acc = new Account();
    private ArrayList<NhanVien> list = new ArrayList<>();
    
    public SignIn_Dialog() {
        initComponents();
        rdoAdmin.setEnabled(false);
        setLocationRelativeTo(null);
        setIconImage(XImage.APP_ICON);
        mouseEvent();
    }

    
    
    private String getSelectedRole() {
        if (rdoStaff.isSelected()) {
            return "Staff";
        } else if (rdoAdmin.isSelected()) {
            return "Admin";
        } else {
            return ""; // hoặc giá trị mặc định khác nếu cần
        }
    }

    private void validateSignIn(String username, String password, String role) {
        if (username.isEmpty()) {
            JOPane.showMessageDialog(this, "Username is empty!");
            txtUsername.requestFocus();
            txtUsername.setBackground(new Color(247, 154, 164));
        } else {
            txtUsername.setBackground(getBackground());
        }

        if (password.isEmpty()) {
            JOPane.showMessageDialog(this, "Password is empty!");
            txtPassword.requestFocus();
            txtPassword.setBackground(new Color(247, 154, 164));
        } else {
            txtPassword.setBackground(getBackground());
        }

        if (role.isEmpty()) {
            JOPane.showMessageDialog(this, "Role not selected!");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGrRole = new javax.swing.ButtonGroup();
        pnlMainLogin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        chkShowPassword = new javax.swing.JCheckBox();
        txtPassword = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        lblSignUp = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        rdoStaff = new javax.swing.JRadioButton();
        rdoAdmin = new javax.swing.JRadioButton();
        lblForgotPassword = new javax.swing.JLabel();
        lblCloseSignIn = new javax.swing.JLabel();
        lblBtnLogin = new javax.swing.JLabel();

        setUndecorated(true);

        pnlMainLogin.setBackground(new java.awt.Color(239, 247, 238));

        jLabel1.setBackground(new java.awt.Color(239, 247, 238));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 2, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sign In");

        jPanel1.setBackground(new java.awt.Color(239, 247, 238));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setText("Username");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel3.setText("Password");

        txtUsername.setBackground(new java.awt.Color(239, 247, 238));
        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUsername.setPreferredSize(new java.awt.Dimension(106, 32));
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsernameKeyReleased(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Logo.png"))); // NOI18N

        chkShowPassword.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        chkShowPassword.setText("Show Password ?");
        chkShowPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowPasswordActionPerformed(evt);
            }
        });

        txtPassword.setBackground(new java.awt.Color(239, 247, 238));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("No user account ?");

        lblSignUp.setBackground(new java.awt.Color(255, 255, 255));
        lblSignUp.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblSignUp.setForeground(new java.awt.Color(51, 51, 51));
        lblSignUp.setText("Sign Up Now");
        lblSignUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSignUpMouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Role");

        btnGrRole.add(rdoStaff);
        rdoStaff.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        rdoStaff.setText("Staff");

        btnGrRole.add(rdoAdmin);
        rdoAdmin.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        rdoAdmin.setText("Admin");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel8)
                        .addComponent(jLabel3)))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdoStaff)
                        .addGap(18, 18, 18)
                        .addComponent(rdoAdmin))
                    .addComponent(chkShowPassword)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                        .addComponent(txtPassword)))
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSignUp)
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkShowPassword)))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(rdoStaff)
                    .addComponent(rdoAdmin))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblSignUp)))
        );

        lblForgotPassword.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblForgotPassword.setForeground(new java.awt.Color(51, 51, 51));
        lblForgotPassword.setText("Forgot Password?");
        lblForgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblForgotPasswordMouseClicked(evt);
            }
        });

        lblCloseSignIn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCloseSignIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Close_2.png"))); // NOI18N
        lblCloseSignIn.setPreferredSize(new java.awt.Dimension(38, 48));
        lblCloseSignIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseSignInMouseClicked(evt);
            }
        });

        lblBtnLogin.setBackground(new java.awt.Color(255, 255, 255));
        lblBtnLogin.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblBtnLogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBtnLogin.setText("Login");
        lblBtnLogin.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.darkGray, java.awt.Color.darkGray));
        lblBtnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBtnLoginMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlMainLoginLayout = new javax.swing.GroupLayout(pnlMainLogin);
        pnlMainLogin.setLayout(pnlMainLoginLayout);
        pnlMainLoginLayout.setHorizontalGroup(
            pnlMainLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLoginLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblCloseSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLoginLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblForgotPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBtnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );
        pnlMainLoginLayout.setVerticalGroup(
            pnlMainLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLoginLayout.createSequentialGroup()
                .addGroup(pnlMainLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCloseSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlMainLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblBtnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(lblForgotPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMainLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMainLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void chkShowPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowPasswordActionPerformed
        if (chkShowPassword.isSelected()) {
            txtPassword.setEchoChar((char) 0);
        } else {
            txtPassword.setEchoChar('*');
        }
    }//GEN-LAST:event_chkShowPasswordActionPerformed

    private void lblCloseSignInMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseSignInMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblCloseSignInMouseClicked

    private void lblBtnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBtnLoginMouseClicked
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty()) {
            JOPane.showMessageDialog(this, "Username is empty!");
            txtUsername.requestFocus();
            txtUsername.setBackground(new Color(250, 207, 212));
            return;
        } else {
            txtUsername.setBackground(getBackground());
        }

        if (password.isEmpty()) {
            JOPane.showMessageDialog(this, "Password is empty!");
            txtPassword.requestFocus();
            txtPassword.setBackground(new Color(250, 207, 212));
            return;
        } else {
            txtPassword.setBackground(getBackground());
        }
        if (rdoAdmin.isSelected()) {
            Auth.isManager();
        }
        NhanVien nv = acc.selectById(username);
        if (nv == null) {
            JOPane.showErrorDialog(this, "Check username", "Error");
        } else if (!nv.getMatkhau().equals(password)) {
            JOPane.showErrorDialog(this, "Check password", "Error");
        } else {
            Auth.user = nv;
            JOPane.showMessageDialog(this, "Login successfully!");
            new main().setVisible(true);
            this.dispose();
        }

    }//GEN-LAST:event_lblBtnLoginMouseClicked

    private void lblSignUpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSignUpMouseClicked
        new SignUp_Dialog().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_lblSignUpMouseClicked

    private void lblForgotPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblForgotPasswordMouseClicked
        new ForgotPass_Dialog().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_lblForgotPasswordMouseClicked

    private void txtUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyReleased
        if (txtUsername.getText().equals("NV000")) {
            rdoAdmin.setEnabled(true);
            rdoAdmin.setSelected(true);
            rdoStaff.setEnabled(false);
        } else {
            rdoAdmin.setEnabled(false);
            rdoStaff.setEnabled(true);
            rdoStaff.setSelected(true);
        }
    }//GEN-LAST:event_txtUsernameKeyReleased

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SignIn_Dialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignIn_Dialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignIn_Dialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignIn_Dialog.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignIn_Dialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGrRole;
    private javax.swing.JCheckBox chkShowPassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblBtnLogin;
    private javax.swing.JLabel lblCloseSignIn;
    private javax.swing.JLabel lblForgotPassword;
    private javax.swing.JLabel lblSignUp;
    private javax.swing.JPanel pnlMainLogin;
    private javax.swing.JRadioButton rdoAdmin;
    private javax.swing.JRadioButton rdoStaff;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    private void mouseEvent() {
        JLabel[] labels = new JLabel[]{lblBtnLogin, lblForgotPassword, lblSignUp};
        txtUsername.setText("NV001");
        txtPassword.setText("12345");
        rdoStaff.setSelected(true);
        for (JLabel label : labels) {
            label.setCursor(cursor);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setForeground(new Color(127, 133, 126));
                    label.setBackground(Color.black);
//                    Border colorBorder = BorderFactory.createEtchedBorder(new Color(127, 133, 126), new Color(127, 133, 126));
//                    label.setBorder(colorBorder);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.setForeground(getBackground());

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

            });
        }
    }
}
