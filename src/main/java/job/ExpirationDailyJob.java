package job;

import dbdao.CouponDBDAO;
import entities.Coupon;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExpirationDailyJob implements Runnable {

	private CouponDBDAO couponDAO;
	private LocalTime checkTime;

	private boolean running = false;
	private Thread job;

	public ExpirationDailyJob() {
		couponDAO = new CouponDBDAO();
		checkTime = LocalTime.of(10, 30);
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		job = new Thread(this);
		job.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			job.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (running) {
			if (LocalTime.now().isBefore(checkTime) && LocalTime.now().plusHours(1).isAfter(checkTime)) {

				for (Coupon coupon : couponDAO.getAllCoupons()) {
					if (LocalDate.now().isAfter(coupon.getEndDate())) {

						couponDAO.deletePurchaseByCouponID(coupon.getId());
						couponDAO.deleteCoupon(coupon.getId());
					}
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
