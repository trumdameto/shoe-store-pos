package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Giay;
import repository.GiayRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.RichTextString;
import model.DanhMuc;
import model.Hang;
import model.KichCo;
import model.KieuDang;
import model.MauSac;
import model.GiayChiTiet;
import repository.GiayChiTietRepository;

public class GiayView extends javax.swing.JPanel {

    private Integer currentPage = 1;
    private Integer totalPage = 1;
    private Integer rowCountPage = 5;

    private List<DanhMuc> listDanhMuc = new ArrayList<>();
    private List<KichCo> listKichCo = new ArrayList<>();
    private List<KieuDang> listKieuDang = new ArrayList<>();
    private List<MauSac> listMauSac = new ArrayList<>();
    private List<Hang> listHang = new ArrayList<>();
    private List<Giay> listGiay = new ArrayList<>();
    private List<GiayChiTiet> listgGiayChiTiets = new ArrayList<>();

    private DefaultTableModel dtm = new DefaultTableModel();
    private GiayRepository repository = new GiayRepository();

    public GiayView() {
        initComponents();
        lblMa.setText("Mã:                      ");
        loadData(repository.selectAll(), currentPage);
    }

    private void setTotalPage(List<Giay> list) {
        int totalItem = list.size();
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = totalItem / rowCountPage;
        }
        lblTrang.setText("Trang " + currentPage + "/" + totalPage);
    }

    private void loadData(List<Giay> list, int page) {
        setTotalPage(list);
        dtm.setRowCount(0);
        dtm = (DefaultTableModel) tblDanhSach.getModel();
        int limit = page * rowCountPage;// 5 = 1 * 5
        int totalItem = list.size();
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            Giay item = list.get(start);
            dtm.addRow(new Object[]{
                item.getMa(),
                item.getName()
            });
            if (start + 1 == limit) {
                return;
            }
        }
    }

    private Giay getForm() {
        String ten = txtTen.getText();
        if (ten.isBlank()) {
            lblErrorTen.setText("Tên không được để trống");
            return null;
        }
        lblErrorTen.setText("");
        Giay giay = new Giay();
        Date date = new Date();
        Long ngayTao = date.getTime() / 1000;
        giay.setName(ten);
        giay.setNgayTao(ngayTao);
        return giay;
    }

    private Giay getTable() {
        int selected = tblDanhSach.getSelectedRow();
        String ma = (String) tblDanhSach.getValueAt(selected, 0);
        Giay giay = new Giay();
        giay.setMa(ma);
        return repository.selectbyId(giay);
    }

    private void detail() {
        Giay giay = getTable();
        lblMa.setText("Mã:                      " + giay.getMa());
        txtTen.setText(giay.getName());
        System.out.println(giay);
    }

    private void btnAdd() {
        if (getForm() == null) {
            return;
        }
        int row = repository.insert(getForm());
        String thongBao = "";
        if (row == 1) {
            thongBao = "Thêm thành công";
        } else {
            thongBao = "Thêm thất bại";
        }
        loadData(repository.selectAll(), currentPage);
        JOptionPane.showMessageDialog(this, thongBao);
    }

    private void btnUpdate() {
        if (tblDanhSach.getSelectedRow() <= -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn đối tượng cần sửa");
            return;
        }
        if (getForm() == null) {
            return;
        }
        Giay giay = getTable();
        giay.setName(getForm().getName());
        int row = repository.update(giay);
        String thongBao = "";
        if (row == 1) {
            thongBao = "Sửa thành công";
        } else {
            thongBao = "Sửa thất bại";
        }
        loadData(repository.selectAll(), currentPage);
        JOptionPane.showMessageDialog(this, thongBao);
    }

    private void search() {
        String search = txtSearch.getText().toLowerCase();
        List<Giay> listSearch = new ArrayList<>();
        for (Giay giay : repository.selectAll()) {
            if (giay.getMa().toLowerCase().contains(search) || giay.getName().toLowerCase().contains(search)) {
                listSearch.add(giay);
            }
        }
        if (totalPage == 1) {
            currentPage = 1;
        }
        loadData(listSearch, currentPage);
    }

    private void btnNext() {
        if (currentPage == totalPage) {
            currentPage = 1;
            search();
        } else {
            currentPage++;
            search();
        }
    }

    private void btnPrev() {
        if (currentPage == 1) {
            currentPage = totalPage;
            search();
        } else {
            currentPage--;
            search();
        }
    }

    private void btnDetail() {
        if (tblDanhSach.getSelectedRow() <= -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn đối tượng cần xem");
            return;
        }
        Giay giay = getTable();
        int confirm = JOptionPane.showConfirmDialog(this, "Quản lý mã: " + giay.getMa() + " \nTên: " + giay.getName());
        if (confirm == JOptionPane.YES_OPTION) {
            ChiTietGiay giayChiTiet = new ChiTietGiay(giay);
            giayChiTiet.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSach = new javax.swing.JTable();
        lblErrorTen = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnChiTiet = new javax.swing.JButton();
        lblTrang = new javax.swing.JLabel();
        lblMa = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btExport = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        tblDanhSach.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDanhSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã", "Tên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDanhSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDanhSach);

        lblErrorTen.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorTen.setText(" ");

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnPrev.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnChiTiet.setText("Chi tiết");
        btnChiTiet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChiTietActionPerformed(evt);
            }
        });

        lblTrang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTrang.setText("Trang");

        lblMa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMa.setText("Mã:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Tên:");

        txtTen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Search");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Giày");

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        btExport.setText("Export");
        btExport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btExportMouseClicked(evt);
            }
        });
        btExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExportActionPerformed(evt);
            }
        });

        btnImport.setText("Import");
        btnImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImportMouseClicked(evt);
            }
        });
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(404, 404, 404)
                .addComponent(lblMa, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(404, 404, 404)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(511, 511, 511)
                .addComponent(lblErrorTen, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(511, 511, 511)
                .addComponent(btnThem)
                .addGap(6, 6, 6)
                .addComponent(btnSua)
                .addGap(6, 6, 6)
                .addComponent(btnChiTiet)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btExport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImport))
            .addGroup(layout.createSequentialGroup()
                .addGap(886, 886, 886)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(lblTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(762, 762, 762)
                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(lblMa)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel4))
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(lblErrorTen)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnChiTiet)
                        .addComponent(btExport)
                        .addComponent(btnImport)))
                .addGap(112, 112, 112)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel7))
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblTrang))
                    .addComponent(btnPrev)
                    .addComponent(btnNext)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblDanhSachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachMouseClicked
        // TODO add your handling code here:
        detail();
    }//GEN-LAST:event_tblDanhSachMouseClicked

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        btnNext();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        btnPrev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        search();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        btnAdd();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        btnUpdate();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnChiTietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChiTietActionPerformed
        // TODO add your handling code here:
        btnDetail();
    }//GEN-LAST:event_btnChiTietActionPerformed

    private void btExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btExportMouseClicked
        // TODO add your handling code here:

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("SanPham");
            XSSFRow row;
            Cell cell;

            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("MaGiay");

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("TenGiay");

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Hang");

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("KieuDang");

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("DanhMuc");

            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue("Mau");

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue("KichCo");

            for (int i = 0; i < listGiay.size(); i++) {
                row = sheet.createRow(i + 1);
                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(listGiay.get(i).getMa());
                cell = row.createCell(1, CellType.NUMERIC);
                cell.setCellValue(listGiay.get(i).getName());

                cell = row.createCell(2, CellType.NUMERIC);
                cell.setCellValue(listHang.get(i).getName());

                cell = row.createCell(3, CellType.NUMERIC);
                cell.setCellValue(listKieuDang.get(i).getName());

                cell = row.createCell(4, CellType.NUMERIC);
                cell.setCellValue(listDanhMuc.get(i).getName());

                cell = row.createCell(5, CellType.NUMERIC);
                cell.setCellValue(listMauSac.get(i).getName());

                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellValue(listKichCo.get(i).getName());

            }

            JFileChooser fileChooser = new JFileChooser();
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try ( FileOutputStream fos = new FileOutputStream(fileToSave)) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(this, "Export successful");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error writing to file");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Export canceled by user");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting data");
        }
    }//GEN-LAST:event_btExportMouseClicked

    private void btExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btExportActionPerformed

    private void importDataFromExcel(File file) {
        try ( Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String tenGiay = row.getCell(1).getStringCellValue();

                Giay giay = new Giay();
                giay.setName(tenGiay);
                Date date = new Date();
                Long ngayTao = date.getTime() / 1000;
                giay.setNgayTao(ngayTao);
                repository.insert(giay);

                loadData(repository.selectAll(), currentPage);

            }

            JOptionPane.showMessageDialog(this, "Import successful");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error importing data");
        }
    }
    private void btnImportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImportMouseClicked
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            importDataFromExcel(selectedFile);
        }
    }//GEN-LAST:event_btnImportMouseClicked

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btExport;
    private javax.swing.JButton btnChiTiet;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblErrorTen;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblTrang;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTen;
    // End of variables declaration//GEN-END:variables
}
