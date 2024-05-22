package repository;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.ThongKeModel;

public class ThongKeRepo {

    private static final String DOANH_THU = "SELECT\n"
            + "    CONVERT(date, NGAYTAO) AS Ngay,\n"
            + "    SUM(TONGTIEN) AS DoanhThu\n"
            + "FROM\n"
            + "    HOADON\n"
            + "GROUP BY\n"
            + "    CONVERT(date, NGAYTAO)\n"
            + "ORDER BY\n"
            + "    CONVERT(date, NGAYTAO) DESC;";
    private static final String THEO_NGAY = "{call sp_TongTienHoaDonTheoNgay1(?)}";
    private static final String TONG = "SELECT SUM(TONGTIEN) AS TongDoanhThu\n"
            + "FROM HOADON;";

    public List<ThongKeModel> getDoanhThuAll() {
        List<ThongKeModel> result = new ArrayList<>();

        try (Connection con = DBConnect.getConnection(); PreparedStatement pstm = con.prepareStatement(DOANH_THU)) {
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                result.add(new ThongKeModel(rs.getDate(1), rs.getBigDecimal(2)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public List<ThongKeModel> getDoanhThuTheoNgay1(Date date) {
    List<ThongKeModel> result = new ArrayList<>();

    try (Connection con = DBConnect.getConnection();
         PreparedStatement pstm = con.prepareStatement("EXEC sp_TongTienHoaDonTheoNgay1 ?")) {

        // Truyền tham số vào câu truy vấn
        pstm.setDate(1, new java.sql.Date(date.getTime()));

        try (ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                result.add(new ThongKeModel(rs.getDate(1), rs.getBigDecimal(2)));
            }
            return result;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
return null;
    
}
        public List<ThongKeModel> getDoanhThuTheoThang(int date) {
        List<ThongKeModel> result = new ArrayList<>();

        try (Connection con = DBConnect.getConnection();
             CallableStatement cstmt = con.prepareCall("{call GetThongTinNgayVaDoanhThu(?)}")) {

            // Truyền tham số vào stored procedure
            cstmt.setInt(1,date);  // Lưu ý: Tháng trong Java bắt đầu từ 0, trong stored procedure bắt đầu từ 1

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new ThongKeModel(rs.getDate(1), rs.getBigDecimal(2)));
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public List<Object[]> getDoanhThuTheoNgay(Date date) {
//        List<ThongKeModel> result = new ArrayList<>();
//
//        try (Connection con = DBConnect.getConnection(); PreparedStatement pstm = con.prepareStatement(THEO_NGAY)) {
//            ResultSet rs = pstm.executeQuery();
//
//            while (rs.next()) {
//                result.add(new ThongKeModel(rs.getDate(1), rs.getBigDecimal(2)));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }

    public BigDecimal getTong() {

        BigDecimal tong = BigDecimal.ZERO;
        try (Connection con = DBConnect.getConnection(); PreparedStatement pstm = con.prepareStatement(TONG)) {
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                tong = rs.getBigDecimal(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;

    }
}
