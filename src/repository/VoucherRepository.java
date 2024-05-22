package repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Voucher;

public class VoucherRepository extends BaseCRUD<Voucher> {

    private String INSERT = "INSERT INTO VOUCHER (MAGIAMGIA, GIATRI, DIEUKIEN, SOLUONG, NGAYBD, NGAYKT, LOAI, NGAYTAO, TRANGTHAI) VALUES (? , ?, ? , ?, ? , ?, ? , ?, ?)";
    private String UPDATE = "UPDATE VOUCHER SET GIATRI = ?, DIEUKIEN = ?, SOLUONG = ?, NGAYBD = ?, NGAYKT = ?, LOAI = ?, TRANGTHAI = ? WHERE MAGIAMGIA = ? OR ID = ?";
    private String DELETE = "DELETE VOUCHER WHERE MAGIAMGIA = ? OR ID = ?";
    private String SELECT_BY = "select ID, GIATRI, MAGIAMGIA, DIEUKIEN, SOLUONG, NGAYBD, NGAYKT, LOAI, NGAYTAO, TRANGTHAI from VOUCHER WHERE MAGIAMGIA = ? OR ID = ? AND TRANGTHAI LIKE N'%ĐANG%'";
    private String SELECT_ALL = "select ID, GIATRI, MAGIAMGIA, DIEUKIEN, SOLUONG, NGAYBD, NGAYKT, LOAI, NGAYTAO, TRANGTHAI from VOUCHER ORDER BY NGAYTAO DESC";
    private String SELECT_DANG = "select ID, GIATRI, MAGIAMGIA, DIEUKIEN, SOLUONG, NGAYBD, NGAYKT, LOAI, NGAYTAO, TRANGTHAI from VOUCHER WHERE TRANGTHAI LIKE N'%ĐANG%' ORDER BY NGAYTAO DESC";

    @Override
    public Integer insert(Voucher entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getMaGiamGia(), entity.getGiaTri(),
                entity.getDieuKien(), entity.getSoLuong(), entity.getNgayBD(), entity.getNgayKT(), entity.getLoai(),
                entity.getNgayTao(), entity.getTrangThai());
        return row;
    }

    @Override
    public Integer update(Voucher entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getGiaTri(),
                entity.getDieuKien(), entity.getSoLuong(), entity.getNgayBD(), entity.getNgayKT(), entity.getLoai(),
                entity.getTrangThai(), entity.getMaGiamGia(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(Voucher entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMaGiamGia(), entity.getId());
        return row;
    }

    @Override
    public Voucher selectbyId(Voucher entity) {
        Voucher voucher = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMaGiamGia(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                BigDecimal giaTri = rs.getBigDecimal(2);
                String ma = rs.getString(3);
                BigDecimal dieuKien = rs.getBigDecimal(4);
                Integer soLuong = rs.getInt(5);
                Long ngayBD = rs.getLong(6);
                Long ngayKT = rs.getLong(7);
                String loai = rs.getString(8);
                Long ngayTao = rs.getLong(9);
                String trangThai = rs.getString(10);
                voucher = new Voucher(id, giaTri, ma, dieuKien, soLuong, ngayBD, ngayKT, loai, ngayTao, trangThai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return voucher;
    }

    @Override
    public List<Voucher> selectAll() {
        List<Voucher> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                BigDecimal giaTri = rs.getBigDecimal(2);
                String ma = rs.getString(3);
                BigDecimal dieuKien = rs.getBigDecimal(4);
                Integer soLuong = rs.getInt(5);
                Long ngayBD = rs.getLong(6);
                Long ngayKT = rs.getLong(7);
                String loai = rs.getString(8);
                Long ngayTao = rs.getLong(9);
                String trangThai = rs.getString(10);
                Voucher voucher = new Voucher(id, giaTri, ma, dieuKien, soLuong, ngayBD, ngayKT, loai, ngayTao, trangThai);
                list.add(voucher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
    
    public List<Voucher> selectAllDang() {
        List<Voucher> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_DANG);
            while (rs.next()) {
                String id = rs.getString(1);
                BigDecimal giaTri = rs.getBigDecimal(2);
                String ma = rs.getString(3);
                BigDecimal dieuKien = rs.getBigDecimal(4);
                Integer soLuong = rs.getInt(5);
                Long ngayBD = rs.getLong(6);
                Long ngayKT = rs.getLong(7);
                String loai = rs.getString(8);
                Long ngayTao = rs.getLong(9);
                String trangThai = rs.getString(10);
                Voucher voucher = new Voucher(id, giaTri, ma, dieuKien, soLuong, ngayBD, ngayKT, loai, ngayTao, trangThai);
                list.add(voucher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
    
    public static void main(String[] args) {
        List<Voucher> list = new VoucherRepository().selectAll();
        for (Voucher voucher : list) {
            System.out.println(voucher);
        }
    }

}
