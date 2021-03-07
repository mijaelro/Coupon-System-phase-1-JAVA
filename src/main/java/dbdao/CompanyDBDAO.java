package dbdao;

import dao.CompanyDAO;
import entities.Company;
import entities.Coupon;
import pool.ConnectionPool;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool pool;

	public CompanyDBDAO() {
		this.pool = ConnectionPool.getInstance();
	}

	public List<Coupon> getCompanyCoupons(long companyID) {
		Connection connection = pool.getConnection();
		List<Coupon> couponList = new ArrayList<>();
		String sql = "SELECT * FROM COUPONS WHERE COMPANY_ID = ?";
		try (PreparedStatement ptst = connection.prepareStatement(sql)) {
			ptst.setLong(1, companyID);
			ResultSet resultSet = ptst.executeQuery();
			while (resultSet.next()) {
				long id = resultSet.getLong(1);
				Long category_Id = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate startDate = resultSet.getDate(6).toLocalDate();
				LocalDate endDate = resultSet.getDate(7).toLocalDate();
				int amount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				Coupon coupon = new Coupon(id, companyID, category_Id, title, description, startDate, endDate, amount,
						price, image);
				couponList.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return couponList;
	}

	public Company getByName(String companyName) {

		Company company = null;

		String sql = "SELECT * FROM COMPANIES WHERE NAME = ?";
		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setString(1, companyName);

			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				String email = resultSet.getString(3);
				String password = resultSet.getString(4);
				company = new Company(companyName, email, password);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}

		return company;
	}

	@Override
	public Company addCompany(Company company) {
		String sql = "INSERT INTO COMPANIES (NAME,EMAIL,PASSWORD) VALUES (? ,?,?)";
		Connection connection = pool.getConnection();
		try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getEmail());
			pstmt.setString(3, company.getPassword());

			pstmt.executeUpdate();

			ResultSet resultSet = pstmt.getGeneratedKeys();

			if (resultSet.next()) {
				company.setId(resultSet.getLong(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}

		return company;

	}

	@Override
	public Company getOneCompany(long companyId) {
		Company company = null;

		String sql = "SELECT * FROM COMPANIES WHERE companyID = ?";
		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setLong(1, companyId);

			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				String password = resultSet.getString(4);
				company = new Company(companyId, name, email, password);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return company;
	}

	@Override
	public List<Company> getAllCompanies() {
		List<Company> all = new ArrayList<>();
		String sql = "SELECT * FROM COMPANIES";
		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				long id = resultSet.getLong(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				String password = resultSet.getString(4);
				Company company = new Company(id, name, email, password);
				all.add(company);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return all;
	}

	@Override
	public boolean isCompanyExists(String email, String password) {
		Company byEmailAndPassword = getByEmailAndPassword(email, password);
		return byEmailAndPassword != null;
	}

	@Override
	public Company updateCompany(Company company) {
		String sql = "UPDATE COMPANIES SET NAME=?, EMAIL=?,PASSWORD=? WHERE ID=?";
		Connection connection = pool.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, company.getName());
			preparedStatement.setString(2, company.getEmail());
			preparedStatement.setString(3, company.getPassword());
			preparedStatement.setLong(4, company.getId());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return company;
	}

	@Override
	public Company deleteCompany(long companyId) {
		Company company = getOneCompany(companyId);
		String sql = "DELETE  FROM COMPANIES WHERE companyID=?";

		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setLong(1, companyId);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return company;
	}

	public Company getByEmailAndPassword(String email, String password) {
		Company company = null;
		String sql = "SELECT* FROM COMPANIES WHERE EMAIL=? AND PASSWORD=?";
		Connection connection = pool.getConnection();
		try (PreparedStatement ptst = connection.prepareStatement(sql)) {
			ptst.setString(1, email);
			ptst.setString(2, password);

			ResultSet resultSet = ptst.executeQuery();

			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				String name = resultSet.getString(2);

				company = new Company(id, name, email, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return company;
	}

	public Company getByEmail(String email) {
		Company company = null;
		String sql = "SELECT* FROM COMPANIES WHERE EMAIL=? ";
		Connection connection = pool.getConnection();
		try (PreparedStatement ptst = connection.prepareStatement(sql)) {
			ptst.setString(1, email);

			ResultSet resultSet = ptst.executeQuery();

			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				String name = resultSet.getString(2);
				String password = resultSet.getString(4);
				company = new Company(id, name, email, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return company;
	}

	public Company updateEmailAndPassword(Company company) {
		Connection connection = pool.getConnection();
		String sql = "UPDATE COMPANIES SET EMAIL = ?, PASSWORD = ?" + "WHERE companyID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, company.getEmail());
			pstmt.setString(2, company.getPassword());
			pstmt.setLong(3, company.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return company;
	}

	public boolean login(String email, String password) {
		Company company = getByEmail(email);
		if (company == null) {
			return false;
		}
		return password.equals(company.getPassword());
	}
}
