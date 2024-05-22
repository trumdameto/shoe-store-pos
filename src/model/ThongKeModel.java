package model;

import java.math.BigDecimal;
import java.util.Date;

public class ThongKeModel {
    private Date date;
    private BigDecimal doanhthu;

    public ThongKeModel() {
    }

    public ThongKeModel(Date date, BigDecimal doanhthu) {
        this.date = date;
        this.doanhthu = doanhthu;
        
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getDoanhthu() {
        return doanhthu;
    }

    public void setDoanhthu(BigDecimal doanhthu) {
        this.doanhthu = doanhthu;
    }
    
    
}