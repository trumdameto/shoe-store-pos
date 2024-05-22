package view;

import model.KhachHang;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import repository.KhachHangRepository;

public class ThanhVienFrame extends javax.swing.JFrame {

    private DefaultTableModel modelKhachHang;
    private KhachHangRepository khachHangRepository = new KhachHangRepository();

    public ThanhVienFrame() {
        initComponents();
        modelKhachHang = (DefaultTableModel) tblKhachHang.getModel();
        init();
        setLocationRelativeTo(null);
    }

    private void init() {
        ShowDataKhachHang(khachHangRepository.selectAll());
    }

    private void configTbl() {
        //1080
        this.tblKhachHang.getColumnModel().getColumn(0).setPreferredWidth(50);
        this.tblKhachHang.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblKhachHang.getColumnModel().getColumn(2).setPreferredWidth(190);
        this.tblKhachHang.getColumnModel().getColumn(3).setPreferredWidth(50);
        this.tblKhachHang.getColumnModel().getColumn(4).setPreferredWidth(120);

    }

    private void ShowDataKhachHang(List<KhachHang> list) {
        modelKhachHang.setRowCount(0);
        for (KhachHang k : list) {
            modelKhachHang.addRow(new Object[]{
                modelKhachHang.getRowCount() + 1,
                k.getMa(),
                k.getName(),
                k.getGioiTinh() == false ? "Nam" : "Nữ",
                k.getSdt(),
                k.getEmail()

            });
        }
        modelKhachHang.setColumnIdentifiers(new String[]{"STT", "Mã ", "Tên khách hàng", "Giới tính", "SĐT", "Email"});
    }

    KhachHang getForm() {
        String ten = txtTenKhachHang.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();
        boolean gt = rdoNam.isSelected() ? false : true;

        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (ten.isEmpty()) {
            lblErrorTen.setText("Tên Không Để Trống Dữ Liệu");
            return null;
        } else {
            lblErrorTen.setText(" ");
        }
        if (sdt.isEmpty()) {
            lblErrorSdt.setText("Số điện thoại Không Để Trống Dữ Liệu");
            return null;
        } else {
            boolean isDuplicate = false;
            for (KhachHang khachHang : khachHangRepository.selectAll()) {
                if (khachHang.getSdt() == sdt) {
                    isDuplicate = true;
                    lblErrorSdt.setText("Số điện thoại Bị Trùng");
                    return null;
                }
            }
            if (!isDuplicate) {
                lblErrorSdt.setText(" ");
            }
        }
        if (email.isEmpty()) {
            lblErrorEmail.setText("Email Không Để Trống Dữ Liệu");
            return null;
        } else {
            if (Pattern.matches(regex, email)) {
                boolean isDuplicate = false;
                for (KhachHang khachHang : khachHangRepository.selectAll()) {
                    if (khachHang.getEmail() == email) {
                        isDuplicate = true;
                        lblErrorEmail.setText("Email Bị Trùng");
                        return null;
                    }
                }
                if (!isDuplicate) {
                    lblErrorEmail.setText(" ");
                }
            } else {
                lblErrorEmail.setText("Email Không Đúng Định Dạng");
                return null;
            }
        }

        KhachHang k = new KhachHang();
        k.setName(ten);
        k.setSdt(sdt);
        k.setGioiTinh(gt);
        k.setEmail(email);
        return k;

    }

    void reset() {
        lblMaKhachHang.setText(null);
        lblMaKhachHang.setText("");
        txtSoDienThoai.setText(null);
        txtTenKhachHang.setText(null);
        txtTimKiem.setText(null);
        txtEmail.setText(null);
        lblErrorEmail.setText(" ");
        lblErrorSdt.setText(" ");
        lblErrorTen.setText(" ");
    }

    private List<KhachHang> search(String search) {
        List<KhachHang> l = new ArrayList<>();
        for (KhachHang khachHang : khachHangRepository.selectAll()) {
            String ma = khachHang.getMa().toLowerCase();
            String ten = khachHang.getName().toLowerCase();
            String sdt = khachHang.getSdt().toLowerCase();
            String email = "";
            if (khachHang.getEmail() != null) {
                email = khachHang.getEmail().toLowerCase();
            } 
            if (ma.contains(search.toLowerCase()) || ten.contains(search.toLowerCase())
                    || sdt.contains(search.toLowerCase()) || email.contains(search.toLowerCase())) {
                l.add(khachHang);
            }
        }
        return l;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();
        lblMaKhachHang = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        txtTenKhachHang = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        txtSoDienThoai = new javax.swing.JTextField();
        jLabel87 = new javax.swing.JLabel();
        rdoNu = new javax.swing.JRadioButton();
        rdoNam = new javax.swing.JRadioButton();
        jLabel88 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnDangKy = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        lblErrorTen = new javax.swing.JLabel();
        lblErrorSdt = new javax.swing.JLabel();
        lblErrorEmail = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel93 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblKhachHang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã", "Tên khách hàng", "Giới tính", "SĐT", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHang.setDoubleBuffered(true);
        tblKhachHang.setRowHeight(30);
        tblKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tblKhachHang);

        lblMaKhachHang.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel85.setText("Mã Thành Viên");

        txtTenKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenKhachHangActionPerformed(evt);
            }
        });

        jLabel86.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel86.setText("Tên Khách Hàng");

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel87.setText("Số Điện Thoại");

        buttonGroup1.add(rdoNu);
        rdoNu.setText("Nữ");

        buttonGroup1.add(rdoNam);
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");

        jLabel88.setBackground(new java.awt.Color(255, 255, 255));
        jLabel88.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel88.setText("Giới Tính");

        txtTimKiem.setBackground(new java.awt.Color(240, 240, 240));
        txtTimKiem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập mã để tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        btnDangKy.setText("Đăng Ký");
        btnDangKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangKyActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnNew.setText("New");
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNewMouseClicked(evt);
            }
        });
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        jLabel89.setForeground(new java.awt.Color(204, 0, 0));
        jLabel89.setText("*");

        jLabel90.setForeground(new java.awt.Color(255, 0, 0));
        jLabel90.setText("*");

        jLabel91.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(0, 51, 51));
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("Đăng Ký Thành Viên");

        lblErrorTen.setForeground(new java.awt.Color(255, 0, 0));
        lblErrorTen.setText(" ");

        lblErrorSdt.setForeground(new java.awt.Color(255, 0, 0));
        lblErrorSdt.setText(" ");

        lblErrorEmail.setForeground(new java.awt.Color(255, 0, 0));
        lblErrorEmail.setText(" ");

        jLabel92.setForeground(new java.awt.Color(255, 0, 0));
        jLabel92.setText("*");

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel93.setText("Email");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 777, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(182, 182, 182)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lblMaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel86)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(lblErrorTen, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(rdoNam)
                        .addGap(44, 44, 44)
                        .addComponent(rdoNu))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                            .addGap(121, 121, 121)
                            .addComponent(lblErrorSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel13Layout.createSequentialGroup()
                            .addComponent(jLabel93)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(67, 67, 67)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(btnNew)
                        .addGap(6, 6, 6)
                        .addComponent(btnDangKy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDangKy, btnNew, btnSua});

        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))))
                .addGap(4, 4, 4)
                .addComponent(lblErrorTen)
                .addGap(10, 10, 10)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(lblErrorSdt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblErrorEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdoNam)
                            .addComponent(rdoNu))))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew)
                    .addComponent(btnDangKy)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel13Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblMaKhachHang, txtEmail, txtSoDienThoai, txtTenKhachHang});

        jPanel13Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDangKy, btnNew, btnSua});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhachHangMouseClicked
        int selectedRow = tblKhachHang.getSelectedRow();
        if (selectedRow != -1) {
            lblMaKhachHang.setText((String) tblKhachHang.getValueAt(selectedRow, 1));
            txtSoDienThoai.setText(tblKhachHang.getValueAt(selectedRow, 4).toString());
            txtTenKhachHang.setText(tblKhachHang.getValueAt(selectedRow, 2).toString());
            lblMaKhachHang.setText(tblKhachHang.getValueAt(selectedRow, 1).toString());
            if (tblKhachHang.getValueAt(selectedRow, 5) == null) {
                txtEmail.setText(null);
            } else {
                txtEmail.setText(tblKhachHang.getValueAt(selectedRow, 5).toString());
            }
            boolean value = tblKhachHang.getValueAt(selectedRow, 3).toString().equals("Nam");
            rdoNam.setSelected(value);
            rdoNu.setSelected(!value);
        }
    }//GEN-LAST:event_tblKhachHangMouseClicked

    private void txtTenKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenKhachHangActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        String searchText = txtTimKiem.getText().trim();
        ShowDataKhachHang(search(searchText));
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void btnDangKyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangKyActionPerformed
        int check = JOptionPane.showConfirmDialog(this, "Đăng Ký Thành Viên");
        if (check == JOptionPane.YES_OPTION) {
            KhachHang khachHang = getForm();
            int row = khachHangRepository.insert(khachHang);
            if (row == 1) {
                JOptionPane.showMessageDialog(this, "Thêm Thành Công");
                ShowDataKhachHang(khachHangRepository.selectAll());
            } else {
                JOptionPane.showMessageDialog(this, "Thêm Thất Bại");
            }
        }
    }//GEN-LAST:event_btnDangKyActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int check = JOptionPane.showConfirmDialog(this, "Sửa Thành Viên");
        if (check == JOptionPane.YES_OPTION) {
            int selectedRow = tblKhachHang.getSelectedRow();
            if (selectedRow >= 0) {
                String ma = (String) tblKhachHang.getValueAt(selectedRow, 1);
                KhachHang k = new KhachHang();
                k.setMa(ma);
                KhachHang khachHang = khachHangRepository.selectbyId(k);
                khachHang.setName(getForm().getName());
                khachHang.setSdt(getForm().getSdt());
                khachHang.setGioiTinh(getForm().getGioiTinh());
                khachHang.setEmail(getForm().getEmail());
                System.out.println(khachHang);

                if (khachHang != null) {
                    if (khachHangRepository.update(khachHang) != null) {
                        init();
                        reset();
                        JOptionPane.showMessageDialog(this, "Sửa Thành Công");

                    } else {
                        JOptionPane.showMessageDialog(this, "Thất Bại");
                    }
                }
                ShowDataKhachHang(khachHangRepository.selectAll());
            }
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNewMouseClicked
        lblMaKhachHang.setText("");
        txtSoDienThoai.setText(null);
        txtTenKhachHang.setText(null);
        txtTimKiem.setText(null);
    }//GEN-LAST:event_btnNewMouseClicked

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        reset();
        init();
    }//GEN-LAST:event_btnNewActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(ThanhVienFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThanhVienFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThanhVienFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThanhVienFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThanhVienFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangKy;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSua;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JLabel lblErrorEmail;
    private javax.swing.JLabel lblErrorSdt;
    private javax.swing.JLabel lblErrorTen;
    private javax.swing.JLabel lblMaKhachHang;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTenKhachHang;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
