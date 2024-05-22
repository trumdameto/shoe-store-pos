package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KichCo;

public class KichCoRepository extends BaseCRUD<KichCo> {

    private String INSERT = "INSERT KichCo (NAME, NGAYTAO) VALUES (?, ?)";
    private String UPDATE = "UPDATE KichCo SET NAME = ? WHERE MA = ? OR ID = ?";
    private String DELETE = "DELETE KichCo WHERE MA = ? OR ID = ?";
    private String SELECT_BY = "SELECT ID, MA, NAME, NGAYTAO FROM KichCo WHERE MA = ? OR ID = ? ORDER BY MA";
    private String SELECT_ALL = "SELECT ID, MA, NAME, NGAYTAO FROM KichCo ORDER BY MA DESC";

    @Override
    public Integer insert(KichCo entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getName(), entity.getNgayTao());
        return row;
    }

    @Override
    public Integer update(KichCo entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getName(), entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public Integer delete(KichCo entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getMa(), entity.getId());
        return row;
    }

    @Override
    public KichCo selectbyId(KichCo entity) {
        KichCo KichCo = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY, entity.getMa(), entity.getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                KichCo = new KichCo(id, ma, name, ngayTao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return KichCo;
    }

    @Override
    public List<KichCo> selectAll() {
        List<KichCo> list = new ArrayList<KichCo>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_ALL);
            while (rs.next()) {
                String id = rs.getString(1);
                String ma = rs.getString(2);
                String name = rs.getString(3);
                Long ngayTao = rs.getLong(4);
                list.add(new KichCo(id, ma, name, ngayTao));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }
        
    public static void main(String[] args) {
        List<KichCo> l = new ArrayList<>();
        l = new KichCoRepository().selectAll();
        for (KichCo KichCo : l) {
            System.out.println(KichCo);
        }
    }

}
