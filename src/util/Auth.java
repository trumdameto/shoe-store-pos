package util;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import model.NhanVien;

public class Auth {

    /*
     * Đối tượng này chứa thông tin người sử dụng sau khi đăng nhập
     */
    public static NhanVien user = null; //người dùng chưa login
    public static Webcam webcam = null;
    public static WebcamPanel panel = null;

    public static void initWebcam(JPanel jpnQR) {
        if (webcam != null) {
            webcam.open();
        } else {
            Dimension size = WebcamResolution.QVGA.getSize();
            webcam = Webcam.getWebcams().get(0);
            webcam.setViewSize(size);
            panel = new WebcamPanel(webcam);
            panel.setPreferredSize(size);
            panel.setFPSDisplayed(true);
            jpnQR.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 223, 178));
        }
//        jpnQR.add(panel, BorderLayout.CENTER);
    }

    public static void clearWebcam() {
        webcam.close();
    }

    /*Xóa thông tin của người dùng khi có yêu cầu đăng xuất
     * 
     */
    public static void clear() {//log out
        Auth.user = null;
    }

    /*
     * Kiểm tra đăng nhập hay chưa
     */
    public static boolean isLogin() {
        return Auth.user != null;
    }

    /*
     * Kiểm tra có phải trường phòng hay không
     */
    public static boolean isManager() {
        return Auth.isLogin() && user.getVaitro().equalsIgnoreCase("Admin");
    }
}
