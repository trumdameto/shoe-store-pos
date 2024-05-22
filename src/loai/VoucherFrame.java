package loai;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadFactory;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.print.DocFlavor;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.KhachHang;
import model.Voucher;
import repository.KhachHangRepository;
import repository.VoucherRepository;
import util.StatusVoucher;

public class VoucherFrame extends javax.swing.JFrame implements Runnable, ThreadFactory {

    private Integer currentPage = 1;
    private Integer totalPage = 1;
    private Integer rowCountPage = 5;

    private DefaultTableModel dtm = new DefaultTableModel();
    private VoucherRepository repository = new VoucherRepository();
    private KhachHangRepository khachHangRepository = new KhachHangRepository();

    private StatusVoucher statusVoucher = new StatusVoucher();

    public VoucherFrame() {
        initComponents();
        setSize(824, 510);
        setLocationRelativeTo(null);
        loadData(repository.selectAll(), currentPage);
        confiColumns();
        clearForm();
        statusVoucher.start();
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                System.out.println("Lỗi");
            }
            Long currentTime = new Date().getTime() / 1000;
            System.out.println("Bắt đầu chạy");
            for (Voucher voucher : repository.selectAll()) {
                if (voucher.getNgayKT() <= currentTime) {
                    voucher.setTrangThai("Đã kết thúc");
                    repository.update(voucher);
                }
                if (voucher.getNgayBD() >= currentTime) {
                    voucher.setTrangThai("Đang diễn ra");
                    repository.update(voucher);
                }
            }
            loadData(repository.selectAll(), currentPage);
            System.out.println("Chạy xong");
        } while (true);
    }

    private void confiColumns() {
        this.tblDanhSach.getColumnModel().getColumn(0).setPreferredWidth(90);
        this.tblDanhSach.getColumnModel().getColumn(1).setPreferredWidth(90);
        this.tblDanhSach.getColumnModel().getColumn(2).setPreferredWidth(80);
        this.tblDanhSach.getColumnModel().getColumn(3).setPreferredWidth(60);
        this.tblDanhSach.getColumnModel().getColumn(4).setPreferredWidth(135);
        this.tblDanhSach.getColumnModel().getColumn(5).setPreferredWidth(135);
        this.tblDanhSach.getColumnModel().getColumn(6).setPreferredWidth(135);
    }

    private void setTotalPage(List<Voucher> list) {
        int totalItem = list.size();
        if (totalItem % rowCountPage != 0) {
            totalPage = (totalItem / rowCountPage) + 1;
        } else {
            totalPage = totalItem / rowCountPage;
        }
        lblTrang.setText("Trang " + currentPage + "/" + totalPage);
    }

    private void loadData(List<Voucher> list, int page) {
        setTotalPage(list);
        dtm.setRowCount(0);
        dtm = (DefaultTableModel) tblDanhSach.getModel();
        int limit = page * rowCountPage;
        int totalItem = list.size();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        for (int start = (page - 1) * rowCountPage; start < totalItem; start++) {
            Voucher item = list.get(start);
            Date ngayBD = new Date(item.getNgayBD() * 1000);
            Date ngayKT = new Date(item.getNgayKT() * 1000);
            String giaTri = "";
            if (item.getLoai().contains("phanTram")) {
                giaTri = item.getGiaTri() + "%";
            } else {
                giaTri = item.getGiaTri() + "VND";
            }
            dtm.addRow(new Object[]{
                item.getMaGiamGia(),
                item.getDieuKien(),
                giaTri,
                item.getSoLuong(),
                simpleDateFormat.format(ngayBD),
                simpleDateFormat.format(ngayKT),
                item.getTrangThai(),});
            if (start + 1 == limit) {
                return;
            }
        }
    }

    private Voucher getForm() {
        String ma = txtMa.getText();
        String dieuKien = txtDieuKien.getText();
        String giaTri = txtGiaTri.getText();
        String soLuong = txtSoLuong.getText();
        String ngayBD = txtNgayBD.getText();
        String ngayKT = txtNgayKT.getText();
        String loai = cboLoai.getSelectedItem().toString();
        String trangThai = "Sắp diễn ra";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Long ngayHienTai = new Date().getTime() / 1000;
        Date dateStart = null;
        Date dateEnd = null;
        Long ngayBatDau = null;
        Long ngayKetThuc = null;

        if (ma.isBlank()) {
            lblErrorMa.setText("Mã không được để trống");
            return null;
        } else {
            lblErrorMa.setText(" ");
            if (dieuKien.isBlank()) {
                lblErrorDieuKien.setText("Điều kiện không được để trống");
                return null;
            } else {
                try {
                    Double.parseDouble(dieuKien);
                    if (Double.parseDouble(dieuKien) < 0) {
                        lblErrorDieuKien.setText("Điều kiện phải lớn hơn hoặc bằng 0");
                        return null;
                    } else {
                        lblErrorDieuKien.setText(" ");
                    }
                } catch (Exception e) {
                    lblErrorDieuKien.setText("Điều kiện phải là số");
                    return null;
                }
            }

            if (giaTri.isBlank()) {
                lblErrorGiaTri.setText("Giá trị không được để trống");
                return null;
            } else {
                try {
                    Double.parseDouble(giaTri);
                    if (Double.parseDouble(giaTri) < 0) {
                        lblErrorGiaTri.setText("Giá trị phải lớn hơn hoặc bằng 0");
                        return null;
                    } else {
                        lblErrorGiaTri.setText(" ");
                    }
                } catch (Exception e) {
                    lblErrorGiaTri.setText("Giá trị phải là số");
                    return null;
                }
            }

            if (soLuong.isBlank()) {
                lblErrorSoLuong.setText("Số lượng không được để trống");
                return null;
            } else {
                try {
                    Integer.parseInt(soLuong);
                    if (Double.parseDouble(soLuong) < 0) {
                        lblErrorSoLuong.setText("Số lượng phải lớn hơn hoặc bằng 0");
                        return null;
                    } else {
                        lblErrorSoLuong.setText(" ");
                    }
                } catch (Exception e) {
                    lblErrorSoLuong.setText("Số lượng phải là số");
                    return null;
                }
            }

            if (cboLoai.getSelectedIndex() == 0) {
                if (Double.parseDouble(giaTri) > 100 || Double.parseDouble(giaTri) < 0) {
                    lblErrorGiaTri.setText("Loại là % => 0 <= giaTri <= 100");
                    return null;
                }
            }

            Voucher findVoucher = new Voucher();
            findVoucher.setMaGiamGia(txtMa.getText().trim());
            Voucher v = repository.selectbyId(findVoucher);
            if (v != null) {
                if (ngayBD.isBlank()) {
                    lblErrorNgayBD.setText("Ngày bắt đầu không được để trống");
                    return null;
                } else {
                    try {
                        dateStart = simpleDateFormat.parse(ngayBD);
                        ngayBatDau = dateStart.getTime() / 1000;
                        if (ngayBatDau < v.getNgayTao()) {
                            lblErrorNgayBD.setText("Ngày bắt đầu > ngày hiện tại");
                            return null;
                        } else {
                            lblErrorNgayBD.setText(" ");
                        }
                    } catch (Exception e) {
                        lblErrorNgayBD.setText("Ngày bắt đầu: hh:mm:ss dd/MM/yyyy");
                        return null;
                    }
                }

            } else {
                if (ngayBD.isBlank()) {
                    lblErrorNgayBD.setText("Ngày bắt đầu không được để trống");
                    return null;
                } else {
                    try {
                        dateStart = simpleDateFormat.parse(ngayBD);
                        ngayBatDau = dateStart.getTime() / 1000;
                        if (ngayBatDau < ngayHienTai) {
                            lblErrorNgayBD.setText("Ngày bắt đầu > ngày hiện tại");
                            return null;
                        } else {
                            lblErrorNgayBD.setText(" ");
                        }
                    } catch (Exception e) {
                        lblErrorNgayBD.setText("Ngày bắt đầu: hh:mm:ss dd/MM/yyyy");
                        return null;
                    }
                }
            }
            if (ngayKT.isBlank()) {
                lblErrorNgayKT.setText("Ngày kết thúc không được để trống");
                return null;
            } else {
                try {
                    dateEnd = simpleDateFormat.parse(ngayKT);
                    ngayKetThuc = dateEnd.getTime() / 1000;
                    if (ngayKetThuc < ngayBatDau) {
                        lblErrorNgayKT.setText("Ngày kết thúc > ngày bắt đầu");
                        return null;
                    } else {
                        lblErrorNgayKT.setText(" ");
                    }
                } catch (Exception e) {
                    lblErrorNgayKT.setText("Ngày kết thúc: hh:mm:ss dd/MM/yyyy");
                    return null;
                }
            }
        }

        Voucher voucher = new Voucher();
        voucher.setMaGiamGia(ma);
        voucher.setDieuKien(BigDecimal.valueOf(Double.parseDouble(dieuKien)));
        voucher.setGiaTri(BigDecimal.valueOf(Double.parseDouble(giaTri)));
        voucher.setSoLuong(Integer.parseInt(soLuong));
        voucher.setNgayBD(ngayBatDau);
        voucher.setNgayKT(ngayKetThuc);
        voucher.setNgayTao(ngayHienTai);
        voucher.setTrangThai(trangThai);
        voucher.setLoai(loai);
        return voucher;
    }

    private Voucher getTable() {
        int selected = tblDanhSach.getSelectedRow();
        String ma = (String) tblDanhSach.getValueAt(selected, 0);
        Voucher voucher = new Voucher();
        voucher.setMaGiamGia(ma);
        return repository.selectbyId(voucher);
    }

    private void detail() {
        Voucher voucher = getTable();
        txtMa.setText(voucher.getMaGiamGia());
        txtDieuKien.setText(voucher.getDieuKien() + "");
        txtGiaTri.setText(voucher.getGiaTri() + "");
        txtSoLuong.setText(voucher.getSoLuong() + "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String ngayBD = simpleDateFormat.format(new Date(voucher.getNgayBD() * 1000));
        String ngayKT = simpleDateFormat.format(new Date(voucher.getNgayKT() * 1000));
        txtNgayBD.setText(ngayBD);
        txtNgayKT.setText(ngayKT);
        cboLoai.setSelectedItem(voucher.getLoai());
        cboTrangThai.setSelectedItem(voucher.getTrangThai());
    }

    private void sendMail(Voucher voucher) {
        for (KhachHang item : khachHangRepository.selectAll()) {
            if (item.getEmail() != null) {
                String from = "hungvtph41955@fpt.edu.vn";
                String pass = "omxn bcir cdqo xvjb";
                Properties pros = new Properties();
                pros.put("mail.smtp.host", "smtp.gmail.com");
                pros.put("mail.smtp.port", "587");
                pros.put("mail.smtp.auth", "true");
                pros.put("mail.smtp.starttls.enable", "true");

                Authenticator auth = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, pass);
                    }
                };

                Session session = Session.getInstance(pros, auth);

                final String to = item.getEmail();

                MimeMessage msg = new MimeMessage(session);

                try {
                    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                    msg.setFrom(from);
                    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                    msg.setSubject("MeoMeo shop gửi tặng voucher");
                    msg.setSentDate(new Date());
                    String maGiamGia = "Mã voucher: " + voucher.getMaGiamGia() + " \n";
                    String giaTriGiam = "";
                    if (voucher.getLoai().equalsIgnoreCase("phanTram")) {
                        giaTriGiam = "Giá trị giảm: " + voucher.getGiaTri() + "%" + "\n";
                    } else {
                        giaTriGiam = "Giá trị giảm: " + voucher.getGiaTri() + "đ" + "\n";
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    String ngayBD = "Ngày bắt đầu: " + simpleDateFormat.format(voucher.getNgayBD() * 1000) + "\n";
                    String ngayKT = "Ngày kết thúc: " + simpleDateFormat.format(voucher.getNgayKT() * 1000) + "\n";

                    String noiDung = maGiamGia + giaTriGiam + ngayBD + ngayKT;
                    msg.setText(noiDung, "UTF-8");

                    Transport.send(msg);
                    System.out.println("Gửi thành công");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Gửi thất bại");
                }
            }
        }
    }

    private void btnAdd() {
        if (getForm() == null) {
            return;
        }
        Boolean isDuplicate = false;
        for (Voucher voucher : repository.selectAll()) {
            if (voucher.getMaGiamGia().equalsIgnoreCase(txtMa.getText())) {
                lblErrorMa.setText("Mã không được trùng");
                isDuplicate = true;
            }
        }
        if (!isDuplicate) {
            lblErrorMa.setText(" ");
        } else {
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
        Voucher voucher = getTable();
        voucher.setMaGiamGia(getForm().getMaGiamGia());
        voucher.setDieuKien(getForm().getDieuKien());
        voucher.setGiaTri(getForm().getGiaTri());
        voucher.setSoLuong(getForm().getSoLuong());
        voucher.setNgayBD(getForm().getNgayBD());
        voucher.setNgayKT(getForm().getNgayKT());
        voucher.setLoai(getForm().getLoai());
        voucher.setTrangThai(getForm().getTrangThai());
        int row = repository.update(voucher);
        String thongBao = "";
        if (row == 1) {
            thongBao = "Sửa thành công";
        } else {
            thongBao = "Sửa thất bại";
        }
        loadData(repository.selectAll(), currentPage);
        JOptionPane.showMessageDialog(this, thongBao);
    }

    private void btnHuy() {
        if (tblDanhSach.getSelectedRow() <= -1) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn đối tượng cần sửa");
            return;
        }
        if (getForm() == null) {
            return;
        }
        Voucher voucher = getTable();
        voucher.setTrangThai("Đã hủy");
        int row = repository.update(voucher);
        String thongBao = "";
        if (row == 1) {
            thongBao = "Hủy thành công";
        } else {
            thongBao = "Hủy thất bại";
        }
        loadData(repository.selectAll(), currentPage);
        JOptionPane.showMessageDialog(this, thongBao);
    }

    private void search() {
        String search = txtSearch.getText();
        List<Voucher> listSearch = new ArrayList<>();
        for (Voucher voucher : repository.selectAll()) {
            if (voucher.getMaGiamGia().toLowerCase().contains(search)) {
                listSearch.add(voucher);
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

    private void clearForm() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Long ngayHienTai = new Date().getTime() / 1000;
        String now = simpleDateFormat.format(new Date(ngayHienTai * 1000));
        txtNgayBD.setText(now);
        txtNgayKT.setText(now);
        txtDieuKien.setText("");
        txtMa.setText("");
        txtGiaTri.setText("");
        txtSoLuong.setText("");
        cboLoai.setSelectedIndex(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSach = new javax.swing.JTable();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        lblTrang = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        lblErrorMa = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtGiaTri = new javax.swing.JTextField();
        lblErrorGiaTri = new javax.swing.JLabel();
        txtDieuKien = new javax.swing.JTextField();
        lblErrorDieuKien = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        lblErrorSoLuong = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNgayBD = new javax.swing.JTextField();
        lblErrorNgayBD = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtNgayKT = new javax.swing.JTextField();
        lblErrorNgayKT = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cboLoai = new javax.swing.JComboBox<>();
        cboTrangThai = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        btnSua1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Voucher");

        tblDanhSach.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDanhSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã", "DieuKien", "GiaTri", "SoLuong", "NgayBD", "NgayKT", "TrangThai"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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

        lblTrang.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblTrang.setText("Trang");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Ma");

        txtMa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("X");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Search");

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        lblErrorMa.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorMa.setText(" ");

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

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("GiaTri");

        txtGiaTri.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblErrorGiaTri.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorGiaTri.setText(" ");

        txtDieuKien.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblErrorDieuKien.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorDieuKien.setText(" ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("SoLuong");

        txtSoLuong.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblErrorSoLuong.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorSoLuong.setText(" ");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("DieuKien");

        txtNgayBD.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblErrorNgayBD.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorNgayBD.setText(" ");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Loai");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("NgayBD");

        txtNgayKT.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblErrorNgayKT.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorNgayKT.setText(" ");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("TrangThai");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("NgayKT");

        cboLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "phanTram", "VND" }));

        cboTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp diễn ra", "Đang diễn ra", "Đã kết thúc", "Đã hủy" }));
        cboTrangThai.setEnabled(false);

        jButton1.setText("New");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnSua1.setText("Hủy");
        btnSua1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSua1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(37, 37, 37)
                                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(btnSua)
                                .addGap(29, 29, 29)
                                .addComponent(btnSua1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblErrorGiaTri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtGiaTri, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblErrorMa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtMa)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblErrorNgayBD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtNgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboTrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lblErrorSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lblErrorNgayKT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtNgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblErrorDieuKien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtDieuKien, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))))
                                .addGap(79, 79, 79)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnSua, btnSua1, btnThem, jButton1});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cboLoai, cboTrangThai, txtDieuKien, txtGiaTri, txtMa, txtNgayBD, txtNgayKT, txtSoLuong});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorMa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtGiaTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorGiaTri)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtNgayBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorNgayBD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(cboLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtDieuKien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorDieuKien)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorSoLuong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtNgayKT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblErrorNgayKT)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(jButton1)
                    .addComponent(btnSua1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTrang)
                    .addComponent(btnNext)
                    .addComponent(btnPrev))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        btnAdd();
        sendMail(repository.selectAll().get(0));
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        btnUpdate();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
        search();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void tblDanhSachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachMouseClicked
        // TODO add your handling code here:
        detail();
    }//GEN-LAST:event_tblDanhSachMouseClicked

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        btnPrev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        btnNext();
    }//GEN-LAST:event_btnNextActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnSua1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSua1ActionPerformed
        // TODO add your handling code here:
        btnHuy();
    }//GEN-LAST:event_btnSua1ActionPerformed

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
            java.util.logging.Logger.getLogger(VoucherFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VoucherFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VoucherFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VoucherFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VoucherFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnSua1;
    private javax.swing.JButton btnThem;
    private javax.swing.JComboBox<String> cboLoai;
    private javax.swing.JComboBox<String> cboTrangThai;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblErrorDieuKien;
    private javax.swing.JLabel lblErrorGiaTri;
    private javax.swing.JLabel lblErrorMa;
    private javax.swing.JLabel lblErrorNgayBD;
    private javax.swing.JLabel lblErrorNgayKT;
    private javax.swing.JLabel lblErrorSoLuong;
    private javax.swing.JLabel lblTrang;
    private javax.swing.JTable tblDanhSach;
    private javax.swing.JTextField txtDieuKien;
    private javax.swing.JTextField txtGiaTri;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtNgayBD;
    private javax.swing.JTextField txtNgayKT;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }
}
