package view;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import model.GiayChiTiet;
import model.HoaDon;
import model.HoaDonChiTiet;
import repository.GiayChiTietRepository;
import repository.HoaDonChiTietRepository;
import repository.HoaDonRepository;
import repository.JOPane;

public class DoiTra extends javax.swing.JFrame {

    private DefaultTableModel dtmHDCT = new DefaultTableModel();
    private DefaultTableModel dtmDSSP = new DefaultTableModel();

    private HoaDonChiTietRepository hoaDonChiTietRepository = new HoaDonChiTietRepository();
    private HoaDonRepository hoaDonRepository = new HoaDonRepository();
    private GiayChiTietRepository giayChiTietRepository = new GiayChiTietRepository();

    private HoaDon hd = null;
    private int selectedHDCT = -1;
    private int selectedDSSP = -1;
    private HoaDon hdGoc = null;
    private BigDecimal tongTienGoc = BigDecimal.ZERO;
    private BigDecimal tongTienMoi = BigDecimal.ZERO;

    public DoiTra(HoaDon hoaDon) {
        initComponents();
//        setLocationRelativeTo(null);
        setLocation(260, 70);
        hd = hoaDon;
//        hdGoc = hoaDon;
        dtmHDCT = (DefaultTableModel) tblHDCT.getModel();
        dtmDSSP = (DefaultTableModel) tblDSSP.getModel();
        loadDataHDCT(hoaDonChiTietRepository.selectAll(hoaDon.getId()));
        loadDataDSSP(giayChiTietRepository.selectAll());
        loadTTHD(hoaDon);
    }

    private String showTien(BigDecimal tien) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(tien);
    }

    private void loadDataHDCT(List<HoaDonChiTiet> list) {
        dtmHDCT.setRowCount(0);
        for (HoaDonChiTiet item : list) {
            if (item.getGiayChiTiet() != null) {
                dtmHDCT.addRow(new Object[]{
                    item.getGiayChiTiet().getMa(),
                    item.getGiayChiTiet().getGiay().getName(),
                    showTien(item.getGia()),
                    item.getSoLuong(),
                    showTien(item.getThanhTien()),
                    item.getTrangThai()
                });
            }
        }
    }

    private void loadDataDSSP(List<GiayChiTiet> list) {
        dtmDSSP.setRowCount(0);
        for (GiayChiTiet item : list) {
            if (item.getTrangThai().equalsIgnoreCase("Còn hàng")) {
                dtmDSSP.addRow(new Object[]{
                    item.getMa(),
                    item.getGiay().getName(),
                    item.getDanhMuc().getName(),
                    item.getMauSac().getName(),
                    item.getKieuDang().getName(),
                    item.getKichCo().getName(),
                    item.getSoLuong(),
                    showTien(item.getGia()),
                    item.getTrangThai()});
            }
        }
    }

    private void loadTTHD(HoaDon hoaDon) {
        lblKhachHang.setText("Khách hàng:     " + hoaDon.getKhachHang().getName());
        lblNhanVien.setText("Nhân viên:       " + hoaDon.getNhanVien().getTen());
        lblNgayMua.setText("Ngày mua:       " + hoaDon.getNgayTao());
        lblTongTien.setText("Tổng tiền:        " + showTien(hoaDon.getTongTien()));
        tongTienGoc = hoaDon.getTongTien();
    }

    private void xuatHD(HoaDon hoaDon) {
        // Mở cửa sổ chọn nơi lưu
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int x = fileChooser.showSaveDialog(this);
        if (x == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getPath();
        }

        Document d = new Document();

        try {
            PdfWriter.getInstance(d, new FileOutputStream(path + hoaDon.getMaHoaDon() + ".pdf"));
            d.open();
            BaseFont baseFont = BaseFont.createFont("font\\SVN-Arial\\SVN-Arial 3.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(baseFont, 18, Font.BOLD);
            Font fontInfo = new Font(baseFont, 12);
            Font fontHeader = new Font(baseFont, 12, Font.BOLD);
            Image logo = Image.getInstance("src\\icon\\logo.png");

            logo.scaleAbsolute(90, 50);
            String title = "Hóa đơn shop giày MeoMeo";
            Paragraph titleHD = new Paragraph(title, fontTitle);
            titleHD.setAlignment(Paragraph.ALIGN_CENTER);

            // Thông tin khách hàng và hóa đơn cơ bản
            PdfPTable tblInfo = new PdfPTable(4);
            tblInfo.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            tblInfo.setTotalWidth(500);
            tblInfo.setLockedWidth(true);

            tblInfo.addCell(new Phrase("Mã hóa đơn:", fontInfo));
            tblInfo.addCell(new Phrase(hoaDon.getMaHoaDon(), fontInfo));

            tblInfo.addCell(new Phrase("Trạng thái:", fontInfo));
            tblInfo.addCell(new Phrase(hoaDon.getTrangThai(), fontInfo));

            tblInfo.addCell(new Phrase("Tên khách hàng:", fontInfo));
            tblInfo.addCell(new Phrase(hoaDon.getKhachHang().getName(), fontInfo));

            tblInfo.addCell(new Phrase("Loại:", fontInfo));
            tblInfo.addCell(new Phrase("Tại quầy", fontInfo));

            tblInfo.addCell(new Phrase("Số điện thoại:", fontInfo));
            tblInfo.addCell(new Phrase(hoaDon.getKhachHang().getSdt(), fontInfo));

            tblInfo.addCell(new Phrase("Ngày mua:", fontInfo));
            tblInfo.addCell(new Phrase(hoaDon.getNgayTao() + "", fontInfo));

            tblInfo.addCell(new Phrase("Địa chỉ:", fontInfo));
            tblInfo.addCell(new Phrase(hoaDon.getKhachHang().getDiaChi() + "", fontInfo));

            tblInfo.addCell(new Phrase("Thành tiền:", fontInfo));
            tblInfo.addCell(new Phrase(showTien(hoaDon.getTongTien()), fontInfo));

            // Thông tin sản phẩm chi tiết
            PdfPTable tbl = new PdfPTable(5);
            tbl.setTotalWidth(400f);
            float[] columsWith = {40f, 150f, 60f, 90f, 50f};
            tbl.setWidths(columsWith);
            tbl.getDefaultCell().setVerticalAlignment(PdfPTable.ALIGN_MIDDLE);
            tbl.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            tbl.setTotalWidth(500);
            tbl.setLockedWidth(true);
            Paragraph title1 = new Paragraph("STT", fontHeader);
            Paragraph title3 = new Paragraph("Giày", fontHeader);
            Paragraph title4 = new Paragraph("Số lượng", fontHeader);
            Paragraph title5 = new Paragraph("Thành tiền", fontHeader);
            Paragraph title6 = new Paragraph("Trạng thái", fontHeader);

            tbl.addCell(title1);
            tbl.addCell(title3);
            tbl.addCell(title4);
            tbl.addCell(title5);
            tbl.addCell(title6);

            List<HoaDonChiTiet> list = hoaDonChiTietRepository.selectAll(hoaDon.getId());
            for (int i = 0; i < list.size(); i++) {
                HoaDonChiTiet get = list.get(i);
                Paragraph stt = new Paragraph((i + 1) + "", fontInfo);
                Paragraph giayCT = new Paragraph("Mã:              " + get.getGiayChiTiet().getMa() + "\n"
                        + "Tên:             " + get.getGiayChiTiet().getGiay().getName() + "\n"
                        + "Danh mục:   " + get.getGiayChiTiet().getDanhMuc().getName() + "\n"
                        + "Hãng:           " + get.getGiayChiTiet().getHang().getName() + "\n"
                        + "Kích cỡ:        " + get.getGiayChiTiet().getKichCo().getName() + "\n"
                        + "Kiểu dáng:    " + get.getGiayChiTiet().getKieuDang().getName() + "\n"
                        + "Màu sắc:      " + get.getGiayChiTiet().getMauSac().getName() + "\n",
                        fontInfo);
                Paragraph soLuong = new Paragraph(get.getSoLuong() + "", fontInfo);
                Paragraph thanhTien = new Paragraph(showTien(get.getGia()), fontInfo);
                Paragraph trangThai = new Paragraph(get.getTrangThai(), fontInfo);
                tbl.addCell(stt);
                tbl.addCell(giayCT);
                tbl.addCell(soLuong);
                tbl.addCell(thanhTien);
                tbl.addCell(trangThai);
            }

            // Tổng tiền hàng, tiền phải trả, phí ship, giảm giá...
            PdfPTable tblFooter = new PdfPTable(4);
            tblFooter.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            tblFooter.getDefaultCell().setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
            tblFooter.getDefaultCell().setVerticalAlignment(PdfPTable.ALIGN_MIDDLE);

            BigDecimal thanhTien = BigDecimal.ZERO;
            for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(hoaDon.getId())) {
                thanhTien = thanhTien.add(item.getThanhTien());
            }
            BigDecimal tienGiam = thanhTien.subtract(hoaDon.getTongTien());

            for (int i = 0; i < 3; i++) {
                String column = "";
                tblFooter.addCell(column);
                tblFooter.addCell(column);
                switch (i) {
                    case 0:
                        Paragraph tienHang = new Paragraph("Thành tiền:", fontInfo);
                        Paragraph tien1 = new Paragraph(showTien(hoaDon.getTongTien()), fontInfo);
                        tblFooter.addCell(tienHang);
                        tblFooter.addCell(tien1);
                        break;
                    case 1:
                        Paragraph tienGiamParagraph = new Paragraph("Tiền được giảm: ", fontInfo);
                        Paragraph tien2 = new Paragraph(showTien(tienGiam), fontInfo);
                        tblFooter.addCell(tienGiamParagraph);
                        tblFooter.addCell(tien2);
                        break;
                    case 2:
                        Paragraph tienTra = new Paragraph("Tiền phải trả: ", fontInfo);
                        Paragraph tien3 = new Paragraph(showTien(hoaDon.getTongTien()), fontInfo);
                        tblFooter.addCell(tienTra);
                        tblFooter.addCell(tien3);
                        break;
                    default:
                        throw new AssertionError();
                }
            }

            d.add(logo);
            d.add(titleHD);
            d.add(Chunk.NEWLINE);
            d.add(tblInfo);
            d.add(Chunk.NEWLINE);
            d.add(tbl);
            d.add(Chunk.NEWLINE);
            d.add(tblFooter);
            JOPane.showMessageDialog(this, "Xuất hóa đơn thành công");
        } catch (DocumentException ex) {
            JOPane.showMessageDialog(this, ex.getMessage());
        } catch (FileNotFoundException ex) {
            JOPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOPane.showMessageDialog(this, ex.getMessage());
        }
        d.close();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHDCT = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDSSP = new javax.swing.JTable();
        btnDoi = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblKhachHang = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        lblNgayMua = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblTrongTienCu = new javax.swing.JLabel();
        lblTongTienMoi = new javax.swing.JLabel();
        lblTrongTienCu2 = new javax.swing.JLabel();
        txtKhachTra = new javax.swing.JTextField();
        lblTrongTienCu3 = new javax.swing.JLabel();
        txtTienTraKhach = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        lblTienChenhLech = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        tblHDCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã", "Tên", "Đơn giá", "Số lượng", "Thành tiền", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHDCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHDCTMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHDCT);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Thông tin hóa đơn");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Hóa đơn chi tiết");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Danh sách sản phẩm");

        tblDSSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã", "Tên", "DanhMuc", "MauSac", "KieuDang", "KiechCo", "SoLuong", "DonGia", "TrangThai"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDSSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDSSPMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDSSP);

        btnDoi.setText("Đổi");
        btnDoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiActionPerformed(evt);
            }
        });

        jButton2.setText("Trả");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lblKhachHang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblKhachHang.setText("Khách hàng: ");

        lblNhanVien.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNhanVien.setText("Nhân viên: ");

        lblNgayMua.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblNgayMua.setText("Ngày mua:");

        lblTongTien.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTongTien.setText("Tổng tiền: ");

        jButton3.setText("Hoàn thành");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thanh toán"));

        lblTrongTienCu.setText("Tổng tiền khách đã trả ");

        lblTongTienMoi.setText("Tổng tiền mới");

        lblTrongTienCu2.setText("Tiền khách phải trả");

        txtKhachTra.setEnabled(false);

        lblTrongTienCu3.setText("Tiền trả khách");

        txtTienTraKhach.setEnabled(false);

        jButton4.setText("Thanh toán");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        lblTienChenhLech.setText("Tiền chênh lệch");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblTrongTienCu3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTienTraKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblTrongTienCu2, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                .addComponent(lblTongTienMoi, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                .addComponent(lblTrongTienCu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtKhachTra)
                                .addComponent(lblTienChenhLech, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(jButton4)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lblTrongTienCu)
                .addGap(31, 31, 31)
                .addComponent(lblTongTienMoi)
                .addGap(35, 35, 35)
                .addComponent(lblTienChenhLech)
                .addGap(44, 44, 44)
                .addComponent(lblTrongTienCu2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtKhachTra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTrongTienCu3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTienTraKhach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Chọn hóa đơn khác");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnDoi))
                                .addComponent(jScrollPane1)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3))
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblNhanVien, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblKhachHang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
                                .addGap(160, 160, 160)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblNgayMua, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(38, 38, 38)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblKhachHang, lblNgayMua, lblNhanVien, lblTongTien});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblKhachHang)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblNhanVien))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblNgayMua)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblTongTien)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(btnDoi)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jButton3))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiActionPerformed
        // TODO add your handling code here:
        if (selectedDSSP <= -1 || selectedHDCT <= -1) {
            JOPane.showMessageDialog(this, "Bạn cần chọn Hóa đơn chi tiết và Giày để đổi");
        } else {
            int soLuongDoi = 0;
//            if (tblHDCT.getValueAt(selectedHDCT, 5).toString().equalsIgnoreCase("Mua")) {
            try {
                soLuongDoi = Integer.parseInt(JOPane.showInputDialog(this, "Nhập số lượng muốn đổi"));
                int soLuongDaMua = (int) tblHDCT.getValueAt(selectedHDCT, 3);
                int soLuongTrongKho = (int) tblDSSP.getValueAt(selectedDSSP, 6);
                String[] options = {"Không ưng", "Hàng lỗi", "Hủy"};
                int lyDo = JOPane.showOptionDialog(this, "Lý do đổi hàng", options, "Lý do");
//                    boolean lyDo = JOPane.showConfirmDialog(this, "Không ưng => Oke \n SP lỗi => HỦY");
                if (soLuongDoi > 0) {
                    if (soLuongDoi <= soLuongDaMua) {
                        if (soLuongDoi <= soLuongTrongKho) {

                            // thực hiển đổi hàng
                            // update soLuong sp đổi trong HDCT sang trangThai doi
                            String maHDCT = (String) tblHDCT.getValueAt(selectedHDCT, 0);
                            GiayChiTiet findGiayChiTietHDCT = new GiayChiTiet();
                            findGiayChiTietHDCT.setMa(maHDCT);
                            GiayChiTiet giayChiTietUpdate = giayChiTietRepository.selectbyId(findGiayChiTietHDCT);
                            int soLuong = giayChiTietUpdate.getSoLuong();
                            HoaDonChiTiet findHoaDonChiTiet = new HoaDonChiTiet();
                            findHoaDonChiTiet.setTrangThai("Mua");
                            findHoaDonChiTiet.setHoaDon(hd);
                            findHoaDonChiTiet.setGiayChiTiet(giayChiTietUpdate);

                            HoaDonChiTiet hoaDonChiTietUpdate = hoaDonChiTietRepository.selectbyId(findHoaDonChiTiet);
                            hoaDonChiTietUpdate.setGia(giayChiTietUpdate.getGia());
                            hoaDonChiTietUpdate.setSoLuong(soLuongDaMua - soLuongDoi);
                            hoaDonChiTietRepository.update(hoaDonChiTietUpdate);

                            // thêm sp đổi trong danh sách hóa đơn chi tiết vài hóa đơn với trạng thái là đổi
                            GiayChiTiet findGiayChiTietDSSP = new GiayChiTiet();
                            String maDSSP = (String) tblDSSP.getValueAt(selectedDSSP, 0);
                            findGiayChiTietDSSP.setMa(maDSSP);
                            GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiayChiTietDSSP);

                            HoaDonChiTiet hoaDonChiTietInsert = new HoaDonChiTiet();

                            hoaDonChiTietInsert.setGiayChiTiet(giayChiTiet);
                            hoaDonChiTietInsert.setHoaDon(hd);
                            hoaDonChiTietInsert.setTrangThai("Đổi");
                            hoaDonChiTietInsert.setGia(giayChiTiet.getGia());
                            hoaDonChiTietInsert.setSoLuong(soLuongDoi);
                            // check trùng
                            boolean isDuplicate = false;
                            for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(hd.getId())) {
                                if (item.getGiayChiTiet() != null) {
                                    if (item.getGiayChiTiet().getMa().equalsIgnoreCase(maDSSP) && item.getTrangThai().equalsIgnoreCase("Mua")) {
                                        isDuplicate = true;
                                        int soLuongUpdate = item.getSoLuong() + soLuongDoi;
                                        HoaDonChiTiet hoaDonChiTiet = item;
                                        hoaDonChiTiet.setSoLuong(soLuongUpdate);
                                        hoaDonChiTietRepository.update(hoaDonChiTiet);
                                    }
                                }
                            }

                            if (!isDuplicate) {
                                hoaDonChiTietRepository.insert(hoaDonChiTietInsert);
                            }

                            for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(hd.getId())) {
                                if (item.getGiayChiTiet() != null) {
                                    if (item.getSoLuong() == 0) {
                                        hoaDonChiTietRepository.delete(item);
                                    }
                                }
                            }
                            // thêm sp bị đổi(lỗi, không ưng) vào danh sách sp
                            // nếu không ưng cộng lại vào giày chi tiết
                            // nếu hàng lỗi thì insert vào giaychitiet trangthai = loi
                            switch (lyDo) {
                                case 0:
                                    JOPane.showMessageDialog(this, "Không ưng");
                                    int soLuongMoi = soLuongDoi + soLuong;
                                    giayChiTietRepository.updateSoLuong(soLuongMoi, maHDCT);
                                    break;
                                case 1:
                                    JOPane.showMessageDialog(this, "SP lỗi");
                                    giayChiTiet.setSoLuong(soLuongDoi);
                                    giayChiTiet.setTrangThai("Lỗi");
                                    boolean isTrung = false;
                                    for (GiayChiTiet item : giayChiTietRepository.selectAll()) {
                                        if (item.getGiay() != null) {
                                            if (item.getTrangThai().equalsIgnoreCase("Lỗi")) {
                                                if (item.getDanhMuc().getName().equalsIgnoreCase(giayChiTiet.getDanhMuc().getName())
                                                        && item.getGiay().getName().equalsIgnoreCase(giayChiTiet.getGiay().getName())
                                                        && item.getKieuDang().getName().equalsIgnoreCase(giayChiTiet.getKieuDang().getName())
                                                        && item.getMauSac().getName().equalsIgnoreCase(giayChiTiet.getMauSac().getName())
                                                        && item.getKichCo().getName().equalsIgnoreCase(giayChiTiet.getKichCo().getName())) {
                                                    isTrung = true;
                                                    GiayChiTiet gct = item;
                                                    gct.setSoLuong(item.getSoLuong() + soLuongDoi);
                                                    gct.setTrangThai("Lỗi");
                                                    giayChiTietRepository.update(gct);
                                                }
                                            }
                                        }
                                    }
                                    if (!isTrung) {
                                        giayChiTietRepository.insert(giayChiTiet);
                                    }
                                    break;
                                case 2:
                                    return;
                                default:
                                    throw new AssertionError();
                            }
//                                if (lyDo) {
//                                    JOPane.showMessageDialog(this, "Oke");
//                                    int soLuongMoi = soLuongDoi + soLuong;
//                                    giayChiTietRepository.updateSoLuong(soLuongMoi, maHDCT);
//                                } else {
//                                    JOPane.showMessageDialog(this, "HỦY");
//                                    giayChiTiet.setSoLuong(soLuongDoi);
//                                    giayChiTiet.setTrangThai("Lỗi");
//                                    boolean isTrung = false;
//                                    for (GiayChiTiet item : giayChiTietRepository.selectAll()) {
//                                        if (item.getTrangThai().equalsIgnoreCase("Lỗi")) {
//                                            if (item.getDanhMuc().getName().equalsIgnoreCase(giayChiTiet.getDanhMuc().getName())
//                                                    && item.getGiay().getName().equalsIgnoreCase(giayChiTiet.getGiay().getName())
//                                                    && item.getKieuDang().getName().equalsIgnoreCase(giayChiTiet.getKieuDang().getName())
//                                                    && item.getMauSac().getName().equalsIgnoreCase(giayChiTiet.getMauSac().getName())
//                                                    && item.getKichCo().getName().equalsIgnoreCase(giayChiTiet.getKichCo().getName())) {
//                                                isTrung = true;
//                                                GiayChiTiet gct = item;
//                                                gct.setSoLuong(item.getSoLuong() + soLuongDoi);
//                                                gct.setTrangThai("Lỗi");
//                                                giayChiTietRepository.update(gct);
//                                            }
//                                        }
//                                    }
//                                    if (!isTrung) {
//                                        giayChiTietRepository.insert(giayChiTiet);
//                                    }
//                                }
                            // =>     done
                            // cập nhập số lượng kho
                            int soLuongMoi = soLuongTrongKho - soLuongDoi;
                            giayChiTietRepository.updateSoLuong(soLuongMoi, maDSSP);
                            loadDataDSSP(giayChiTietRepository.selectAll());
                            loadDataHDCT(hoaDonChiTietRepository.selectAll(hd.getId()));
                        } else {
                            JOPane.showMessageDialog(this, "Không đủ hàng");
                        }
                    } else {
                        JOPane.showMessageDialog(this, "Số lượng đổi <= số lượng đã mua");
                    }
                } else {
                    JOPane.showMessageDialog(this, "Số lượng đổi phải lớn hơn 0");
                }
            } catch (Exception e) {
                JOPane.showMessageDialog(this, "Số lượng phải là số nguyên");
                System.out.println(e.getMessage());
            }
//            } else {
//                JOPane.showMessageDialog(this, "Đổi chỉ áp dụng với sản phẩm mua");
//            }
        }
    }//GEN-LAST:event_btnDoiActionPerformed

    private void tblHDCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHDCTMouseClicked
        // TODO add your handling code here:
        selectedHDCT = tblHDCT.getSelectedRow();
    }//GEN-LAST:event_tblHDCTMouseClicked

    private void tblDSSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDSSPMouseClicked
        // TODO add your handling code here:
        selectedDSSP = tblDSSP.getSelectedRow();
    }//GEN-LAST:event_tblDSSPMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (selectedHDCT >= 0) {
            String[] options = {"Trả hết", "Trả 1 phần", "Hủy"};
            String[] options1 = {"Không ưng", "Hàng lỗi", "Hủy"};
            int loaiTra = JOPane.showOptionDialog(null, "Chọn hướng trả", options, options[2]);
            int lyDo = JOPane.showOptionDialog(null, "Lý do trả hàng", options1, options1[2]);
            int soLuongTrongGH = (int) tblHDCT.getValueAt(selectedHDCT, 3);
            String maHDCT = (String) tblHDCT.getValueAt(selectedHDCT, 0);

            HoaDonChiTiet findHoaDonChiTiet = new HoaDonChiTiet();
            GiayChiTiet findGiayChiTiet = new GiayChiTiet();
            findGiayChiTiet.setMa(maHDCT);
            GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiayChiTiet);

            findHoaDonChiTiet.setHoaDon(hd);
            findHoaDonChiTiet.setGiayChiTiet(giayChiTiet);

            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.selectbyId(findHoaDonChiTiet);
            switch (loaiTra) {
                case 0:
                    // trả hết
                    
                    if (lyDo == 0) {
                        // hàng oke không lỗi
                        hoaDonChiTietRepository.delete(hoaDonChiTiet);
                        int soLuongMoi = soLuongTrongGH + giayChiTiet.getSoLuong();
                        giayChiTietRepository.updateSoLuong(soLuongMoi, maHDCT);
                    } else if (lyDo == 1) {
                        // hàng lỗi
                        hoaDonChiTietRepository.delete(hoaDonChiTiet);
                        giayChiTiet.setTrangThai("Lỗi");
                        giayChiTiet.setSoLuong(soLuongTrongGH);
                        boolean isTrung = false;
                        for (GiayChiTiet item : giayChiTietRepository.selectAll()) {
                            if (item.getTrangThai().equalsIgnoreCase("Lỗi")) {
                                if (item.getDanhMuc().getName().equalsIgnoreCase(giayChiTiet.getDanhMuc().getName())
                                        && item.getGiay().getName().equalsIgnoreCase(giayChiTiet.getGiay().getName())
                                        && item.getKieuDang().getName().equalsIgnoreCase(giayChiTiet.getKieuDang().getName())
                                        && item.getMauSac().getName().equalsIgnoreCase(giayChiTiet.getMauSac().getName())
                                        && item.getKichCo().getName().equalsIgnoreCase(giayChiTiet.getKichCo().getName())) {
                                    isTrung = true;
                                    GiayChiTiet gct = item;
                                    gct.setSoLuong(item.getSoLuong() + soLuongTrongGH);
                                    gct.setTrangThai("Lỗi");
                                    giayChiTietRepository.update(gct);
                                }
                            }
                        }
                        if (!isTrung) {
                            giayChiTietRepository.insert(giayChiTiet);
                        }
                    } else {
                        // không làm gì
                    }
                    break;
                case 1:
                    JOPane.showMessageDialog(this, "Trả 1 phần");
                    int soLuongTra = 0;
                    try {
                        soLuongTra = Integer.parseInt(JOPane.showInputDialog(this, "Nhập số lượng trả"));
                        if (lyDo == 0) {
                            int soLuongMoi = soLuongTra + giayChiTiet.getSoLuong();
                            giayChiTietRepository.updateSoLuong(soLuongMoi, maHDCT);
                        } else if (lyDo == 1) {
                            giayChiTiet.setTrangThai("Lỗi");
                            giayChiTiet.setSoLuong(soLuongTrongGH);
                            boolean isTrung = false;
                            for (GiayChiTiet item : giayChiTietRepository.selectAll()) {
                                if (item.getTrangThai().equalsIgnoreCase("Lỗi")) {
                                    if (item.getDanhMuc().getName().equalsIgnoreCase(giayChiTiet.getDanhMuc().getName())
                                            && item.getGiay().getName().equalsIgnoreCase(giayChiTiet.getGiay().getName())
                                            && item.getKieuDang().getName().equalsIgnoreCase(giayChiTiet.getKieuDang().getName())
                                            && item.getMauSac().getName().equalsIgnoreCase(giayChiTiet.getMauSac().getName())
                                            && item.getKichCo().getName().equalsIgnoreCase(giayChiTiet.getKichCo().getName())) {
                                        isTrung = true;
                                        GiayChiTiet gct = item;
                                        gct.setSoLuong(item.getSoLuong() + soLuongTra);
                                        gct.setTrangThai("Lỗi");
                                        giayChiTietRepository.update(gct);
                                    }
                                }
                            }
                            if (!isTrung) {
                                giayChiTietRepository.insert(giayChiTiet);
                            }

                            for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(hd.getId())) {
                                if (item.getSoLuong() == 0) {
                                    hoaDonChiTietRepository.delete(item);
                                }
                            }
                        }
                    } catch (Exception e) {
                        JOPane.showMessageDialog(this, "Số lượng trả là 1 số nguyên");
                    }
                    System.out.println(giayChiTiet);

                    HoaDonChiTiet hoaDonChiTietUpdate = hoaDonChiTiet;
                    hoaDonChiTietUpdate.setSoLuong(soLuongTrongGH - soLuongTra);
                    hoaDonChiTietUpdate.setGia(giayChiTiet.getGia());
                    hoaDonChiTietUpdate.setTrangThai("Mua");
                    System.out.println("hoaDonChiTietUpdate" + hoaDonChiTietUpdate);
                    hoaDonChiTietRepository.update(hoaDonChiTiet);
                    break;
                case 2:
                    return;
                default:
                    throw new AssertionError();
            }
            loadDataHDCT(hoaDonChiTietRepository.selectAll(hd.getId()));
            loadDataDSSP(giayChiTietRepository.selectAll());
        } else {
            JOPane.showMessageDialog(this, "Bạn cần chọn hóa đơn chi tiết để trả");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        tongTienMoi = BigDecimal.ZERO;
        for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(hd.getId())) {
            if (item.getGiayChiTiet() != null) {
                tongTienMoi = tongTienMoi.add(item.getThanhTien());
            }
        }
        BigDecimal tienChenhLech = tongTienGoc.subtract(tongTienMoi);
        if (tienChenhLech.compareTo(BigDecimal.ZERO) >= 0) {
            txtTienTraKhach.setText(showTien(tienChenhLech.abs()));
            hd.setTienKhachDua(hd.getTienKhachDua().subtract(tienChenhLech));
        } else {
            txtKhachTra.setText(showTien(tienChenhLech.abs()));
            hd.setTienKhachDua(hd.getTienKhachDua().add(tienChenhLech));
        }
        lblTrongTienCu.setText("Tổng tiền khách đã trả:      " + showTien(tongTienGoc));
        lblTongTienMoi.setText("Tổng tiền mới:                     " + showTien(tongTienMoi));
        lblTienChenhLech.setText("Tiền chênh lệch:                  " + showTien(tienChenhLech.abs()));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        hd.setTongTien(tongTienMoi);
        int row = hoaDonRepository.update(hd);
        if (row > 0) {
            lblTrongTienCu.setText("Tổng tiền khách đã trả:");
            lblTongTienMoi.setText("Tổng tiền mới:");
            lblTienChenhLech.setText("Tiền chênh lệch:");
            txtKhachTra.setText(null);
            txtTienTraKhach.setText(null);
            JOPane.showMessageDialog(this, "Thanh toán thành công");
            boolean check = JOPane.showConfirmDialog(this, "Xuất hóa đơn");
            if (check) {
                xuatHD(hd);
            }
            hd = null;
            this.dispose();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        hd = null;
        this.dispose();
    }//GEN-LAST:event_jLabel4MouseClicked

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
            java.util.logging.Logger.getLogger(DoiTra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DoiTra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DoiTra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DoiTra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HoaDon hoaDon = new HoaDon();
                new DoiTra(hoaDon).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoi;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblNgayMua;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblTienChenhLech;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel lblTongTienMoi;
    private javax.swing.JLabel lblTrongTienCu;
    private javax.swing.JLabel lblTrongTienCu2;
    private javax.swing.JLabel lblTrongTienCu3;
    private javax.swing.JTable tblDSSP;
    private javax.swing.JTable tblHDCT;
    private javax.swing.JTextField txtKhachTra;
    private javax.swing.JTextField txtTienTraKhach;
    // End of variables declaration//GEN-END:variables
}
