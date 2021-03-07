package entities;

import java.util.ArrayList;

public class Company {

	private long id;
	private String name;
	private String email;
	private String password;
	private ArrayList<Coupon> coupons;

	public Company(String name, String email, String password) {
		this.name = name;
		this.password = password;
		this.email = email;
	}

	public Company(long id, String name, String email, String password) {
		this( name,email, password);
		this.id = id;

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ArrayList<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Company{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", password='"
				+ password + '\'' + '}';
	}
}
