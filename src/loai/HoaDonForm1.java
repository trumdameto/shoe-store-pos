package loai;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.DanhMuc;
import model.Giay;
import model.GiayChiTiet;
import model.Hang;
import model.HoaDon;
import model.HoaDonChiTiet;
import model.KhachHang;
import model.KichCo;
import model.KieuDang;
import model.MauSac;
import model.NhanVien;
import repository.DanhMucRepository;
import repository.GiayChiTietRepository;
import repository.GiayRepository;
import repository.HangRepository;
import repository.HoaDonChiTietRepository;
import repository.HoaDonRepository;
import repository.KhachHangRepository;
import repository.KichCoRepository;
import repository.KieuDangRepository;
import repository.MauSacRepository;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import model.Voucher;
import repository.VoucherRepository;
import util.Auth;

public final class HoaDonForm1 extends javax.swing.JFrame implements Runnable, ThreadFactory {

    CardLayout card;

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private Boolean openWeb = true;
    private final Executor executor = Executors.newSingleThreadExecutor(this);
    private GiayChiTietRepository giayChiTietRepository = new GiayChiTietRepository();
    private HoaDonRepository hoaDonRepository = new HoaDonRepository();
    private HoaDonChiTietRepository hoaDonChiTietRepository = new HoaDonChiTietRepository();
    private KhachHangRepository khachHangRepository = new KhachHangRepository();

    private DanhMucRepository danhMucRepository = new DanhMucRepository();
    private HangRepository hangRepository = new HangRepository();
    private KieuDangRepository kieuDangRepository = new KieuDangRepository();
    private MauSacRepository mauSacRepository = new MauSacRepository();
    private KichCoRepository kichCoRepository = new KichCoRepository();
    private GiayRepository giayRepository = new GiayRepository();
    private VoucherRepository voucherRepository = new VoucherRepository();
//    private Nhan giayRepository = new GiayRepository();

    private DefaultTableModel dtmHD;
    private DefaultTableModel dtmGH;
    private DefaultTableModel dtmSP;

    private Integer currentPage = 1;
    private Integer totalPage = 1;
    private Integer rowCountPage = 5;
//    private Integer soLuongTrongHD = 0;

    private String danhMucSelected = "";
    private String hangSelected = "";
    private String kieuDangSelected = "";
    private String mauSacSelected = "";
    private String kichCoSelected = "";
    private String giaySeleted = "";

    private BigDecimal tongTien = BigDecimal.ZERO;
    private BigDecimal tichDiem = BigDecimal.ZERO;

    private NhanVien nhanVien = null;
    private HoaDon hd;

    public HoaDonForm1() {
        initComponents();
        initWebcam();
//        Auth.initWebcam(jpnQR);
        card = (CardLayout) pnlBanHang.getLayout();
        card.show(pnlBanHang, "CardMuaHang");

        dtmHD = (DefaultTableModel) tblListHoaDon.getModel();
        dtmGH = (DefaultTableModel) tblGioHangCho.getModel();
        dtmSP = (DefaultTableModel) tblDanhSachSp.getModel();

        showDataHoaDon(hoaDonRepository.selectAllCho());
        showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
        this.configTblCol();
        loadDataCbo();
        dtmGH.setRowCount(0);

        nhanVien = new NhanVien("3925886C-D6CB-4EF6-AC27-E051DB4CF72D", "NV006",
                "Vũ Trọng Hùng", true, "0876546678", "Nam Định", new Date(1996, 06, 06), "12345", "Staff", "Hoạt động");
        NhanVien findNhanVien = new NhanVien();
        findNhanVien.setMa("NV006");
    }

    private void configTblCol() {
        //Config bảng hóa đơn 
        this.tblListHoaDon.getColumnModel().getColumn(0).setPreferredWidth(76);
        this.tblListHoaDon.getColumnModel().getColumn(1).setPreferredWidth(100);
        this.tblListHoaDon.getColumnModel().getColumn(2).setPreferredWidth(120);
        this.tblListHoaDon.getColumnModel().getColumn(3).setPreferredWidth(100);
        this.tblListHoaDon.getColumnModel().getColumn(4).setPreferredWidth(150);
        //Config bảng giở hàng 
        this.tblGioHangCho.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.tblGioHangCho.getColumnModel().getColumn(1).setPreferredWidth(113);
        this.tblGioHangCho.getColumnModel().getColumn(2).setPreferredWidth(80);
        this.tblGioHangCho.getColumnModel().getColumn(3).setPreferredWidth(80);
        this.tblGioHangCho.getColumnModel().getColumn(4).setPreferredWidth(75);
        this.tblGioHangCho.getColumnModel().getColumn(5).setPreferredWidth(75);
        this.tblGioHangCho.getColumnModel().getColumn(6).setPreferredWidth(60);
        this.tblGioHangCho.getColumnModel().getColumn(7).setPreferredWidth(40);
        this.tblGioHangCho.getColumnModel().getColumn(8).setPreferredWidth(70);
        this.tblGioHangCho.getColumnModel().getColumn(9).setPreferredWidth(85);
        //Config bảng danh sách sản phẩm 
        this.tblDanhSachSp.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.tblDanhSachSp.getColumnModel().getColumn(1).setPreferredWidth(118);
        this.tblDanhSachSp.getColumnModel().getColumn(2).setPreferredWidth(80);
        this.tblDanhSachSp.getColumnModel().getColumn(3).setPreferredWidth(80);
        this.tblDanhSachSp.getColumnModel().getColumn(4).setPreferredWidth(80);
        this.tblDanhSachSp.getColumnModel().getColumn(5).setPreferredWidth(73);
        this.tblDanhSachSp.getColumnModel().getColumn(6).setPreferredWidth(73);
        this.tblDanhSachSp.getColumnModel().getColumn(7).setPreferredWidth(70);
        this.tblDanhSachSp.getColumnModel().getColumn(8).setPreferredWidth(100);

    }

    private void initWebcam() {
        if (openWeb) {
            Dimension size = WebcamResolution.QVGA.getSize();
            webcam = Webcam.getWebcams().get(0);
            webcam.setViewSize(size);
            panel = new WebcamPanel(webcam);
            panel.setPreferredSize(size);
            panel.setFPSDisplayed(true);
            jpnQR.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 223, 178));
            executor.execute(this);
        } else {
            jpnQR.remove(panel);
        }
    }

    // quét QR
    @Override
    public void run() {
        if (webcam != null) {
            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {

                }

                Result result = null;
                BufferedImage image = null;
//                if (openWeb) {
                if (webcam.isOpen()) {
                    if ((image = webcam.getImage()) == null) {
                        continue;
                    }

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    try {
                        result = new MultiFormatReader().decode(bitmap);
                    } catch (NotFoundException ex) {
                    }
                    if (result != null) {
                        GiayChiTiet findGiayChiTiet = new GiayChiTiet();
                        findGiayChiTiet.setId(result.toString());
                        GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiayChiTiet);

                        if (hd == null) {
                            JOptionPane.showMessageDialog(this, "Bạn chưa chọn hóa đơn");
                        } else {
                            int check = JOptionPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");

                            if (check == JOptionPane.YES_OPTION) {
                                if (giayChiTiet != null) {
                                    String soLuong = JOptionPane.showInputDialog(null, "Nhập Số Lượng");
                                    try {
                                        if (Integer.valueOf(soLuong) <= 0) {
                                            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương");
                                        } else if (Integer.valueOf(soLuong) >= 20 && nhanVien.getVaitro().equalsIgnoreCase("Staff")) {
                                            JOptionPane.showMessageDialog(this, "Bạn bị giới hạn số lượng");
                                        } else if (Integer.valueOf(soLuong) <= giayChiTiet.getSoLuong()) {
                                            int soLuongThemGH = Integer.parseInt(soLuong);
                                            BigDecimal gia = giayChiTiet.getGia();
                                            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                                            hoaDonChiTiet.setGiayChiTiet(new GiayChiTiet(""));
                                            hoaDonChiTiet.setHoaDon(hd);
                                            hoaDonChiTiet.setSoLuong(soLuongThemGH);
                                            hoaDonChiTiet.setGiayChiTiet(giayChiTiet);
                                            hoaDonChiTiet.setGia(gia);
                                            hoaDonChiTiet.setTrangThai("Chờ thanh toán");
                                            Boolean isDuplicate = false;
                                            for (GiayChiTiet item : giayChiTietRepository.selectByHD(hd.getId())) {
                                                System.out.println(item);
                                                if (item.getId().equalsIgnoreCase(hoaDonChiTiet.getGiayChiTiet().getId())) {
                                                    isDuplicate = true;
                                                }
                                            }
                                            String thongBao = "";
                                            if (!isDuplicate) {
                                                if (hoaDonChiTietRepository.insert(hoaDonChiTiet) == 1) {
                                                    // Thay đổi số lượng trong kho
                                                    int soLuongMoi = giayChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuong();
                                                    giayChiTietRepository.updateSoLuong(soLuongMoi, giayChiTiet.getMa());
                                                    thongBao = "Bỏ Thành Công Vào Giỏ Thành Công";
                                                }
                                            } else {
                                                Integer soLuongGocGioHang = hoaDonChiTietRepository.selectbyId(hoaDonChiTiet).getSoLuong();
                                                Integer soLuongGiHangThayDoi = soLuongThemGH + soLuongGocGioHang;
                                                hoaDonChiTiet.setSoLuong(soLuongGiHangThayDoi);
                                                if (hoaDonChiTietRepository.update(hoaDonChiTiet) == 1) {
                                                    // Thay đổi số lượng trong kho
                                                    int soLuongMoi = giayChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuong() + soLuongGocGioHang;
                                                    giayChiTietRepository.updateSoLuong(soLuongMoi, giayChiTiet.getMa());
                                                    thongBao = "Thay Đổi Số Lượng Thành Công";
                                                }
                                            }
                                            // Thay đổi số lượng trong kho
                                            showDataGH(hd.getId());
                                            showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
                                            JOptionPane.showMessageDialog(this, thongBao);

                                        } else {
                                            JOptionPane.showMessageDialog(this, "Xin Lỗi ! Chúng Tôi Không Có Đủ Số Lượng");
                                        }
                                    } catch (NumberFormatException e) {
                                        JOptionPane.showMessageDialog(this, "Số Lượng là số nguyên");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
                                }
                            }
                        }
                    }
                }
//                }
            } while (true);
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }

    private int getSoLuongGiayCT(String ma) {
        int soLuongSanPham = 0;
        for (GiayChiTiet item : giayChiTietRepository.selectAll()) {
            if (item.getMa().equalsIgnoreCase(ma)) {
                soLuongSanPham = item.getSoLuong();
            }
        }
        return soLuongSanPham;
    }

    // load data tbl
    private void showDataHoaDon(List<HoaDon> listHD) {
        dtmHD.setRowCount(0);
        int stt = 1;
        for (HoaDon hoaDon : listHD) {
            dtmHD.addRow(new Object[]{
                stt,
                hoaDon.getMaHoaDon(),
                hoaDon.getNgayTao(),
                hoaDon.getNhanVien().getMa(),
                hoaDon.getNhanVien().getTen(),});
            stt++;
        }
    }

    private void showDataSanPham(List<GiayChiTiet> listSP, int page) {
        dtmSP.setRowCount(0);
        int totalItem = listSP.size();
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = totalItem / rowCountPage;
        }
        lblPage.setText("Trang " + currentPage + "/" + totalPage);
        int limit = page * rowCountPage;
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            GiayChiTiet item = listSP.get(start);
            dtmSP.addRow(new Object[]{
                item.getMa(),
                item.getGiay().getName(),
                item.getHang().getName(),
                item.getKieuDang().getName(),
                item.getDanhMuc().getName(),
                item.getMauSac().getName(),
                item.getKichCo().getName(),
                item.getSoLuong(),
                item.getGia(),});
            if (start + 1 == limit) {
                return;
            }
        }
    }

    private void showDataGH(String idHD) {
        dtmGH.setRowCount(0);
        for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(idHD)) {
            dtmGH.addRow(new Object[]{
                item.getGiayChiTiet().getMa(),
                item.getGiayChiTiet().getGiay().getName(),
                item.getGiayChiTiet().getHang().getName(),
                item.getGiayChiTiet().getKieuDang().getName(),
                item.getGiayChiTiet().getDanhMuc().getName(),
                item.getGiayChiTiet().getMauSac().getName(),
                item.getGiayChiTiet().getKichCo().getName(),
                item.getSoLuong(),
                item.getGiayChiTiet().getGia(),
                item.getGia(),});
        }
    }

    private void cboGiay() {
        cboGiay.removeAllItems();
        cboGiay.addItem("Giày");
        for (Giay giay : giayRepository.selectAll()) {
            cboGiay.addItem(giay.getName());
        }
    }

    private void cboDanhMuc() {
        cboDanhMuc.removeAllItems();
        cboDanhMuc.addItem("Danh mục");
        for (DanhMuc danhMuc : danhMucRepository.selectAll()) {
            cboDanhMuc.addItem(danhMuc.getName());
        }
    }

    private void cboHang() {
        cboHang.removeAllItems();
        cboHang.addItem("Hãng");
        for (Hang hang : hangRepository.selectAll()) {
            cboHang.addItem(hang.getName());
        }
    }

    private void cboKieuDang() {
        cboKieuDang.removeAllItems();
        cboKieuDang.addItem("Kiểu dáng");
        for (KieuDang kieuDang : kieuDangRepository.selectAll()) {
            cboKieuDang.addItem(kieuDang.getName());
        }
    }

    private void cboMauSac() {
        cboMauSac.removeAllItems();
        cboMauSac.addItem("Màu sắc");
        for (MauSac mauSac : mauSacRepository.selectAll()) {
            cboMauSac.addItem(mauSac.getName());
        }
    }

    private void cboKichCo() {
        cboKichCo.removeAllItems();
        cboKichCo.addItem("Kích cỡ");
        for (KichCo kichCo : kichCoRepository.selectAll()) {
            cboKichCo.addItem(kichCo.getName());
        }
    }

    private void loadDataCbo() {
        cboGiay();
        cboDanhMuc();
        cboHang();
        cboKieuDang();
        cboMauSac();
        cboKichCo();
    }

    private List<GiayChiTiet> search() {
        List<GiayChiTiet> listSearch = new ArrayList<>();
        List<GiayChiTiet> list = giayChiTietRepository.selectAll();
        String search = txtSearch.getText() != null ? txtSearch.getText() : "";
        for (GiayChiTiet o : list) {
            String dm = o.getDanhMuc().getName().toLowerCase();
            String ha = o.getHang().getName().toLowerCase();
            String kd = o.getKieuDang().getName().toLowerCase();
            String ms = o.getMauSac().getName().toLowerCase();
            String kc = o.getKichCo().getName().toLowerCase();
            String ga = o.getGiay().getName().toLowerCase();

            String ma = (o.getMa() + "").toLowerCase();
            String gia = (o.getGia() + "").toLowerCase();
            if (dm.contains(danhMucSelected) && ha.contains(hangSelected)
                    && kd.contains(kieuDangSelected) && ms.contains(mauSacSelected)
                    && kc.contains(kichCoSelected) && ga.contains(giaySeleted) && (ma.contains(search)
                    || gia.contains(search))) {
                listSearch.add(o);
            }
        }
        return listSearch;
    }

    private void setTextTT() {
        lblNhanVien.setText(hd.getNhanVien().getMa());
        lblTongTien.setText(tongTien.toString());
//        lblTongTien1.setText(tongTien.toString());
        lblKiemTraDiem.setText(tichDiem.toString());
        String maHoaDon = hd.getMaHoaDon();
        lblMaHoaDon.setText(maHoaDon);
//        txtMaHoaDon2.setText(maHoaDon);
    }

    private BigDecimal tinhVaThemTongTien(int columnIndex) {
        int rowCount = tblGioHangCho.getRowCount();
        tongTien = BigDecimal.ZERO;
        for (int i = 0; i < rowCount; i++) {
            try {
                Double giaTien = (Double) tblGioHangCho.getValueAt(i, columnIndex);
                if (giaTien != null) {
                    BigDecimal gia = new BigDecimal(giaTien);
                    tongTien = tongTien.add(gia);
                    setTextTT();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return tongTien;
    }

    private void clearFormBH() {
        lblNhanVien.setText("");
        lblMaHoaDon.setText("");
        lblKiemTraDiem.setText("");
        lblTongTien.setText("");
        lblTienThua.setText("");
        txtTienKhachDua.setText("");
        cboHinhThucTT.setSelectedIndex(0);
    }

    void resetThanhToan() {
        lblMaHoaDon.setText(null);
        txtTienKhachDua.setText("0");
        lblTongTien.setText("0");
        lblErrKiemTraDiem.setText(null);
        lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
        dtmGH.setRowCount(0);
        lblTienThua.setText("0");
        lblKiemTraDiem.setText(null);
        txtMaKhach.setText("");
        lblKhachHang.setText(null);
        cboHinhThucTT.setSelectedIndex(0);
        lblDiemKH.setText(null);
    }

    private void xoaSPGH(String idHD) {
        int row = hoaDonRepository.huyGioHang(idHD);
        System.out.println(row);
    }

    private void xuatHD() {
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
            PdfWriter.getInstance(d, new FileOutputStream(path + hd.getMaHoaDon() + ".pdf"));
            d.open();
            BaseFont baseFont = BaseFont.createFont("font\\SVN-Arial\\SVN-Arial 3.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(baseFont, 18, Font.BOLD);
            Font fontInfo = new Font(baseFont, 12);
            Font fontHeader = new Font(baseFont, 12, Font.BOLD);
            Image logo = Image.getInstance("C:\\Users\\bachv\\OneDrive\\Pictures\\hientai.jpg");

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
            tblInfo.addCell(new Phrase(hd.getMaHoaDon(), fontInfo));

            tblInfo.addCell(new Phrase("Trạng thái:", fontInfo));
            tblInfo.addCell(new Phrase(hd.getTrangThai(), fontInfo));

            tblInfo.addCell(new Phrase("Tên khách hàng:", fontInfo));
            tblInfo.addCell(new Phrase(hd.getKhachHang().getName(), fontInfo));

            tblInfo.addCell(new Phrase("Loại:", fontInfo));
            tblInfo.addCell(new Phrase("Tại quầy", fontInfo));

            tblInfo.addCell(new Phrase("Số điện thoại:", fontInfo));
            tblInfo.addCell(new Phrase(hd.getKhachHang().getSdt(), fontInfo));

            tblInfo.addCell(new Phrase("Ngày mua:", fontInfo));
            tblInfo.addCell(new Phrase(hd.getNgayTao() + "", fontInfo));

            tblInfo.addCell(new Phrase("Thành tiền:", fontInfo));
            tblInfo.addCell(new Phrase(hd.getTongTien() + "", fontInfo));

            // Thông tin sản phẩm chi tiết
            PdfPTable tbl = new PdfPTable(5);
            tbl.setTotalWidth(400f);
            float[] columsWith = {30f, 100f, 90f, 50f, 140f};
            tbl.setWidths(columsWith);
            tbl.getDefaultCell().setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
            tbl.getDefaultCell().setVerticalAlignment(PdfPTable.ALIGN_MIDDLE);
            tbl.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            tbl.setTotalWidth(500);
            tbl.setLockedWidth(true);
            Paragraph title1 = new Paragraph("STT", fontHeader);
            Paragraph title2 = new Paragraph("Ảnh", fontHeader);
            Paragraph title3 = new Paragraph("Giày", fontHeader);
            Paragraph title4 = new Paragraph("Số lượng", fontHeader);
            Paragraph title5 = new Paragraph("Thành tiền", fontHeader);

            tbl.addCell(title1);
            tbl.addCell(title2);
            tbl.addCell(title3);
            tbl.addCell(title4);
            tbl.addCell(title5);

            List<HoaDonChiTiet> list = hoaDonChiTietRepository.selectAll(hd.getId());
            for (int i = 0; i < list.size(); i++) {
                HoaDonChiTiet get = list.get(i);
                Paragraph stt = new Paragraph((i + 1) + "", fontInfo);

                Image hinhAnh = Image.getInstance("C:\\Users\\bachv\\OneDrive\\Máy tính\\Folder\\DuAn1\\MeoMeoGr\\MeoMeoGr\\src\\icon\\logo.png");
                Paragraph giayCT = new Paragraph(get.getGiayChiTiet().getMa() + "\n"
                        + get.getGiayChiTiet().getGiay().getName() + "\n"
                        + get.getGiayChiTiet().getDanhMuc().getName() + "\n"
                        + get.getGiayChiTiet().getHang().getName() + "\n"
                        + get.getGiayChiTiet().getKichCo().getName() + "\n"
                        + get.getGiayChiTiet().getKieuDang().getName() + "\n"
                        + get.getGiayChiTiet().getMauSac().getName() + "\n",
                        fontInfo);
                Paragraph soLuong = new Paragraph(get.getSoLuong() + "", fontInfo);
                Paragraph thanhTien = new Paragraph(get.getGia() + "", fontInfo);
                tbl.addCell(stt);
                tbl.addCell(hinhAnh);
                tbl.addCell(giayCT);
                tbl.addCell(soLuong);
                tbl.addCell(thanhTien);

            }

            // Tổng tiền hàng, tiền phải trả, phí ship, giảm giá...
            PdfPTable tblFooter = new PdfPTable(4);
            tblFooter.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            tblFooter.getDefaultCell().setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
            tblFooter.getDefaultCell().setVerticalAlignment(PdfPTable.ALIGN_MIDDLE);

            for (int row = 0; row < 3; row++) {
                String column = "";
                tblFooter.addCell(column);
                tblFooter.addCell(column);
                switch (row) {
                    case 0:
                        Paragraph tienHang = new Paragraph("Tiền hàng:", fontInfo);
                        Paragraph tien1 = new Paragraph(hd.getTongTien() + " đ", fontInfo);
                        tblFooter.addCell(tienHang);
                        tblFooter.addCell(tien1);
                        break;
                    case 1:
                        Paragraph thanhTien = new Paragraph("Tổng tiền giảm:", fontInfo);
                        Paragraph tien3 = new Paragraph(" đ", fontInfo);
                        tblFooter.addCell(thanhTien);
                        tblFooter.addCell(tien3);
                        break;
                    case 2:
                        Paragraph tongTien1 = new Paragraph("Tổng tiền:", fontInfo);
                        Paragraph tien4 = new Paragraph(hd.getTongTien() + " đ", fontInfo);
                        tblFooter.addCell(tongTien1);
                        tblFooter.addCell(tien4);
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
            JOptionPane.showMessageDialog(this, "Xuất hóa đơn thành công");
        } catch (DocumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        d.close();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDSSP = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSachSp = new javax.swing.JTable();
        lblError = new javax.swing.JLabel();
        cboGiay = new javax.swing.JComboBox<>();
        cboDanhMuc = new javax.swing.JComboBox<>();
        cboMauSac = new javax.swing.JComboBox<>();
        cboKichCo = new javax.swing.JComboBox<>();
        cboHang = new javax.swing.JComboBox<>();
        cboKieuDang = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        lblPage = new javax.swing.JLabel();
        btnBoBL = new javax.swing.JButton();
        pnlGioHang = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGioHangCho = new javax.swing.JTable();
        pnlDSHD = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListHoaDon = new javax.swing.JTable();
        pnlBanHang = new javax.swing.JPanel();
        CardMuaHang = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnHuy = new javax.swing.JButton();
        btnHoaDon = new javax.swing.JButton();
        cboHinhThucTT = new javax.swing.JComboBox<>();
        lblMaHoaDon = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        lblTienThua = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        lblKiemTraDiem = new javax.swing.JLabel();
        btnSuDungDien = new javax.swing.JButton();
        lblErrKiemTraDiem = new javax.swing.JLabel();
        lblErrTienKhachDua = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtVoucher = new javax.swing.JTextField();
        btnSuDungVoucher = new javax.swing.JButton();
        pnlThanhVien = new javax.swing.JPanel();
        txtMaKhach = new javax.swing.JTextField();
        lblKhachHang = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        lblErrKhach = new javax.swing.JLabel();
        lblDiemKH = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jpnQR = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlDSSP.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Sản Phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        tblDanhSachSp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDanhSachSp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã", "Tên", "Hãng", "Kiểu dáng", "Danh mục", "Màu sắc", "Kích cỡ", "Số lượng", "Đơn giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDanhSachSp.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblDanhSachSp.setGridColor(new java.awt.Color(204, 204, 255));
        tblDanhSachSp.setIntercellSpacing(new java.awt.Dimension(5, 5));
        tblDanhSachSp.setOpaque(false);
        tblDanhSachSp.setRowHeight(20);
        tblDanhSachSp.setSelectionBackground(new java.awt.Color(204, 255, 255));
        tblDanhSachSp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachSpMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDanhSachSp);

        lblError.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lblError.setForeground(new java.awt.Color(204, 0, 0));

        cboGiay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboGiayItemStateChanged(evt);
            }
        });

        cboDanhMuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboDanhMuc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboDanhMucItemStateChanged(evt);
            }
        });

        cboMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMauSac.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMauSacItemStateChanged(evt);
            }
        });

        cboKichCo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKichCo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboKichCoItemStateChanged(evt);
            }
        });

        cboHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboHang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboHangItemStateChanged(evt);
            }
        });

        cboKieuDang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKieuDang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboKieuDangItemStateChanged(evt);
            }
        });

        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        lblPage.setText("Trang");

        btnBoBL.setText("Bỏ");
        btnBoBL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBoBLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDSSPLayout = new javax.swing.GroupLayout(pnlDSSP);
        pnlDSSP.setLayout(pnlDSSPLayout);
        pnlDSSPLayout.setHorizontalGroup(
            pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSSPLayout.createSequentialGroup()
                .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDSSPLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlDSSPLayout.createSequentialGroup()
                                .addComponent(cboGiay, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlDSSPLayout.createSequentialGroup()
                                .addComponent(cboKichCo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboKieuDang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBoBL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(pnlDSSPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDSSPLayout.createSequentialGroup()
                                .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(459, 459, 459)
                                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(btnNext))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlDSSPLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cboDanhMuc, cboGiay, cboHang, cboKichCo, cboKieuDang, cboMauSac});

        pnlDSSPLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnNext, btnPrev});

        pnlDSSPLayout.setVerticalGroup(
            pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSSPLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboGiay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboKichCo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboKieuDang, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnBoBL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNext)
                    .addComponent(btnPrev)
                    .addComponent(lblPage))
                .addGap(21, 21, 21))
        );

        pnlDSSPLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBoBL, btnSearch, cboDanhMuc, cboGiay, cboHang, cboKichCo, cboKieuDang, cboMauSac, txtSearch});

        getContentPane().add(pnlDSSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 415, 795, 325));

        pnlGioHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ Hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        btnDelete.setBackground(new java.awt.Color(0, 0, 0));
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Bỏ sản phẩm");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tblGioHangCho.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tblGioHangCho.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblGioHangCho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã", "Tên", "Hãng", "Kiểu dáng", "Danh mục", "Màu sắc", "Kích cỡ", "SL", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGioHangCho.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblGioHangCho.setGridColor(new java.awt.Color(204, 204, 255));
        tblGioHangCho.setIntercellSpacing(new java.awt.Dimension(5, 5));
        tblGioHangCho.setOpaque(false);
        tblGioHangCho.setRowHeight(20);
        tblGioHangCho.setSelectionBackground(new java.awt.Color(204, 255, 255));
        tblGioHangCho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangChoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblGioHangCho);

        javax.swing.GroupLayout pnlGioHangLayout = new javax.swing.GroupLayout(pnlGioHang);
        pnlGioHang.setLayout(pnlGioHangLayout);
        pnlGioHangLayout.setHorizontalGroup(
            pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGioHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGioHangLayout.createSequentialGroup()
                        .addComponent(btnDelete)
                        .addGap(0, 672, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        pnlGioHangLayout.setVerticalGroup(
            pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGioHangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete)
                .addGap(64, 64, 64))
        );

        getContentPane().add(pnlGioHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 200, 795, 205));

        pnlDSHD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hóa Đơn Chờ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        tblListHoaDon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblListHoaDon.setForeground(new java.awt.Color(51, 51, 51));
        tblListHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã hóa đơn", "Ngày tạo", "Mã NV", "Tên NV"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblListHoaDon.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblListHoaDon.setGridColor(new java.awt.Color(204, 204, 255));
        tblListHoaDon.setIntercellSpacing(new java.awt.Dimension(5, 5));
        tblListHoaDon.setOpaque(false);
        tblListHoaDon.setRowHeight(20);
        tblListHoaDon.setSelectionBackground(new java.awt.Color(204, 255, 255));
        tblListHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblListHoaDon);
        tblListHoaDon.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tblListHoaDon.getColumnModel().getColumnCount() > 0) {
            tblListHoaDon.getColumnModel().getColumn(0).setResizable(false);
            tblListHoaDon.getColumnModel().getColumn(1).setResizable(false);
            tblListHoaDon.getColumnModel().getColumn(2).setResizable(false);
            tblListHoaDon.getColumnModel().getColumn(3).setResizable(false);
            tblListHoaDon.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout pnlDSHDLayout = new javax.swing.GroupLayout(pnlDSHD);
        pnlDSHD.setLayout(pnlDSHDLayout);
        pnlDSHDLayout.setHorizontalGroup(
            pnlDSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
        );
        pnlDSHDLayout.setVerticalGroup(
            pnlDSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSHDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        getContentPane().add(pnlDSHD, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 560, 184));

        pnlBanHang.setLayout(new java.awt.CardLayout());

        CardMuaHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Đơn hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        CardMuaHang.setName("Tạo Hoá Đơn"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("Mã hóa đơn");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel5.setText("Nhân Viên");

        btnThanhToan.setBackground(new java.awt.Color(0, 0, 0));
        btnThanhToan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel9.setText("Tiền khách đưa");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel10.setText("Tiền thừa");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel12.setText("Hình thức TT");

        btnHuy.setBackground(new java.awt.Color(0, 0, 0));
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setText("Huỷ Hoá Đơn");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnHoaDon.setBackground(new java.awt.Color(0, 0, 0));
        btnHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnHoaDon.setText("Tạo Hoá Đơn");
        btnHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoaDonActionPerformed(evt);
            }
        });

        cboHinhThucTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền Mặt", "Thẻ " }));

        lblMaHoaDon.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N

        lblNhanVien.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblNhanVien.setText("Null");

        lblTongTien.setText("0");

        lblTienThua.setText("0");

        txtTienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTienKhachDuaKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel18.setText("Điểm tích lũy");

        lblKiemTraDiem.setText("0");

        btnSuDungDien.setBackground(new java.awt.Color(0, 0, 0));
        btnSuDungDien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuDungDien.setForeground(new java.awt.Color(255, 255, 255));
        btnSuDungDien.setText("Dùng");
        btnSuDungDien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuDungDienMouseClicked(evt);
            }
        });
        btnSuDungDien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuDungDienActionPerformed(evt);
            }
        });

        lblErrKiemTraDiem.setForeground(new java.awt.Color(204, 0, 0));

        lblErrTienKhachDua.setForeground(new java.awt.Color(204, 0, 0));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel19.setText("Tổng tiền");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel13.setText("Voucher");

        txtVoucher.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVoucherKeyReleased(evt);
            }
        });

        btnSuDungVoucher.setBackground(new java.awt.Color(0, 0, 0));
        btnSuDungVoucher.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuDungVoucher.setForeground(new java.awt.Color(255, 255, 255));
        btnSuDungVoucher.setText("Dùng");
        btnSuDungVoucher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuDungVoucherMouseClicked(evt);
            }
        });
        btnSuDungVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuDungVoucherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CardMuaHangLayout = new javax.swing.GroupLayout(CardMuaHang);
        CardMuaHang.setLayout(CardMuaHangLayout);
        CardMuaHangLayout.setHorizontalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnSuDungDien))))
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                                .addComponent(txtVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnSuDungVoucher)))
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnHoaDon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnHuy)
                        .addGap(24, 24, 24)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(255, 255, 255)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CardMuaHangLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnHoaDon, btnHuy});

        CardMuaHangLayout.setVerticalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHuy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGap(162, 162, 162)
                                .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(163, 163, 163))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSuDungDien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSuDungVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))))
        );

        CardMuaHangLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnHoaDon, btnHuy});

        pnlBanHang.add(CardMuaHang, "CardMuaHang");

        getContentPane().add(pnlBanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 200, 390, 540));

        pnlThanhVien.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thành viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        pnlThanhVien.setPreferredSize(new java.awt.Dimension(390, 185));

        txtMaKhach.setBackground(new java.awt.Color(240, 240, 240));
        txtMaKhach.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập mã khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 14))); // NOI18N
        txtMaKhach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaKhachKeyReleased(evt);
            }
        });

        lblKhachHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tên khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 3, 12))); // NOI18N
        lblKhachHang.setEnabled(false);

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Đăng Ký Thành Viên");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        lblErrKhach.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        lblErrKhach.setForeground(new java.awt.Color(204, 0, 0));
        lblErrKhach.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblDiemKH.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblDiemKH.setForeground(new java.awt.Color(255, 51, 0));
        lblDiemKH.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDiemKH.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Điểm tích lũy", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Đăng Ký Thành Viên");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlThanhVienLayout = new javax.swing.GroupLayout(pnlThanhVien);
        pnlThanhVien.setLayout(pnlThanhVienLayout);
        pnlThanhVienLayout.setHorizontalGroup(
            pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhVienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaKhach)
                            .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDiemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addComponent(lblErrKhach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96))))
        );
        pnlThanhVienLayout.setVerticalGroup(
            pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addComponent(txtMaKhach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblDiemKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(jButton4)))
                .addGap(24, 24, 24))
        );

        getContentPane().add(pnlThanhVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 5, 390, 185));

        jpnQR.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "QR Camera", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        jpnQR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jpnQR, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 5, 230, 185));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblDanhSachSpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSpMouseClicked
        int selectedRowSP = tblDanhSachSp.getSelectedRow();
        int selectedRowHD = tblListHoaDon.getSelectedRow();
        GiayChiTiet findGiay = new GiayChiTiet();
        findGiay.setMa((String) tblDanhSachSp.getValueAt(selectedRowSP, 0));
        GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiay);

        int soLuongKho = (int) tblDanhSachSp.getValueAt(selectedRowSP, 7);

        int check = JOptionPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");
        if (check == JOptionPane.YES_OPTION) {
            if (hd == null) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn hóa đơn");
            } else {
                String soLuong = JOptionPane.showInputDialog(null, "Nhập số lượng");
                int soLuongNhap = 0;
                try {
                    soLuongNhap = Integer.parseInt(soLuong);
                    if (soLuongNhap < 1) {
                        JOptionPane.showMessageDialog(null, "Số lượng phải là số nguyên dương >= 1");
                    } else if (soLuongNhap <= soLuongKho) {
                        Boolean isDuplicate = false;
                        // xu ly trung sp
                        for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(hd.getId())) { // danh sách hdct theo id hóa đơn
                            if (item.getGiayChiTiet().getId().equalsIgnoreCase(giayChiTiet.getId())) {
                                HoaDonChiTiet findHoaDonChiTiet = new HoaDonChiTiet();
                                findHoaDonChiTiet.setGiayChiTiet(giayChiTiet);
                                findHoaDonChiTiet.setHoaDon(hd);

                                HoaDonChiTiet updateSL = hoaDonChiTietRepository.selectbyId(findHoaDonChiTiet);
                                updateSL.setTrangThai("Mua");
                                updateSL.setGiayChiTiet(giayChiTiet);
                                updateSL.setHoaDon(hd);
                                int soLuongTrongGH = updateSL.getSoLuong();
                                updateSL.setSoLuong(soLuongNhap + soLuongTrongGH);
                                hoaDonChiTietRepository.update(updateSL);
                                // xu ly cong tru soLuong trong DSSP
                                giayChiTietRepository.updateSoLuong(soLuongKho - soLuongNhap, giayChiTiet.getMa());
                                isDuplicate = true;
                            }
                        }
                        // xử lý thêm vào giỏ hàng
                        if (!isDuplicate) {
                            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                            hoaDonChiTiet.setGiayChiTiet(giayChiTiet);
                            hoaDonChiTiet.setHoaDon(hd);
                            hoaDonChiTiet.setSoLuong(soLuongNhap);
                            hoaDonChiTiet.setGia(giayChiTiet.getGia());
                            hoaDonChiTiet.setTrangThai("Mua hang");
                            hoaDonChiTietRepository.insert(hoaDonChiTiet);
                            // xu ly cong tru soLuong trong DSSP
                            giayChiTietRepository.updateSoLuong(soLuongKho - soLuongNhap, giayChiTiet.getMa());
                        }

                        showDataGH(hd.getId());
                        showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
                        tinhVaThemTongTien(9);
                    } else {
                        JOptionPane.showMessageDialog(this, "Không đủ hàng");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải là số");
                }

            }
        }
    }//GEN-LAST:event_tblDanhSachSpMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int check = JOptionPane.showConfirmDialog(this, "Xoá Khỏi Giỏ ? Xoá ");

        try {
            int indexDelete = tblGioHangCho.getSelectedRow();
            if (indexDelete >= 0) {
                if (check == JOptionPane.YES_OPTION) {
                    HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.selectAll(hd.getId()).get(indexDelete);

                    if (hoaDonChiTietRepository.delete(hoaDonChiTiet) == 1) {
                        Integer soLuongGioHang = Integer.valueOf(tblGioHangCho.getValueAt(indexDelete, 7).toString());
                        String maGiayChiTietDelete = hoaDonChiTiet.getGiayChiTiet().getMa();
                        int soLuongSanPham = getSoLuongGiayCT(maGiayChiTietDelete);
                        Integer soLuongCapNhat = soLuongGioHang + soLuongSanPham;
                        giayChiTietRepository.updateSoLuong(soLuongCapNhat, maGiayChiTietDelete);
                        showDataGH(hd.getId());
                        showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
                        tinhVaThemTongTien(9);
                        JOptionPane.showMessageDialog(this, "Xoá Thành Công");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chưa Chọn Mà Xoá");
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonActionPerformed

        int check = JOptionPane.showConfirmDialog(this, "Tạo Hoá Đơn");
        try {
            if (check == JOptionPane.YES_OPTION) {
                if (hoaDonRepository.selectAllCho().size() >= 5) {
                    JOptionPane.showMessageDialog(this, "Giới hạn số lượng hóa đơn chờ là 5");
                } else {
                    HoaDon hoaDon = new HoaDon();
                    hoaDon.setNhanVien(nhanVien);
                    hoaDon.setNgayTao(new Date());
                    hoaDon.setTrangThai("Chờ thanh toán");
                    hoaDonRepository.insert(hoaDon);
                    showDataHoaDon(hoaDonRepository.selectAllCho());
                }
            }
        } catch (HeadlessException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnHoaDonActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        if (!lblMaHoaDon.getText().trim().isEmpty()) {
            int check = JOptionPane.showConfirmDialog(this, "Thanh Toán ?");
            if (check == JOptionPane.YES_OPTION) {
                KhachHang findKhachHang = new KhachHang();
                String maKH = txtMaKhach.getText();
                if (lblKhachHang.getText().equalsIgnoreCase("")) {
                    maKH = "kh017";
                }
                if (txtTienKhachDua.getText().equalsIgnoreCase("")) {
                    JOptionPane.showMessageDialog(this, "Khách chưa đưa tiền");
                } else {
                    findKhachHang.setMa(maKH);
                    System.out.println(khachHangRepository.selectbyId(findKhachHang));
                    BigDecimal tienThua = BigDecimal.valueOf(Double.parseDouble(txtTienKhachDua.getText())).subtract(tongTien);
                    if (Double.parseDouble(tienThua.toString()) >= 0) {
                        if (khachHangRepository.selectbyId(findKhachHang) != null) {
                            HoaDon hoaDon = hd;
                            hoaDon.setKhachHang(khachHangRepository.selectbyId(findKhachHang));
                            hoaDon.setTienThua(tienThua);
                            hoaDon.setTongTien(tongTien);
                            hoaDon.setTienKhachDua(BigDecimal.valueOf(Double.parseDouble(txtTienKhachDua.getText())));
                            hoaDon.setTrangThai("Đã thanh toán");
                            hoaDon.setHinhThucThanhToan(cboHinhThucTT.getSelectedItem().toString());
                            int row = hoaDonRepository.update(hoaDon);
                            if (row == 1) {
                                int xuatHoaDon = JOptionPane.showConfirmDialog(this, "Bạn có muốn xuất hóa đơn");
                                if (xuatHoaDon == JOptionPane.YES_OPTION) {
                                    xuatHD();
                                }
                                if (hd.getVoucher() != null) {
                                    Voucher findVoucher = hd.getVoucher();
                                    findVoucher.setSoLuong(findVoucher.getSoLuong() - 1);
                                    voucherRepository.update(findVoucher);
                                }
                                JOptionPane.showMessageDialog(this, "Thanh toán thành công");
                                dtmGH.setRowCount(0);
                                hd = null;
                                clearFormBH();
                            } else {
                                JOptionPane.showMessageDialog(this, "Thanh toán thất bại");
                            }
                            showDataHoaDon(hoaDonRepository.selectAllCho());
                            showDataSanPham(giayChiTietRepository.selectAll(), currentPage);

                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chưa tạo hóa đơn!");
            }
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
//        Auth.clearWebcam();
        webcam.close();
//        DangKyThanhVienForm t = new DangKyThanhVienForm();
//        t.setVisible(true);
        openWeb = false;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnSuDungDienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuDungDienMouseClicked
//        String ma = txtMaKhach.getText().trim();
//        BigDecimal diem = new BigDecimal(lblKiemTraDiem.getText().trim());
//        BigDecimal result = tinhVaThemTongTien(5).subtract(diem);
//        BigDecimal diemConlai = diem.subtract(diem);
//
//        // Kiểm tra hủy sử dụng điểm
//        if (evt.getClickCount() == 2) {
//            huySuDungDiem(ma, diem, result);
//            return;
//        }
//
//        // Kiểm tra sử dụng điểm
//        if (evt.getClickCount() == 1) {
//            suDungDiem(ma, result, diemConlai);
//        }
    }//GEN-LAST:event_btnSuDungDienMouseClicked

    private void txtTienKhachDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachDuaKeyReleased
        String tienKhachDua = txtTienKhachDua.getText().trim();
        try {
            BigDecimal tienKhachDuaDecimal = new BigDecimal(tienKhachDua);

            BigDecimal tongTien = new BigDecimal(lblTongTien.getText().trim());
            if (!tienKhachDua.equalsIgnoreCase("0") || !tienKhachDua.isEmpty()) {

                if (tienKhachDuaDecimal.compareTo(tongTien) == 1 || tienKhachDuaDecimal.compareTo(tongTien) == 0) {
                    BigDecimal tienthua = tienKhachDuaDecimal.subtract(tongTien);
                    lblTienThua.setText(tienthua.toString());
                    lblErrTienKhachDua.setText(null);
                } else {
                    lblErrKiemTraDiem.setText(null);
                    lblErrTienKhachDua.setText("Chưa Đủ Tiền");
                    lblTienThua.setText("0");
                }
            } else {
                lblErrTienKhachDua.setText("Nhập tiền khách đưa");
            }
        } catch (NumberFormatException e) {
            lblErrTienKhachDua.setText("Tiền Phải Là Số");
            lblTienThua.setText("0");
        }
    }//GEN-LAST:event_txtTienKhachDuaKeyReleased

    private void txtMaKhachKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaKhachKeyReleased
        String maKhach = txtMaKhach.getText().trim();
        KhachHang findKhachHang = new KhachHang();
        findKhachHang.setMa(maKhach);

        if (!maKhach.isEmpty() || !maKhach.equalsIgnoreCase("")) {
            KhachHang khachHang = khachHangRepository.selectbyId(findKhachHang);
            if (khachHang != null) {
                lblKhachHang.setText(khachHang.getName());
                lblErrKhach.setText(null);
                lblKiemTraDiem.setText(tichDiem.toString());
                lblKiemTraDiem.setForeground(getForeground());
                lblTienThua.setText("0");
                txtTienKhachDua.setText("");

                lblDiemKH.setText(tichDiem.toString());
            } else {
                lblErrKhach.setText(null);
                lblDiemKH.setText(tichDiem.toString());
                lblKiemTraDiem.setText(null);
                lblKhachHang.setText(null);
            }
        } else {
            lblErrKhach.setText(null);
            lblDiemKH.setText(tichDiem.toString());
            lblKiemTraDiem.setText(null);
            lblKhachHang.setText(null);
        }
    }//GEN-LAST:event_txtMaKhachKeyReleased

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        int i = tblListHoaDon.getSelectedRow();
        if (i >= 0) {
            int check = JOptionPane.showConfirmDialog(this, "Huỷ Hoá Đơn");
            if (check == JOptionPane.YES_OPTION) {
                xoaSPGH(hd.getId());
                lblTongTien.setText("0");
                lblMaHoaDon.setText(null);
//                txtMaHoaDon2.setText("0");
//                hoaDonRepository.delete(hd);
                showDataHoaDon(hoaDonRepository.selectAllCho());
                showDataGH(hd.getId());
                showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
            } else {
                JOptionPane.showMessageDialog(this, "Muốn hủy hóa đơn nào cơ ?");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn hóa đơn");
        }
    }//GEN-LAST:event_btnHuyActionPerformed

    private void tblGioHangChoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangChoMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRowGH = tblGioHangCho.getSelectedRow();
            int messegertype = JOptionPane.QUESTION_MESSAGE;
            String[] obtion = {"Thay đổi số lượng", "Huỷ"};
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.selectAll(hd.getId()).get(selectedRowGH);

            GiayChiTiet giayChiTiet = hoaDonChiTiet.getGiayChiTiet();

            int code = JOptionPane.showOptionDialog(this, "BẠN MUỐN LÀM GÌ ?", "LỰA CHỌN", 0, messegertype, null, obtion, "Số Lượng");
            switch (code) {
                case 0 -> {
                    String soLuongNhap = JOptionPane.showInputDialog("Nhập Số Lượng");
                    try {
                        int soLuongMoi = Integer.parseInt(soLuongNhap);
                        int soLuongCu = Integer.parseInt(tblGioHangCho.getValueAt(selectedRowGH, 7).toString());
                        int soLuongTrongKho = giayChiTietRepository.selectbyId(giayChiTiet).getSoLuong();
                        if (soLuongMoi == 0) {
                            hoaDonChiTietRepository.delete(hoaDonChiTiet);
                            giayChiTietRepository.updateSoLuong(soLuongTrongKho + soLuongCu - soLuongMoi, giayChiTiet.getMa());
                        } else if (soLuongMoi > (soLuongTrongKho + soLuongCu)) {
                            JOptionPane.showMessageDialog(this, "Không đủ hàng");
                        } else if (soLuongMoi < 0) {
                            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương");
                        } else {
                            HoaDonChiTiet updateSL = new HoaDonChiTiet();
                            updateSL.setSoLuong(soLuongMoi);
                            updateSL.setTrangThai("Mua");
                            updateSL.setGiayChiTiet(giayChiTiet);
                            updateSL.setHoaDon(hd);
                            hoaDonChiTietRepository.update(updateSL);
                            giayChiTietRepository.updateSoLuong(soLuongTrongKho + soLuongCu - soLuongMoi, giayChiTiet.getMa());
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập một số nguyên");
                        return;
                    }
                }
                case 1 -> {
                    return;
                }
            }
            showDataGH(hd.getId());
            showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
            tinhVaThemTongTien(9);
            setTextTT();
        }
    }//GEN-LAST:event_tblGioHangChoMouseClicked

    private void tblListHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListHoaDonMouseClicked
        int selectedRow = tblListHoaDon.getSelectedRow();
        tongTien = tinhVaThemTongTien(9);
        lblError.setText(null);
        tongTien = BigDecimal.ZERO;
        if (selectedRow != -1) {
            if (tblGioHangCho.getRowCount() >= 0) {
                lblMaHoaDon.setText((String) tblListHoaDon.getValueAt(selectedRow, 1));

                hd = hoaDonRepository.selectAllCho().get(selectedRow);
                lblNhanVien.setText(hd.getNhanVien().getMa());
                String idHoaDon = hd.getId();

                showDataGH(idHoaDon);

                int rowCount = tblGioHangCho.getRowCount();
                if (rowCount <= 0) {
                    lblTongTien.setText("0");
                    lblKiemTraDiem.setText("0");
                    lblKiemTraDiem.setForeground(Color.black);
                    lblTienThua.setText("0");
                } else {
                    for (int i = 0; i < rowCount; i++) {
                        String tongTienGio = tblGioHangCho.getValueAt(i, 9).toString();

                        Double giaTien = Double.parseDouble(tongTienGio);
                        tongTien = tongTien.add(BigDecimal.valueOf(giaTien));
                        lblTongTien.setText(tongTien.toString());
                        lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
                        lblError.setText(null);
                    }

                }
            } else {
                lblTongTien.setText("0");
                tongTien = BigDecimal.ZERO;
            }
            if (!lblMaHoaDon.getText().isEmpty()) {
                lblErrKiemTraDiem.setText(null);
            }
        }
    }//GEN-LAST:event_tblListHoaDonMouseClicked

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        showDataSanPham(search(), currentPage);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cboGiayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboGiayItemStateChanged
        if (cboGiay.getSelectedIndex() > 0) {
            giaySeleted = cboGiay.getSelectedItem().toString().toLowerCase();
        } else {
            giaySeleted = "";
        }
    }//GEN-LAST:event_cboGiayItemStateChanged

    private void cboDanhMucItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboDanhMucItemStateChanged
        if (cboDanhMuc.getSelectedIndex() > 0) {
            danhMucSelected = cboDanhMuc.getSelectedItem().toString().toLowerCase();
        } else {
            danhMucSelected = "";
        }
    }//GEN-LAST:event_cboDanhMucItemStateChanged

    private void cboMauSacItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMauSacItemStateChanged
        if (cboMauSac.getSelectedIndex() > 0) {
            mauSacSelected = cboMauSac.getSelectedItem().toString().toLowerCase();
        } else {
            mauSacSelected = "";
        }
    }//GEN-LAST:event_cboMauSacItemStateChanged

    private void cboKichCoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboKichCoItemStateChanged
        if (cboKichCo.getSelectedIndex() > 0) {
            kichCoSelected = cboKichCo.getSelectedItem().toString().toLowerCase();
        } else {
            kichCoSelected = "";
        }
    }//GEN-LAST:event_cboKichCoItemStateChanged

    private void cboHangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboHangItemStateChanged
        if (cboHang.getSelectedIndex() > 0) {
            hangSelected = cboHang.getSelectedItem().toString().toLowerCase();
        } else {
            hangSelected = "";
        }
    }//GEN-LAST:event_cboHangItemStateChanged

    private void cboKieuDangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboKieuDangItemStateChanged
        if (cboKieuDang.getSelectedIndex() > 0) {
            kieuDangSelected = cboKieuDang.getSelectedItem().toString().toLowerCase();
        } else {
            kieuDangSelected = "";
        }
    }//GEN-LAST:event_cboKieuDangItemStateChanged

    private void btnBoBLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBoBLActionPerformed
        // TODO add your handling code here:
        cboDanhMuc.setSelectedIndex(0);
        cboGiay.setSelectedIndex(0);
        cboHang.setSelectedIndex(0);
        cboKichCo.setSelectedIndex(0);
        cboKieuDang.setSelectedIndex(0);
        cboMauSac.setSelectedIndex(0);
        txtSearch.setText("");
        showDataSanPham(search(), currentPage);
    }//GEN-LAST:event_btnBoBLActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        if (currentPage == totalPage) {
            currentPage = 1;
            showDataSanPham(search(), currentPage);
        } else {
            currentPage++;
            showDataSanPham(search(), currentPage);
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        if (currentPage == 1) {
            currentPage = totalPage;
            showDataSanPham(search(), currentPage);
        } else {
            currentPage--;
            showDataSanPham(search(), currentPage);
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnSuDungDienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuDungDienActionPerformed
        // TODO add your handling code here:
        if (hd == null) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn hóa đơn để áp dụng");
        } else {
            tongTien = tongTien.subtract(tichDiem);
            tichDiem = tichDiem.subtract(tichDiem);
            System.out.println(tongTien);
            setTextTT();
        }
    }//GEN-LAST:event_btnSuDungDienActionPerformed

    private void txtVoucherKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVoucherKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVoucherKeyReleased

    private void btnSuDungVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuDungVoucherMouseClicked
        // TODO add your handling code here:
        if (hd == null) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn hóa đơn để thanh toán");
        } else {
            String codeVoucher = txtVoucher.getText();
            if (codeVoucher.isBlank()) {
            } else {
                Voucher findVoucher = new Voucher();
                findVoucher.setMaGiamGia(codeVoucher);
                Voucher voucher = voucherRepository.selectbyId(findVoucher);
                if (voucher != null) {
                    BigDecimal giaTri = voucher.getGiaTri();
                    BigDecimal dieuKien = voucher.getDieuKien();
                    if (tongTien.compareTo(dieuKien) > 0) {
                        if (voucher.getLoai().equalsIgnoreCase("phanTram")) {
                            tongTien = tongTien.subtract((tongTien.multiply(giaTri).divide(BigDecimal.valueOf(10))));
                        } else {
                            tongTien = tongTien.subtract((tongTien.multiply(giaTri)));
                        }
                        hd.setVoucher(voucher);
                        lblTongTien.setText(tongTien + "");
                    } else {
                        JOptionPane.showMessageDialog(this, "Không đủ điều diện để sử dụng voucher");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy voucher");
                }
            }
        }
    }//GEN-LAST:event_btnSuDungVoucherMouseClicked

    private void btnSuDungVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuDungVoucherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuDungVoucherActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
//        Auth.initWebcam(jpnQR);
        webcam.open();
        openWeb = true;
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Window".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoaDonForm1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new HoaDonForm1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CardMuaHang;
    private javax.swing.JButton btnBoBL;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSuDungDien;
    private javax.swing.JButton btnSuDungVoucher;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JComboBox<String> cboDanhMuc;
    private javax.swing.JComboBox<String> cboGiay;
    private javax.swing.JComboBox<String> cboHang;
    private javax.swing.JComboBox<String> cboHinhThucTT;
    private javax.swing.JComboBox<String> cboKichCo;
    private javax.swing.JComboBox<String> cboKieuDang;
    private javax.swing.JComboBox<String> cboMauSac;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpnQR;
    private javax.swing.JLabel lblDiemKH;
    private javax.swing.JLabel lblErrKhach;
    private javax.swing.JLabel lblErrKiemTraDiem;
    private javax.swing.JLabel lblErrTienKhachDua;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblKiemTraDiem;
    private javax.swing.JLabel lblMaHoaDon;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblPage;
    private javax.swing.JLabel lblTienThua;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel pnlBanHang;
    private javax.swing.JPanel pnlDSHD;
    private javax.swing.JPanel pnlDSSP;
    private javax.swing.JPanel pnlGioHang;
    private javax.swing.JPanel pnlThanhVien;
    private javax.swing.JTable tblDanhSachSp;
    private javax.swing.JTable tblGioHangCho;
    private javax.swing.JTable tblListHoaDon;
    private javax.swing.JTextField txtMaKhach;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtVoucher;
    // End of variables declaration//GEN-END:variables

//    private void huySuDungDiem(String ma, BigDecimal diem, BigDecimal result) {
//        int check = JOptionPane.showConfirmDialog(this, "Hủy Sử Dụng Điểm");
//        if (check == JOptionPane.YES_OPTION) {
//            BigDecimal diemHuy = tdrp.selectDiem(ma);
//            lblKiemTraDiem.setForeground(java.awt.Color.BLACK);
//            lblKiemTraDiem.setText(String.valueOf(diemHuy));
//            lblDiemKH.setText(String.valueOf(diemHuy));
//            BigDecimal tongTien = result;
//            BigDecimal tongTienHuy = tongTien.add(diem);
//            lblTongTien.setText(String.valueOf(tongTienHuy));
//            BigDecimal tienKhachDua = new BigDecimal(txtTienKhachDua.getText().trim());
//
//            if (!tienKhachDua.equals(BigDecimal.ZERO)) {
//                lblTienThua.setText(String.valueOf(tienKhachDua.subtract(tongTienHuy)));
//            }
//
//            // Cộng lại số điểm vào bảng TICHDIEN
//            tdrp.tichDiem(diemHuy, ma);
//        }
//    }
//
//    private void suDungDiem(String ma, BigDecimal result, BigDecimal diemConlai) {
//        String tongTienText = lblTongTien.getText().trim();
//        String tienKhachDuaText = txtTienKhachDua.getText().trim();
//
//        if (!lblMaHoaDon.getText().isEmpty()) {
//            if (ma.isBlank()) {
//                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng");
//            } else {
//                try {
//                    BigDecimal tongTien = new BigDecimal(tongTienText);
//                    BigDecimal tienKhachDua = new BigDecimal(tienKhachDuaText);
//
//                    if (tongTien.compareTo(BigDecimal.ZERO) != 0 && tienKhachDua.compareTo(BigDecimal.ZERO) > 0) {
//                        lblTongTien.setText(String.valueOf(result));
//                        lblTienThua.setText(String.valueOf(tienKhachDua.subtract(result)));
//                        lblKiemTraDiem.setForeground(java.awt.Color.RED);
//                        lblKiemTraDiem.setText(String.valueOf(diemConlai));
//                        lblDiemKH.setText(String.valueOf(diemConlai));
//                    } else {
//                        lblErrKiemTraDiem.setText("Không thể sử dụng");
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Error: " + e.getMessage());
//                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ!");
//                }
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Chưa tạo hóa đơn!");
//        }
//    }
//
    //Xóa Sản Phẩm khỏi giỏ
//    private void xoaSanPhamGio() {
//        int check = JOptionPane.showConfirmDialog(this, "Hủy Hóa Đơn ?");
//        if (check == JOptionPane.YES_OPTION) {
//            int indexHoaDon = tblListHoaDon.getSelectedRow();
//            HoaDon hoaDon = listHoaDon.get(indexHoaDon);
//            if (hdrepo.deleteAllHoaDonChiTiet(hoaDon.getId()) != null) {
//                showDataSanPham();
//                showDataGoHang(hoaDon.getId());
//            }
//        }
//    }
}
