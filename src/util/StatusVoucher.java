package util;

import java.util.Date;
import model.Voucher;
import repository.VoucherRepository;

public class StatusVoucher extends Thread {

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
            }
            VoucherRepository voucherRepository = new VoucherRepository();
            Long currentTime = new Date().getTime() / 1000;
            System.out.println("Bắt đầu chạy");
            for (Voucher voucher : voucherRepository.selectAll()) {
                if (voucher.getTrangThai().equalsIgnoreCase("Đã hủy") == false) {
                    if (voucher.getTrangThai().equalsIgnoreCase("Hết voucher") == false && voucher.getSoLuong() > 0) {
                        if (voucher.getNgayKT() <= currentTime) {
                            voucher.setTrangThai("Đã kết thúc");
                            voucherRepository.update(voucher);
                        } else if (voucher.getNgayBD() <= currentTime) {
                            voucher.setTrangThai("Đang diễn ra");
                            voucherRepository.update(voucher);
                        } else {
                            voucher.setTrangThai("Sắp diễn ra");
                            voucherRepository.update(voucher);
                        }
                    }
                }
            }
        } while (true);
    }
}
