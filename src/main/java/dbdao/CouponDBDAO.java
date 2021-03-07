package dbdao;

import categories.Category;
import dao.CouponDAO;
import entities.Coupon;
import pool.ConnectionPool;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CouponDBDAO implements CouponDAO {

	private ConnectionPool pool;

	public CouponDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	public boolean existsByTitle(String title) {
		boolean isExist = false;
		Connection connection = pool.getConnection();
		String sql = "SELECT TITLE FROM COUPONS WHERE TITLE = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setString(1, title);

			ResultSet resultSet = pstmt.executeQuery();
			isExist = resultSet.next();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return isExist;
	}

	@Override
	public Coupon addCoupon(Coupon coupon) {
		String sql = "INSERT INTO COUPONS (COMPANY_ID,CATEGORY_ID,TITLE,DESCRIPTION,Sart_Date,End_Date,AMOUNT,PRICE,IMAGE) VALUES"
				+ "(?,?,?,?,?,?,?,?,?)";
		Connection connection = pool.getConnection();
		try (PreparedStatement ptmst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ptmst.setLong(1, coupon.getCompanyId());
			ptmst.setLong(2, coupon.getCategoryId());
			ptmst.setString(3, coupon.getTitle());
			ptmst.setString(4, coupon.getDescription());
			ptmst.setDate(5, Date.valueOf(coupon.getStartDate()));
			ptmst.setDate(6, Date.valueOf(coupon.getEndDate()));
			ptmst.setInt(7, coupon.getAmount());
			ptmst.setDouble(8, coupon.getPrice());
			ptmst.setString(9, coupon.getImage());
			ptmst.executeUpdate();

			ResultSet resultSet = ptmst.getGeneratedKeys();

			if (resultSet.next()) {
				coupon.setId(resultSet.getLong(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupon;
	}

	@Override
	public Coupon updateCoupon(Coupon coupon) {
		String sql = "UPDATE COUPONS SET COMPANY_ID=?,CATEGORY_ID=?,TITLE=?,DESCRIPTION=?,SART_DATE=?,E"
				+ "ND_DATE=?,AMOUNT=?,PRICE=?,IMAGE=? WHERE ID=?";

		Connection connection = pool.getConnection();

		try (PreparedStatement ptmst = connection.prepareStatement(sql)) {
			ptmst.setLong(1, coupon.getCompanyId());
			ptmst.setLong(2, coupon.getCategoryId());
			ptmst.setString(3, coupon.getTitle());
			ptmst.setString(4, coupon.getDescription());
			ptmst.setDate(5, Date.valueOf(coupon.getStartDate()));
			ptmst.setDate(6, Date.valueOf(coupon.getEndDate()));
			ptmst.setInt(7, coupon.getAmount());
			ptmst.setDouble(8, coupon.getPrice());
			ptmst.setString(9, coupon.getImage());
			ptmst.setLong(10, coupon.getId());
			ptmst.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupon;
	}

	public Coupon getCompanyCoupon(long couponId, long companyId) {
		Coupon coupon = null;

		String sql = "SELECT * FROM COUPONS WHERE ID = ? AND COMPANY_ID IN ( SELECT ID FROM COMPANIES WHERE ID = ?)";

		Connection connection = pool.getConnection();

		try (PreparedStatement ptmst = connection.prepareStatement(sql)) {

			ptmst.setLong(1, couponId);
			ptmst.setLong(2, companyId);

			ResultSet resultSet = ptmst.executeQuery();

			while (resultSet.next()) {

				long categoryId = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate startDate = resultSet.getDate(6).toLocalDate();
				LocalDate endDate = resultSet.getDate(7).toLocalDate();
				int amount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				coupon = new Coupon(couponId, companyId, categoryId, title, description, startDate, endDate, amount,
						price, image);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupon;
	}

	@Override
	public Coupon getOneCoupon(long couponId) {
		Coupon coupon = null;

		String sql = "SELECT * FROM COUPONS WHERE ID = ?";

		Connection connection = pool.getConnection();

		try (PreparedStatement ptmst = connection.prepareStatement(sql)) {

			ptmst.setLong(1, couponId);

			ResultSet resultSet = ptmst.executeQuery();

			while (resultSet.next()) {

				long companyId = resultSet.getLong(2);
				long categoryId = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate startDate = resultSet.getDate(6).toLocalDate();
				LocalDate endDate = resultSet.getDate(7).toLocalDate();
				int amount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				coupon = new Coupon(couponId, companyId, categoryId, title, description, startDate, endDate, amount,
						price, image);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupon;
	}

	@Override
	public ArrayList<Coupon> getAllCoupons() {

		ArrayList<Coupon> all = new ArrayList<>();
		String sql = "SELECT * FROM COUPONS";
		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {

				long couponId = resultSet.getLong(1);
				long companyId = resultSet.getLong(2);
				long categoryId = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate startDate = resultSet.getDate(6).toLocalDate();
				LocalDate endDate = resultSet.getDate(7).toLocalDate();
				int amount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				Coupon coupon = new Coupon(couponId, companyId, categoryId, title, description, startDate, endDate,
						amount, price, image);
				all.add(coupon);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return all;
	}

	@Override
	public Coupon deleteCoupon(long couponId) {

		Coupon coupon = getOneCoupon(couponId);

		String sql = "DELETE FROM COUPONS WHERE ID=?";

		Connection connection = pool.getConnection();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setLong(1, couponId);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupon;
	}

	@Override
	public Coupon addCouponPurchase(long customerId, long couponId) {
		Coupon coupon = null;
		Connection connection = pool.getConnection();
		String sqlSelect = "SELECT * FROM COUPONS WHERE ID = ?";
		String sqlInsert = "INSERT INTO CUSTOMERS_VS_COUPONS(CUSTOMER_ID,COUPON_ID) VALUES(?,?)";

		try (PreparedStatement selectPstmt = connection.prepareStatement(sqlSelect);
				PreparedStatement insertPstmt = connection.prepareStatement(sqlInsert)) {

			selectPstmt.setLong(1, couponId);
			ResultSet resultSet = selectPstmt.executeQuery();
			while (resultSet.next()) {
				long companyId = resultSet.getLong(2);
				long categoryId = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate startDate = resultSet.getDate(6).toLocalDate();
				LocalDate endDate = resultSet.getDate(7).toLocalDate();
				int amount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				coupon = new Coupon(couponId, companyId, categoryId, title, description, startDate, endDate, amount,
						price, image);
			}
			if (coupon == null)
				return null;
			insertPstmt.setLong(1, customerId);
			insertPstmt.setLong(2, couponId);
			insertPstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupon;
	}

	@Override
	public Coupon deleteCouponPurchase(long customerId, long couponId) {
		Coupon coupon = null;
		Connection connection = pool.getConnection();
		String sql = "DELETE FROM CUSTOMERS_VS_COUPONS WHERE CUSTOMER_ID = ? AND COUPON_ID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setLong(1, customerId);
			pstmt.setLong(2, couponId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupon;
	}

	public boolean deleteCustomerAndCoupon(long couponId) {

		boolean isDeleted = false;
		Connection connection = pool.getConnection();
		String sql = "delete from CUSTOMERS_VS_COUPONS WHERE COUPON_ID = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setLong(1, couponId);
			pstmt.executeUpdate();
			isDeleted = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return isDeleted;
	}

	public Coupon updateCompanyCoupon(Coupon coupon) {

		Connection connection = pool.getConnection();

		String sql = "UPDATE COUPONS SET CATEGORY_ID = ?,TITLE = ?,DESCRIPTION = ?,SARt_DATE = ?,END_DATE = ?,AMOUNT = ?,PRICE = ?,IMAGE = ?"
				+ "WHERE COMPANY_ID IN (SELECT ID FROM COMPANIES WHERE companyID = ?) AND ID = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setLong(1, coupon.getCategoryId());
			pstmt.setString(2, coupon.getTitle());
			pstmt.setString(3, coupon.getDescription());
			pstmt.setDate(4, Date.valueOf(coupon.getStartDate()));
			pstmt.setDate(5, Date.valueOf(coupon.getEndDate()));
			pstmt.setInt(6, coupon.getAmount());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());
			pstmt.setLong(9, coupon.getCompanyId());
			pstmt.setLong(10, coupon.getId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupon;
	}

	public boolean deleteCompanyFromCoupons(long companyId) {
		boolean isdeleted = false;
		Connection connection = pool.getConnection();
		String sql = "delete from coupons where COMPANY_ID = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setLong(1, companyId);
			pstmt.executeUpdate();
			isdeleted = true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return isdeleted;
	}

	public List<Coupon> getByCustomerAndCategory(long customerId, Category category) {
		List<Coupon> coupons = new ArrayList<>();
		String sql = "SELECT * FROM COUPONS INNER JOIN CUSTOMERS_VS_COUPONS ON CUSTOMERS_VS_COUPONS.COUPON_ID = ID WHERE CUSTOMER_ID = ? "
				+ "AND category_ID IN (SELECT ID FROM CATEGORIES WHERE NAME = ?)";
		Connection connection = pool.getConnection();
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setLong(1, customerId);
			pstmt.setString(2, category.name());

			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				Long id = resultSet.getLong(1);
				Long category_Id = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate start_Date = resultSet.getDate(6).toLocalDate();
				LocalDate end_Date = resultSet.getDate(7).toLocalDate();
				int ammount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				Coupon coupon = new Coupon(id, customerId, category_Id, title, description, start_Date, end_Date,
						ammount, price, image);
				coupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupons;
	}

	public List<Coupon> getByCompanyAndCategory(long companyId, Category category) {
		List<Coupon> coupons = new ArrayList<>();
		String sql = "Select * FROM COUPONS WHERE COMPANY_ID = ? AND CATEGORY_ID IN ("
				+ "SELECT ID FROM CATEGORIES WHERE NAME = ? )";
		Connection connection = pool.getConnection();
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setLong(1, companyId);
			pstmt.setString(2, category.name());

			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				long id = resultSet.getLong(2);
				long category_Id = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate start_Date = resultSet.getDate(6).toLocalDate();
				LocalDate end_Date = resultSet.getDate(7).toLocalDate();
				int ammount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				Coupon coupon = new Coupon(id, companyId, category_Id, title, description, start_Date, end_Date,
						ammount, price, image);
				coupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupons;
	}

	public List<Coupon> getByCompany(long companyId) {
		List<Coupon> coupons = new ArrayList<>();
		String sql = "Select * FROM COUPONS WHERE COMPANY_ID = ?";
		Connection connection = pool.getConnection();
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setLong(1, companyId);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				long id = resultSet.getLong(2);
				long category_Id = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate start_Date = resultSet.getDate(6).toLocalDate();
				LocalDate end_Date = resultSet.getDate(7).toLocalDate();
				int ammount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				Coupon coupon = new Coupon(id, companyId, category_Id, title, description, start_Date, end_Date,
						ammount, price, image);
				coupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupons;
	}

	public List<Coupon> getByCustomer(long customerId) {
		List<Coupon> coupons = new ArrayList<>();
		String sql = "SELECT * FROM COUPONS INNER JOIN CUSTOMERS_VS_COUPONS ON CUSTOMERS_VS_COUPONS.COUPON_ID = ID WHERE CUSTOMER_ID = ? ";

		Connection connection = pool.getConnection();
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setLong(1, customerId);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				long id = resultSet.getLong(2);
				long companyId = resultSet.getLong(3);
				String title = resultSet.getString(4);
				String description = resultSet.getString(5);
				LocalDate start_Date = resultSet.getDate(6).toLocalDate();
				LocalDate end_Date = resultSet.getDate(7).toLocalDate();
				int ammount = resultSet.getInt(8);
				double price = resultSet.getDouble(9);
				String image = resultSet.getString(10);
				Coupon coupon = new Coupon(id, customerId, companyId, title, description, start_Date, end_Date, ammount,
						price, image);
				coupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
		return coupons;
	}

	public void deletePurchaseByCouponID(long couponID) {
		Connection connection = pool.getConnection();
		String sql = "DELETE FROM CUSTOMERS_VS_COUPONS WHERE COUPON_ID = ?";
		try (PreparedStatement prstmt = connection.prepareStatement(sql)) {
			prstmt.setLong(1, couponID);
			prstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.restoreConnections(connection);
		}
	}
}
