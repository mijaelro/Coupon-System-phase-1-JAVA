package test;

import categories.Category;
import categories.ClientType;
import entities.Company;
import entities.Coupon;
import entities.Customer;
import facades.AdminFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;
import job.ExpirationDailyJob;
import manager.LoginManager;
import pool.ConnectionPool;
import utils.ArtUtil;

import java.time.LocalDate;

public class Test {
	public static void testAll() {
		ExpirationDailyJob expirationDailyJob = new ExpirationDailyJob();
		ArtUtil.printAscii();
		expirationDailyJob.start();

		try {

			LoginManager loginManager = LoginManager.getInstance();

			AdminFacade adminFacade = (AdminFacade) loginManager.login("admin@admin.com", "admin",
					ClientType.ADMINISTRATOR);
			Company company = new Company("palsala", "paaslla@gmail.com", "0000");
			company = adminFacade.addCompany(company);
			company.setPassword("1234");
			adminFacade.updateCompany(company);
			adminFacade.deleteCompany(company.getId());
			System.out.println("companies: ");
			adminFacade.getAllCompanies().forEach(System.out::println);
			System.out.println("printing the company with id 1: ");
			System.out.println(adminFacade.getOneCompany(1L));

			Customer customer = new Customer("moysaa", "moysaloal", "moyaslaol@l.com", "0000");
			customer = adminFacade.addCustomer(customer);
			customer.setPassword("cool");
			adminFacade.updateCustomer(customer);
			adminFacade.deleteCustomer(customer.getId());
			System.out.println("printing all customers: ");
			adminFacade.getAllCustomers().forEach(System.out::println);
			System.out.println("printing the customer with id 2: ");
			System.out.println(adminFacade.getCustomerById(2));

			CompanyFacade companyFacade = (CompanyFacade) loginManager.login("google@gmail.com", "123456", ClientType.COMPANY);
			Coupon coupon = new Coupon(3, 2, "newCouponaForApple", "couponTedst daescriptions new apple", LocalDate.now(),
					LocalDate.now().plusMonths(1), 30, 100.2, "");
			coupon = companyFacade.addCoupon(coupon);
			coupon.setImage("http://image.jpgs");
			companyFacade.updateCoupon(coupon);
			System.out.println("the company logged in: ");
			System.out.println(companyFacade.getCompanyDetails());
			System.out.println("printing all the company coupons");
			companyFacade.getCompanyCoupons().forEach(System.out::println);
			System.out.println("all company coupons with food category: ");
			companyFacade.getCompanyCoupons(Category.FOOD).forEach(System.out::println);
			System.out.println("all company coupons under the price of 20: ");
			companyFacade.getCompanyCoupons(20).forEach(System.out::println);

			CustomerFacade customerFacade = (CustomerFacade) loginManager.login("yona@matsa.com", "121212",
					ClientType.CUSTOMER);
			customerFacade.purchaseCoupon(coupon);
			System.out.println("the customer logged in: ");
			System.out.println(customerFacade.getCustomerDetails());
			System.out.println("all customer coupons: ");
			customerFacade.getCustomerCoupons().forEach(System.out::println);
			System.out.println("all customer coupons with electronics category: ");
			customerFacade.getCustomerCoupons(Category.ELECTRICITY).forEach(System.out::println);
			System.out.println("all customer coupons under the price of 400: ");
			customerFacade.getCustomerCoupons(400).forEach(System.out::println);
			companyFacade.deleteCoupon(coupon.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		expirationDailyJob.stop();
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		connectionPool.closeConnections();
	}
}
