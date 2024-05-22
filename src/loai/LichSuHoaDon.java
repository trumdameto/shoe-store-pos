package loai;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.GiayChiTiet;
import model.HoaDon;
import model.HoaDonChiTiet;
import repository.GiayChiTietRepository;
import repository.HoaDonChiTietRepository;
import repository.HoaDonRepository;
import repository.JOPane;
import view.DoiTra;
import view.DoiTra;

public class LichSuHoaDon extends javax.swing.JFrame {

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

    public LichSuHoaDon() {
        setLocationRelativeTo(null);
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
        this.tblHD.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.tblHD.getColumnModel().getColumn(1).setPreferredWidth(75);
        this.tblHD.getColumnModel().getColumn(2).setPreferredWidth(70);
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

    private void showDataHDCT(List<HoaDonChiTiet> list, int page) {
        dtmHDCT.setRowCount(0);
        int stt = 1;
        for (HoaDonChiTiet hoaDonChiTiet : list) {
            if (hoaDonChiTiet.getGiayChiTiet() != null) {
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHD = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHDCT = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtMaKH = new javax.swing.JTextField();
        lblTrang = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtNgayBD = new javax.swing.JTextField();
        txtNgayKT = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblMa = new javax.swing.JLabel();
        lblTen = new javax.swing.JLabel();
        lblHang = new javax.swing.JLabel();
        lblKieuDang = new javax.swing.JLabel();
        lblDanhMuc = new javax.swing.JLabel();
        lblMauSac = new javax.swing.JLabel();
        lblKichCo = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblErrorNgay = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        btnDoiTra = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 51));
        jLabel1.setText("Hóa đơn chi tiết");

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

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 51));
        jLabel2.setText("Quản lý hóa đơn");

        txtMaNV.setBackground(new java.awt.Color(240, 240, 240));
        txtMaNV.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMaNV.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MaNV", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });

        txtMaKH.setBackground(new java.awt.Color(240, 240, 240));
        txtMaKH.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMaKH.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MaKH", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        lblTrang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTrang.setText("Trang");

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText(">");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("<");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Tìm");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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

        txtNgayKT.setBackground(new java.awt.Color(240, 240, 240));
        txtNgayKT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtNgayKT.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "NgayKT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtNgayKT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayKTActionPerformed(evt);
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

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
                    .addComponent(lblKichCo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(22, 22, 22)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblErrorNgay.setForeground(new java.awt.Color(255, 0, 0));
        lblErrorNgay.setText(" ");

        txtMaHD.setBackground(new java.awt.Color(240, 240, 240));
        txtMaHD.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtMaHD.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MaHD", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHDActionPerformed(evt);
            }
        });

        txtSDT.setBackground(new java.awt.Color(240, 240, 240));
        txtSDT.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtSDT.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SDT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });

        btnDoiTra.setBackground(new java.awt.Color(0, 0, 0));
        btnDoiTra.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDoiTra.setForeground(new java.awt.Color(255, 255, 255));
        btnDoiTra.setText("Đổi trả");
        btnDoiTra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiTraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDoiTra))))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDoiTra))
                        .addGap(5, 5, 5)
                        .addComponent(lblErrorNgay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaNV)
                            .addComponent(jButton3)
                            .addComponent(txtSDT))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTrang)
                            .addComponent(jButton2)
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNVActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        next();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        prev();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHDMouseClicked
        // TODO add your handling code here:
        int selected = tblHD.getSelectedRow();
        if (selected >= 0) {
            String maHD = (String) tblHD.getValueAt(selected, 0);
            HoaDon findHoaDon = new HoaDon();
            findHoaDon.setMaHoaDon(maHD);
            HoaDon hoaDon = hoaDonRepository.selectbyId(findHoaDon);
            if (hoaDon != null) {
                List<HoaDonChiTiet> listHDCT = hoaDonChiTietRepository.selectAll(hoaDon.getId());
                showDataHDCT(listHDCT, 1);
            }
        }
    }//GEN-LAST:event_tblHDMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        showDataHD(search(), currentPageHD);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtNgayBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayBDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayBDActionPerformed

    private void txtNgayKTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayKTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayKTActionPerformed

    private void tblHDCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHDCTMouseClicked
        // TODO add your handling code here:
        int selected = tblHDCT.getSelectedRow();
        detailGiay(selected);
    }//GEN-LAST:event_tblHDCTMouseClicked

    private void txtMaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHDActionPerformed

    private void txtSDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSDTActionPerformed

    private void btnDoiTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiTraActionPerformed
        // TODO add your handling code here:
        int selected = tblHD.getSelectedRow();
        if (selected >= 0) {
            String maHD = (String) tblHD.getValueAt(selected, 0);
            HoaDon findHoaDon = new HoaDon();
            findHoaDon.setMaHoaDon(maHD);
            HoaDon hoaDon = hoaDonRepository.selectbyId(findHoaDon);
            Date now = new Date();
            if (now.getTime() / 1000 - hoaDon.getNgayTao().getTime() / 1000 < 259200) {
                DoiTra doiTra = new DoiTra(hoaDon);
                doiTra.setVisible(true);
            } else {
                JOPane.showMessageDialog(this, "Hóa đơn đã quá hạn đổi trả");
            }
        } else {
            JOPane.showMessageDialog(this, "Bạn cần chọn hóa đơn để đổi trả");
        }
    }//GEN-LAST:event_btnDoiTraActionPerformed

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
                if ("Window".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LichSuHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LichSuHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LichSuHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LichSuHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LichSuHoaDon().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoiTra;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
