package model;

import java.math.BigDecimal;
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

public class HoaDon {

    String id, maHoaDon;
    NhanVien nhanVien;
    KhachHang khachHang;
    Voucher voucher;
    Date ngayTao;
    BigDecimal tongTien;
    String trangThai;
    BigDecimal tienKhachDua, tienThua;
    String hinhThucThanhToan;

}
