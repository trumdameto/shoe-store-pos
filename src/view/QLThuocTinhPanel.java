package view;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.DanhMuc;
import model.Hang;
import model.KichCo;
import model.KieuDang;
import model.MauSac;
import repository.DanhMucRepository;
import repository.HangRepository;
import repository.KichCoRepository;
import repository.KieuDangRepository;
import repository.MauSacRepository;

public class QLThuocTinhPanel extends javax.swing.JPanel {

    private DefaultTableModel dtm = new DefaultTableModel();
    private List<DanhMuc> listDanhMuc = new ArrayList<>();
    private List<KichCo> listKichCo = new ArrayList<>();
    private List<KieuDang> listKieuDang = new ArrayList<>();
    private List<MauSac> listMauSac = new ArrayList<>();
    private List<Hang> listHang = new ArrayList<>();
    
    private final DanhMucRepository danhMucRepository = new DanhMucRepository();
    private final KichCoRepository kichCoRepository = new KichCoRepository();
    private final KieuDangRepository kieuDangRepository = new KieuDangRepository();
    private final MauSacRepository mauSacRepository = new MauSacRepository();
    private final HangRepository hangRepository = new HangRepository();
    
    private Integer currentPage = 1;
    private final Integer rowCountPage = 5;
    private Integer totalPage = 1;
    
    public QLThuocTinhPanel() {
        initComponents();
        dtm = (DefaultTableModel) tblDanhSach.getModel();
        loadDataDanhMuc(danhMucRepository.selectAll(), currentPage);
        initLoadTable();
        setOpaque(false);
    }

    // Phương thức load dữ liệu lên table theo thuộc tính
    private void loadDataDanhMuc(List<DanhMuc> list, int page) {
        dtm.setRowCount(0);
        int totalItem = list.size();
        int limit = page * rowCountPage;
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = (totalItem / rowCountPage);
        }
        lblPage.setText("Trang " + currentPage + "/" + totalPage);
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            DanhMuc get = list.get(start);
            dtm.addRow(new Object[]{
                get.getMa(),
                get.getName()
            });
            if (start + 1 == limit) {
                return;
            }
        }
    }
    
    private void loadDataHang(List<Hang> list, int page) {
        dtm.setRowCount(0);
        int totalItem = list.size();
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = (totalItem / rowCountPage);
        }
        int limit = page * rowCountPage;
        lblPage.setText("Trang " + currentPage + "/" + totalPage);
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            Hang get = list.get(start);
            dtm.addRow(new Object[]{
                get.getMa(),
                get.getName()
            });
            if (start + 1 == limit) {
                return;
            }
        }
    }
    
    private void loadDataMauSac(List<MauSac> list, int page) {
        dtm.setRowCount(0);
        int totalItem = list.size();
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = (totalItem / rowCountPage);
        }
        int limit = page * rowCountPage;
        lblPage.setText("Trang " + currentPage + "/" + totalPage);
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            MauSac get = list.get(start);
            dtm.addRow(new Object[]{
                get.getMa(),
                get.getName()
            });
            if (start + 1 == limit) {
                return;
            }
        }
    }
    
    private void loadDataKichThuoc(List<KichCo> list, int page) {
        dtm.setRowCount(0);
        int totalItem = list.size();
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = (totalItem / rowCountPage);
        }
        int limit = page * rowCountPage;
        lblPage.setText("Trang " + currentPage + "/" + totalPage);
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            KichCo get = list.get(start);
            dtm.addRow(new Object[]{
                get.getMa(),
                get.getName()
            });
            if (start + 1 == limit) {
                return;
            }
        }
    }
    
    private void loadDataKieuDang(List<KieuDang> list, int page) {
        dtm.setRowCount(0);
        int totalItem = list.size();
        int limit = page * rowCountPage;
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = (totalItem / rowCountPage);
        }
        lblPage.setText("Trang " + currentPage + "/" + totalPage);
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            KieuDang get = list.get(start);
            dtm.addRow(new Object[]{
                get.getMa(),
                get.getName()
            });
            if (start + 1 == limit) {
                return;
            }
        }
    }
    
    private void clearForm() {
        txtTen.setText("");
    }

    // Chọn rdo show thuộc tính
    private void initLoadTable() {
        rdoKichCo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    clearForm();
                    currentPage = 1;
                    loadDataKichThuoc(kichCoRepository.selectAll(), currentPage);
                }
            }
        });
        
        rdoKieuDang.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    clearForm();
                    currentPage = 1;
                    loadDataKieuDang(kieuDangRepository.selectAll(), currentPage);
                }
            }
        });
        
        rdoMauSac.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    clearForm();
                    currentPage = 1;
                    loadDataMauSac(mauSacRepository.selectAll(), currentPage);
                }
            }
        });
        
        rdoDanhMuc.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    clearForm();
                    currentPage = 1;
                    loadDataDanhMuc(danhMucRepository.selectAll(), currentPage);
                }
            }
        });
        
        rdoHang.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    clearForm();
                    currentPage = 1;
                    loadDataHang(hangRepository.selectAll(), currentPage);
                }
            }
        });
    }
    
    private Object getDataForm() {
        String name = txtTen.getText();
        Date date = new Date();
        Long ngayTao = date.getTime() / 1000;
        
        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setName(name);
        danhMuc.setNgayTao(ngayTao);
        
        KichCo kichCo = new KichCo();
        kichCo.setName(name);
        kichCo.setNgayTao(ngayTao);
        
        MauSac mauSac = new MauSac();
        mauSac.setName(name);
        mauSac.setNgayTao(ngayTao);
        
        Hang hang = new Hang();
        hang.setName(name);
        hang.setNgayTao(ngayTao);
        
        KieuDang kieuDang = new KieuDang();
        kieuDang.setName(name);
        kieuDang.setNgayTao(ngayTao);
        
        if (rdoDanhMuc.isSelected()) {
            return danhMuc;
        } else if (rdoHang.isSelected()) {
            return hang;
        } else if (rdoKichCo.isSelected()) {
            return kichCo;
        } else if (rdoKieuDang.isSelected()) {
            return kieuDang;
        } else {
            return mauSac;
        }
    }
    
    private Object getDataTable() {
        int selected = tblDanhSach.getSelectedRow();
        String ma = (String) tblDanhSach.getValueAt(selected, 0);
        
        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setMa(ma);
        
        KichCo kichCo = new KichCo();
        kichCo.setMa(ma);
        
        MauSac mauSac = new MauSac();
        mauSac.setMa(ma);
        
        Hang hang = new Hang();
        hang.setMa(ma);
        
        KieuDang kieuDang = new KieuDang();
        kieuDang.setMa(ma);
        
        if (rdoDanhMuc.isSelected()) {
            return danhMucRepository.selectbyId(danhMuc);
        } else if (rdoHang.isSelected()) {
            return hangRepository.selectbyId(hang);
        } else if (rdoKichCo.isSelected()) {
            return kichCoRepository.selectbyId(kichCo);
        } else if (rdoKieuDang.isSelected()) {
            return kieuDangRepository.selectbyId(kieuDang);
        } else {
            return mauSacRepository.selectbyId(mauSac);
        }
    }
    
    private void detail() {
        if (rdoDanhMuc.isSelected()) {
            DanhMuc danhMuc = (DanhMuc) getDataTable();
            txtTen.setText(danhMuc.getName());
            lblMa.setText("Mã:                     " + danhMuc.getMa());
        } else if (rdoHang.isSelected()) {
            Hang hang = (Hang) getDataTable();
            txtTen.setText(hang.getName());
            lblMa.setText("Mã:                     " + hang.getMa());
        } else if (rdoKichCo.isSelected()) {
            KichCo kichCo = (KichCo) getDataTable();
            txtTen.setText(kichCo.getName());
            lblMa.setText("Mã:                     " + kichCo.getMa());
        } else if (rdoKieuDang.isSelected()) {
            KieuDang kieuDang = (KieuDang) getDataTable();
            txtTen.setText(kieuDang.getName());
            lblMa.setText("Mã:                     " + kieuDang.getMa());
        } else {
            MauSac mauSac = (MauSac) getDataTable();
            txtTen.setText(mauSac.getName());
            lblMa.setText("Mã:                     " + mauSac.getMa());
        }
    }
    
    private String them() {
        int row = -1;
        if (getDataForm() != null) {
            if (rdoDanhMuc.isSelected()) {
                DanhMuc danhMuc = (DanhMuc) getDataForm();
                row = danhMucRepository.insert(danhMuc);
                loadDataDanhMuc(danhMucRepository.selectAll(), currentPage);
            } else if (rdoHang.isSelected()) {
                Hang hang = (Hang) getDataForm();
                row = hangRepository.insert(hang);
                loadDataHang(hangRepository.selectAll(), currentPage);
            } else if (rdoKichCo.isSelected()) {
                KichCo kichCo = (KichCo) getDataForm();
                row = kichCoRepository.insert(kichCo);
                loadDataKichThuoc(kichCoRepository.selectAll(), currentPage);
            } else if (rdoKieuDang.isSelected()) {
                KieuDang kieuDang = (KieuDang) getDataForm();
                row = kieuDangRepository.insert(kieuDang);
                loadDataKieuDang(kieuDangRepository.selectAll(), currentPage);
            } else {
                MauSac mauSac = (MauSac) getDataForm();
                row = mauSacRepository.insert(mauSac);
                loadDataMauSac(mauSacRepository.selectAll(), currentPage);
            }
        }
        if (row == 1) {
            return "Thêm thành công";
        }
        return "Thêm thất bại";
    }
    
    private String sua() {
        int row = -1;
        if (tblDanhSach.getSelectedRow() < 0) {
            return "Vui lòng chọn thuộc tính trong bảng để sửa";
        }
        String name = txtTen.getText();
        if (getDataTable() != null) {
            if (rdoDanhMuc.isSelected()) {
                DanhMuc danhMuc = (DanhMuc) getDataTable();
                danhMuc.setName(name);
                row = danhMucRepository.update(danhMuc);
                loadDataDanhMuc(danhMucRepository.selectAll(), currentPage);
            } else if (rdoHang.isSelected()) {
                Hang hang = (Hang) getDataTable();
                hang.setName(name);
                row = hangRepository.update(hang);
                loadDataHang(hangRepository.selectAll(), currentPage);
            } else if (rdoKichCo.isSelected()) {
                KichCo kichCo = (KichCo) getDataTable();
                kichCo.setName(name);
                row = kichCoRepository.update(kichCo);
                loadDataKichThuoc(kichCoRepository.selectAll(), currentPage);
            } else if (rdoKieuDang.isSelected()) {
                KieuDang kieuDang = (KieuDang) getDataTable();
                kieuDang.setName(name);
                row = kieuDangRepository.update(kieuDang);
                loadDataKieuDang(kieuDangRepository.selectAll(), currentPage);
            } else {
                MauSac mauSac = (MauSac) getDataTable();
                mauSac.setName(name);
                row = mauSacRepository.update(mauSac);
                loadDataMauSac(mauSacRepository.selectAll(), currentPage);
            }
        }
        if (row == 1) {
            return "Sửa thành công";
        }
        return "Sửa thất bại";
    }
    
    private void search() {
        String search = txtSearch.getText().toLowerCase();
        List<DanhMuc> lDanhMuc = new ArrayList<>();
        List<KichCo> lKichCo = new ArrayList<>();
        List<MauSac> lMauSac = new ArrayList<>();
        List<KieuDang> lKieuDang = new ArrayList<>();
        List<Hang> lHang = new ArrayList<>();
        for (DanhMuc danhMuc : danhMucRepository.selectAll()) {
            if (danhMuc.getMa().toLowerCase().contains(search)
                    || danhMuc.getName().toLowerCase().contains(search)) {
                lDanhMuc.add(danhMuc);
            }
        }
        for (KichCo kichCo : kichCoRepository.selectAll()) {
            if (kichCo.getMa().toLowerCase().contains(search)
                    || kichCo.getName().toLowerCase().contains(search)) {
                lKichCo.add(kichCo);
            }
        }
        for (KieuDang kieuDang : kieuDangRepository.selectAll()) {
            if (kieuDang.getMa().toLowerCase().contains(search)
                    || kieuDang.getName().toLowerCase().contains(search)) {
                lKieuDang.add(kieuDang);
            }
        }
        for (MauSac mauSac : mauSacRepository.selectAll()) {
            if (mauSac.getMa().toLowerCase().contains(search)
                    || mauSac.getName().toLowerCase().contains(search)) {
                lMauSac.add(mauSac);
            }
        }
        for (Hang hang : hangRepository.selectAll()) {
            if (hang.getMa().toLowerCase().contains(search)
                    || hang.getName().toLowerCase().contains(search)) {
                lHang.add(hang);
            }
        }
        
        if (rdoDanhMuc.isSelected()) {
            loadDataDanhMuc(lDanhMuc, currentPage);
        } else if (rdoKichCo.isSelected()) {
            loadDataKichThuoc(lKichCo, currentPage);
        } else if (rdoKieuDang.isSelected()) {
            loadDataKieuDang(lKieuDang, currentPage);
        } else if (rdoHang.isSelected()) {
            loadDataHang(lHang, currentPage);
        } else {
            loadDataMauSac(lMauSac, currentPage);
        }
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblMa = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        lblErrorTen = new javax.swing.JLabel();
        rdoDanhMuc = new javax.swing.JRadioButton();
        rdoMauSac = new javax.swing.JRadioButton();
        rdoHang = new javax.swing.JRadioButton();
        rdoKichCo = new javax.swing.JRadioButton();
        rdoKieuDang = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSach = new javax.swing.JTable();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        lblPage = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(1200, 750));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản lý Thuộc Tính Sản Phẩm");

        lblMa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblMa.setText("Mã: ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Tên: ");

        txtTen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btnThem.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        lblErrorTen.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblErrorTen.setText(" ");

        buttonGroup1.add(rdoDanhMuc);
        rdoDanhMuc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoDanhMuc.setSelected(true);
        rdoDanhMuc.setText("DanhMuc");

        buttonGroup1.add(rdoMauSac);
        rdoMauSac.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoMauSac.setText("MauSac");

        buttonGroup1.add(rdoHang);
        rdoHang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoHang.setText("Hang");

        buttonGroup1.add(rdoKichCo);
        rdoKichCo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoKichCo.setText("KichCo");

        buttonGroup1.add(rdoKieuDang);
        rdoKieuDang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rdoKieuDang.setText("KieuDang");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Thuộc tính");

        tblDanhSach.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDanhSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã", "Tên"
            }
        ));
        tblDanhSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDanhSach);

        btnNext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        lblPage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblPage.setText("Trang");

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Search:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(btnPrev)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(21, 21, 21)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblMa, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblErrorTen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTen)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(btnThem)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(207, 207, 207)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(rdoKieuDang)
                                .addGap(27, 27, 27)
                                .addComponent(rdoHang)
                                .addGap(28, 28, 28)
                                .addComponent(rdoKichCo))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(90, 90, 90))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(rdoDanhMuc)
                                .addGap(29, 29, 29)
                                .addComponent(rdoMauSac)
                                .addGap(36, 36, 36)))))
                .addContainerGap(90, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnNext, btnPrev});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(25, 25, 25)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoDanhMuc)
                            .addComponent(rdoMauSac))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoHang)
                            .addComponent(rdoKieuDang)
                            .addComponent(rdoKichCo)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblMa)
                        .addGap(25, 25, 25)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorTen)
                        .addGap(7, 7, 7)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnThem)
                            .addComponent(btnSua))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(69, 69, 69)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNext)
                    .addComponent(btnPrev)
                    .addComponent(lblPage))
                .addGap(22, 22, 22))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 698, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, them());
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        sua();
    }//GEN-LAST:event_btnSuaActionPerformed

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

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblErrorTen;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblPage;
    private javax.swing.JRadioButton rdoDanhMuc;
    private javax.swing.JRadioButton rdoHang;
    private javax.swing.JRadioButton rdoKichCo;
    private javax.swing.JRadioButton rdoKieuDang;
    private javax.swing.JRadioButton rdoMauSac;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTen;
    // End of variables declaration//GEN-END:variables
}
