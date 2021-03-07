package facades;

import categories.Category;
import entities.Coupon;
import entities.Customer;
import exceptions.AlreadyExistException;
import exceptions.ExpiredException;
import exceptions.NotExistException;
import exceptions.NotLoggedException;
import exceptions.SoldOutException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerFacade extends ClientFacade {
	private Long customerId;
//	private Long companyId;

	private boolean isLoggedIn() {
		return customerId != null;
	}

	public CustomerFacade() {
	}

	@Override
	public boolean logIn(String email, String password) {
		if (!customerDAO.login(email, password))
			return false;
		customerId = customerDAO.getByEmailAndPassword(email, password).getId();
		return true;
	}

	public Coupon purchaseCoupon(Coupon coupon)
			throws NotExistException, ExpiredException, SoldOutException, NotLoggedException, AlreadyExistException {

		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}

		long couponId = coupon.getId();

		if (couponDAO.getOneCoupon(couponId) == null) {
			throw new NotExistException("the coupon: " + coupon + "doesn't exist");
		}

		if (coupon.getEndDate().isBefore(LocalDate.now())) {
			throw new ExpiredException("coupon:" + coupon + " has already expired ");
		} else {
			if (coupon.getAmount() == 0) {
				throw new SoldOutException("the coupon: " + coupon + "is sold out");
			} else {
				int amount = coupon.getAmount();
				amount--;
				coupon.setAmount(amount);
				couponDAO.updateCoupon(coupon);
				couponDAO.addCouponPurchase(customerId, couponId);
			}
		}

		return coupon;
	}

	public List<Coupon> getCustomerCoupons() throws NotLoggedException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		List<Coupon> getAll = new ArrayList<>();
		getAll = couponDAO.getAllCoupons();
		return getAll;
	}

	public List<Coupon> getCustomerCoupons(Category category) throws NotLoggedException, NotExistException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to log in ");
		}
		return couponDAO.getByCustomerAndCategory(customerId, category);
	}

	public List<Coupon> getCustomerCoupons(double maxPrice) throws NotLoggedException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		List<Coupon> coupons = getCustomerCoupons();
		List<Coupon> getAll = new ArrayList<>();
		for (Coupon coupon : coupons) {
			if (coupon.getPrice() < maxPrice) {
				getAll.add(coupon);
			}
		}
		return getAll;
	}

	public Customer getCustomerDetails() throws NotLoggedException, NotExistException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		Customer customer = null;
		customer = customerDAO.getOneCustomer(customerId);
		if (customerDAO.getOneCustomer(customerId) == null) {
			throw new NotExistException("the customer by the id:" + customerId + "doesn't exists");
		}
		return customer;
	}
}
