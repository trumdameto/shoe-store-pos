package repository;

import model.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import repository.DBConnect;

public class Account {

    public Integer InsertNV(NhanVien nv) {
        String sql = "Insert into NhanVien (id,ma,ten,gioitinh,sdt,diachi,ngaysinh,matkhau,vaitro,trangthai) values (newid(),dbo.AUTO_MaNV(),?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getTen());
            ps.setBoolean(2, nv.getGioitinh());
            ps.setString(3, nv.getSdt());
            ps.setString(4, nv.getDiachi());
            ps.setDate(5, new java.sql.Date(nv.getNgaysinh().getTime()));
            ps.setString(6, nv.getMatkhau());
            ps.setString(7, nv.getVaitro());
            ps.setString(8, nv.getTrangthai());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public ArrayList<NhanVien> getNV() {
        ArrayList<NhanVien> list = new ArrayList<>();
        String sql = "Select * from NhanVien";

        try {
            ResultSet rs = JDBCHelper.executeQuery(sql);
            while (rs.next()) {
                list.add(new NhanVien(
                        rs.getString("id"),
                        rs.getString("ma"),
                        rs.getString("ten"),
                        rs.getBoolean("gioitinh"),
                        rs.getString("sdt"),
                        rs.getString("diachi"),
                        rs.getDate("ngaysinh"),
                        rs.getString("matkhau"),
                        rs.getString("vaitro"),
                        rs.getString("trangthai")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<NhanVien> selectBySql(String sql, Object... args) {
        ArrayList<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JDBCHelper.executeQuery(sql, args);
                while (rs.next()) {
                    NhanVien entity = new NhanVien();
                    entity.setId(rs.getString("ID"));
                    entity.setMa(rs.getString("MA"));
                    entity.setTen(rs.getString("TEN"));
                    entity.setGioitinh(rs.getBoolean("GIOITINH"));
                    entity.setSdt(rs.getString("SDT"));
                    entity.setDiachi(rs.getString("DIACHI"));
                    entity.setNgaysinh(rs.getDate("NGAYSINH"));
                    entity.setMatkhau(rs.getString("MATKHAU"));
                    entity.setVaitro(rs.getString("VAITRO"));
                    entity.setTrangthai(rs.getString("TRANGTHAI"));
                    list.add(entity);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    public NhanVien selectById(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE MA=?";
        ArrayList<NhanVien> list = selectBySql(sql, maNV);
        return list.size() > 0 ? list.get(0) : null;
    }
}
