package view;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Image;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import model.DanhMuc;
import model.Giay;
import model.GiayChiTiet;
import model.Hang;
import model.KichCo;
import model.KieuDang;
import model.MauSac;
import repository.DanhMucRepository;
import repository.GiayChiTietRepository;
import repository.HangRepository;
import repository.KichCoRepository;
import repository.KieuDangRepository;
import repository.MauSacRepository;

public class ChiTietGiay extends javax.swing.JFrame {

    private final DanhMucFrame danhMucFrame = new DanhMucFrame();
    private final HangFrame hangFrame = new HangFrame();
    private final KieuDangFrame kieuDangFrame = new KieuDangFrame();
    private final MauSacFrame mauSacFrame = new MauSacFrame();
    private final KichCoFrame kichCoFrame = new KichCoFrame();

    private DefaultTableModel dtm = new DefaultTableModel();

    private final DanhMucRepository danhMucRepository = new DanhMucRepository();
    private final KichCoRepository kichCoRepository = new KichCoRepository();
    private final KieuDangRepository kieuDangRepository = new KieuDangRepository();
    private final MauSacRepository mauSacRepository = new MauSacRepository();
    private final HangRepository hangRepository = new HangRepository();
    private final GiayChiTietRepository giayChiTietRepository = new GiayChiTietRepository();

    private final Giay giayData;
    private Integer currentPage = 1;
    private final Integer rowCountPage = 5;
    private Integer totalPage;
    private String urlAnh = "";

    private String danhMucSelected = "";
    private String hangSelected = "";
    private String kieuDangSelected = "";
    private String mauSacSelected = "";
    private String kichCoSelected = "";

    public ChiTietGiay(Giay giay) {
        initComponents();
        giayData = giay;
        setSize(1200, 750);
        setLocation(260, 70);
        showPanel(pnlForm);
        setCbo();
        confiColumn();
        lblMaTen.setText(giay.getMa() + " - " + giay.getName());
        dtm = (DefaultTableModel) tblDanhSach.getModel();
        loadDataTable(giayChiTietRepository.selectByGiay(giay), currentPage);
    }

    private void confiColumn() {
        tblDanhSach.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblDanhSach.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblDanhSach.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblDanhSach.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblDanhSach.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblDanhSach.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblDanhSach.getColumnModel().getColumn(6).setPreferredWidth(80);
        tblDanhSach.getColumnModel().getColumn(7).setPreferredWidth(60);
        tblDanhSach.getColumnModel().getColumn(8).setPreferredWidth(60);
        tblDanhSach.getColumnModel().getColumn(9).setPreferredWidth(90);
    }

    private void showPanel(JPanel panel) {
        pnlForm.setVisible(false);
        pnlBoLoc.setVisible(false);
        panel.setVisible(true);
    }

    private void showAnh(String icon) {
        ImageIcon imageIcon = new ImageIcon(icon);
        Image newImage = imageIcon.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newImage);
        urlAnh = icon;
        lblAnh.setIcon(newIcon);
    }

    // load dữ liệu combobox
    private void setDataDanhMuc() {
        cboDanhMuc.removeAllItems();
        cboDanhMuc1.removeAllItems();
        cboDanhMuc1.addItem("");
        for (DanhMuc danhMuc : danhMucRepository.selectAll()) {
            cboDanhMuc.addItem(danhMuc.getName());
            cboDanhMuc1.addItem(danhMuc.getName());
        }
    }

    private void setDataHang() {
        cboHang.removeAllItems();
        cboHang1.removeAllItems();
        cboHang1.addItem("");
        for (Hang hang : hangRepository.selectAll()) {
            cboHang.addItem(hang.getName());
            cboHang1.addItem(hang.getName());
        }
    }

    private void setDataKichCo() {
        cboKichCo.removeAllItems();
        cboKichCo1.removeAllItems();
        cboKichCo1.addItem("");
        for (KichCo kichCo : kichCoRepository.selectAll()) {
            cboKichCo.addItem(kichCo.getName());
            cboKichCo1.addItem(kichCo.getName());
        }
    }

    private void setDataKieuDang() {
        cboKieuDang.removeAllItems();
        cboKieuDang1.removeAllItems();
        cboKieuDang1.addItem("");
        for (KieuDang kieuDang : kieuDangRepository.selectAll()) {
            cboKieuDang.addItem(kieuDang.getName());
            cboKieuDang1.addItem(kieuDang.getName());
        }
    }

    private void setDataMauSac() {
        cboMauSac.removeAllItems();
        cboMauSac1.removeAllItems();
        cboMauSac1.addItem("");
        for (MauSac mauSac : mauSacRepository.selectAll()) {
            cboMauSac.addItem(mauSac.getName());
            cboMauSac1.addItem(mauSac.getName());
        }
    }

    private void setDataTrangThai() {
        cboTrangThai.removeAllItems();
        String[] trangThai = {"Còn hàng", "Hết hàng"};
        for (int i = 0; i < trangThai.length; i++) {
            String string = trangThai[i];
            cboTrangThai.addItem(string);
        }
    }

    private void setCbo() {
        setDataDanhMuc();
        setDataHang();
        setDataKichCo();
        setDataKieuDang();
        setDataMauSac();
        setDataTrangThai();
    }

    private void loadDataTable(List<GiayChiTiet> list, int page) {
        dtm.setRowCount(0);
        int totalItem = list.size();
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = totalItem / rowCountPage;
        }
        if (totalPage <= 1) {
            currentPage = 1;
        }
        lblTrang.setText("Trang " + currentPage + "/" + totalPage);
        int limit = page * rowCountPage;
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            GiayChiTiet item = list.get(start);
            dtm.addRow(new Object[]{
                item.getMa(),
                item.getGiay().getName(),
                item.getHang().getName(),
                item.getKieuDang().getName(),
                item.getDanhMuc().getName(),
                item.getMauSac().getName(),
                item.getKichCo().getName(),
                item.getGia(),
                item.getSoLuong(),
                item.getTrangThai()
            });
            if (start + 1 == limit) {
                return;
            }
        }
    }

    private GiayChiTiet getDataForm() {
        Hang hang = null;
        KieuDang kieuDang = null;
        DanhMuc danhMuc = null;
        MauSac mauSac = null;
        KichCo kichCo = null;
        String gia = txtGia.getText();
        String soLuong = txtSoLuong.getText();
        String trangThai = "Còn hàng";
        String moTa = txtMoTa.getText();
        BigDecimal dongia = BigDecimal.ZERO;
        int soluong = 0;

        if (soLuong.isEmpty()) {
            txtSoLuong.requestFocus();
            lblErrorSoLuong.setText("Nhập số lượng");
            return null;
        } else {
            try {
                soluong = Integer.parseInt(soLuong);
                if (soluong <= 0) {
                    txtSoLuong.requestFocus();
                    lblErrorSoLuong.setText("Số lượng lớn hơn 0");
                    return null;
                } else {
                    lblErrorSoLuong.setText(" ");
                }
            } catch (NumberFormatException e) {
                txtSoLuong.requestFocus();
                lblErrorSoLuong.setText("Số lượng là một số nguyên");
                return null;
            }
        }

        if (gia.isEmpty()) {
            txtGia.requestFocus();
            lblErrorSoLuong1.setText("Nhập giá");
            return null;
        } else {
            try {
                Double dongiaDouble = Double.parseDouble(gia);
                dongia = BigDecimal.valueOf(dongiaDouble);
                if (dongia.compareTo(BigDecimal.ZERO) <= 0) {
                    txtGia.requestFocus();
                    lblErrorSoLuong1.setText("Giá phải lớn hơn 0");
                    return null;
                } else {
                    lblErrorSoLuong1.setText(" ");
                }
            } catch (NumberFormatException e) {
                txtGia.requestFocus();
                lblErrorSoLuong1.setText("Giá phải là một số thập phân");
                return null;
            }
        }

        for (int i = 0; i < danhMucRepository.selectAll().size(); i++) {
            DanhMuc get = danhMucRepository.selectAll().get(i);
            if (i == cboDanhMuc.getSelectedIndex()) {
                danhMuc = get;
            }
        }
        for (int i = 0; i < kieuDangRepository.selectAll().size(); i++) {
            KieuDang get = kieuDangRepository.selectAll().get(i);
            if (i == cboKieuDang.getSelectedIndex()) {
                kieuDang = get;
            }
        }
        for (int i = 0; i < mauSacRepository.selectAll().size(); i++) {
            MauSac get = mauSacRepository.selectAll().get(i);
            if (i == cboMauSac.getSelectedIndex()) {
                mauSac = get;
            }
        }
        for (int i = 0; i < kichCoRepository.selectAll().size(); i++) {
            KichCo get = kichCoRepository.selectAll().get(i);
            if (i == cboKichCo.getSelectedIndex()) {
                kichCo = get;
            }
        }
        for (int i = 0; i < hangRepository.selectAll().size(); i++) {
            Hang get = hangRepository.selectAll().get(i);
            if (i == cboHang.getSelectedIndex()) {
                hang = get;
            }
        }
        if (giayData == null) {
            return null;
        }
        Long ngayTao = new Date().getTime() / 1000;
        GiayChiTiet giayChiTiet = new GiayChiTiet();
        giayChiTiet.setGiay(giayData);
        giayChiTiet.setDanhMuc(danhMuc);
        giayChiTiet.setHang(hang);
        giayChiTiet.setHinhAnh(urlAnh);
        giayChiTiet.setKichCo(kichCo);
        giayChiTiet.setKieuDang(kieuDang);
        giayChiTiet.setMauSac(mauSac);
        giayChiTiet.setGia(dongia);
        giayChiTiet.setSoLuong(soluong);
        giayChiTiet.setNgayTao(ngayTao);
        giayChiTiet.setTrangThai(trangThai);
        giayChiTiet.setMoTa(moTa);
        return giayChiTiet;
    }

    private List<GiayChiTiet> searchByText(String search) {
        List<GiayChiTiet> listSearch = new ArrayList<>();
        for (GiayChiTiet o : giayChiTietRepository.selectByGiay(giayData)) {
            String dm = o.getDanhMuc().getName().toLowerCase();
            String ha = o.getHang().getName().toLowerCase();
            String kd = o.getKieuDang().getName().toLowerCase();
            String ms = o.getMauSac().getName().toLowerCase();
            String kc = o.getKichCo().getName().toLowerCase();

            String ma = (o.getMa() + "").toLowerCase();
            String gia = (o.getGia() + "").toLowerCase();
            if ((ma.contains(search) || gia.contains(search)
                    || dm.contains(search) || ha.contains(search) || kd.contains(search)
                    || ms.contains(search) || kc.contains(search))) {
                listSearch.add(o);
            }
        }
        return listSearch;
    }

    private List<GiayChiTiet> searchBoLoc(String search) {
        List<GiayChiTiet> listSearch = new ArrayList<>();
        List<GiayChiTiet> list = giayChiTietRepository.selectByGiay(giayData);
        for (GiayChiTiet o : list) {
            String dm = o.getDanhMuc().getName().toLowerCase();
            String ha = o.getHang().getName().toLowerCase();
            String kd = o.getKieuDang().getName().toLowerCase();
            String ms = o.getMauSac().getName().toLowerCase();
            String kc = o.getKichCo().getName().toLowerCase();

            String ma = (o.getMa() + "").toLowerCase();
            String gia = (o.getGia() + "").toLowerCase();
            String trangThai = (o.getTrangThai() + "").toLowerCase();
            if (dm.contains(danhMucSelected) && ha.contains(hangSelected)
                    && kd.contains(kieuDangSelected) && ms.contains(mauSacSelected)
                    && kc.contains(kichCoSelected) && (ma.contains(search)
                    || gia.contains(search) || trangThai.contains(search))) {
                listSearch.add(o);
            }
        }
        return listSearch;
    }

    private GiayChiTiet getDataTable(int selected) {
        String ma = (String) tblDanhSach.getValueAt(selected, 0);
        GiayChiTiet giayChiTiet = new GiayChiTiet();
        giayChiTiet.setMa(ma);
        return giayChiTietRepository.selectbyId(giayChiTiet);
    }

    private void detail(GiayChiTiet giayChiTiet) {
        cboHang.setSelectedItem(giayChiTiet.getHang().getName());
        cboKieuDang.setSelectedItem(giayChiTiet.getKieuDang().getName());
        cboDanhMuc.setSelectedItem(giayChiTiet.getDanhMuc().getName());
        cboMauSac.setSelectedItem(giayChiTiet.getMauSac().getName());
        cboKichCo.setSelectedItem(giayChiTiet.getKichCo().getName());
        cboTrangThai.setSelectedItem(giayChiTiet.getTrangThai());
        txtSoLuong.setText(giayChiTiet.getSoLuong() + "");
        txtGia.setText(giayChiTiet.getGia() + "");
        txtMoTa.setText(giayChiTiet.getMoTa());
        showAnh(giayChiTiet.getHinhAnh());
    }

    private void btnNext() {
        if (currentPage == totalPage) {
            currentPage = 1;
            String search = txtSearchBL.getText();
            loadDataTable(searchBoLoc(search), currentPage);
        } else {
            currentPage++;
            String search = txtSearchBL.getText();
            loadDataTable(searchBoLoc(search), currentPage);
        }
    }

    private void btnPrev() {
        if (currentPage == 1) {
            currentPage = totalPage;
            String search = txtSearchBL.getText();
            loadDataTable(searchBoLoc(search), currentPage);
        } else {
            currentPage--;
            String search = txtSearchBL.getText();
            loadDataTable(searchBoLoc(search), currentPage);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblMaTen = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlForm = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cboHang = new javax.swing.JComboBox<>();
        btnHang = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cboKieuDang = new javax.swing.JComboBox<>();
        btnKieuDang = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        cboDanhMuc = new javax.swing.JComboBox<>();
        btnDanhMuc = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cboMauSac = new javax.swing.JComboBox<>();
        btnMauSac = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        cboKichCo = new javax.swing.JComboBox<>();
        btnKichCo = new javax.swing.JButton();
        lblSoLuong = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cboTrangThai = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        lblAnh = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        lblEmty = new javax.swing.JLabel();
        lblErrorSoLuong = new javax.swing.JLabel();
        lblSoLuong1 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        lblErrorSoLuong1 = new javax.swing.JLabel();
        pnlBoLoc = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        cboHang1 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cboKieuDang1 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        cboDanhMuc1 = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        cboMauSac1 = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        cboKichCo1 = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnShowThem = new javax.swing.JButton();
        txtSearchBL = new javax.swing.JTextField();
        btnTimKiemBL = new javax.swing.JButton();
        pnlDanhSach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSach = new javax.swing.JTable();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        lblTrang = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txtSearchDS = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(873, 580));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMaTen.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblMaTen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMaTen.setText("Mã - Tên");
        lblMaTen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblMaTen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMaTenMouseClicked(evt);
            }
        });
        jPanel1.add(lblMaTen, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1200, -1));

        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Click để thay đổi Giày");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 1200, -1));

        pnlForm.setBackground(new java.awt.Color(255, 255, 255));
        pnlForm.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 255), new java.awt.Color(51, 51, 255), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        jLabel5.setText("Hãng");

        cboHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboHangMouseClicked(evt);
            }
        });

        btnHang.setText("+");
        btnHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHangActionPerformed(evt);
            }
        });

        jLabel6.setText("Kiểu dáng");

        cboKieuDang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKieuDang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboKieuDangMouseClicked(evt);
            }
        });

        btnKieuDang.setText("+");
        btnKieuDang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKieuDangActionPerformed(evt);
            }
        });

        jLabel7.setText("Danh mục");

        cboDanhMuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboDanhMuc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboDanhMucMouseClicked(evt);
            }
        });

        btnDanhMuc.setText("+");
        btnDanhMuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDanhMucActionPerformed(evt);
            }
        });

        jLabel8.setText("Màu sắc");

        cboMauSac.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMauSac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboMauSacMouseClicked(evt);
            }
        });

        btnMauSac.setText("+");
        btnMauSac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMauSacActionPerformed(evt);
            }
        });

        jLabel9.setText("Kích cỡ");

        cboKichCo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKichCo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboKichCoMouseClicked(evt);
            }
        });

        btnKichCo.setText("+");
        btnKichCo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKichCoActionPerformed(evt);
            }
        });

        lblSoLuong.setText("Số lượng(*)");

        jLabel10.setText("  Trạng thái");

        cboTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboTrangThai.setEnabled(false);

        jLabel11.setText("Mô tả");

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane2.setViewportView(txtMoTa);

        lblAnh.setBackground(new java.awt.Color(255, 255, 255));
        lblAnh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnh.setText("Chọn ảnh");
        lblAnh.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblAnh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhMouseClicked(evt);
            }
        });

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        lblEmty.setForeground(new java.awt.Color(255, 51, 51));
        lblEmty.setText("(*) Bắt buộc nhập liệu !");

        lblErrorSoLuong.setForeground(new java.awt.Color(255, 0, 0));
        lblErrorSoLuong.setText(" ");

        lblSoLuong1.setText("Giá(*)");

        txtGia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGiaKeyReleased(evt);
            }
        });

        lblErrorSoLuong1.setForeground(new java.awt.Color(255, 0, 0));
        lblErrorSoLuong1.setText(" ");

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lblEmty, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(28, 28, 28)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnHang))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(cboKieuDang, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnKieuDang))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(cboDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnDanhMuc))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnMauSac))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(cboKichCo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnKichCo))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(90, 90, 90)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormLayout.createSequentialGroup()
                                    .addComponent(lblSoLuong1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblErrorSoLuong1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(pnlFormLayout.createSequentialGroup()
                                    .addComponent(lblSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblErrorSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jScrollPane2))
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(btnThem)
                        .addGap(1, 1, 1)
                        .addComponent(btnSua)))
                .addGap(97, 97, 97))
        );
        pnlFormLayout.setVerticalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThem)
                            .addComponent(btnSua)))
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblEmty)
                        .addGap(11, 11, 11)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(jLabel6)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel7)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel8)
                                .addGap(21, 21, 21)
                                .addComponent(jLabel9)
                                .addGap(25, 25, 25)
                                .addComponent(jLabel10))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlFormLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(cboHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnHang))
                                .addGap(13, 13, 13)
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlFormLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(cboKieuDang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnKieuDang))
                                .addGap(13, 13, 13)
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlFormLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(cboDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnDanhMuc))
                                .addGap(13, 13, 13)
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlFormLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(cboMauSac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnMauSac))
                                .addGap(13, 13, 13)
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlFormLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(cboKichCo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnKichCo))
                                .addGap(21, 21, 21)
                                .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSoLuong)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addComponent(lblErrorSoLuong)
                        .addGap(12, 12, 12)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSoLuong1)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorSoLuong1)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1.add(pnlForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 1080, 280));

        pnlBoLoc.setBackground(new java.awt.Color(255, 255, 255));
        pnlBoLoc.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 51, 255), new java.awt.Color(51, 51, 255), new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Hãng");

        cboHang1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboHang1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboHang1ItemStateChanged(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Kiểu dáng");

        cboKieuDang1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKieuDang1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboKieuDang1ItemStateChanged(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Danh mục");

        cboDanhMuc1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboDanhMuc1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboDanhMuc1ItemStateChanged(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Màu sắc");

        cboMauSac1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMauSac1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMauSac1ItemStateChanged(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Kích cỡ");

        cboKichCo1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboKichCo1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboKichCo1ItemStateChanged(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Tìm kiếm");

        jLabel18.setText("BỘ LỌC");

        btnShowThem.setText("Trở về");
        btnShowThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowThemActionPerformed(evt);
            }
        });

        btnTimKiemBL.setText("Tìm kiếm");
        btnTimKiemBL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemBLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBoLocLayout = new javax.swing.GroupLayout(pnlBoLoc);
        pnlBoLoc.setLayout(pnlBoLocLayout);
        pnlBoLocLayout.setHorizontalGroup(
            pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBoLocLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlBoLocLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(579, 579, 579))
                    .addGroup(pnlBoLocLayout.createSequentialGroup()
                        .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboDanhMuc1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboMauSac1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(90, 90, 90)
                        .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboHang1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboKichCo1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(90, 90, 90)
                        .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboKieuDang1, 0, 140, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSearchBL)
                            .addComponent(btnTimKiemBL, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnShowThem, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        pnlBoLocLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cboDanhMuc1, cboHang1, cboKichCo1, cboKieuDang1, cboMauSac1, txtSearchBL});

        pnlBoLocLayout.setVerticalGroup(
            pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBoLocLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShowThem))
                .addGap(30, 30, 30)
                .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlBoLocLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboKichCo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBoLocLayout.createSequentialGroup()
                        .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBoLocLayout.createSequentialGroup()
                                .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboHang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboDanhMuc1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlBoLocLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboKieuDang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(24, 24, 24)
                        .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBoLocLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboMauSac1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBoLocLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSearchBL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(btnTimKiemBL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBoLocLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cboDanhMuc1, cboHang1, cboKichCo1, cboKieuDang1, cboMauSac1, txtSearchBL});

        jPanel1.add(pnlBoLoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 1080, 0));

        pnlDanhSach.setBackground(new java.awt.Color(255, 255, 255));
        pnlDanhSach.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153), new java.awt.Color(51, 51, 255), new java.awt.Color(51, 51, 255)));
        pnlDanhSach.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblDanhSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Ma", "Ten", "Hang", "KieuDang", "DanhMuc", "MauSac", "KichCo", "Gia", "SL", "TrangThai"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDanhSach.setAutoscrolls(false);
        tblDanhSach.setRowHeight(18);
        tblDanhSach.setShowHorizontalLines(false);
        tblDanhSach.setShowVerticalLines(false);
        tblDanhSach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDanhSach);

        pnlDanhSach.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 990, 120));

        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        pnlDanhSach.add(btnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 240, 60, -1));

        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });
        pnlDanhSach.add(btnPrev, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 240, 70, -1));

        lblTrang.setText("Trang");
        pnlDanhSach.add(lblTrang, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 210, -1));

        jButton1.setText("BoLoc");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        pnlDanhSach.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 50, -1, -1));

        txtSearchDS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchDSKeyReleased(evt);
            }
        });
        pnlDanhSach.add(txtSearchDS, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 50, 150, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Danh sách");
        pnlDanhSach.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 90, -1));

        jPanel1.add(pnlDanhSach, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 1080, 290));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHangActionPerformed
        hangFrame.setVisible(true);
    }//GEN-LAST:event_btnHangActionPerformed

    private void btnKieuDangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKieuDangActionPerformed
        kieuDangFrame.setVisible(true);
    }//GEN-LAST:event_btnKieuDangActionPerformed

    private void btnDanhMucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDanhMucActionPerformed
        danhMucFrame.setVisible(true);
    }//GEN-LAST:event_btnDanhMucActionPerformed

    private void btnMauSacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMauSacActionPerformed
        mauSacFrame.setVisible(true);
    }//GEN-LAST:event_btnMauSacActionPerformed

    private void btnKichCoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKichCoActionPerformed
        kichCoFrame.setVisible(true);
    }//GEN-LAST:event_btnKichCoActionPerformed

    private void lblAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhMouseClicked
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Lấy đường dẫn của file được chọn
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();

            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                // Kiểm tra kiểu tệp (ví dụ: .jpg, .png)
                String fileExtension = imageFile.getName().substring(imageFile.getName().lastIndexOf("."));
                if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".gif")) {
                    ImageIcon imageIcon = new ImageIcon(imagePath);
                    System.out.println("imageIcon: " + imageIcon);
                    showAnh(imageIcon.toString());
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn hình ảnh!", "Thông báo", JOptionPane.OK_OPTION);
                    System.out.println("Tệp không phải là hình ảnh.");
                }
            } else {
                lblAnh.setIcon(null);
            }
        }
    }//GEN-LAST:event_lblAnhMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        GiayChiTiet giayCT = getDataForm();
        if (giayCT == null) {
            return;
        } else {
            String thongBao = "";
            int check = JOptionPane.showConfirmDialog(this, "Are you sure?", "Meo Meo Confirm", JOptionPane.YES_NO_OPTION);
            if (check == JOptionPane.YES_OPTION) {
                // check Trùng
                boolean isDuplicate = false;
                for (GiayChiTiet item : giayChiTietRepository.selectByGiay(giayData)) {
                    if (item.getDanhMuc().getName().equalsIgnoreCase(giayCT.getDanhMuc().getName())
                            && item.getHang().getName().equalsIgnoreCase(giayCT.getHang().getName())
                            && item.getKieuDang().getName().equalsIgnoreCase(giayCT.getKieuDang().getName())
                            && item.getMauSac().getName().equalsIgnoreCase(giayCT.getMauSac().getName())
                            && item.getKichCo().getName().equalsIgnoreCase(giayCT.getKichCo().getName())) {
                        isDuplicate = true;
                        thongBao = "Sản phẩm đã có với mã là: " + item.getMa();
                    }
                }
                if (!isDuplicate) {
                    int row = giayChiTietRepository.insert(giayCT);
                    if (row == 1) {
                        thongBao = "Thêm thành công";
                        String Qr = giayChiTietRepository.selectByGiay(giayData).get(0).getMa();
                        String path = "D:\\" + Qr + ".jpg";
                        try {
                            BitMatrix matrix = new MultiFormatWriter().encode(Qr, BarcodeFormat.QR_CODE, 200, 200);
                            MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(path));
                        } catch (WriterException ex) {
                            Logger.getLogger(ChiTietGiay.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(ChiTietGiay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        thongBao = "Thêm thất bại";
                    }
                }
                loadDataTable(giayChiTietRepository.selectByGiay(giayData), currentPage);
                JOptionPane.showMessageDialog(this, thongBao);
            }
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int selected = tblDanhSach.getSelectedRow();
        String thongBao = null;
        if (selected == -1) {
            thongBao = "Bạn cần chọn dữ liệu để sửa";
        } else {
            if (getDataForm() == null) {
                return;
            } else {
                GiayChiTiet giayChiTiet = getDataTable(selected);
                giayChiTiet.setDanhMuc(getDataForm().getDanhMuc());
                giayChiTiet.setHang(getDataForm().getHang());
                giayChiTiet.setKichCo(getDataForm().getKichCo());
                giayChiTiet.setKieuDang(getDataForm().getKieuDang());
                giayChiTiet.setMauSac(getDataForm().getMauSac());
                giayChiTiet.setMoTa(getDataForm().getMoTa());
                giayChiTiet.setGia(getDataForm().getGia());
                giayChiTiet.setSoLuong(getDataForm().getSoLuong());
                giayChiTiet.setTrangThai(getDataForm().getTrangThai());
                giayChiTiet.setHinhAnh(getDataForm().getHinhAnh());

                int row = giayChiTietRepository.update(giayChiTiet);
                if (row == 1) {
                    thongBao = "Sửa thành công";
                } else {
                    thongBao = "Sửa thất bại";
                }
            }
        }
        loadDataTable(giayChiTietRepository.selectByGiay(giayData), currentPage);
        JOptionPane.showMessageDialog(this, thongBao);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void txtGiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGiaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiaKeyReleased

    private void btnShowThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowThemActionPerformed
        showPanel(pnlForm);
    }//GEN-LAST:event_btnShowThemActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showPanel(pnlBoLoc);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cboHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboHangMouseClicked
        setDataHang();
    }//GEN-LAST:event_cboHangMouseClicked

    private void cboKieuDangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboKieuDangMouseClicked
        setDataKieuDang();
    }//GEN-LAST:event_cboKieuDangMouseClicked

    private void cboDanhMucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboDanhMucMouseClicked
        setDataDanhMuc();
    }//GEN-LAST:event_cboDanhMucMouseClicked

    private void cboMauSacMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboMauSacMouseClicked
        setDataMauSac();
    }//GEN-LAST:event_cboMauSacMouseClicked

    private void cboKichCoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboKichCoMouseClicked
        setDataKichCo();
    }//GEN-LAST:event_cboKichCoMouseClicked

    private void txtSearchDSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchDSKeyReleased
        // TODO add your handling code here:
        String search = txtSearchDS.getText();
        if (!search.isBlank()) {
            loadDataTable(searchByText(search), currentPage);
        } else {
            loadDataTable(giayChiTietRepository.selectByGiay(giayData), currentPage);
        }
    }//GEN-LAST:event_txtSearchDSKeyReleased

    private void btnTimKiemBLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemBLActionPerformed
        String search = txtSearchBL.getText();
        loadDataTable(searchBoLoc(search), currentPage);
    }//GEN-LAST:event_btnTimKiemBLActionPerformed

    private void tblDanhSachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachMouseClicked
        // TODO add your handling code here:
        detail(getDataTable(tblDanhSach.getSelectedRow()));
    }//GEN-LAST:event_tblDanhSachMouseClicked

    private void cboDanhMuc1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboDanhMuc1ItemStateChanged
        // TODO add your handling code here:
        if (cboDanhMuc1.getSelectedItem() != null) {
            danhMucSelected = cboDanhMuc1.getSelectedItem().toString().toLowerCase();
        }
    }//GEN-LAST:event_cboDanhMuc1ItemStateChanged

    private void cboHang1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboHang1ItemStateChanged
        // TODO add your handling code here:
        if (cboHang1.getSelectedItem() != null) {
            hangSelected = cboHang1.getSelectedItem().toString().toLowerCase();
        }
    }//GEN-LAST:event_cboHang1ItemStateChanged

    private void cboKieuDang1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboKieuDang1ItemStateChanged
        // TODO add your handling code here:
        if (cboKieuDang1.getSelectedItem() != null) {
            kieuDangSelected = cboKieuDang1.getSelectedItem().toString().toLowerCase();
        }
    }//GEN-LAST:event_cboKieuDang1ItemStateChanged

    private void cboMauSac1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMauSac1ItemStateChanged
        // TODO add your handling code here:
        if (cboMauSac1.getSelectedItem() != null) {
            mauSacSelected = cboMauSac1.getSelectedItem().toString().toLowerCase();
        }
    }//GEN-LAST:event_cboMauSac1ItemStateChanged

    private void cboKichCo1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboKichCo1ItemStateChanged
        // TODO add your handling code here:
        if (cboKichCo1.getSelectedItem() != null) {
            kichCoSelected = cboKichCo1.getSelectedItem().toString().toLowerCase();
        }
    }//GEN-LAST:event_cboKichCo1ItemStateChanged

    private void lblMaTenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMaTenMouseClicked
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_lblMaTenMouseClicked

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        btnNext();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        btnPrev();
    }//GEN-LAST:event_btnPrevActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChiTietGiay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChiTietGiay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChiTietGiay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChiTietGiay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Giay giay = new Giay();
                new ChiTietGiay(giay).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDanhMuc;
    private javax.swing.JButton btnHang;
    private javax.swing.JButton btnKichCo;
    private javax.swing.JButton btnKieuDang;
    private javax.swing.JButton btnMauSac;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnShowThem;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiemBL;
    private javax.swing.JComboBox<String> cboDanhMuc;
    private javax.swing.JComboBox<String> cboDanhMuc1;
    private javax.swing.JComboBox<String> cboHang;
    private javax.swing.JComboBox<String> cboHang1;
    private javax.swing.JComboBox<String> cboKichCo;
    private javax.swing.JComboBox<String> cboKichCo1;
    private javax.swing.JComboBox<String> cboKieuDang;
    private javax.swing.JComboBox<String> cboKieuDang1;
    private javax.swing.JComboBox<String> cboMauSac;
    private javax.swing.JComboBox<String> cboMauSac1;
    private javax.swing.JComboBox<String> cboTrangThai;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblEmty;
    private javax.swing.JLabel lblErrorSoLuong;
    private javax.swing.JLabel lblErrorSoLuong1;
    private javax.swing.JLabel lblMaTen;
    private javax.swing.JLabel lblSoLuong;
    private javax.swing.JLabel lblSoLuong1;
    private javax.swing.JLabel lblTrang;
    private javax.swing.JPanel pnlBoLoc;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtSearchBL;
    private javax.swing.JTextField txtSearchDS;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables
}
