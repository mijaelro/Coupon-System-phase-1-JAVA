package facades;

import entities.Company;
import entities.Coupon;
import entities.Customer;
import exceptions.AlreadyExistException;
import exceptions.NotExistException;
import exceptions.NotLoggedException;

import java.util.List;

public class AdminFacade extends ClientFacade {

	private boolean isLoggedIn = false;

	public AdminFacade() {
	}

	@Override
	public boolean logIn(String email, String password) {

//		String originalEmail = "admin@admin.com";
//        String originalPassword = "admin";
        isLoggedIn = (email.equals("admin@admin.com") && password.equals("admin"));
        return isLoggedIn;
//		return email.equals("admin@admin.com") && password.equals("admin");
	}

	public Company addCompany(Company company) throws AlreadyExistException, NotLoggedException, NotExistException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		if (company == null) {
			throw new NotExistException("the company : " + company + "doesn't exist");
		}
		Company byName = companyDAO.getByName(company.getName());
		Company byEmail = companyDAO.getByEmail(company.getEmail());

		if (byName != null || byEmail != null) {
			throw new AlreadyExistException(
					"Error , the comapany : " + company + " already exists, choose different attributes");
		}

		return companyDAO.addCompany(company);
	}

	public Company updateCompany(Company company) throws NotExistException, NotLoggedException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		if (company == null) {
			throw new NotExistException("the company: " + company + "doesn't exist");
		}

		return companyDAO.updateEmailAndPassword(company);
	}

	public void deleteCompany(long companyId) throws NotExistException, NotLoggedException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		if (companyDAO.getOneCompany(companyId) == null) {
			throw new NotExistException(String.format("There is no company with the id %d", companyId));
		}
		List<Coupon> toDelete = companyDAO.getCompanyCoupons(companyId);
//        List<Customer> allCustomers = customerDAO.getAllCustomers();
		for (Coupon coupon : toDelete) {
			couponDAO.deletePurchaseByCouponID(coupon.getId());
			couponDAO.deleteCoupon(coupon.getId());
		}
		companyDAO.deleteCompany(companyId);
	}

	public List<Company> getAllCompanies() throws NotLoggedException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		return companyDAO.getAllCompanies();
	}

	public Company getOneCompany(long id) throws NotExistException, NotLoggedException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		if (companyDAO.getOneCompany(id) == null) {
			throw new NotExistException("this id : " + id + "does not exist");
		}
		return companyDAO.getOneCompany(id);
	}

	public Customer addCustomer(Customer customer) throws AlreadyExistException, NotLoggedException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}

		Customer byEmail = customerDAO.getByEmail(customer.getEmail());
		if (byEmail != null) {
			throw new AlreadyExistException("the email:" + byEmail + " is not available, choose another one please ");
		}
		return customerDAO.addCustomer(customer);
	}

	public Customer updateCustomer(Customer customer) throws NotLoggedException, NotExistException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		if (customerDAO.updateCustomer(customer) == null) {
			throw new NotExistException("update failed, the customer " + customer + " doesn't exists");
		}

		return customer;
	}

	public boolean deleteCustomer(long customerId) throws NotExistException, NotLoggedException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		boolean isDeleted = false;

		if (!isDeleted) {
			customerDAO.deleteCustomer(customerId);
			couponDAO.deleteCustomerAndCoupon(customerId);
			isDeleted = true;
		} else {
			throw new NotExistException(
					"the customer with the id : " + customerId + " doesn't exists , therefore it cant be erased");
		}
		return isDeleted;
	}

	public List<Customer> getAllCustomers() throws NotLoggedException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		return customerDAO.getAllCustomers();
	}

	public Customer getCustomerById(long id) throws NotExistException, NotLoggedException {
		if (!isLoggedIn) {
			throw new NotLoggedException("Error , unable to logg in ");
		}
		if (customerDAO.getOneCustomer(id) == null) {
			throw new NotExistException("this id : " + id + " does not exist");
		}

		return customerDAO.getOneCustomer(id);
	}

}
