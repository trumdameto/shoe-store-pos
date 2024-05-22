package repository;

import java.awt.Image;
import javax.swing.ImageIcon;

public class XImage {

    public static final Image APP_ICON;

    static {
        String appIconFile = "/icon/titleApp.png";
        APP_ICON = loadImage(appIconFile);
    }

    // Phương thức tải hình ảnh dựa trên đường dẫn file
    public static Image loadImage(String filePath) {
        return new ImageIcon(XImage.class.getResource(filePath)).getImage();
    }
}

