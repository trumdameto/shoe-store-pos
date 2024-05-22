package main;

import component.Header;
import component.Menu;
import event.EventMenuSelected;
import form.MainForm;

import form.panel2;
import form.panel3;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import swing.CloseFrame;
import loai.GiayPanel1;
import view.GiayView;
import loai.HoaDonForm1;
import loai.HoaDonPanel;
import view.HoaDonView;
import loai.LichSuHoaDon;
import view.LichSuHoaDonView;
import view.MenuFrame;
import view.QLThuocTinhPanel;
import view.ThanhVienView;
import view.ThongKe;
import view.VoucherView;

public class main extends javax.swing.JFrame implements CloseFrame {
    
    private MigLayout layout;
    private Menu menu;
//    private Header header;
    private MainForm mainForm;
    
    public main() {
        initComponents();
        init();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon/titleApp.png")));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
    }
    
    private void init() {
        layout = new MigLayout("fill", "0[]0[100%, fill]0", "0[fill, top]0");
        jMain.setLayout(layout);
        menu = new Menu();
//        header = new Header();
        mainForm = new MainForm();
        
        menu.addEvent(new EventMenuSelected() {
            @Override
            public void menuSelected(int menuIdex, int subMenuIdex) {
//                System.out.println("Menu ");
                if (menuIdex == 0) {
                    if (subMenuIdex == 0) {
                        mainForm.showForm(new GiayView());
                    } else if (subMenuIdex == 1) {
                        mainForm.showForm(new QLThuocTinhPanel());
                    }
                } else if (menuIdex == 1) {
                    if (subMenuIdex == 0) {
                     mainForm.showForm(new HoaDonView());
                    } else if (subMenuIdex == 1) {
                        mainForm.showForm(new LichSuHoaDonView());
                    }
                }else if(menuIdex==2){
                    mainForm.showForm(new ThanhVienView());
                }else if(menuIdex==3){
                    mainForm.showForm(new VoucherView());
                }
                else if(menuIdex==4){
                    mainForm.showForm(new ThongKe());
                }
            }
        });
        menu.initMenu();
        jMain.add(menu, "w 230!, spany2");
//        jMain.add(header, "h 50!, wrap");
        jMain.add(mainForm, "w 100%, h 100%");
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMain = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMain.setBackground(new java.awt.Color(255, 255, 255));
        jMain.setOpaque(true);
        jMain.setPreferredSize(new java.awt.Dimension(1500, 800));

        javax.swing.GroupLayout jMainLayout = new javax.swing.GroupLayout(jMain);
        jMain.setLayout(jMainLayout);
        jMainLayout.setHorizontalGroup(
            jMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1500, Short.MAX_VALUE)
        );
        jMainLayout.setVerticalGroup(
            jMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMain, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane jMain;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onClose(JFrame frame) {
        frame.dispose();
    }
}
