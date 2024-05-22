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

public class Voucher {

    String id;
    BigDecimal giaTri;
    String maGiamGia;
    BigDecimal dieuKien;
    Integer soLuong;
    Long ngayBD;
    Long ngayKT;
    String loai;
    Long ngayTao;
    String trangThai;
    
}
