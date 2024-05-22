package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.MauSac;

public class MauSacRepository extends BaseCRUD<MauSac> {

    private String INSERT = "INSERT MauSac (NAME, NGAYTAO) VALUES (?, ?)";
    private String UPDATE = "UPDATE MauSac SET NAME = ? WHERE MA = ? OR ID = ?";
    private String DELETE = "DELETE MauSac WHERE MA = ? OR ID = ?";
    private String SELECT_BY = "SELECT ID, MA, NAME, NGAYTAO FROM MauSac WHERE MA = ? OR ID = ? ORDER BY MA";
    private String SELECT_ALL = "SELECT ID, MA, NAME, NGAYTAO FROM MauSac ORDER BY MA DESC";

    @Override
    public Integer insert(MauSac entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getName(), entity.getNgayTao());
        return row;
    }

    @Override
    public Integer update(MauSac entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getName(), entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(MauSac entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public MauSac selectbyId(MauSac entity) {
        MauSac MauSac = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMa(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                MauSac = new MauSac(id, ma, name, ngayTao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return MauSac;
    }

    @Override
    public List<MauSac> selectAll() {
        List<MauSac> list = new ArrayList<MauSac>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                list.add(new MauSac(id, ma, name, ngayTao));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
        
    public static void main(String[] args) {
        List<MauSac> l = new ArrayList<>();
        l = new MauSacRepository().selectAll();
        for (MauSac MauSac : l) {
            System.out.println(MauSac);
        }
    }

}
