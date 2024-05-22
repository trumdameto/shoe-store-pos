package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Giay;

public class GiayRepository extends BaseCRUD<Giay> {

    private String INSERT = "INSERT Giay (NAME, NGAYTAO) VALUES (?, ?)";
    private String UPDATE = "UPDATE Giay SET NAME = ? WHERE MA_GIAY = ? OR ID = ?";
    private String DELETE = "DELETE Giay WHERE MA_GIAY = ? OR ID = ?";
    private String SELECT_BY = "SELECT ID, MA_GIAY, NAME, NGAYTAO FROM Giay WHERE MA_GIAY = ? OR ID = ? ORDER BY MA_GIAY";
    private String SELECT_ALL = "SELECT ID, MA_GIAY, NAME, NGAYTAO FROM Giay ORDER BY MA_GIAY DESC";

    @Override
    public Integer insert(Giay entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getName(), entity.getNgayTao());
        return row;
    }

    @Override
    public Integer update(Giay entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getName(), entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(Giay entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Giay selectbyId(Giay entity) {
        Giay Giay = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMa(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                Giay = new Giay(id, ma, name, ngayTao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return Giay;
    }

    @Override
    public List<Giay> selectAll() {
        List<Giay> list = new ArrayList<Giay>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                list.add(new Giay(id, ma, name, ngayTao));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
    
    public static void main(String[] args) {
        List<Giay> l = new ArrayList<>();
        l = new GiayRepository().selectAll();
        for (Giay giay : l) {
            System.out.println(giay);
        }
    }

}
