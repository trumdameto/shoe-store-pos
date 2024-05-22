package repository;

import java.math.BigDecimal;
import model.KhachHang;
import model.NhanVien;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.HoaDon;
import model.TichDiem;
import model.Voucher;

public class HoaDonRepository extends BaseCRUD<HoaDon> {

    private String INSERT = """
                            INSERT INTO HOADON (ID_NHANVIEN, NGAYTAO, TRANGTHAI)
                            VALUES (?, ?, ?)
                            """;
    private String UPDATE = """
                            UPDATE HOADON SET ID_VOUCHER = ?, ID_KHACHHANG = ?, ID_NHANVIEN = ?, NGAYTAO = ?, TONGTIEN = ?, TIENKHACHDUA = ?, 
                            TIENTHUA = ?, HINHTHUCTHANHTOAN = ?, TRANGTHAI = ? WHERE MA = ? OR ID = ? 
                            """;
    private String DELETE = "DELETE HOADON WHERE TRANGTHAI LIKE N'%C%' AND MA = ? OR ID = ?";
    private String SELECT_BY = """
                               SELECT A.ID, A.MA, B.*, C.*, D.*, 
                               A.NGAYTAO, A.HINHTHUCTHANHTOAN, A.TRANGTHAI, 
                               A.TIENKHACHDUA, A.TIENTHUA, A.TONGTIEN
                               FROM HOADON A
                               LEFT JOIN VOUCHER B ON A.ID_VOUCHER = B.ID
                               LEFT JOIN NHANVIEN C ON A.ID_NHANVIEN = C.ID
                               LEFT JOIN KHACHHANG D ON A.ID_KHACHHANG = D.ID
                               WHERE A.MA = ? OR A.ID = ?
                               ORDER BY A.MA DESC
                               """;
    private String SELECT_ALL = """
                                SELECT A.ID, A.MA, B.*, C.*, D.*,
                                A.NGAYTAO, A.HINHTHUCTHANHTOAN, A.TRANGTHAI, 
                                A.TIENKHACHDUA, A.TIENTHUA, A.TONGTIEN
                                FROM HOADON A
                                LEFT JOIN VOUCHER B ON A.ID_VOUCHER = B.ID
                                LEFT JOIN NHANVIEN C ON A.ID_NHANVIEN = C.ID
                                LEFT JOIN KHACHHANG D ON A.ID_KHACHHANG = D.ID
                                ORDER BY A.MA DESC
                                """;
    private String SELECT_CHO = """
                                SELECT A.ID, A.MA, B.*, C.*, D.*, 
                                A.NGAYTAO, A.HINHTHUCTHANHTOAN, A.TRANGTHAI,
                                A.TIENKHACHDUA, A.TIENTHUA, A.TONGTIEN
                                FROM HOADON A
                                LEFT JOIN VOUCHER B ON A.ID_VOUCHER = B.ID
                                LEFT JOIN NHANVIEN C ON A.ID_NHANVIEN = C.ID
                                LEFT JOIN KHACHHANG D ON A.ID_KHACHHANG = D.ID
                                WHERE A.TRANGTHAI LIKE N'%C%'
                                ORDER BY A.MA DESC
                                """;
    private String SELECT_HT = """
                                SELECT A.ID, A.MA, B.*, C.*, D.*,
                                A.NGAYTAO, A.HINHTHUCTHANHTOAN, A.TRANGTHAI,
                                A.TIENKHACHDUA, A.TIENTHUA, A.TONGTIEN
                                FROM HOADON A
                                LEFT JOIN VOUCHER B ON A.ID_VOUCHER = B.ID
                                LEFT JOIN NHANVIEN C ON A.ID_NHANVIEN = C.ID
                                LEFT JOIN KHACHHANG D ON A.ID_KHACHHANG = D.ID
                                WHERE A.TRANGTHAI LIKE N'%Đã%'
                                ORDER BY A.MA DESC
                                """;
    private String XOA_GIOHANG = """
                                 {CALL HuyTatCaHoaDonChiTiet(?)}
                                 """;
    private String NGAY_TAO_MIN = """
                                  SELECT TOP 1 NGAYTAO FROM HOADON ORDER BY NGAYTAO
                                  """;
    private String NGAY_TAO_MAX = """
                                  SELECT TOP 1 NGAYTAO FROM HOADON ORDER BY NGAYTAO DESC
                                  """;

    @Override
    public Integer insert(HoaDon entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getNhanVien().getId(),
                entity.getNgayTao(), entity.getTrangThai());
        return row;
    }

    @Override
    public Integer update(HoaDon entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getVoucher().getId(),
                entity.getKhachHang().getId(), entity.getNhanVien().getId(),
                entity.getNgayTao(), entity.getTongTien(),
                entity.getTienKhachDua(), entity.getTienThua(),
                entity.getHinhThucThanhToan(), entity.getTrangThai(),
                entity.getMaHoaDon(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(HoaDon entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMaHoaDon(), entity.getId());
        return row;
    }

    public Integer huyGioHang(String idHD) {
        int row = JDBCHelper.excuteUpdate(XOA_GIOHANG, idHD);
        return row;
    }

    @Override
    public HoaDon selectbyId(HoaDon entity) {
        HoaDon hoaDon = new HoaDon();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMaHoaDon(), entity.getId());
            while (rs.next()) {

                String id = rs.getString(1);
                String ma = rs.getString(2);
                Voucher voucher = new Voucher(rs.getString(3), rs.getBigDecimal(4),
                        rs.getString(5), rs.getBigDecimal(6), rs.getInt(7), rs.getLong(8),
                        rs.getLong(9), rs.getString(10), rs.getLong(11), rs.getString(12));
                NhanVien nhanVien = new NhanVien(rs.getString(13), rs.getString(14),
                        rs.getString(15), rs.getBoolean(16), rs.getString(17), rs.getString(18),
                        rs.getDate(19), rs.getString(20), rs.getString(21), rs.getString(22));
                KhachHang khachHang = new KhachHang(rs.getString(23), rs.getString(24),
                        rs.getString(25), rs.getBoolean(26), rs.getString(27),
                        rs.getString(28), rs.getString(29));
                Date ngayTao = rs.getDate(30);
                String hinhThucTT = rs.getString(31);
                String trangThai = rs.getString(32);
                BigDecimal tienKhachDua = rs.getBigDecimal(33);
                BigDecimal tienThua = rs.getBigDecimal(34);
                BigDecimal tongTien = rs.getBigDecimal(35);

                hoaDon.setId(id);
                hoaDon.setMaHoaDon(ma);
                hoaDon.setNhanVien(nhanVien);
                hoaDon.setKhachHang(khachHang);
                hoaDon.setVoucher(voucher);
                hoaDon.setNgayTao(ngayTao);
                hoaDon.setHinhThucThanhToan(hinhThucTT);
                hoaDon.setTrangThai(trangThai);
                hoaDon.setTienKhachDua(tienKhachDua);
                hoaDon.setTienThua(tienThua);
                hoaDon.setTongTien(tongTien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return hoaDon;
    }

    @Override
    public List<HoaDon> selectAll() {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                Voucher voucher = new Voucher(rs.getString(3), rs.getBigDecimal(4),
                        rs.getString(5), rs.getBigDecimal(6), rs.getInt(7), rs.getLong(8),
                        rs.getLong(9), rs.getString(10), rs.getLong(11), rs.getString(12));
                NhanVien nhanVien = new NhanVien(rs.getString(13), rs.getString(14),
                        rs.getString(15), rs.getBoolean(16), rs.getString(17), rs.getString(18),
                        rs.getDate(19), rs.getString(20), rs.getString(21), rs.getString(22));
                KhachHang khachHang = new KhachHang(rs.getString(23), rs.getString(24),
                        rs.getString(25), rs.getBoolean(26), rs.getString(27),
                        rs.getString(28), rs.getString(29));
                Date ngayTao = rs.getDate(30);
                String hinhThucTT = rs.getString(31);
                String trangThai = rs.getString(32);
                BigDecimal tienKhachDua = rs.getBigDecimal(33);
                BigDecimal tienThua = rs.getBigDecimal(34);
                BigDecimal tongTien = rs.getBigDecimal(35);

                HoaDon hoaDon = new HoaDon(id, ma, nhanVien, khachHang, voucher,
                        ngayTao, tongTien, trangThai, tienKhachDua, tienThua, hinhThucTT);
                list.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }

    public List<HoaDon> selectAllCho() {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_CHO);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                Voucher voucher = new Voucher(rs.getString(3), rs.getBigDecimal(4),
                        rs.getString(5), rs.getBigDecimal(6), rs.getInt(7), rs.getLong(8),
                        rs.getLong(9), rs.getString(10), rs.getLong(11), rs.getString(12));
                NhanVien nhanVien = new NhanVien(rs.getString(13), rs.getString(14),
                        rs.getString(15), rs.getBoolean(16), rs.getString(17), rs.getString(18),
                        rs.getDate(19), rs.getString(20), rs.getString(21), rs.getString(22));
                KhachHang khachHang = new KhachHang(rs.getString(23), rs.getString(24),
                        rs.getString(25), rs.getBoolean(26), rs.getString(27),
                        rs.getString(28), rs.getString(29));
                Date ngayTao = rs.getDate(30);
                String hinhThucTT = rs.getString(31);
                String trangThai = rs.getString(32);
                BigDecimal tienKhachDua = rs.getBigDecimal(33);
                BigDecimal tienThua = rs.getBigDecimal(34);
                BigDecimal tongTien = rs.getBigDecimal(35);

                HoaDon hoaDon = new HoaDon(id, ma, nhanVien, khachHang, voucher,
                        ngayTao, tongTien, trangThai, tienKhachDua, tienThua, hinhThucTT);
                list.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
    
    public List<HoaDon> selectAllHT() {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_HT);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                Voucher voucher = new Voucher(rs.getString(3), rs.getBigDecimal(4),
                        rs.getString(5), rs.getBigDecimal(6), rs.getInt(7), rs.getLong(8),
                        rs.getLong(9), rs.getString(10), rs.getLong(11), rs.getString(12));
                NhanVien nhanVien = new NhanVien(rs.getString(13), rs.getString(14),
                        rs.getString(15), rs.getBoolean(16), rs.getString(17), rs.getString(18),
                        rs.getDate(19), rs.getString(20), rs.getString(21), rs.getString(22));
                KhachHang khachHang = new KhachHang(rs.getString(23), rs.getString(24),
                        rs.getString(25), rs.getBoolean(26), rs.getString(27),
                        rs.getString(28), rs.getString(29));
                Date ngayTao = rs.getDate(30);
                String hinhThucTT = rs.getString(31);
                String trangThai = rs.getString(32);
                BigDecimal tienKhachDua = rs.getBigDecimal(33);
                BigDecimal tienThua = rs.getBigDecimal(34);
                BigDecimal tongTien = rs.getBigDecimal(35);
                
                HoaDon hoaDon = new HoaDon(id, ma, nhanVien, khachHang, voucher,
                        ngayTao, tongTien, trangThai, tienKhachDua, tienThua, hinhThucTT);
                list.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }

    public Date ngayTaoMin() {
        Date date = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(NGAY_TAO_MIN);
            while (rs.next()) {
                date = rs.getDate(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return date;
    }

    public Date ngayTaoMax() {
        Date date = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(NGAY_TAO_MAX);
            while (rs.next()) {
                date = rs.getDate(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return date;
    }

    public static void main(String[] args) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon("HD00001");
        HoaDon findHD = new HoaDonRepository().selectbyId(hoaDon);
        System.out.println("find: " + findHD);
        List<HoaDon> l = new HoaDonRepository().selectAll();
        for (HoaDon hd : l) {
            System.out.println(hd);
        }
    }

}
