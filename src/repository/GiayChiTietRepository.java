package repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DanhMuc;
import model.Giay;
import model.GiayChiTiet;
import model.GiayChiTiet;
import model.Hang;
import model.KichCo;
import model.KieuDang;
import model.MauSac;

public class GiayChiTietRepository extends BaseCRUD<GiayChiTiet> {

    private String INSERT = """
                            INSERT INTO GIAYCHITIET (ID_GIAY, ID_HANG, 
                            ID_KIEUDANG, ID_DANHMUC, ID_MAUSAC, ID_KICHCO, 
                            HINHANH, GIA, SOLUONG, NGAYTAO, TRANGTHAI, MOTA) 
                            VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
                            """;
    private String UPDATE = """
                            UPDATE GIAYCHITIET SET ID_GIAY = ?, ID_HANG = ?, 
                            ID_KIEUDANG = ?, ID_DANHMUC = ?, ID_MAUSAC = ?, ID_KICHCO = ?, 
                            HINHANH = ?, GIA = ?, SOLUONG = ?, NGAYTAO = ?, TRANGTHAI = ?, 
                            MOTA = ? WHERE MA = ? OR ID = ?
                            """;
    private String UPDATE_SOLUONG = """
                            UPDATE GIAYCHITIET SET SOLUONG = ? WHERE MA = ?
                            """;
    private String DELETE = "DELETE GiayChiTiet WHERE MA = ? OR ID = ?";
    private String SELECT_BY = """
                               SELECT A.ID, A.MA, B.*, C.*, D.*, E.*, G.*, H.*, 
                               A.HINHANH, A.GIA, A.SOLUONG, A.NGAYTAO, A.TRANGTHAI, 
                               A.MOTA FROM GIAYCHITIET A 
                               JOIN GIAY B ON A.ID_GIAY = B.ID
                               JOIN HANG C ON A.ID_HANG = C.ID
                               JOIN KIEUDANG D ON A.ID_KIEUDANG = D.ID
                               JOIN DANHMUC E ON A.ID_DANHMUC = E.ID
                               JOIN MAUSAC G ON A.ID_MAUSAC = G.ID
                               JOIN KICHCO H ON A.ID_KICHCO = H.ID
                               WHERE A.MA = ? OR A.ID = ?
                               ORDER BY A.NGAYTAO DESC
                               """;
    private String SELECT_BY_GIAY = """
                               SELECT A.ID, A.MA, B.*, C.*, D.*, E.*, G.*, H.*, 
                               A.HINHANH, A.GIA, A.SOLUONG, A.NGAYTAO, A.TRANGTHAI, 
                               A.MOTA FROM GIAYCHITIET A 
                               JOIN GIAY B ON A.ID_GIAY = B.ID
                               JOIN HANG C ON A.ID_HANG = C.ID
                               JOIN KIEUDANG D ON A.ID_KIEUDANG = D.ID
                               JOIN DANHMUC E ON A.ID_DANHMUC = E.ID
                               JOIN MAUSAC G ON A.ID_MAUSAC = G.ID
                               JOIN KICHCO H ON A.ID_KICHCO = H.ID
                               WHERE A.ID_GIAY = ? AND A.TRANGTHAI LIKE N'%Còn%'
                               ORDER BY A.NGAYTAO DESC
                               """;
    private String SELECT_ALL = """
                                SELECT A.ID, A.MA, B.*, C.*, D.*, E.*, G.*, H.*, 
                                A.HINHANH, A.GIA, A.SOLUONG, A.NGAYTAO, A.TRANGTHAI, 
                                A.MOTA FROM GIAYCHITIET A 
                                JOIN GIAY B ON A.ID_GIAY = B.ID
                                JOIN HANG C ON A.ID_HANG = C.ID
                                JOIN KIEUDANG D ON A.ID_KIEUDANG = D.ID
                                JOIN DANHMUC E ON A.ID_DANHMUC = E.ID
                                JOIN MAUSAC G ON A.ID_MAUSAC = G.ID
                                JOIN KICHCO H ON A.ID_KICHCO = H.ID 
                                WHERE A.TRANGTHAI LIKE N'%Còn%'
                                ORDER BY A.MA DESC 
                                """;
    private String SELECT_BY_HD_CT = """
                                SELECT A.ID, A.MA, B.*, C.*, D.*, E.*, G.*, H.*, 
                                                                A.HINHANH, A.GIA, A.SOLUONG, A.NGAYTAO, A.TRANGTHAI, 
                                                                A.MOTA FROM GIAYCHITIET A 
                                                                JOIN GIAY B ON A.ID_GIAY = B.ID
                                                                JOIN HANG C ON A.ID_HANG = C.ID
                                                                JOIN KIEUDANG D ON A.ID_KIEUDANG = D.ID
                                                                JOIN DANHMUC E ON A.ID_DANHMUC = E.ID
                                                                JOIN MAUSAC G ON A.ID_MAUSAC = G.ID
                                                                JOIN KICHCO H ON A.ID_KICHCO = H.ID
                                                                RIGHT JOIN HOADONCHITIET K ON K.ID_GIAYCT = A.ID
                                                                WHERE K.ID_HOADON = ?
                                                                ORDER BY A.NGAYTAO DESC
                                """;

    @Override
    public Integer insert(GiayChiTiet entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getGiay().getId(),
                entity.getHang().getId(), entity.getKieuDang().getId(), entity.getDanhMuc().getId(),
                entity.getMauSac().getId(), entity.getKichCo().getId(), entity.getHinhAnh(),
                entity.getGia(), entity.getSoLuong(), entity.getNgayTao(),
                entity.getTrangThai(), entity.getMoTa());
        return row;
    }

    @Override
    public Integer update(GiayChiTiet entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getGiay().getId(),
                entity.getHang().getId(), entity.getKieuDang().getId(), entity.getDanhMuc().getId(),
                entity.getMauSac().getId(), entity.getKichCo().getId(), entity.getHinhAnh(),
                entity.getGia(), entity.getSoLuong(), entity.getNgayTao(),
                entity.getTrangThai(), entity.getMoTa(), entity.getMa(), entity.getId());
        return row;
    }

    public Integer updateSoLuong(int soLuongMoi, String ma) {
        int row = JDBCHelper.excuteUpdate(UPDATE_SOLUONG, soLuongMoi, ma);
        return row;
    }

    @Override
    public Integer delete(GiayChiTiet entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public GiayChiTiet selectbyId(GiayChiTiet entity) {
        GiayChiTiet giayChiTiet = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMa(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                Giay giay = new Giay(rs.getString(3), rs.getString(4), rs.getString(5), rs.getLong(6));
                Hang hang = new Hang(rs.getString(7), rs.getString(8), rs.getString(9), rs.getLong(10));
                KieuDang kieuDang = new KieuDang(rs.getString(11), rs.getString(12), rs.getString(13), rs.getLong(14));
                DanhMuc danhMuc = new DanhMuc(rs.getString(15), rs.getString(16), rs.getString(17), rs.getLong(18));
                MauSac mauSac = new MauSac(rs.getString(19), rs.getString(20), rs.getString(21), rs.getLong(22));
                KichCo kichCo = new KichCo(rs.getString(23), rs.getString(24), rs.getString(25), rs.getLong(26));
                String hinhAnh = rs.getString(27);
                BigDecimal gia = rs.getBigDecimal(28);
                Integer soLuong = rs.getInt(29);
                Long ngayTao = rs.getLong(30);
                String trangThai = rs.getString(31);
                String moTa = rs.getString(32);
                giayChiTiet = new GiayChiTiet(id, ma, giay, hang, kieuDang, danhMuc, mauSac, kichCo, hinhAnh, gia, soLuong, ngayTao, trangThai, moTa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return giayChiTiet;
    }

    @Override
    public List<GiayChiTiet> selectAll() {
        List<GiayChiTiet> list = new ArrayList<GiayChiTiet>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                Giay giay = new Giay(rs.getString(3), rs.getString(4), rs.getString(5), rs.getLong(6));
                Hang hang = new Hang(rs.getString(7), rs.getString(8), rs.getString(9), rs.getLong(10));
                KieuDang kieuDang = new KieuDang(rs.getString(11), rs.getString(12), rs.getString(13), rs.getLong(14));
                DanhMuc danhMuc = new DanhMuc(rs.getString(15), rs.getString(16), rs.getString(17), rs.getLong(18));
                MauSac mauSac = new MauSac(rs.getString(19), rs.getString(20), rs.getString(21), rs.getLong(22));
                KichCo kichCo = new KichCo(rs.getString(23), rs.getString(24), rs.getString(25), rs.getLong(26));
                String hinhAnh = rs.getString(27);
                BigDecimal gia = rs.getBigDecimal(28);
                Integer soLuong = rs.getInt(29);
                Long ngayTao = rs.getLong(30);
                String trangThai = rs.getString(31);
                String moTa = rs.getString(32);
                GiayChiTiet giayChiTiet = new GiayChiTiet(id, ma, giay, hang, kieuDang, danhMuc, mauSac, kichCo, hinhAnh, gia, soLuong, ngayTao, trangThai, moTa);
                list.add(giayChiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }

    public List<GiayChiTiet> selectByGiay(Giay g) {
        List<GiayChiTiet> list = new ArrayList<GiayChiTiet>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY_GIAY, g.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                Giay giay = new Giay(rs.getString(3), rs.getString(4), rs.getString(5), rs.getLong(6));
                Hang hang = new Hang(rs.getString(7), rs.getString(8), rs.getString(9), rs.getLong(10));
                KieuDang kieuDang = new KieuDang(rs.getString(11), rs.getString(12), rs.getString(13), rs.getLong(14));
                DanhMuc danhMuc = new DanhMuc(rs.getString(15), rs.getString(16), rs.getString(17), rs.getLong(18));
                MauSac mauSac = new MauSac(rs.getString(19), rs.getString(20), rs.getString(21), rs.getLong(22));
                KichCo kichCo = new KichCo(rs.getString(23), rs.getString(24), rs.getString(25), rs.getLong(26));
                String hinhAnh = rs.getString(27);
                BigDecimal gia = rs.getBigDecimal(28);
                Integer soLuong = rs.getInt(29);
                Long ngayTao = rs.getLong(30);
                String trangThai = rs.getString(31);
                String moTa = rs.getString(32);
                GiayChiTiet giayChiTiet = new GiayChiTiet(id, ma, giay, hang, kieuDang, danhMuc, mauSac, kichCo, hinhAnh, gia, soLuong, ngayTao, trangThai, moTa);
                list.add(giayChiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }

    public List<GiayChiTiet> selectByHD(String idHD) {
        List<GiayChiTiet> list = new ArrayList<GiayChiTiet>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY_HD_CT, idHD);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                Giay giay = new Giay(rs.getString(3), rs.getString(4), rs.getString(5), rs.getLong(6));
                Hang hang = new Hang(rs.getString(7), rs.getString(8), rs.getString(9), rs.getLong(10));
                KieuDang kieuDang = new KieuDang(rs.getString(11), rs.getString(12), rs.getString(13), rs.getLong(14));
                DanhMuc danhMuc = new DanhMuc(rs.getString(15), rs.getString(16), rs.getString(17), rs.getLong(18));
                MauSac mauSac = new MauSac(rs.getString(19), rs.getString(20), rs.getString(21), rs.getLong(22));
                KichCo kichCo = new KichCo(rs.getString(23), rs.getString(24), rs.getString(25), rs.getLong(26));
                String hinhAnh = rs.getString(27);
                BigDecimal gia = rs.getBigDecimal(28);
                Integer soLuong = rs.getInt(29);
                Long ngayTao = rs.getLong(30);
                String trangThai = rs.getString(31);
                String moTa = rs.getString(32);
                GiayChiTiet giayChiTiet = new GiayChiTiet(id, ma, giay, hang, kieuDang, danhMuc, mauSac, kichCo, hinhAnh, gia, soLuong, ngayTao, trangThai, moTa);
                list.add(giayChiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }

    public static void main(String[] args) {
        String idHD = "998E3CEF-3734-41AB-9CEE-00B55FEFA4AA";
        GiayChiTiet giayChiTiet = new GiayChiTiet();
        giayChiTiet.setId(idHD);
        GiayChiTiet gct = new GiayChiTietRepository().selectbyId(giayChiTiet);
        System.out.println(gct);
//        List<GiayChiTiet> l = new ArrayList<>();
//        l = new GiayChiTietRepository().selectByHD(idHD);
//        for (GiayChiTiet GiayChiTiet : l) {
//            System.out.println(GiayChiTiet);
//        }
    }

}
