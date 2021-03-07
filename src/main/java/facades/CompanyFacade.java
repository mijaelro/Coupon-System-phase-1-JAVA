package facades;

import categories.Category;
import entities.Company;
import entities.Coupon;
import exceptions.AlreadyExistException;
import exceptions.NotExistException;
import exceptions.NotLoggedException;

import java.util.ArrayList;
import java.util.List;

public class CompanyFacade extends ClientFacade {

	private Long companyId;

	private boolean isLoggedIn() {
		return companyId != null;
	}

	public CompanyFacade() {
	}

	@Override
	public boolean logIn(String email, String password) {
		if (!companyDAO.login(email, password))
			return false;
		companyId = companyDAO.getByEmailAndPassword(email, password).getId();
		return true;

	}

	public Coupon addCoupon(Coupon coupon) throws NotLoggedException, AlreadyExistException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		for (Coupon coupon1 : couponDAO.getAllCoupons()) {
			if (coupon1.getTitle() == coupon.getTitle() && coupon1.getCompanyId() == coupon.getCompanyId()) {
				throw new AlreadyExistException(
						String.format("A coupon with the title %s from the company %s already exists",
								coupon.getTitle(), companyDAO.getOneCompany(coupon.getCompanyId()).getName()));
			}
		}
		coupon = couponDAO.addCoupon(coupon);
		return coupon;
	}

	public Coupon createCoupon(Coupon coupon) throws AlreadyExistException, NotLoggedException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		if (couponDAO.existsByTitle(coupon.getTitle())) {
			throw new AlreadyExistException(
					"Error , coupon: " + coupon + " already exists, choose different attributes");
		}
		coupon.setCompanyId(companyId);
		return couponDAO.addCoupon(coupon);
	}

	public Coupon updateCoupon(Coupon coupon) throws NotLoggedException, NotExistException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}

		if (couponDAO.getOneCoupon(coupon.getId()) == null) {
			throw new NotExistException(String.format("Coupon by the id %d does not exist", coupon.getId()));
		}
		coupon.setCompanyId(companyId);
		couponDAO.updateCompanyCoupon(coupon);
		return coupon;
	}

	public boolean deleteCoupon(long couponId) throws NotExistException, NotLoggedException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		boolean isDeleted = false;
		if (!isDeleted) {

			couponDAO.deleteCustomerAndCoupon(couponId);
			couponDAO.deleteCoupon(couponId);
		} else {
			throw new NotExistException("the coupon by the id :" + couponId + "doesn't exist");
		}
		return isDeleted;
	}

	public List<Coupon> getCompanyCoupons() throws NotLoggedException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		List<Coupon> getAll = new ArrayList<>();
		getAll = couponDAO.getAllCoupons();

		return getAll;
	}

	public List<Coupon> getCompanyCoupons(Category category) throws NotLoggedException, NotExistException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		return couponDAO.getByCompanyAndCategory(companyId, category);
	}

	public List<Coupon> getCompanyCoupons(double max_Price) throws NotLoggedException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		List<Coupon> coupons = couponDAO.getByCompany(companyId);
		List<Coupon> getAll = new ArrayList<>();
		for (Coupon coupon : coupons) {
			if (coupon.getPrice() < max_Price) {
				getAll.add(coupon);
			}
		}
		return getAll;
	}

	public Company getCompanyDetails() throws NotLoggedException, NotExistException {
		if (!isLoggedIn()) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		Company company = companyDAO.getOneCompany(companyId);
		if (company == null) {
			throw new NotExistException("the company by the id:" + companyId + "doesn't exist");
		}
		return company;
	}
}
