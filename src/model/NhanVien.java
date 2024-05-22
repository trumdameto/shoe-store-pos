package model;

import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class NhanVien {

    String id, ma, ten;
    Boolean gioitinh;
    String sdt, diachi;
    Date ngaysinh;
    String matkhau, vaitro, trangthai;

    public NhanVien(String ten, Boolean gioitinh, String sdt, String diachi, Date ngaysinh, String matkhau, String vaitro, String trangthai) {
        this.ten = ten;
        this.gioitinh = gioitinh;
        this.sdt = sdt;
        this.diachi = diachi;
        this.ngaysinh = ngaysinh;
        this.matkhau = matkhau;
        this.vaitro = vaitro;
        this.trangthai = trangthai;
    }

}
