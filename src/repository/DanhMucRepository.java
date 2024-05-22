package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DanhMuc;

public class DanhMucRepository extends BaseCRUD<DanhMuc> {

    private String INSERT = "INSERT DANHMUC (NAME, NGAYTAO) VALUES (? , ?)";
    private String UPDATE = "UPDATE DANHMUC SET NAME = ? WHERE MA = ? OR ID = ?";
    private String DELETE = "DELETE DANHMUC WHERE MA = ? OR ID = ?";
    private String SELECT_BY = "SELECT ID, MA, NAME, NGAYTAO FROM DANHMUC WHERE MA = ? OR ID = ? ORDER BY MA";
    private String SELECT_ALL = "SELECT ID, MA, NAME, NGAYTAO FROM DANHMUC ORDER BY MA DESC";
    
    @Override
    public Integer insert(DanhMuc entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getName(), entity.getNgayTao());
        return row;
    }

    @Override
    public Integer update(DanhMuc entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getName(), entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(DanhMuc entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public DanhMuc selectbyId(DanhMuc entity) {
        DanhMuc danhMuc = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMa(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                danhMuc = new DanhMuc(id, ma, name, ngayTao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return danhMuc;
    }

    @Override
    public List<DanhMuc> selectAll() {
        List<DanhMuc> list = new ArrayList<DanhMuc>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                list.add(new DanhMuc(id, ma, name, ngayTao));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }

}
