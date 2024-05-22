package repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.GiayChiTiet;
import model.HoaDon;
import model.HoaDonChiTiet;

public class HoaDonChiTietRepository extends BaseCRUD<HoaDonChiTiet> {

    private String INSERT = """
                            INSERT INTO HOADONCHITIET (ID_GIAYCT, ID_HOADON, GIA, SOLUONG, TRANGTHAI) values (?, ?, ?, ?, ?)
                            """;
    private String UPDATE = """
                            UPDATE HOADONCHITIET SET SOLUONG = ?, TRANGTHAI = ?, GIA = ? WHERE ID_GIAYCT = ? AND ID_HOADON = ?
                            """;
    private String DELETE = """
                            DELETE HOADONCHITIET WHERE ID = ?
                            """;
    private String SELECT_BY_HD = """
                                  select * from HOADONCHITIET
                                  WHERE ID_HOADON = ?
                                  """;
    private String SELECT_BY_HDG = """
                                  select * from HOADONCHITIET
                                  WHERE ID_HOADON = ? AND ID_GIAYCT = ? 
                                  """;
    
    private GiayChiTietRepository giayChiTietRepository = new GiayChiTietRepository();

    @Override
    public Integer insert(HoaDonChiTiet entity) {
        int row = JDBCHelper.excuteUpdate(INSERT, entity.getGiayChiTiet().getId(),
                entity.getHoaDon().getId(), entity.getGia(), entity.getSoLuong(),
                entity.getTrangThai());
        return row;
    }

    @Override
    public Integer update(HoaDonChiTiet entity) {
        int row = JDBCHelper.excuteUpdate(UPDATE, entity.getSoLuong(),
                entity.getTrangThai(), entity.getGia(), entity.getGiayChiTiet().getId(),
                entity.getHoaDon().getId());
        return row;
    }

    @Override
    public Integer delete(HoaDonChiTiet entity) {
        int row = JDBCHelper.excuteUpdate(DELETE, entity.getId());
        return row;
    }

    @Override
    public HoaDonChiTiet selectbyId(HoaDonChiTiet entity) {
        HoaDonChiTiet hoaDonChiTiet = null;
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY_HDG, entity.getHoaDon().getId(), entity.getGiayChiTiet().getId());
            while (rs.next()) {
                String id = rs.getString(1);
                String idGiay = rs.getString(2);
                String idHoaDon = rs.getString(3);
                GiayChiTiet findGiay = new GiayChiTiet();
                findGiay.setId(idGiay);
                HoaDon findHoaDon = new HoaDon();
                findHoaDon.setId(idHoaDon);
                GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiay);
                HoaDon hoaDon = new HoaDonRepository().selectbyId(findHoaDon);
                BigDecimal gia = rs.getBigDecimal(4);
                Integer soLuong = rs.getInt(5);
                String trangThai = rs.getString(6);
                hoaDonChiTiet = new HoaDonChiTiet(id, giayChiTiet, hoaDon, gia, soLuong, trangThai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return hoaDonChiTiet;
    }

    public List<HoaDonChiTiet> selectAll(String idHD) {
        List<HoaDonChiTiet> list = new ArrayList<HoaDonChiTiet>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(SELECT_BY_HD, idHD);
            while (rs.next()) {
                String id = rs.getString(1);
                String idGiay = rs.getString(2);
                String idHoaDon = rs.getString(3);
                GiayChiTiet findGiay = new GiayChiTiet();
                findGiay.setId(idGiay);
                HoaDon findHoaDon = new HoaDon();
                findHoaDon.setId(idHoaDon);
                GiayChiTiet giayChiTiet = giayChiTietRepository.selectbyId(findGiay);
                HoaDon hoaDon = new HoaDonRepository().selectbyId(findHoaDon);
                BigDecimal gia = rs.getBigDecimal(4);
                Integer soLuong = rs.getInt(5);
                String trangThai = rs.getString(6);
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet(id, giayChiTiet, hoaDon, gia, soLuong, trangThai);
                list.add(hoaDonChiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCHelper.closeConnection();
        }
        return list;
    }

    public static void main(String[] args) {
        List<HoaDonChiTiet> l = new HoaDonChiTietRepository().selectAll("49309C5B-9592-4C9A-A8DC-12588CB29168");
        System.out.println(l.size());
        for (HoaDonChiTiet o : l) {
            System.out.println(o);
        }
    }

    @Override
    public List<HoaDonChiTiet> selectAll() {
        return null;
    }

}
