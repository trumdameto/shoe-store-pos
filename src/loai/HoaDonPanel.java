package loai;

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
import java.math.BigInteger;
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

public class HoaDonPanel extends javax.swing.JFrame {

    CardLayout card;

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private final Executor executor = Executors.newSingleThreadExecutor(null);
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

    private DefaultTableModel dtmHD;
    private DefaultTableModel dtmGH;
    private DefaultTableModel dtmSP;

    private Integer currentPage = 1;
    private Integer totalPage = 1;
    private Integer rowCountPage = 5;

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

    public HoaDonPanel() {
        initComponents();
        initWebcam();
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

        nhanVien = new NhanVien("FB6921DD-4A8D-4932-AAD4-A4C3A958D42D", "NV006",
                "Vũ Trọng Hùng", true, "0876546678", "Nam Định", new Date(1996, 06, 06), "12345", "Staff", "Hoạt động");
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

        Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        jpnQR.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 223, 178));

        executor.execute(null);
    }

    // quét QR
//    @Override
    public void run() {
//        do {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//
//            }
//
//            Result result = null;
//            BufferedImage image = null;
//            if (webcam.open()) {
//                if ((image = webcam.getImage()) == null) {
//                    continue;
//                }
//            }
//            LuminanceSource source = new BufferedImageLuminanceSource(image);
//            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//            try {
//                result = new MultiFormatReader().decode(bitmap);
//            } catch (NotFoundException ex) {
//
//            }
//
//            if (result != null) {
//                int indexDanhSachSp = 0;
//                for (GiayChiTiet giayChiTiet : listGiayChiTiet) {
//
//                    if (giayChiTiet.getiD().equals(result.toString())) {
//                        indexDanhSachSp = listGiayChiTiet.indexOf(giayChiTiet);
//                    }
//                }
//                int check = JOptionPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");
//
//                listGiayChiTiet = gct.getAllGiay();
//                listHoaDon = hdrepo.getAllHoaDon();
//
//                if (check == JOptionPane.YES_OPTION) {
//                    if (!lblMaHoaDon.getText().isEmpty()) {
//                        if (indexDanhSachSp >= 0) {
//                            Integer soLuongGoc = Integer.valueOf(tblDanhSachSp.getValueAt(indexDanhSachSp, 7).toString());
//                            if (soLuongGoc >= 0) {
//
//                                String soLuong = JOptionPane.showInputDialog(null, "Nhập Số Lượng");
//                                try {
//                                    if (Integer.valueOf(soLuong) <= soLuongGoc) {
//
//                                        if (soLuong != null && !soLuong.isEmpty()) {
//                                            int selectedRow = tblListHoaDon.getSelectedRow();
//                                            HoaDon indexHoaDon = listHoaDon.get(selectedRow);//Lý Do
//                                            String idHoaDonz = indexHoaDon.getId();
//                                            GiayChiTiet indexGiay = listGiayChiTiet.get(indexDanhSachSp);
//                                            String donGia = tblDanhSachSp.getValueAt(indexDanhSachSp, 8).toString();
//                                            int soluongGioHang = Integer.parseInt(soLuong);
//                                            Integer soLuongGocGioHang = hdctrepo.selectSoLuongGioHangGoc(idHoaDonz, indexGiay.getiD());
//                                            Integer soLuongGiHangThayDoi = soluongGioHang + soLuongGocGioHang;
//                                            Integer idGiayCtTonTai = hdrepo.selectIdSanPhamTrongGioHang(indexGiay.getiD(), idHoaDonz);
//                                            if (selectedRow >= 0) {
//                                                if (idGiayCtTonTai == 0) {
//                                                    if (hdctrepo.creatGiHang(indexGiay.getiD(), idHoaDonz, new BigDecimal(donGia), soluongGioHang) != null) {
//                                                        updateProductQuantity(indexDanhSachSp, soluongGioHang);
//                                                        showDataSanPham();
//                                                        showDataGoHang(idHoaDonz);
//                                                        JOptionPane.showMessageDialog(this, "Bỏ Thành Công Vào Giỏ");
//                                                    }
//                                                } else {
//                                                    if (hdctrepo.updateSoLuong(soLuongGiHangThayDoi, indexGiay.getiD()) != null) {
//                                                        updateProductQuantity(indexDanhSachSp, soluongGioHang);// trừ số lượng ở sản phẩm                                   
//                                                        showDataGoHang(idHoaDonz);
//                                                        showDataSanPham();
//                                                        JOptionPane.showMessageDialog(this, "Thay Đổi Số Lượng ");
//                                                    }
//
//                                                }
//                                                showDataGoHang(idHoaDonz);
//                                            }
//
//                                        }
//
//                                    } else {
//                                        lblError.setText("Xin Lỗi ! Chúng Tôi Không Có Đủ Số Lượng ");
//
//                                        return;
//                                    }
//                                } catch (NumberFormatException e) {
//                                    JOptionPane.showMessageDialog(this, "Số Lượng không Đúng Định Dạng Số");
//                                }
//
//                            } else {
//
//                                lblError.setText("HẾT HÀNG");
//                                return;
//                            }
//
//                        } else {
//                            JOptionPane.showMessageDialog(this, "Chọn một sản phẩm trước khi thêm vào giỏ nha!!!^^");
//                        }
//                    } else {
//                        lblError.setText("Xin Lỗi ! Bạn Chưa Chọn Hoặc Tạo Hoá Đơn ");
//
//                        return;
//                    }
//                }
//            }
//        } while (true);
    }

//    @Override
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
        lblTongTien1.setText(tongTien.toString());
        lblKiemTraDiem.setText(tichDiem.toString());
        String maHoaDon = hd.getMaHoaDon();
        lblMaHoaDon.setText(maHoaDon);
        txtMaHoaDon2.setText(maHoaDon);
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

    private void clearFormBH(){
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
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
        cardDatHang = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        lblTongTien1 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        txtMaHoaDon2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btnTroVeBanHang = new javax.swing.JButton();
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
        btnVaoDatHang = new javax.swing.JButton();
        lblErrTienKhachDua = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        pnlThanhVien = new javax.swing.JPanel();
        txtMaKhach = new javax.swing.JTextField();
        lblKhachHang = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        lblErrKhach = new javax.swing.JLabel();
        lblDiemKH = new javax.swing.JLabel();
        jpnQR = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 750));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        pnlDSSPLayout.setVerticalGroup(
            pnlDSSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDSSPLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
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

        jPanel1.add(pnlDSSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 415, 795, 325));

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
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE))
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

        jPanel1.add(pnlGioHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 200, 795, 205));

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

        jPanel1.add(pnlDSHD, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 560, 184));

        pnlBanHang.setLayout(new java.awt.CardLayout());

        cardDatHang.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Đặt Hàng");

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Đặt Hàng");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jLabel8.setText("Mã Hoá Đơn");

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));
        jPanel15.setForeground(new java.awt.Color(0, 102, 0));

        lblTongTien1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTongTien1.setForeground(new java.awt.Color(0, 102, 0));
        lblTongTien1.setText("0");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTongTien1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTongTien1)
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaHoaDon2)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtMaHoaDon2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel13.setText("jLabel13");

        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );

        jLabel14.setText("jLabel14");

        jLabel17.setText("jLabel17");

        btnTroVeBanHang.setText("Bán Hàng");
        btnTroVeBanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTroVeBanHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cardDatHangLayout = new javax.swing.GroupLayout(cardDatHang);
        cardDatHang.setLayout(cardDatHangLayout);
        cardDatHangLayout.setHorizontalGroup(
            cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardDatHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(cardDatHangLayout.createSequentialGroup()
                        .addGroup(cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cardDatHangLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 209, Short.MAX_VALUE)
                                .addComponent(btnTroVeBanHang))
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(cardDatHangLayout.createSequentialGroup()
                                .addGroup(cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel13))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        cardDatHangLayout.setVerticalGroup(
            cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardDatHangLayout.createSequentialGroup()
                .addGroup(cardDatHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardDatHangLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTroVeBanHang)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardDatHangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)))
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addGap(26, 26, 26)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel14)
                .addGap(2, 2, 2)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel17)
                .addGap(17, 17, 17)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );

        pnlBanHang.add(cardDatHang, "cardDatHang");

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

        btnVaoDatHang.setBackground(new java.awt.Color(0, 0, 0));
        btnVaoDatHang.setForeground(new java.awt.Color(255, 255, 255));
        btnVaoDatHang.setText("Đặt Hàng");
        btnVaoDatHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVaoDatHangActionPerformed(evt);
            }
        });

        lblErrTienKhachDua.setForeground(new java.awt.Color(204, 0, 0));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel19.setText("Tổng tiền");

        javax.swing.GroupLayout CardMuaHangLayout = new javax.swing.GroupLayout(CardMuaHang);
        CardMuaHang.setLayout(CardMuaHangLayout);
        CardMuaHangLayout.setHorizontalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnHoaDon)
                    .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblMaHoaDon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNhanVien, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblKiemTraDiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSuDungDien))
                    .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addComponent(btnHuy)
                        .addGap(18, 18, 18)
                        .addComponent(btnVaoDatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CardMuaHangLayout.setVerticalGroup(
            CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardMuaHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVaoDatHang)
                    .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(CardMuaHangLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CardMuaHangLayout.createSequentialGroup()
                                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSuDungDien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(CardMuaHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblErrTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(lblTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrKiemTraDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100))))
        );

        pnlBanHang.add(CardMuaHang, "CardMuaHang");

        jPanel1.add(pnlBanHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 200, 390, 540));

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

        javax.swing.GroupLayout pnlThanhVienLayout = new javax.swing.GroupLayout(pnlThanhVien);
        pnlThanhVien.setLayout(pnlThanhVienLayout);
        pnlThanhVienLayout.setHorizontalGroup(
            pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhVienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlThanhVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlThanhVienLayout.createSequentialGroup()
                        .addComponent(lblErrKhach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addComponent(txtMaKhach)
                    .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDiemKH, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jButton3))
                .addGap(24, 24, 24))
        );

        jPanel1.add(pnlThanhVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 5, 390, 185));

        jpnQR.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "QR Camera", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N
        jpnQR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jpnQR, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 5, 230, 185));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDanhSachSpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachSpMouseClicked
        int selectedRowSP = tblDanhSachSp.getSelectedRow();
        int selectedRowHD = tblListHoaDon.getSelectedRow();
        GiayChiTiet findGiay = new GiayChiTiet();
        findGiay.setMa((String) tblDanhSachSp.getValueAt(selectedRowSP, 0));
        GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiay); // => done
        System.out.println(giayChiTiet);

        int soLuongGoc = (int) tblDanhSachSp.getValueAt(selectedRowSP, 7);
        System.out.println("" + soLuongGoc);

        int check = JOptionPane.showConfirmDialog(this, "Bỏ Vào Giỏ ! Mua");
        if (check == JOptionPane.YES_OPTION) {
            if (hd != null) { // => Nhớ sử lại
                String soLuongNhap = JOptionPane.showInputDialog(null, "Nhập số lượng");
                try {
                    Integer.parseInt(soLuongNhap);
                    if (Integer.parseInt(soLuongNhap) < 1) {
                        System.out.println("Số lượng phải là số nguyên dương");
                    } else if (Integer.parseInt(soLuongNhap) <= soLuongGoc
                            && selectedRowHD != -1) {
                        Boolean isDuplicate = false;
                        // xu ly trung sp
                        for (HoaDonChiTiet item : hoaDonChiTietRepository.selectAll(hd.getId())) {
                            if (item.getGiayChiTiet().getId().equalsIgnoreCase(giayChiTiet.getId())) {
                                HoaDonChiTiet updateSL = new HoaDonChiTiet();
                                updateSL.setSoLuong(Integer.parseInt(soLuongNhap) + ((int) tblGioHangCho.getValueAt(selectedRowSP, 7)));
                                updateSL.setTrangThai("Mua");
                                updateSL.setGiayChiTiet(giayChiTiet);
                                updateSL.setHoaDon(hd);
                                hoaDonChiTietRepository.update(updateSL);
                                isDuplicate = true;
                            }
                        }
                        // xử lý thêm vào giỏ hàng
                        if (!isDuplicate) {
                            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                            hoaDonChiTiet.setGiayChiTiet(giayChiTiet);
                            hoaDonChiTiet.setHoaDon(hd);
                            hoaDonChiTiet.setSoLuong(Integer.parseInt(soLuongNhap));
                            hoaDonChiTiet.setGia(giayChiTiet.getGia());
                            hoaDonChiTiet.setTrangThai("Mua hang");
                            hoaDonChiTietRepository.insert(hoaDonChiTiet);
                        }

                        // them thanh cong
                        // xu ly cong tru soLuong trong DSSP
                        int soLuongCu = (int) tblDanhSachSp.getValueAt(selectedRowSP, 7);
                        int soLuongMoi = soLuongCu - Integer.parseInt(soLuongNhap);
                        giayChiTietRepository.updateSoLuong(soLuongMoi, giayChiTiet.getMa());

                        showDataGH(hd.getId());
                        showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
                        tinhVaThemTongTien(9);
                    } else {
                        System.out.println("Không đủ hàng");
                    }

                } catch (Exception e) {
                    System.out.println("Số lượng phải là số nguyên dương");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn hóa đơn");
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

    private void tblGioHangChoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangChoMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedRowGH = tblGioHangCho.getSelectedRow();
            int messegertype = JOptionPane.QUESTION_MESSAGE;
            String[] obtion = {"Thay đổi số lượng", "Huỷ"};
            HoaDonChiTiet h = hoaDonChiTietRepository.selectAll(hd.getId()).get(selectedRowGH);

            GiayChiTiet giayChiTiet = h.getGiayChiTiet();

            int code = JOptionPane.showOptionDialog(this, "BẠN MUỐN LÀM GÌ ?", "LỰA CHỌN", 0, messegertype, null, obtion, "Số Lượng");
            switch (code) {
                case 0 -> {
                    String soLuongNhap = JOptionPane.showInputDialog("Nhập Số Lượng");
                    try {
                        int soLuongMoi = Integer.parseInt(soLuongNhap);
                        HoaDonChiTiet updateSL = new HoaDonChiTiet();
                        updateSL.setSoLuong(soLuongMoi);
                        updateSL.setTrangThai("Mua");
                        updateSL.setGiayChiTiet(giayChiTiet);
                        updateSL.setHoaDon(hd);
                        hoaDonChiTietRepository.update(updateSL);
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

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnTroVeBanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTroVeBanHangActionPerformed
        card.show(pnlBanHang, "CardMuaHang");
    }//GEN-LAST:event_btnTroVeBanHangActionPerformed

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

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        int i = tblListHoaDon.getSelectedRow();
        if (i >= 0) {
            int check = JOptionPane.showConfirmDialog(this, "Huỷ Hoá Đơn");
            if (check == JOptionPane.YES_OPTION) {
                xoaSPGH(hd.getId());
                lblTongTien.setText("0");
                lblMaHoaDon.setText(null);
                txtMaHoaDon2.setText("0");
                hoaDonRepository.delete(hd);
                showDataHoaDon(hoaDonRepository.selectAllCho());
                showDataGH(hd.getId());
                showDataSanPham(giayChiTietRepository.selectAll(), currentPage);
            } else {
                JOptionPane.showMessageDialog(this, "Muốn hủy hóa đơn nào cơ ?");
            }
        }
    }//GEN-LAST:event_btnHuyActionPerformed

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

    private void btnVaoDatHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVaoDatHangActionPerformed
        card.show(pnlBanHang, "cardDatHang");
    }//GEN-LAST:event_btnVaoDatHangActionPerformed

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DangKyThanhVienForm t = new DangKyThanhVienForm();
        t.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(HoaDonPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoaDonPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoaDonPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoaDonPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HoaDonPanel().setVisible(true);
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
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnTroVeBanHang;
    private javax.swing.JButton btnVaoDatHang;
    private javax.swing.JPanel cardDatHang;
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
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
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
    private javax.swing.JLabel lblTongTien1;
    private javax.swing.JPanel pnlBanHang;
    private javax.swing.JPanel pnlDSHD;
    private javax.swing.JPanel pnlDSSP;
    private javax.swing.JPanel pnlGioHang;
    private javax.swing.JPanel pnlThanhVien;
    private javax.swing.JTable tblDanhSachSp;
    private javax.swing.JTable tblGioHangCho;
    private javax.swing.JTable tblListHoaDon;
    private javax.swing.JTextField txtMaHoaDon2;
    private javax.swing.JTextField txtMaKhach;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTienKhachDua;
    // End of variables declaration//GEN-END:variables
}
