package model;

import java.math.BigDecimal;
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

public class GiayChiTiet {

    String id;
    String ma;
    Giay giay;
    Hang hang;
    KieuDang kieuDang;
    DanhMuc danhMuc;
    MauSac mauSac;
    KichCo kichCo;
    String hinhAnh;
    BigDecimal gia;
    Integer soLuong;
    Long ngayTao;
    String trangThai;
    String moTa;

    public GiayChiTiet(String ma) {
        this.ma = ma;
    }

    

}
