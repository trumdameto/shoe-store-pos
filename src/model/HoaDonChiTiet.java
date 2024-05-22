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

public class HoaDonChiTiet {

    String id;
    GiayChiTiet giayChiTiet;
    HoaDon hoaDon;
    BigDecimal gia;
    Integer soLuong;
    String trangThai;
    
    public BigDecimal getThanhTien(){
        return this.gia.multiply(BigDecimal.valueOf(this.soLuong));
    }

}
