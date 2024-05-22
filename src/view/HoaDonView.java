package view;

import loai.DangKyThanhVienForm;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
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
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JFileChooser;
import model.Voucher;
import repository.JOPane;
import repository.VoucherRepository;
import util.Auth;

public class HoaDonView extends javax.swing.JPanel implements Runnable, ThreadFactory {

    CardLayout card;

    private Webcam webcam = null;
    private WebcamPanel panel = null;
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
    private BigDecimal tienGiam = BigDecimal.ZERO;

    private NhanVien nhanVien = null;
    private HoaDon hd;
    private Voucher voucher = null;
    private KhachHang khachHang = null;

    public HoaDonView() {
        initComponents();
        initWebcam();

        nhanVien = Auth.user;
        dtmHD = (DefaultTableModel) tblListHoaDon.getModel();
        dtmGH = (DefaultTableModel) tblGioHangCho.getModel();
        dtmSP = (DefaultTableModel) tblDanhSachSp.getModel();

        if (hoaDonRepository.selectAllCho() != null) {
            showDataHoaDon(hoaDonRepository.selectAllCho());
        }
        if (giayChiTietRepository.selectAll() != null) {
            showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
        }
        this.configTblCol();
        loadDataCbo();
        dtmGH.setRowCount(0);
        setOpaque(false);
        lblNhanVien.setText(Auth.user.getMa());
    }

    private void configTblCol() {
        //Config bảng hóa đơn 
        this.tblListHoaDon.getColumnModel().getColumn(0).setPreferredWidth(86);
        this.tblListHoaDon.getColumnModel().getColumn(1).setPreferredWidth(100);
        this.tblListHoaDon.getColumnModel().getColumn(2).setPreferredWidth(120);
        this.tblListHoaDon.getColumnModel().getColumn(3).setPreferredWidth(100);
        this.tblListHoaDon.getColumnModel().getColumn(4).setPreferredWidth(152);
        //Config bảng giở hàng 
        this.tblGioHangCho.getColumnModel().getColumn(0).setPreferredWidth(70);
        this.tblGioHangCho.getColumnModel().getColumn(1).setPreferredWidth(93);
        this.tblGioHangCho.getColumnModel().getColumn(2).setPreferredWidth(75);
        this.tblGioHangCho.getColumnModel().getColumn(3).setPreferredWidth(80);
        this.tblGioHangCho.getColumnModel().getColumn(4).setPreferredWidth(85);
        this.tblGioHangCho.getColumnModel().getColumn(5).setPreferredWidth(70);
        this.tblGioHangCho.getColumnModel().getColumn(6).setPreferredWidth(50);
        this.tblGioHangCho.getColumnModel().getColumn(7).setPreferredWidth(50);
        this.tblGioHangCho.getColumnModel().getColumn(8).setPreferredWidth(100);
        this.tblGioHangCho.getColumnModel().getColumn(9).setPreferredWidth(123);
        //Config bảng danh sách sản phẩm 
        this.tblDanhSachSp.getColumnModel().getColumn(0).setPreferredWidth(90);
        this.tblDanhSachSp.getColumnModel().getColumn(1).setPreferredWidth(118);
        this.tblDanhSachSp.getColumnModel().getColumn(2).setPreferredWidth(85);
        this.tblDanhSachSp.getColumnModel().getColumn(3).setPreferredWidth(85);
        this.tblDanhSachSp.getColumnModel().getColumn(4).setPreferredWidth(85);
        this.tblDanhSachSp.getColumnModel().getColumn(5).setPreferredWidth(80);
        this.tblDanhSachSp.getColumnModel().getColumn(6).setPreferredWidth(73);
        this.tblDanhSachSp.getColumnModel().getColumn(7).setPreferredWidth(70);
        this.tblDanhSachSp.getColumnModel().getColumn(8).setPreferredWidth(105);
    }

    private String showTien(BigDecimal tien) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(tien);
    }

    private void initWebcam() {

        Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        jpnQR.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 223, 185));

        executor.execute(this);
    }

    // quét QR
    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {

            }

            Result result = null;
            BufferedImage image = null;
            if (webcam.open()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                result = new MultiFormatReader().decode(bitmap);
            } catch (NotFoundException ex) {
            }
            if (result != null) {
                GiayChiTiet findGiayChiTiet = new GiayChiTiet();
                findGiayChiTiet.setMa(result.toString());
                GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiayChiTiet);
                Integer soLuongGioHangHT = tinhSL(7);
                int soLuongKho = giayChiTiet.getSoLuong();

                if (hd == null) {
                    JOPane.showMessageDialog(this, "Bạn chưa chọn hóa đơn");
                } else {
                    boolean check = JOPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");
                    if (check) {
                        String soLuong = JOPane.showInputDialog(null, "Nhập số lượng");
                        int soLuongNhap = 0;
                        try {
                            soLuongNhap = Integer.parseInt(soLuong);
                            if (Auth.isManager()) {
                                if (soLuongNhap < 1) {
                                    JOPane.showMessageDialog(null, "Số lượng phải là số nguyên dương >= 1");
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
                                    tinhTien();
                                } else {
                                    JOPane.showMessageDialog(this, "Không đủ hàng");
                                }
                            } else {
                                if (soLuongGioHangHT + soLuongNhap <= 20) {
                                    if (soLuongNhap > 0) {
                                        if (soLuongNhap <= soLuongKho) {
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
                                                hoaDonChiTiet.setTrangThai("Mua");
                                                hoaDonChiTietRepository.insert(hoaDonChiTiet);
                                                // xu ly cong tru soLuong trong DSSP
                                                giayChiTietRepository.updateSoLuong(soLuongKho - soLuongNhap, giayChiTiet.getMa());
                                            }

                                            showDataGH(hd.getId());
                                            showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
                                            tinhTien();
                                        } else {
                                            JOPane.showMessageDialog(this, "Không đủ hàng");
                                        }
                                    } else {
                                        JOPane.showMessageDialog(this, "Số lượng phải là số nguyên dương");
                                    }
                                } else {
                                    JOPane.showMessageDialog(this, "Bạn không được bán quá 20 sản phẩm");
                                }
                            }
                        } catch (Exception e) {
                            JOPane.showMessageDialog(this, "Số lượng phải là số");
                        }
                    }
                }
            }
        } while (true);
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
            if (hoaDon.getNhanVien().getMa().equalsIgnoreCase(Auth.user.getMa())) {
                dtmHD.addRow(new Object[]{
                    stt,
                    hoaDon.getMaHoaDon(),
                    hoaDon.getNgayTao(),
                    hoaDon.getNhanVien().getMa(),
                    hoaDon.getNhanVien().getTen(),});
                stt++;
            }
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
                showTien(item.getGia()),});
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
                showTien(item.getGiayChiTiet().getGia()),
                showTien(item.getGia().multiply(BigDecimal.valueOf(item.getSoLuong()))),});
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
        if (hd != null) {
            lblNhanVien.setText(hd.getNhanVien().getMa());
            lblTongTien.setText(showTien(tongTien));
            String maHoaDon = hd.getMaHoaDon();
            lblMaHoaDon.setText(maHoaDon);
        }
    }

    private BigDecimal tinhTien() {
        tongTien = BigDecimal.ZERO;
        if (hd != null) {
            for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(hd.getId())) {
                tongTien = tongTien.add(BigDecimal.valueOf(item.getSoLuong()).multiply(item.getGia()));
            }
        }
        setTextTT();
        return tongTien;
    }

    private Integer tinhSL(int columnIndex) {
        Integer sl = 0;
        int rowCount = tblGioHangCho.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            Integer soLuong = Integer.valueOf(tblGioHangCho.getValueAt(i, columnIndex).toString());
            sl += soLuong;
        }
        return sl;
    }

    private void clearFormBH() {
        lblNhanVien.setText("");
        lblMaHoaDon.setText("");
        lblTongTien.setText("");
        lblTienThua.setText("");
        txtTienKhachDua.setText("");
        cboHinhThucTT.setSelectedIndex(0);
        txtVoucher.setText("");
    }

    void resetThanhToan() {
        lblMaHoaDon.setText(null);
        txtTienKhachDua.setText("0");
        lblTongTien.setText("0");
        lblErrKiemTraDiem.setText(null);
        dtmGH.setRowCount(0);
        lblTienThua.setText("0");
        txtMaKhach.setText("");
        lblKhachHang.setText(null);
        cboHinhThucTT.setSelectedIndex(0);
    }

    private void xoaSPGH(String idHD) {
        hoaDonRepository.huyGioHang(idHD);
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

        jpnQR = new javax.swing.JPanel();
        pnlThanhVien = new javax.swing.JPanel();
        txtMaKhach = new javax.swing.JTextField();
        lblKhachHang = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        pnlDSHD = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListHoaDon = new javax.swing.JTable();
        pnlGioHang = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGioHangCho = new javax.swing.JTable();
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
        lblErrKiemTraDiem = new javax.swing.JLabel();
        lblErrTienKhachDua = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtVoucher = new javax.swing.JTextField();
        btnSuDungVoucher = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1200, 750));

        jpnQR.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "QR Camera", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        jpnQR.setOpaque(false);
        jpnQR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlThanhVien.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thành viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        pnlThanhVien.setOpaque(false);
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
        jButton3.setText("Đăng ký");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlThanhVienLayout = new javax.swing.GroupLayout(pnlThanhVien);
        pnlThanhVien.setLayout(pnlThanhVienLayout);
        pnlThanhVienLayout.setHorizontalGroup(
            pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMaKhach, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(lblKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnlThanhVienLayout.setVerticalGroup(
            pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhVienLayout.createSequentialGroup()
                .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(txtMaKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlThanhVienLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblKhachHang, txtMaKhach});

        pnlDSHD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hóa Đơn Chờ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        pnlDSHD.setOpaque(false);

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
        tblListHoaDon.setGridColor(new java.awt.Color(51, 51, 51));
        tblListHoaDon.setIntercellSpacing(new java.awt.Dimension(5, 5));
        tblListHoaDon.setOpaque(false);
        tblListHoaDon.setRowHeight(25);
        tblListHoaDon.setSelectionBackground(new java.awt.Color(51, 51, 51));
        tblListHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListHoaDonMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblListHoaDon);
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
            .addGroup(pnlDSHDLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlDSHDLayout.setVerticalGroup(
            pnlDSHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSHDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        pnlGioHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Giỏ Hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        pnlGioHang.setOpaque(false);

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
                "Mã", "Tên", "Hãng", "Kiểu dáng", "Danh mục", "Màu sắc", "Size", "SL", "Đơn giá", "Thành tiền"
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
        tblGioHangCho.setGridColor(new java.awt.Color(51, 51, 51));
        tblGioHangCho.setIntercellSpacing(new java.awt.Dimension(5, 5));
        tblGioHangCho.setOpaque(false);
        tblGioHangCho.setRowHeight(25);
        tblGioHangCho.setSelectionBackground(new java.awt.Color(51, 51, 51));
        tblGioHangCho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangChoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblGioHangCho);
        if (tblGioHangCho.getColumnModel().getColumnCount() > 0) {
            tblGioHangCho.getColumnModel().getColumn(0).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(1).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(2).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(3).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(4).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(5).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(6).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(7).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(8).setResizable(false);
            tblGioHangCho.getColumnModel().getColumn(9).setResizable(false);
        }

        javax.swing.GroupLayout pnlGioHangLayout = new javax.swing.GroupLayout(pnlGioHang);
        pnlGioHang.setLayout(pnlGioHangLayout);
        pnlGioHangLayout.setHorizontalGroup(
            pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGioHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGioHangLayout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())
                    .addGroup(pnlGioHangLayout.createSequentialGroup()
                        .addComponent(btnDelete)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlGioHangLayout.setVerticalGroup(
            pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGioHangLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(btnDelete)
                .addContainerGap())
        );

        pnlDSSP.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Sản Phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        pnlDSSP.setOpaque(false);

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
        tblDanhSachSp.setGridColor(new java.awt.Color(51, 51, 51));
        tblDanhSachSp.setIntercellSpacing(new java.awt.Dimension(5, 5));
        tblDanhSachSp.setOpaque(false);
        tblDanhSachSp.setRowHeight(25);
        tblDanhSachSp.setSelectionBackground(new java.awt.Color(51, 51, 51));
        tblDanhSachSp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachSpMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblDanhSachSpMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(tblDanhSachSp);
        if (tblDanhSachSp.getColumnModel().getColumnCount() > 0) {
            tblDanhSachSp.getColumnModel().getColumn(0).setResizable(false);
            tblDanhSachSp.getColumnModel().getColumn(1).setResizable(false);
            tblDanhSachSp.getColumnModel().getColumn(2).setResizable(false);
            tblDanhSachSp.getColumnModel().getColumn(3).setResizable(false);
            tblDanhSachSp.getColumnModel().getColumn(4).setResizable(false);
            tblDanhSachSp.getColumnModel().getColumn(5).setResizable(false);
            tblDanhSachSp.getColumnModel().getColumn(6).setResizable(false);
            tblDanhSachSp.getColumnModel().getColumn(7).setResizable(false);
            tblDanhSachSp.getColumnModel().getColumn(8).setResizable(false);
        }

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
                .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlDSSPLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlDSSPLayout.createSequentialGroup()
                                .addComponent(cboKichCo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboKieuDang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pnlDSSPLayout.createSequentialGroup()
                                .addComponent(cboGiay, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlDSSPLayout.createSequentialGroup()
                                .addComponent(btnSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBoBL))
                            .addComponent(txtSearch)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDSSPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDSSPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlDSSPLayout.setVerticalGroup(
            pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSSPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187))
            .addGroup(pnlDSSPLayout.createSequentialGroup()
                .addContainerGap()
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNext)
                    .addComponent(btnPrev)
                    .addComponent(lblPage))
                .addContainerGap())
        );

        pnlDSSPLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBoBL, btnSearch, cboDanhMuc, cboGiay, cboHang, cboKichCo, cboKieuDang, cboMauSac, txtSearch});

        CardMuaHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Đơn hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        CardMuaHang.setName("Tạo Hoá Đơn"); // NOI18N
        CardMuaHang.setOpaque(false);
        CardMuaHang.setPreferredSize(new java.awt.Dimension(637, 550));

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
        lblNhanVien.setText(" ");

        lblTongTien.setText("0");

        lblTienThua.setText("0");

        txtTienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTienKhachDuaKeyReleased(evt);
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
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(37, 37, 37))
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                                        .addComponent(txtVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSuDungVoucher))
                                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(btnHoaDon)
                        .addGap(48, 48, 48)
                        .addComponent(btnHuy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CardMuaHangLayout.setVerticalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(163, 163, 163))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSuDungVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlDSHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jpnQR, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlGioHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlDSSP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlThanhVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CardMuaHang, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlDSHD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpnQR, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlThanhVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlGioHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlDSSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(CardMuaHang, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaKhachKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaKhachKeyReleased
        String maKhach = txtMaKhach.getText().trim();
        KhachHang findKhachHang = new KhachHang();
        findKhachHang.setMa(maKhach);

        if (!maKhach.isEmpty() || !maKhach.equalsIgnoreCase("")) {
            khachHang = khachHangRepository.selectbyId(findKhachHang);
            if (khachHang != null) {
                lblKhachHang.setText(khachHang.getName());
                lblTienThua.setText("0");
                txtTienKhachDua.setText("");
            } else {
                lblKhachHang.setText(null);
            }
        } else {
            lblKhachHang.setText(null);
        }
    }//GEN-LAST:event_txtMaKhachKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ThanhVienFrame t = new ThanhVienFrame();
        t.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tblListHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListHoaDonMouseClicked
        int selectedRow = tblListHoaDon.getSelectedRow();
        tinhTien();
        lblError.setText(null);
        tongTien = BigDecimal.ZERO;
        if (selectedRow != -1) {
            lblMaHoaDon.setText((String) tblListHoaDon.getValueAt(selectedRow, 1));
            hd = hoaDonRepository.selectAllCho().get(selectedRow);
            lblNhanVien.setText(hd.getNhanVien().getMa());
            String idHoaDon = hd.getId();
            tinhTien();
            showDataGH(idHoaDon);
        }
    }//GEN-LAST:event_tblListHoaDonMouseClicked

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        boolean check = JOPane.showConfirmDialog(this, "Xoá Khỏi Giỏ ? Xoá ");

        try {
            int indexDelete = tblGioHangCho.getSelectedRow();
            if (indexDelete >= 0) {
                if (check) {
                    HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.selectAll(hd.getId()).get(indexDelete);

                    if (hoaDonChiTietRepository.delete(hoaDonChiTiet) == 1) {
                        Integer soLuongGioHang = Integer.valueOf(tblGioHangCho.getValueAt(indexDelete, 7).toString());
                        String maGiayChiTietDelete = hoaDonChiTiet.getGiayChiTiet().getMa();
                        int soLuongSanPham = getSoLuongGiayCT(maGiayChiTietDelete);
                        Integer soLuongCapNhat = soLuongGioHang + soLuongSanPham;
                        giayChiTietRepository.updateSoLuong(soLuongCapNhat, maGiayChiTietDelete);
                        showDataGH(hd.getId());
                        showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
                        tinhTien();
                        JOPane.showMessageDialog(this, "Xoá Thành Công");
                    }
                }
            } else {
                JOPane.showMessageDialog(this, "Chưa Chọn Mà Xoá");
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblGioHangChoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangChoMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRowGH = tblGioHangCho.getSelectedRow();
            String[] obtion = {"Thay đổi số lượng", "Huỷ"};
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.selectAll(hd.getId()).get(selectedRowGH);

            GiayChiTiet giayChiTiet = hoaDonChiTiet.getGiayChiTiet();

            int code = JOPane.showOptionDialog(this, "Bạn muốn làm gì", obtion, "Số lượng");
            int soLuongMoi = 0;
            try {
                soLuongMoi = Integer.parseInt(JOPane.showInputDialog(this, "Nhập số lượng muốn thay đổi"));
            } catch (NumberFormatException e) {
                JOPane.showMessageDialog(this, "Vui lòng nhập một số nguyên");
                return;
            }
            if (Auth.isManager()) {
                switch (code) {
                    case 0 -> {
                        int soLuongCu = Integer.parseInt(tblGioHangCho.getValueAt(selectedRowGH, 7).toString());
                        int soLuongTrongKho = giayChiTietRepository.selectbyId(giayChiTiet).getSoLuong();
                        if (soLuongMoi == 0) {
                            hoaDonChiTietRepository.delete(hoaDonChiTiet);
                            giayChiTietRepository.updateSoLuong(soLuongTrongKho + soLuongCu - soLuongMoi, giayChiTiet.getMa());
                            voucher = null;
                        } else if (soLuongMoi > (soLuongTrongKho + soLuongCu)) {
                            JOPane.showMessageDialog(this, "Không đủ hàng");
                        } else if (soLuongMoi < 0) {
                            JOPane.showMessageDialog(this, "Số lượng phải là số nguyên dương");
                        } else {
                            HoaDonChiTiet updateSL = new HoaDonChiTiet();
                            updateSL.setSoLuong(soLuongMoi);
                            updateSL.setTrangThai("Mua");
                            updateSL.setGiayChiTiet(giayChiTiet);
                            updateSL.setHoaDon(hd);
                            hoaDonChiTietRepository.update(updateSL);
                            giayChiTietRepository.updateSoLuong(soLuongTrongKho + soLuongCu - soLuongMoi, giayChiTiet.getMa());
                        }

                    }
                    case 1 -> {
                        return;
                    }
                }
            } else {
                switch (code) {
                    case 0 -> {
                        int soLuongCu = Integer.parseInt(tblGioHangCho.getValueAt(selectedRowGH, 7).toString());
                        Integer soLuongGioHangHT = tinhSL(7);
                        if (soLuongGioHangHT + soLuongMoi <= 20) {
                            int soLuongTrongKho = giayChiTietRepository.selectbyId(giayChiTiet).getSoLuong();
                            if (soLuongMoi == 0) {
                                hoaDonChiTietRepository.delete(hoaDonChiTiet);
                                giayChiTietRepository.updateSoLuong(soLuongTrongKho + soLuongCu - soLuongMoi, giayChiTiet.getMa());
                                voucher = null;
                            } else if (soLuongMoi > (soLuongTrongKho + soLuongCu)) {
                                JOPane.showMessageDialog(this, "Không đủ hàng");
                            } else if (soLuongMoi < 0) {
                                JOPane.showMessageDialog(this, "Số lượng phải là số nguyên dương");
                            } else {
                                HoaDonChiTiet updateSL = new HoaDonChiTiet();
                                updateSL.setSoLuong(soLuongMoi);
                                updateSL.setTrangThai("Mua");
                                updateSL.setGiayChiTiet(giayChiTiet);
                                updateSL.setHoaDon(hd);
                                updateSL.setGia(giayChiTiet.getGia());
                                hoaDonChiTietRepository.update(updateSL);
                                giayChiTietRepository.updateSoLuong(soLuongTrongKho + soLuongCu - soLuongMoi, giayChiTiet.getMa());
                            }
                        } else {
                            JOPane.showMessageDialog(this, "Bạn không được bán quá 20 sản phẩm");
                        }
                    }
                    case 1 -> {
                        return;
                    }
                }
            }
            showDataGH(hd.getId());
            showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
            tinhTien();
        }
    }//GEN-LAST:event_tblGioHangChoMouseClicked

    private void tblDanhSachSpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSpMouseClicked
        int selectedRowSP = tblDanhSachSp.getSelectedRow();
        Integer soLuongGioHangHT = tinhSL(7);

        GiayChiTiet findGiay = new GiayChiTiet();
        findGiay.setMa((String) tblDanhSachSp.getValueAt(selectedRowSP, 0));
        GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiay);

        int soLuongKho = (int) tblDanhSachSp.getValueAt(selectedRowSP, 7);

        if (hd == null) {
            JOPane.showMessageDialog(this, "Bạn chưa chọn hóa đơn");
        } else {
            boolean check = JOPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");
            if (check) {
                String soLuong = JOPane.showInputDialog(null, "Nhập số lượng");
                int soLuongNhap = 0;
                try {
                    soLuongNhap = Integer.parseInt(soLuong);
                    if (Auth.isManager()) {
                        if (soLuongNhap < 1) {
                            JOPane.showMessageDialog(null, "Số lượng phải là số nguyên dương >= 1");
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
                            tinhTien();
                        } else {
                            JOPane.showMessageDialog(this, "Không đủ hàng");
                        }
                    } else {
                        if (soLuongGioHangHT + soLuongNhap <= 20) {
                            if (soLuongNhap > 0 && soLuongNhap < 20) {
                                if (soLuongNhap <= soLuongKho) {
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
                                        hoaDonChiTiet.setTrangThai("Mua");
                                        hoaDonChiTietRepository.insert(hoaDonChiTiet);
                                        // xu ly cong tru soLuong trong DSSP
                                        giayChiTietRepository.updateSoLuong(soLuongKho - soLuongNhap, giayChiTiet.getMa());
                                    }

                                    showDataGH(hd.getId());
                                    showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
                                    tinhTien();
                                } else {
                                    JOPane.showMessageDialog(this, "Không đủ hàng");
                                }
                            } else {
                                JOPane.showMessageDialog(this, "Số lượng phải là số nguyên dương");
                            }
                        } else {
                            JOPane.showMessageDialog(this, "Bạn không được bán quá 20 sản phẩm");
                        }
                    }
                } catch (Exception e) {
                    JOPane.showMessageDialog(this, "Số lượng phải là số");
                }
            }
        }
    }//GEN-LAST:event_tblDanhSachSpMouseClicked

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

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        showDataSanPham(search(), currentPage);
    }//GEN-LAST:event_btnSearchActionPerformed

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

    private void btnSuDungVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuDungVoucherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuDungVoucherActionPerformed

    private void btnSuDungVoucherMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuDungVoucherMouseClicked
        // TODO add your handling code here:
        tienGiam = BigDecimal.ZERO;
        if (hd == null) {
            JOPane.showMessageDialog(this, "Bạn cần chọn hóa đơn để thanh toán");
        } else {
            if (khachHang != null && khachHang.getMa().equalsIgnoreCase("KH000") == false) {
                String codeVoucher = txtVoucher.getText();
                tinhTien();
                if (codeVoucher.isBlank()) {
                } else {
                    Voucher findVoucher = new Voucher();
                    findVoucher.setMaGiamGia(codeVoucher);
                    voucher = voucherRepository.selectbyId(findVoucher);
                    if (voucher != null && voucher.getTrangThai().equalsIgnoreCase("Đang diễn ra") && voucher.getSoLuong() > 0) {
                        BigDecimal giaTri = voucher.getGiaTri();
                        BigDecimal dieuKien = voucher.getDieuKien();
                        if (tongTien.compareTo(dieuKien) > 0) {
                            if (voucher.getLoai().equalsIgnoreCase("phanTram")) {
                                tienGiam = (tongTien.multiply(giaTri)).divide(BigDecimal.valueOf(100));
                            } else {
                                tienGiam = (tongTien.multiply(giaTri));
                            }
                            tongTien = tongTien.subtract(tienGiam);
                            hd.setVoucher(voucher);
                            setTextTT();
                        } else {
                            JOPane.showMessageDialog(this, "Không đủ điều diện để sử dụng voucher");
                        }
                    } else {
                        JOPane.showMessageDialog(this, "Không tìm thấy voucher");
                    }
                }
            } else {
                JOPane.showMessageDialog(this, "Voucher không áp dụng cho khách vãng lai");
            }
        }
    }//GEN-LAST:event_btnSuDungVoucherMouseClicked

    private void txtVoucherKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVoucherKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVoucherKeyReleased

    private void txtTienKhachDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachDuaKeyReleased
        String tienKhachDua = txtTienKhachDua.getText().trim();
        try {
            BigDecimal tienKhachDuaDecimal = new BigDecimal(tienKhachDua);
            if (!tienKhachDua.equalsIgnoreCase("0") || !tienKhachDua.isEmpty()) {
                if (tienKhachDuaDecimal.compareTo(tongTien) == 1 || tienKhachDuaDecimal.compareTo(tongTien) == 0) {
                    BigDecimal tienthua = tienKhachDuaDecimal.subtract(tongTien);
                    lblTienThua.setText(showTien(tienthua));
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

    private void btnHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonActionPerformed
        boolean check = JOPane.showConfirmDialog(this, "Tạo Hoá Đơn");
        try {
            if (check) {
                if (hoaDonRepository.selectAllCho().size() >= 5) {
                    JOPane.showMessageDialog(this, "Giới hạn số lượng hóa đơn chờ là 5");
                } else {
                    HoaDon hoaDon = new HoaDon();
                    hoaDon.setNhanVien(nhanVien);
                    hoaDon.setNgayTao(new Date());
                    hoaDon.setTrangThai("Chờ thanh toán");
                    hoaDonRepository.insert(hoaDon);
                    hd = hoaDonRepository.selectAllCho().get(0);
                    tongTien = BigDecimal.ZERO;
                    dtmGH.setRowCount(0);
                    setTextTT();
                    showDataHoaDon(hoaDonRepository.selectAllCho());
                    tblListHoaDon.setRowSelectionInterval(0, 0);
                }
            }
        } catch (HeadlessException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnHoaDonActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        int i = tblListHoaDon.getSelectedRow();
        if (i >= 0) {
            boolean check = JOPane.showConfirmDialog(this, "Huỷ Hoá Đơn");
            if (check) {
                xoaSPGH(hd.getId());
                lblTongTien.setText("0");
                lblMaHoaDon.setText(null);
                hoaDonRepository.delete(hd);
                showDataHoaDon(hoaDonRepository.selectAllCho());
                showDataGH(hd.getId());
                showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
            }
        } else {
            JOPane.showMessageDialog(this, "Bạn chưa chọn hóa đơn");
        }
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        if (!lblMaHoaDon.getText().trim().isEmpty()) {
            boolean check = JOPane.showConfirmDialog(this, "Thanh Toán ?");
            if (check) {
                KhachHang findKhachHang = new KhachHang();
                String maKH = txtMaKhach.getText();
                if (lblKhachHang.getText().equalsIgnoreCase("")) {
                    maKH = "kh000";
                }
                if (txtTienKhachDua.getText().equalsIgnoreCase("")) {
                    JOPane.showMessageDialog(this, "Khách chưa đưa tiền");
                } else {
                    findKhachHang.setMa(maKH);
                    System.out.println(khachHangRepository.selectbyId(findKhachHang));
                    BigDecimal tienThua = BigDecimal.valueOf(Double.parseDouble(txtTienKhachDua.getText())).subtract(tongTien);
                    if (Double.parseDouble(tienThua.toString()) >= 0) {
                        if (khachHangRepository.selectbyId(findKhachHang) != null) {
                            HoaDon hoaDon = hd;
                            hoaDon.setNhanVien(nhanVien);
                            hoaDon.setKhachHang(khachHangRepository.selectbyId(findKhachHang));
                            hoaDon.setTienThua(tienThua);
                            hoaDon.setTongTien(tongTien);
                            hoaDon.setTienKhachDua(new BigDecimal(txtTienKhachDua.getText()));
                            hoaDon.setTrangThai("Đã thanh toán");
                            hoaDon.setHinhThucThanhToan(cboHinhThucTT.getSelectedItem().toString());
                            int row = hoaDonRepository.update(hoaDon);
                            if (row == 1) {
                                JOPane.showMessageDialog(this, "Thanh toán thành công");
                                boolean xuatHoaDon = JOPane.showConfirmDialog(this, "Bạn có muốn xuất hóa đơn");
                                if (xuatHoaDon) {
                                    xuatHD(hd);
                                }
                                if (hd.getVoucher() != null) {
                                    Voucher findVoucher = hd.getVoucher();
                                    findVoucher.setSoLuong(findVoucher.getSoLuong() - 1);
                                    voucherRepository.update(findVoucher);
                                }
                                dtmGH.setRowCount(0);
                                hd = null;
                                clearFormBH();
                            } else {
                                JOPane.showMessageDialog(this, "Thanh toán thất bại");
                            }
                            showDataHoaDon(hoaDonRepository.selectAllCho());
                            showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
                        }
                    }
                }
            }
        } else {
            JOPane.showMessageDialog(this, "Chưa tạo hóa đơn!");
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void tblDanhSachSpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSpMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDanhSachSpMouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CardMuaHang;
    private javax.swing.JButton btnBoBL;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSearch;
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel jpnQR;
    private javax.swing.JLabel lblErrKiemTraDiem;
    private javax.swing.JLabel lblErrTienKhachDua;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblMaHoaDon;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblPage;
    private javax.swing.JLabel lblTienThua;
    private javax.swing.JLabel lblTongTien;
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
}
