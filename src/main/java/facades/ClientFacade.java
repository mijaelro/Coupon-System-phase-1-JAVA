package facades;

import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;

public abstract class ClientFacade {
	CompanyDBDAO companyDAO;
	CustomerDBDAO customerDAO;
	CouponDBDAO couponDAO;

	public ClientFacade() {
		this.companyDAO = new CompanyDBDAO();
		this.customerDAO = new CustomerDBDAO();
		this.couponDAO = new CouponDBDAO();
	}

	public abstract boolean logIn(String email, String password);
}
