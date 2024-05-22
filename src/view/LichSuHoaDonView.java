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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.GiayChiTiet;
import model.HoaDon;
import model.HoaDonChiTiet;
import repository.GiayChiTietRepository;
import repository.HoaDonChiTietRepository;
import repository.HoaDonRepository;
import repository.JOPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LichSuHoaDonView extends javax.swing.JPanel {

    private DefaultTableModel dtmHD;
    private DefaultTableModel dtmHDCT;
    private HoaDonRepository hoaDonRepository = new HoaDonRepository();
    private HoaDonChiTietRepository hoaDonChiTietRepository = new HoaDonChiTietRepository();
    private GiayChiTietRepository giayChiTietRepository = new GiayChiTietRepository();
    private Integer currentPageHD = 1;
    private Integer currentPageHDCT = 1;
    private Integer totalPage = 1;
    private Integer rowCountPage = 5;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private List<HoaDon> listHD = new ArrayList<>();
    public LichSuHoaDonView() {

        initComponents();
        dtmHD = (DefaultTableModel) tblHD.getModel();
        txtNgayBD.setText(simpleDateFormat.format(hoaDonRepository.ngayTaoMin()));
        txtNgayKT.setText(simpleDateFormat.format(hoaDonRepository.ngayTaoMax()));
        dtmHDCT = (DefaultTableModel) tblHDCT.getModel();
        dtmHDCT.setRowCount(0);
        showDataHD(hoaDonRepository.selectAllHT(), currentPageHD);

        configTable();
    }

    private String showTien(BigDecimal tien) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(tien);
    }

    private void configTable() {
        this.tblHD.getColumnModel().getColumn(0).setPreferredWidth(70);
        this.tblHD.getColumnModel().getColumn(1).setPreferredWidth(65);
        this.tblHD.getColumnModel().getColumn(2).setPreferredWidth(60);
        this.tblHD.getColumnModel().getColumn(3).setPreferredWidth(100);
        this.tblHD.getColumnModel().getColumn(4).setPreferredWidth(105);
        this.tblHD.getColumnModel().getColumn(5).setPreferredWidth(110);
        this.tblHD.getColumnModel().getColumn(6).setPreferredWidth(110);
        this.tblHD.getColumnModel().getColumn(7).setPreferredWidth(80);
        this.tblHD.getColumnModel().getColumn(8).setPreferredWidth(110);
    }

    private void showDataHD(List<HoaDon> list, int page) {
        dtmHD.setRowCount(0);
        totalPage = (int) Math.ceil((double) list.size() / rowCountPage);
        lblTrang.setText("Trang " + page + "/" + totalPage);
        int limit = page * rowCountPage;
        for (int start = (page - 1) * rowCountPage; start < list.size(); start++) {
            HoaDon hoaDon = list.get(start);
            dtmHD.addRow(new Object[]{
                hoaDon.getMaHoaDon(),
                hoaDon.getNhanVien().getMa(),
                hoaDon.getKhachHang().getMa(),
                hoaDon.getNgayTao(),
                showTien(hoaDon.getTongTien()),
                showTien(hoaDon.getTienKhachDua()),
                showTien(hoaDon.getTienThua()),
                hoaDon.getHinhThucThanhToan(),
                hoaDon.getTrangThai(),});
            if (start + 1 == limit) {
                return;
            }
        }
        if (totalPage == 1) {
            currentPageHD = 1;
        }
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

    private void showDataHDCT(List<HoaDonChiTiet> list, int page) {
        dtmHDCT.setRowCount(0);
        int stt = 1;
            for (HoaDonChiTiet hoaDonChiTiet : list) {
                if(hoaDonChiTiet.getGiayChiTiet() != null){
                    dtmHDCT.addRow(new Object[]{
                    stt,
                    hoaDonChiTiet.getGiayChiTiet().getMa(),
                    hoaDonChiTiet.getGiayChiTiet().getGiay().getName(),
                    hoaDonChiTiet.getSoLuong(),
                    showTien(hoaDonChiTiet.getGia()),
                    showTien(hoaDonChiTiet.getThanhTien()),});
                stt++;
                }
            }
    }

    private List<HoaDon> search() {
        String maNV = txtMaNV.getText().trim().toLowerCase();
        String maKH = txtMaKH.getText().trim().toLowerCase();
        String ngayBD = txtNgayBD.getText().trim();
        String ngayKT = txtNgayKT.getText().trim();
        String maHD = txtMaHD.getText().trim().toLowerCase();
        String sdt = txtSDT.getText().trim().toLowerCase();

        Date start = null;
        Date end = null;

        Long startDate = hoaDonRepository.ngayTaoMin().getTime();
        Long endDate = hoaDonRepository.ngayTaoMax().getTime();

        if (!ngayBD.isBlank()) {
            try {
                start = simpleDateFormat.parse(ngayBD);
                end = simpleDateFormat.parse(ngayKT);
                if (start.getTime() > end.getTime()) {
                    lblErrorNgay.setText("Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
                    return null;
                } else {
                    startDate = start.getTime();
                    endDate = end.getTime();
                    lblErrorNgay.setText(" ");
                }
            } catch (Exception e) {
                lblErrorNgay.setText("Sai định dạng ngày: dd-MM-yyyy");
                return null;
            }
        } else {
            lblErrorNgay.setText("Ngày không được để trống");
        }
        List<HoaDon> listSearch = new ArrayList<HoaDon>();
        if (maKH.isBlank()) {

            for (HoaDon hoaDon : hoaDonRepository.selectAllHT()) {
                if (hoaDon.getNhanVien().getMa().toLowerCase().contains(maNV)
                        && hoaDon.getNgayTao().getTime() >= startDate
                        && hoaDon.getNgayTao().getTime() <= endDate
                        && ((hoaDon.getKhachHang().getSdt() + "").toLowerCase().contains(sdt)
                        || (hoaDon.getNhanVien().getSdt() + "").toLowerCase().contains(sdt))
                        && (hoaDon.getMaHoaDon() + "").toLowerCase().contains(maHD)) {
                    listSearch.add(hoaDon);
                }
            }
        } else {
            for (HoaDon hoaDon : hoaDonRepository.selectAllHT()) {
                if (hoaDon.getNhanVien().getMa().toLowerCase().contains(maNV)
                        && (hoaDon.getKhachHang().getMa() + "").toLowerCase().contains(maKH)
                        && hoaDon.getNgayTao().getTime() >= startDate
                        && hoaDon.getNgayTao().getTime() <= endDate
                        && ((hoaDon.getKhachHang().getSdt() + "").toLowerCase().contains(sdt)
                        || (hoaDon.getNhanVien().getSdt() + "").toLowerCase().contains(sdt))
                        && (hoaDon.getMaHoaDon() + "").toLowerCase().contains(maHD)) {
                    listSearch.add(hoaDon);
                }
            }
        }
        return listSearch;
    }

    private void next() {
        if (currentPageHD == totalPage) {
            currentPageHD = 1;
            showDataHD(search(), currentPageHD);
        } else {
            currentPageHD++;
            showDataHD(search(), currentPageHD);
        }
    }

    private void prev() {
        if (currentPageHD == 1) {
            currentPageHD = totalPage;
            showDataHD(search(), currentPageHD);
        } else {
            currentPageHD--;
            showDataHD(search(), currentPageHD);
        }
    }

    private void detailGiay(int selectedHDCT) {
        String ma = (String) tblHDCT.getValueAt(selectedHDCT, 1);
        GiayChiTiet findGiayChiTiet = new GiayChiTiet();
        findGiayChiTiet.setMa(ma);
        GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiayChiTiet);
        lblMa.setText("Mã:            " + giayChiTiet.getMa());
        lblTen.setText("Tên giày:     " + giayChiTiet.getGiay().getName());
        lblHang.setText("Hãng:          " + giayChiTiet.getHang().getName());
        lblKieuDang.setText("Kiểu dáng:    " + giayChiTiet.getKieuDang().getName());
        lblDanhMuc.setText("Danh mục:    " + giayChiTiet.getDanhMuc().getName());
        lblMauSac.setText("Màu sắc:      " + giayChiTiet.getMauSac().getName());
        lblKichCo.setText("Kích cỡ:        " + giayChiTiet.getKichCo().getName());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblMa = new javax.swing.JLabel();
        lblTen = new javax.swing.JLabel();
        lblHang = new javax.swing.JLabel();
        lblKieuDang = new javax.swing.JLabel();
        lblDanhMuc = new javax.swing.JLabel();
        lblMauSac = new javax.swing.JLabel();
        lblKichCo = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        txtNgayKT = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblTrang = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        txtMaHD = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHD = new javax.swing.JTable();
        lblErrorNgay = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHDCT = new javax.swing.JTable();
        txtMaNV = new javax.swing.JTextField();
        txtNgayBD = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnXuatHD = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnXuat = new javax.swing.JButton();

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Tìm");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin giày", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        lblMa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMa.setText("Mã giày:");

        lblTen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTen.setText("Tên giày:");

        lblHang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblHang.setText("Hãng:");

        lblKieuDang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblKieuDang.setText("Kiểu dáng:");

        lblDanhMuc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDanhMuc.setText("Danh mục:");

        lblMauSac.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMauSac.setText("Màu sắc:");

        lblKichCo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblKichCo.setText("Kích cỡ:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTen, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(lblHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(lblKieuDang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(lblDanhMuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMauSac, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(lblKichCo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblMa)
                .addGap(22, 22, 22)
                .addComponent(lblTen)
                .addGap(22, 22, 22)
                .addComponent(lblHang)
                .addGap(22, 22, 22)
                .addComponent(lblKieuDang)
                .addGap(22, 22, 22)
                .addComponent(lblDanhMuc)
                .addGap(22, 22, 22)
                .addComponent(lblMauSac)
                .addGap(22, 22, 22)
                .addComponent(lblKichCo)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("<");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txtNgayKT.setBackground(new java.awt.Color(240, 240, 240));
        txtNgayKT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNgayKT.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "NgayKT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtNgayKT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayKTActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 51));
        jLabel2.setText("Quản lý hóa đơn");

        txtMaKH.setBackground(new java.awt.Color(240, 240, 240));
        txtMaKH.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMaKH.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MaKH", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 51));
        jLabel1.setText("Hóa đơn chi tiết");

        lblTrang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTrang.setText("Trang");

        txtSDT.setBackground(new java.awt.Color(240, 240, 240));
        txtSDT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtSDT.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SDT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });

        txtMaHD.setBackground(new java.awt.Color(240, 240, 240));
        txtMaHD.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMaHD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MaHD", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHDActionPerformed(evt);
            }
        });

        tblHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã HĐ", "Mã NV", "Mã KH", "Ngày tạo", "Tổng tiền", "Tiền khách đưa", "Tiền thừa", "Hình thức TT", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHD.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblHD.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblHD.setOpaque(false);
        tblHD.setRowHeight(25);
        tblHD.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblHD.setSelectionForeground(new java.awt.Color(51, 51, 51));
        tblHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHDMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHD);

        lblErrorNgay.setForeground(new java.awt.Color(255, 0, 0));
        lblErrorNgay.setText(" ");

        tblHDCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stt", "Mã giày", "Tên", "SoLuong", "DonGia", "ThanhTien"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHDCT.setRowHeight(30);
        tblHDCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHDCTMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHDCT);

        txtMaNV.setBackground(new java.awt.Color(240, 240, 240));
        txtMaNV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMaNV.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MaNV", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });

        txtNgayBD.setBackground(new java.awt.Color(240, 240, 240));
        txtNgayBD.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNgayBD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "NgayBD", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtNgayBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayBDActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText(">");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnXuatHD.setBackground(new java.awt.Color(0, 0, 0));
        btnXuatHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXuatHD.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatHD.setText("Xuất HD");
        btnXuatHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHDActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Đổi trả");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        btnXuat.setBackground(new java.awt.Color(0, 0, 0));
        btnXuat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXuat.setForeground(new java.awt.Color(255, 255, 255));
        btnXuat.setText("Xuất");
        btnXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXuatMouseClicked(evt);
            }
        });
        btnXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtNgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(153, 153, 153)
                                .addComponent(lblErrorNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXuatHD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXuat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(70, 70, 70))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(btnXuat))
                .addGap(5, 5, 5)
                .addComponent(lblErrorNgay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaNV)
                    .addComponent(jButton3)
                    .addComponent(txtSDT)
                    .addComponent(btnXuatHD))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTrang)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addGap(98, 98, 98)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        showDataHD(search(), currentPageHD);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        prev();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtNgayKTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayKTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayKTActionPerformed

    private void txtSDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSDTActionPerformed

    private void txtMaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHDActionPerformed

    private void tblHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHDMouseClicked
        // TODO add your handling code here:
        int selected = tblHD.getSelectedRow();
        if (selected >= 0) {
            String maHD = (String) tblHD.getValueAt(selected, 0);
            HoaDon findHoaDon = new HoaDon();
            findHoaDon.setMaHoaDon(maHD);
            HoaDon hoaDon = hoaDonRepository.selectbyId(findHoaDon);
            System.out.println(hoaDon.getId());
            if (hoaDon != null) {
                List<HoaDonChiTiet> listHDCT = hoaDonChiTietRepository.selectAll(hoaDon.getId());
                showDataHDCT(listHDCT, 1);
            }
        }
    }//GEN-LAST:event_tblHDMouseClicked

    private void tblHDCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHDCTMouseClicked
        // TODO add your handling code here:
        int selected = tblHDCT.getSelectedRow();
        detailGiay(selected);
    }//GEN-LAST:event_tblHDCTMouseClicked

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNVActionPerformed

    private void txtNgayBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayBDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayBDActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        next();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnXuatHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHDActionPerformed
        // TODO add your handling code here:
        int selected = tblHD.getSelectedRow();
        if (selected >= 0) {
            xuatHD(hoaDonRepository.selectAllHT().get(0));
        } else {
            JOPane.showMessageDialog(this, "Chọn hóa đơn muốn xuất");
        }
    }//GEN-LAST:event_btnXuatHDActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int selected = tblHD.getSelectedRow();
        if (selected >= 0) {
            String maHD = (String) tblHD.getValueAt(selected, 0);
            HoaDon findHoaDon = new HoaDon();
            findHoaDon.setMaHoaDon(maHD);
            HoaDon hoaDon = hoaDonRepository.selectbyId(findHoaDon);
            if (hoaDon.getVoucher().getId() == null) {
                Date now = new Date();
                if (now.getTime() / 1000 - hoaDon.getNgayTao().getTime() / 1000 < 259200) {
                    DoiTra doiTra = new DoiTra(hoaDon);
                    doiTra.setVisible(true);
                } else {
                    JOPane.showMessageDialog(this, "Hóa đơn đã quá hạn đổi trả");
                }
            } else {
                JOPane.showMessageDialog(this, "Đổi trả không áp dụng với hóa đơn sử dụng voucher");
            }
        } else {
            JOPane.showMessageDialog(this, "Bạn cần chọn hóa đơn để đổi trả");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXuatMouseClicked
        // TODO add your handling code here:
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("HoaDon");
            XSSFRow row;
            Cell cell;

            // Header row
            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("MaHD");

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("MaNV");

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("MaKH");

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("NgayTao");

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("TongTien");

            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue("TienKhachDua");

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue("TiemThua");

            cell = row.createCell(7, CellType.STRING);
            cell.setCellValue("HinhThucTT");

            cell = row.createCell(8, CellType.STRING);
            cell.setCellValue("TrangThai");
            // Data rows
            for (int i = 0; i < listHD.hashCode(); i++) {
                row = sheet.createRow(i + 1);
                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(listHD.get(i).getMaHoaDon());

                cell = row.createCell(1, CellType.NUMERIC);
                cell.setCellValue((RichTextString) listHD.get(i).getNhanVien());

                cell = row.createCell(2, CellType.NUMERIC);
                cell.setCellValue((RichTextString) listHD.get(i).getKhachHang());

                cell = row.createCell(3, CellType.NUMERIC);
                cell.setCellValue(listHD.get(i).getNgayTao());

                cell = row.createCell(4, CellType.NUMERIC);
                cell.setCellValue((RichTextString) listHD.get(i).getTongTien());

                cell = row.createCell(5, CellType.NUMERIC);
                cell.setCellValue((RichTextString) listHD.get(i).getTienKhachDua());

                cell = row.createCell(6, CellType.NUMERIC);
                cell.setCellValue((RichTextString) listHD.get(i).getTienThua());

                cell = row.createCell(7, CellType.NUMERIC);
                cell.setCellValue(listHD.get(i).getHinhThucThanhToan());

                cell = row.createCell(8, CellType.NUMERIC);
                cell.setCellValue(listHD.get(i).getTrangThai());

            }

//            XSSFSheet sheet1 = workbook.createSheet("HoaDonChiTiet");
//            XSSFRow row1 = null;
//            Cell cell1;
//            // Header row
//            row1 = sheet1.createRow(0);
//            cell1 = row1.createCell(0, CellType.STRING);
//            cell1.setCellValue("STT");
//
//            cell1 = row1.createCell(1, CellType.STRING);
//            cell1.setCellValue("MaGiay");
//
//            cell1 = row1.createCell(2, CellType.STRING);
//            cell1.setCellValue("TenGiay");
//
//            cell1 = row1.createCell(3, CellType.STRING);
//            cell1.setCellValue("SoLuong");
//
//            cell1 = row1.createCell(4, CellType.STRING);
//            cell1.setCellValue("DonGia");
//
//            cell1 = row1.createCell(5, CellType.STRING);
//            cell1.setCellValue("TongTien");

//            for (int i = 0; i < listHoaDonChiTiet.hashCode(); i++) {
//                row1 = sheet1.createRow(i + 1);
//                cell1 = row1.createCell(0, CellType.NUMERIC);
//                cell1.setCellValue(i + 1);
//
//                cell1 = row1.createCell(1, CellType.NUMERIC);
//                cell1.setCellValue(listHoaDonChiTiet.get(i).getId());
//
//                cell1 = row1.createCell(2, CellType.NUMERIC);
//                cell1.setCellValue((RichTextString) listHoaDonChiTiet.get(i).getGiayChiTiet());
//
//                cell1 = row1.createCell(3, CellType.NUMERIC);
//                cell1.setCellValue(listHoaDonChiTiet.get(i).getSoLuong());
//
//                cell1 = row1.createCell(4, CellType.NUMERIC);
//                cell1.setCellValue((RichTextString) listHoaDonChiTiet.get(i).getGia());
//
//                cell1 = row1.createCell(5, CellType.NUMERIC);
//                cell1.setCellValue((RichTextString) listHD.get(i).getTongTien());
//
//            }
            // Use JFileChooser to choose the file location
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
    }//GEN-LAST:event_btnXuatMouseClicked

    private void btnXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXuatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnXuat;
    private javax.swing.JButton btnXuatHD;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDanhMuc;
    private javax.swing.JLabel lblErrorNgay;
    private javax.swing.JLabel lblHang;
    private javax.swing.JLabel lblKichCo;
    private javax.swing.JLabel lblKieuDang;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblMauSac;
    private javax.swing.JLabel lblTen;
    private javax.swing.JLabel lblTrang;
    private javax.swing.JTable tblHD;
    private javax.swing.JTable tblHDCT;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtNgayBD;
    private javax.swing.JTextField txtNgayKT;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration//GEN-END:variables
}
