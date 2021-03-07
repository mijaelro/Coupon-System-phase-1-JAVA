package dao;

import entities.Customer;

import java.util.ArrayList;

public interface CustomerDAO {

	boolean isCustomerExists(String email, String password);

	Customer addCustomer(Customer customer);

	Customer updateCustomer(Customer customer);

	Customer deleteCustomer(long customerId);

	ArrayList<Customer> getAllCustomers();

	Customer getOneCustomer(long customerId);

}
