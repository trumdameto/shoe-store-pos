package view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import repository.JOPane;
import repository.ThongKeRepo;
import model.ThongKeModel;

public class ThongKe extends javax.swing.JPanel {

    private DefaultTableModel model = new DefaultTableModel();
    private ThongKeRepo thongKe = new ThongKeRepo();
    private List<ThongKeModel> listtk = new ArrayList<>();

    public ThongKe() {
        initComponents();
        setOpaque(false);
        listtk = thongKe.getDoanhThuAll();
        fillTableDoanhThuTheoNgay();
        model = (DefaultTableModel) tblDoanhThu.getModel();
        hienThiTongTien(listtk);
    }

    void fillTableDoanhThuTheoNgay() {//Điền dữ liệu vào bảng doanh thu theo ngày tương ứng
        model.setRowCount(0);
        for (ThongKeModel t : listtk) {
            model.addRow(new Object[]{t.getDate(), t.getDoanhthu()});
        }
    }
    public void hienThiTongTien(List<ThongKeModel> danhSachDoanhThu) {
    BigDecimal tongTien = BigDecimal.ZERO;

    for (ThongKeModel thongKeModel : danhSachDoanhThu) {
        tongTien = tongTien.add(thongKeModel.getDoanhthu());
    }

    lblTong.setText(tongTien.toString());
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDoanhThu = new javax.swing.JTable();
        txtNgay = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnTim = new javax.swing.JButton();
        lblTong = new javax.swing.JLabel();
        cboThang = new javax.swing.JComboBox<>();

        tblDoanhThu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ngày", "Doanh Thu"
            }
        ));
        jScrollPane1.setViewportView(tblDoanhThu);

        jLabel1.setText("Ngay");

        btnTim.setText("Tìm");
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        lblTong.setText("jLabel2");

        cboThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cboThang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboThangItemStateChanged(evt);
            }
        });
        cboThang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboThangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1043, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(81, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTim))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(cboThang, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTong, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(271, 271, 271))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(lblTong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(cboThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTim))
                        .addGap(49, 49, 49)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        String ngay = txtNgay.getText();
        if (ngay.trim().isEmpty()) {
            JOPane.showMessageDialog(this, "Hãy nhập ngày để xem doanh thu");
        } else {
            try {
                Date ngayDT = new SimpleDateFormat("yyyy-MM-dd").parse(ngay);
                List<ThongKeModel> doanhThuList = thongKe.getDoanhThuTheoNgay1(ngayDT);

                if (doanhThuList != null) {
                    model.setRowCount(0);
                    for (ThongKeModel t : doanhThuList) {
                        model.addRow(new Object[]{t.getDate(), t.getDoanhthu()});
                    }
                     hienThiTongTien( doanhThuList);

                } else {
                    JOPane.showMessageDialog(this, "Lỗi khi truy vấn doanh thu theo ngày");
                }
            } catch (ParseException ex) {
                Logger.getLogger(ThongKeModel.class.getName()).log(Level.SEVERE, null, ex);
                JOPane.showMessageDialog(this, "Lỗi chuyển đổi ngày");
            }
        }
    }//GEN-LAST:event_btnTimActionPerformed

    private void cboThangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboThangItemStateChanged

        String thang = cboThang.getSelectedItem().toString();
        if (!thang.trim().isEmpty()) {
            int thangchuyn = Integer.parseInt(thang);
            thongKe.getDoanhThuTheoThang(thangchuyn);
            model.setRowCount(0);
            for (ThongKeModel t : thongKe.getDoanhThuTheoThang(thangchuyn)) {
                model.addRow(new Object[]{t.getDate(), t.getDoanhthu()});
            }
            fillTableDoanhThuTheoNgay();
            hienThiTongTien( thongKe.getDoanhThuTheoThang(thangchuyn));
        }
    }//GEN-LAST:event_cboThangItemStateChanged

    private void cboThangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboThangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboThangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTim;
    private javax.swing.JComboBox<String> cboThang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTong;
    private javax.swing.JTable tblDoanhThu;
    private javax.swing.JTextField txtNgay;
    // End of variables declaration//GEN-END:variables
}
