package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KieuDang;

public class KieuDangRepository extends BaseCRUD<KieuDang> {

    private String INSERT = "INSERT KieuDang (NAME, NGAYTAO) VALUES (?, ?)";
    private String UPDATE = "UPDATE KieuDang SET NAME = ? WHERE MA = ? OR ID = ?";
    private String DELETE = "DELETE KieuDang WHERE MA = ? OR ID = ?";
    private String SELECT_BY = "SELECT ID, MA, NAME, NGAYTAO FROM KieuDang WHERE MA = ? OR ID = ? ORDER BY MA";
    private String SELECT_ALL = "SELECT ID, MA, NAME, NGAYTAO FROM KieuDang ORDER BY MA DESC";

    @Override
    public Integer insert(KieuDang entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getName(), entity.getNgayTao());
        return row;
    }

    @Override
    public Integer update(KieuDang entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getName(), entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(KieuDang entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public KieuDang selectbyId(KieuDang entity) {
        KieuDang KieuDang = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMa(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                KieuDang = new KieuDang(id, ma, name, ngayTao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return KieuDang;
    }

    @Override
    public List<KieuDang> selectAll() {
        List<KieuDang> list = new ArrayList<KieuDang>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                list.add(new KieuDang(id, ma, name, ngayTao));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
        
    public static void main(String[] args) {
        List<KieuDang> l = new ArrayList<>();
        l = new KieuDangRepository().selectAll();
        for (KieuDang KieuDang : l) {
            System.out.println(KieuDang);
        }
    }

}
