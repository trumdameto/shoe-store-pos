package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Hang;

public class HangRepository extends BaseCRUD<Hang> {

    private String INSERT = "INSERT Hang (NAME, NGAYTAO) VALUES (?, ?)";
    private String UPDATE = "UPDATE Hang SET NAME = ? WHERE MA = ? OR ID = ?";
    private String DELETE = "DELETE Hang WHERE MA = ? OR ID = ?";
    private String SELECT_BY = "SELECT ID, MA, NAME, NGAYTAO FROM Hang WHERE MA = ? OR ID = ? ORDER BY MA";
    private String SELECT_ALL = "SELECT ID, MA, NAME, NGAYTAO FROM Hang ORDER BY MA DESC";

    @Override
    public Integer insert(Hang entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getName(), entity.getNgayTao());
        return row;
    }

    @Override
    public Integer update(Hang entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getName(), entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(Hang entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Hang selectbyId(Hang entity) {
        Hang Hang = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMa(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                Hang = new Hang(id, ma, name, ngayTao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return Hang;
    }

    @Override
    public List<Hang> selectAll() {
        List<Hang> list = new ArrayList<Hang>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                list.add(new Hang(id, ma, name, ngayTao));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
        
    public static void main(String[] args) {
        List<Hang> l = new ArrayList<>();
        l = new HangRepository().selectAll();
        for (Hang hang : l) {
            System.out.println(hang);
        }
    }

}
