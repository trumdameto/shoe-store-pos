package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MenuFrame extends javax.swing.JFrame {

    private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
    private CardLayout cardLayout;
    private int width = 230;
    private int height = 686;

    public MenuFrame() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon/titleApp.png")));
        cardLayout = (CardLayout) pnlView.getLayout();
        calendar();
        mouseAction();
        setIcons();
        setLayout();    
    }

      void setLayout() {
        String[] panelNames = {"pnlViewThongKe", "pnlViewNhanVien", "pnlViewHoaDon", "pnlViewKhachHang", "pnlViewDMK", "pnlViewSanPham"};
        String[] labelTitles = {"Thống kê", "Nhân viên", "Hóa đơn", "Khách hàng", "Đổi mật khẩu", "Sản Phẩm"};
        JLabel[] labels = {lblThongKe, lblNhanVien, lblHoaDon, lblKhachHang, lblDoiMatKhau, lblSanPham};
        JPanel[] panels = {pnlViewThongKe, pnlViewNhanVien, pnlViewHoaDon, pnlViewKhachHang, pnlViewDMK, pnlViewSanPham};
        for (JPanel panel : panels) {
            panel.setBackground(new Color(255,255,255)); // Thay màu các panelView
        }
        for (int i = 0; i < labels.length; i++) {
            int finalI = i;
            labels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pnlView.add(panels[finalI],panelNames[finalI]);
                    cardLayout.show(pnlView, panelNames[finalI]);
                    lblTitleView.setText(labelTitles[finalI]);
                    pnlHeader.setBackground(new Color(255,255,255));
                    pnlCalendar.setBackground(new Color(255,255,255));
                    if(labels[finalI]==lblSanPham){
                        new GiayFrame().setVisible(true);
                    }
                }
            });
        }

    }

    void OpenMenu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < width; i++) {
                    pnlMenu.setSize(i, height);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MenuFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }
//Đóng menu

    void CloseMenu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = width; i > 0; i--) {
                    pnlMenu.setSize(i, height);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MenuFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    }

    void setIcons() {
        // Đường dẫn của icon trong Menu_Frame
        String[] urlIcon = {
            "/icon/Close_2.png", "/icon/Logo.png", "/icon/Document.png",
            "/icon/Fast Moving Consumer Goods.png", "/icon/Staff.png",
            "/icon/Purchase Order.png", "/icon/Customer.png", "/icon/Change.png",
            "/icon/Logout.png", "/icon/Menu.png", "/icon/Calendar.png"
        };

        ArrayList<JLabel> labels = new ArrayList<>();
        labels.add(lblCloseMenu);
        labels.add(lblLogo);
        labels.add(lblThongKe);
        labels.add(lblSanPham);
        labels.add(lblNhanVien);
        labels.add(lblHoaDon);
        labels.add(lblKhachHang);
        labels.add(lblDoiMatKhau);
        labels.add(lblDangXuat);
        labels.add(lblOpenMenu);
        labels.add(lblCalendar);

        for (int i = 0; i < labels.size(); i++) {
            ImageIcon icon = new ImageIcon(getClass().getResource(urlIcon[i]));
            labels.get(i).setIcon(icon);
        }
    }

// Set các mouseevent cho labbel button
    void mouseAction() {
        ArrayList<JLabel> labels = new ArrayList<>(); // ArrayList chứa tất cả các label

//Thêm vào ds label
        labels.add(lblThongKe);
        labels.add(lblSanPham);
        labels.add(lblNhanVien);
        labels.add(lblHoaDon);
        labels.add(lblKhachHang);
        labels.add(lblDoiMatKhau);
        labels.add(lblDangXuat);

// Tạo các sự kiện chuọt cho tất cả các nhãn(label)
        for (JLabel label : labels) {
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {//Khi di chuột vào
                    label.setForeground(Color.WHITE);
                    label.setCursor(cursor);
                    label.setBackground(Color.CYAN);
                }

                @Override
                public void mouseExited(MouseEvent e) {//Khi di chuột ra 
                    label.setForeground(getForeground());
                    label.setBackground(getBackground());
                }

                @Override
                public void mousePressed(MouseEvent e) {//Khi Cick chuột vào
                    label.setBackground(Color.BLACK);
                    label.setForeground(Color.white);
                }

                @Override
                public void mouseReleased(MouseEvent e) {//Khi click chuột xong
                    label.setBackground(getBackground());
                    label.setForeground(getForeground());
                }
            });
        }
    }

//Tạo đồng hồ
    void calendar() {
        new Timer(1000, new ActionListener() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public void actionPerformed(ActionEvent e) {
                lblCalendar.setText(sdf.format(new Date()));
            }
        }).start();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblOpenMenu = new javax.swing.JLabel();
        pnlCalendar = new javax.swing.JPanel();
        lblCalendar = new javax.swing.JLabel();
        lblTitleView = new javax.swing.JLabel();
        pnlMenu = new javax.swing.JPanel();
        pnlCloseMenu = new javax.swing.JPanel();
        lblCloseMenu = new javax.swing.JLabel();
        pnlLogo = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        pnlButton = new javax.swing.JPanel();
        pnlThong_Ke = new javax.swing.JPanel();
        lblThongKe = new javax.swing.JLabel();
        pnlSan_Pham = new javax.swing.JPanel();
        lblSanPham = new javax.swing.JLabel();
        pnlNhan_Vien = new javax.swing.JPanel();
        lblNhanVien = new javax.swing.JLabel();
        pnlHoa_Don = new javax.swing.JPanel();
        lblHoaDon = new javax.swing.JLabel();
        pnlKhach_Hang = new javax.swing.JPanel();
        lblKhachHang = new javax.swing.JLabel();
        pnlDMK = new javax.swing.JPanel();
        lblDoiMatKhau = new javax.swing.JLabel();
        pnlDang_Xuat = new javax.swing.JPanel();
        lblDangXuat = new javax.swing.JLabel();
        pnlView = new javax.swing.JPanel();
        pnlViewSanPham = new javax.swing.JPanel();
        pnlViewNhanVien = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlViewHoaDon = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pnlViewKhachHang = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pnlViewDMK = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        pnlViewDangXuat = new javax.swing.JPanel();
        pnlViewThongKe = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setPreferredSize(new java.awt.Dimension(1140, 686));

        pnlHeader.setBackground(new java.awt.Color(255, 255, 255));

        lblOpenMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpenMenu.setPreferredSize(new java.awt.Dimension(40, 40));
        lblOpenMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOpenMenuMouseClicked(evt);
            }
        });

        pnlCalendar.setBackground(new java.awt.Color(255, 255, 255));
        pnlCalendar.setPreferredSize(new java.awt.Dimension(622, 40));

        lblCalendar.setBackground(new java.awt.Color(255, 255, 255));
        lblCalendar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblCalendar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCalendar.setPreferredSize(new java.awt.Dimension(24, 24));

        javax.swing.GroupLayout pnlCalendarLayout = new javax.swing.GroupLayout(pnlCalendar);
        pnlCalendar.setLayout(pnlCalendarLayout);
        pnlCalendarLayout.setHorizontalGroup(
            pnlCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlCalendarLayout.setVerticalGroup(
            pnlCalendarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        lblTitleView.setFont(new java.awt.Font("Times New Roman", 3, 20)); // NOI18N
        lblTitleView.setPreferredSize(new java.awt.Dimension(26, 40));

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addComponent(lblOpenMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(920, 920, 920)
                .addComponent(pnlCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addGap(18, 18, 18))
            .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                    .addContainerGap(531, Short.MAX_VALUE)
                    .addComponent(lblTitleView, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(471, Short.MAX_VALUE)))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOpenMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlCalendar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlHeaderLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblTitleView, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pnlMenu.setBackground(new java.awt.Color(204, 255, 205));
        pnlMenu.setPreferredSize(new java.awt.Dimension(230, 680));

        pnlCloseMenu.setBackground(new java.awt.Color(204, 255, 205));

        lblCloseMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCloseMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlCloseMenuLayout = new javax.swing.GroupLayout(pnlCloseMenu);
        pnlCloseMenu.setLayout(pnlCloseMenuLayout);
        pnlCloseMenuLayout.setHorizontalGroup(
            pnlCloseMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCloseMenuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCloseMenu)
                .addContainerGap())
        );
        pnlCloseMenuLayout.setVerticalGroup(
            pnlCloseMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCloseMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCloseMenu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlLogo.setBackground(new java.awt.Color(204, 255, 205));

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.gray, java.awt.Color.gray));

        javax.swing.GroupLayout pnlLogoLayout = new javax.swing.GroupLayout(pnlLogo);
        pnlLogo.setLayout(pnlLogoLayout);
        pnlLogoLayout.setHorizontalGroup(
            pnlLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlLogoLayout.setVerticalGroup(
            pnlLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlButton.setBackground(new java.awt.Color(204, 255, 205));

        pnlThong_Ke.setBackground(new java.awt.Color(204, 255, 205));
        pnlThong_Ke.setPreferredSize(new java.awt.Dimension(105, 29));

        lblThongKe.setBackground(new java.awt.Color(255, 255, 255));
        lblThongKe.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblThongKe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThongKe.setText("Thống Kê");
        lblThongKe.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblThongKe.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongKeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlThong_KeLayout = new javax.swing.GroupLayout(pnlThong_Ke);
        pnlThong_Ke.setLayout(pnlThong_KeLayout);
        pnlThong_KeLayout.setHorizontalGroup(
            pnlThong_KeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThong_KeLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlThong_KeLayout.setVerticalGroup(
            pnlThong_KeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThong_KeLayout.createSequentialGroup()
                .addComponent(lblThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlSan_Pham.setBackground(new java.awt.Color(204, 255, 205));
        pnlSan_Pham.setPreferredSize(new java.awt.Dimension(202, 44));

        lblSanPham.setBackground(new java.awt.Color(255, 255, 255));
        lblSanPham.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSanPham.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSanPham.setText("Sản Phẩm");
        lblSanPham.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblSanPham.setPreferredSize(new java.awt.Dimension(105, 29));
        lblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSanPhamMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlSan_PhamLayout = new javax.swing.GroupLayout(pnlSan_Pham);
        pnlSan_Pham.setLayout(pnlSan_PhamLayout);
        pnlSan_PhamLayout.setHorizontalGroup(
            pnlSan_PhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSan_PhamLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSan_PhamLayout.setVerticalGroup(
            pnlSan_PhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnlNhan_Vien.setBackground(new java.awt.Color(204, 255, 205));
        pnlNhan_Vien.setPreferredSize(new java.awt.Dimension(202, 44));

        lblNhanVien.setBackground(new java.awt.Color(255, 255, 255));
        lblNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNhanVien.setText("Nhân Viên");
        lblNhanVien.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pnlNhan_VienLayout = new javax.swing.GroupLayout(pnlNhan_Vien);
        pnlNhan_Vien.setLayout(pnlNhan_VienLayout);
        pnlNhan_VienLayout.setHorizontalGroup(
            pnlNhan_VienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNhan_VienLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNhan_VienLayout.setVerticalGroup(
            pnlNhan_VienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNhan_VienLayout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlHoa_Don.setBackground(new java.awt.Color(204, 255, 205));
        pnlHoa_Don.setMinimumSize(new java.awt.Dimension(202, 44));

        lblHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        lblHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoaDon.setText("Hóa Đơn");
        lblHoaDon.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pnlHoa_DonLayout = new javax.swing.GroupLayout(pnlHoa_Don);
        pnlHoa_Don.setLayout(pnlHoa_DonLayout);
        pnlHoa_DonLayout.setHorizontalGroup(
            pnlHoa_DonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHoa_DonLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHoa_DonLayout.setVerticalGroup(
            pnlHoa_DonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHoa_DonLayout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(lblHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlKhach_Hang.setBackground(new java.awt.Color(204, 255, 205));
        pnlKhach_Hang.setPreferredSize(new java.awt.Dimension(202, 44));

        lblKhachHang.setBackground(new java.awt.Color(255, 255, 255));
        lblKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblKhachHang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblKhachHang.setText("Khách Hàng");
        lblKhachHang.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pnlKhach_HangLayout = new javax.swing.GroupLayout(pnlKhach_Hang);
        pnlKhach_Hang.setLayout(pnlKhach_HangLayout);
        pnlKhach_HangLayout.setHorizontalGroup(
            pnlKhach_HangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKhach_HangLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlKhach_HangLayout.setVerticalGroup(
            pnlKhach_HangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlKhach_HangLayout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(lblKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlDMK.setBackground(new java.awt.Color(204, 255, 205));
        pnlDMK.setPreferredSize(new java.awt.Dimension(202, 44));

        lblDoiMatKhau.setBackground(new java.awt.Color(255, 255, 255));
        lblDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDoiMatKhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDoiMatKhau.setText("Đổi Mật Khẩu");
        lblDoiMatKhau.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pnlDMKLayout = new javax.swing.GroupLayout(pnlDMK);
        pnlDMK.setLayout(pnlDMKLayout);
        pnlDMKLayout.setHorizontalGroup(
            pnlDMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDMKLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDMKLayout.setVerticalGroup(
            pnlDMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDMKLayout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(lblDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlDang_Xuat.setBackground(new java.awt.Color(204, 255, 205));
        pnlDang_Xuat.setPreferredSize(new java.awt.Dimension(202, 44));

        lblDangXuat.setBackground(new java.awt.Color(255, 255, 255));
        lblDangXuat.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDangXuat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDangXuat.setText("Đăng Xuất");
        lblDangXuat.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDangXuatMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlDang_XuatLayout = new javax.swing.GroupLayout(pnlDang_Xuat);
        pnlDang_Xuat.setLayout(pnlDang_XuatLayout);
        pnlDang_XuatLayout.setHorizontalGroup(
            pnlDang_XuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDang_XuatLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDang_XuatLayout.setVerticalGroup(
            pnlDang_XuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDang_XuatLayout.createSequentialGroup()
                .addComponent(lblDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlButtonLayout = new javax.swing.GroupLayout(pnlButton);
        pnlButton.setLayout(pnlButtonLayout);
        pnlButtonLayout.setHorizontalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlThong_Ke, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
            .addComponent(pnlSan_Pham, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
            .addComponent(pnlNhan_Vien, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
            .addComponent(pnlHoa_Don, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlKhach_Hang, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
            .addComponent(pnlDMK, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
            .addComponent(pnlDang_Xuat, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
        );
        pnlButtonLayout.setVerticalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(pnlThong_Ke, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSan_Pham, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNhan_Vien, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlHoa_Don, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlKhach_Hang, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDMK, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
                .addComponent(pnlDang_Xuat, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCloseMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addComponent(pnlCloseMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlView.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout pnlViewSanPhamLayout = new javax.swing.GroupLayout(pnlViewSanPham);
        pnlViewSanPham.setLayout(pnlViewSanPhamLayout);
        pnlViewSanPhamLayout.setHorizontalGroup(
            pnlViewSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1140, Short.MAX_VALUE)
        );
        pnlViewSanPhamLayout.setVerticalGroup(
            pnlViewSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );

        pnlView.add(pnlViewSanPham, "card3");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Nhân Viên");

        javax.swing.GroupLayout pnlViewNhanVienLayout = new javax.swing.GroupLayout(pnlViewNhanVien);
        pnlViewNhanVien.setLayout(pnlViewNhanVienLayout);
        pnlViewNhanVienLayout.setHorizontalGroup(
            pnlViewNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewNhanVienLayout.createSequentialGroup()
                .addGap(387, 387, 387)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(500, Short.MAX_VALUE))
        );
        pnlViewNhanVienLayout.setVerticalGroup(
            pnlViewNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewNhanVienLayout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(322, Short.MAX_VALUE))
        );

        pnlView.add(pnlViewNhanVien, "card4");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setText("Hóa Đơn");

        javax.swing.GroupLayout pnlViewHoaDonLayout = new javax.swing.GroupLayout(pnlViewHoaDon);
        pnlViewHoaDon.setLayout(pnlViewHoaDonLayout);
        pnlViewHoaDonLayout.setHorizontalGroup(
            pnlViewHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewHoaDonLayout.createSequentialGroup()
                .addGap(387, 387, 387)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(500, Short.MAX_VALUE))
        );
        pnlViewHoaDonLayout.setVerticalGroup(
            pnlViewHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewHoaDonLayout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(322, Short.MAX_VALUE))
        );

        pnlView.add(pnlViewHoaDon, "card5");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel3.setText("Khách Hàng");

        javax.swing.GroupLayout pnlViewKhachHangLayout = new javax.swing.GroupLayout(pnlViewKhachHang);
        pnlViewKhachHang.setLayout(pnlViewKhachHangLayout);
        pnlViewKhachHangLayout.setHorizontalGroup(
            pnlViewKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewKhachHangLayout.createSequentialGroup()
                .addGap(387, 387, 387)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(500, Short.MAX_VALUE))
        );
        pnlViewKhachHangLayout.setVerticalGroup(
            pnlViewKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewKhachHangLayout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(322, Short.MAX_VALUE))
        );

        pnlView.add(pnlViewKhachHang, "card6");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel4.setText("DMK");

        javax.swing.GroupLayout pnlViewDMKLayout = new javax.swing.GroupLayout(pnlViewDMK);
        pnlViewDMK.setLayout(pnlViewDMKLayout);
        pnlViewDMKLayout.setHorizontalGroup(
            pnlViewDMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewDMKLayout.createSequentialGroup()
                .addGap(387, 387, 387)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(500, Short.MAX_VALUE))
        );
        pnlViewDMKLayout.setVerticalGroup(
            pnlViewDMKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewDMKLayout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(322, Short.MAX_VALUE))
        );

        pnlView.add(pnlViewDMK, "card7");

        javax.swing.GroupLayout pnlViewDangXuatLayout = new javax.swing.GroupLayout(pnlViewDangXuat);
        pnlViewDangXuat.setLayout(pnlViewDangXuatLayout);
        pnlViewDangXuatLayout.setHorizontalGroup(
            pnlViewDangXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1140, Short.MAX_VALUE)
        );
        pnlViewDangXuatLayout.setVerticalGroup(
            pnlViewDangXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );

        pnlView.add(pnlViewDangXuat, "card8");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel5.setText("Thống Kê");

        javax.swing.GroupLayout pnlViewThongKeLayout = new javax.swing.GroupLayout(pnlViewThongKe);
        pnlViewThongKe.setLayout(pnlViewThongKeLayout);
        pnlViewThongKeLayout.setHorizontalGroup(
            pnlViewThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewThongKeLayout.createSequentialGroup()
                .addGap(387, 387, 387)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(500, Short.MAX_VALUE))
        );
        pnlViewThongKeLayout.setVerticalGroup(
            pnlViewThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlViewThongKeLayout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(322, Short.MAX_VALUE))
        );

        pnlView.add(pnlViewThongKe, "card2");

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlView, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblOpenMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOpenMenuMouseClicked
        OpenMenu();
    }//GEN-LAST:event_lblOpenMenuMouseClicked

    private void lblCloseMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCloseMenuMouseClicked
        CloseMenu();
    }//GEN-LAST:event_lblCloseMenuMouseClicked

    private void lblThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongKeMouseClicked

    }//GEN-LAST:event_lblThongKeMouseClicked

    private void lblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSanPhamMouseClicked

    }//GEN-LAST:event_lblSanPhamMouseClicked

    private void lblDangXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDangXuatMouseClicked
        //        int choose = JOptionPane.showConfirmDialog(this, "Are you want logout?", "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        //        if (choose == JOptionPane.YES_OPTION) {
            //            LoginFrame lgf = new LoginFrame();
            //            this.dispose();
            //            lgf.setVisible(true);
            //        }
    }//GEN-LAST:event_lblDangXuatMouseClicked

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
            java.util.logging.Logger.getLogger(MenuFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblCalendar;
    private javax.swing.JLabel lblCloseMenu;
    private javax.swing.JLabel lblDangXuat;
    private javax.swing.JLabel lblDoiMatKhau;
    private javax.swing.JLabel lblHoaDon;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblOpenMenu;
    private javax.swing.JLabel lblSanPham;
    private javax.swing.JLabel lblThongKe;
    private javax.swing.JLabel lblTitleView;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlCalendar;
    private javax.swing.JPanel pnlCloseMenu;
    private javax.swing.JPanel pnlDMK;
    private javax.swing.JPanel pnlDang_Xuat;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHoa_Don;
    private javax.swing.JPanel pnlKhach_Hang;
    private javax.swing.JPanel pnlLogo;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlNhan_Vien;
    private javax.swing.JPanel pnlSan_Pham;
    private javax.swing.JPanel pnlThong_Ke;
    private javax.swing.JPanel pnlView;
    private javax.swing.JPanel pnlViewDMK;
    private javax.swing.JPanel pnlViewDangXuat;
    private javax.swing.JPanel pnlViewHoaDon;
    private javax.swing.JPanel pnlViewKhachHang;
    private javax.swing.JPanel pnlViewNhanVien;
    private javax.swing.JPanel pnlViewSanPham;
    private javax.swing.JPanel pnlViewThongKe;
    // End of variables declaration//GEN-END:variables
}
