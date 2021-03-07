package dbdao;

import dao.CustomerDAO;
import entities.Customer;
import pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDBDAO implements CustomerDAO {

	private ConnectionPool pool;

	public CustomerDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	@Override
	public boolean isCustomerExists(String email, String password) {
		Customer byEmailAndPassword = getByEmailAndPassword(email, password);
		return byEmailAndPassword != null;
	}

	@Override
	public Customer addCustomer(Customer customer) {

		String sql = "INSERT INTO CUSTOMERS (FIRSTNAME, LASTNAME,EMAIL,PASSWORD) VALUES" + "(?,?,?,?)";

		Connection connection = pool.getConnection();

		try (PreparedStatement ptmst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ptmst.setString(1, customer.getFirstName());
			ptmst.setString(2, customer.getLastName());
			ptmst.setString(3, customer.getEmail());
			ptmst.setString(4, customer.getPassword());

			ptmst.executeUpdate();

			ResultSet resultSet = ptmst.getGeneratedKeys();

			if (resultSet.next()) {
				customer.setId(resultSet.getLong(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return customer;
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		String sql = "UPDATE CUSTOMERS SET FIRSTNAME=?,LASTNAME=?,EMAIL=?,PASSWORD=?WHERE ID=?";

		Connection connection = pool.getConnection();

		try (PreparedStatement ptmst = connection.prepareStatement(sql)) {

			ptmst.setString(1, customer.getFirstName());
			ptmst.setString(2, customer.getLastName());
			ptmst.setString(3, customer.getEmail());
			ptmst.setString(4, customer.getPassword());
			ptmst.setLong(5, customer.getId());
			ptmst.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return customer;
	}

	@Override
	public Customer deleteCustomer(long customerId) {
		Customer customer = getOneCustomer(customerId);
		String sql = "DELETE  FROM CUSTOMERS WHERE ID=?";

		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setLong(1, customerId);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return customer;
	}

	@Override
	public ArrayList<Customer> getAllCustomers() {
		ArrayList<Customer> all = new ArrayList<>();

		String sql = "SELECT * FROM CUSTOMERS";
		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				Long customerId = resultSet.getLong(1);
				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String email = resultSet.getString(4);
				String password = resultSet.getString(5);
				Customer customer = new Customer(customerId, firstName, lastName, email, password);
				all.add(customer);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return all;
	}

	@Override
	public Customer getOneCustomer(long customerId) {
		Customer customer = null;

		String sql = "SELECT * FROM CUSTOMERS WHERE ID = ?";

		Connection connection = pool.getConnection();

		try (PreparedStatement ptmst = connection.prepareStatement(sql)) {

			ptmst.setLong(1, customerId);

			ResultSet resultSet = ptmst.executeQuery();

			while (resultSet.next()) {

				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String email = resultSet.getString(4);
				String password = resultSet.getString(5);
				customer = new Customer(customerId, firstName, lastName, email, password);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return customer;
	}

	public Customer getByEmailAndPassword(String email, String password) {
		Customer customer = null;
		String sql = "SELECT * FROM CUSTOMERS WHERE EMAIL=? AND PASSWORD=?";
		Connection connection = pool.getConnection();

		try (PreparedStatement ptst = connection.prepareStatement(sql)) {
			ptst.setString(1, email);
			ptst.setString(2, password);

			ResultSet resultSet = ptst.executeQuery();

			if (resultSet.next()) {
				long customerId = resultSet.getLong(1);
				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				customer = new Customer(customerId, firstName, lastName, email, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return customer;
	}

	public Customer getByEmail(String email) {
		Customer customer = null;
		String sql = "SELECT * FROM CUSTOMERS WHERE EMAIL=?";
		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, email);

			ResultSet resultSet = pstmt.executeQuery();

			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String password = resultSet.getString(5);
				customer = new Customer(id, firstName, lastName, email, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return customer;
	}

	public Customer getByName(String firstName, String lastName) {
		Connection connection = pool.getConnection();
		Customer customer = null;
		String sql = "SELECT * FROM CUSTOMERS WHERE FIRST_NAME = ? and LAST_NAME = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);

			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				String email = resultSet.getString(4);
				String password = resultSet.getString(5);
				customer = new Customer(id, firstName, lastName, email, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return customer;
	}

	public boolean login(String email, String password) {
		Customer customer = getByEmail(email);
		if (customer == null) {
			return false;
		}
		return password.equals(customer.getPassword());
	}

}
