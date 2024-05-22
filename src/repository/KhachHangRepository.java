package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;

public class KhachHangRepository extends BaseCRUD<KhachHang>{
    
    private String INSERT = "INSERT INTO KHACHHANG (TEN, GIOITINH, SDT, DIACHI, EMAIL) VALUES (?, ?, ?, ?, ?)";
    private String UPDATE = "UPDATE KHACHHANG SET TEN = ?, GIOITINH = ?, SDT = ?, DIACHI = ?, EMAIL = ? WHERE MA = ? OR ID = ?";
    private String DELETE = "DELETE KHACHHANG WHERE MA = ? OR ID = ?";
    private String SELECT_BY = "SELECT ID, MA, TEN, GIOITINH, SDT, DIACHI, EMAIL FROM KHACHHANG WHERE MA = ? OR ID = ? ORDER BY MA";
    private String SELECT_ALL = "SELECT ID, MA, TEN, GIOITINH, SDT, DIACHI, EMAIL FROM KHACHHANG ORDER BY MA DESC";

    @Override
    public Integer insert(KhachHang entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getName(), 
                entity.getGioiTinh(), entity.getSdt(), entity.getDiaChi(), 
                    entity.getEmail());
        return row;
    }

    @Override
    public Integer update(KhachHang entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getName(), 
                entity.getGioiTinh(), entity.getSdt(), entity.getDiaChi(), 
                entity.getEmail(), entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(KhachHang entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public KhachHang selectbyId(KhachHang entity) {
        KhachHang khachHang = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMa(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Boolean gioiTinh = rs.getBoolean(4);
                String sdt = rs.getString(5);
                String diaChi = rs.getString(6);
                String email = rs.getString(7);
                khachHang = new KhachHang(id, ma, name, gioiTinh, sdt, diaChi, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return khachHang;
    }

    @Override
    public List<KhachHang> selectAll() {
        List<KhachHang> list = new ArrayList<KhachHang>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Boolean gioiTinh = rs.getBoolean(4);
                String sdt = rs.getString(5);
                String diaChi = rs.getString(6);
                String email = rs.getString(7);
                list.add(new KhachHang(id, ma, name, gioiTinh, sdt, diaChi, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
    
}
