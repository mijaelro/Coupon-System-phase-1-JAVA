package pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConnectionPool {
	private static ConnectionPool instance = null;
	private Set<Connection> connections;
	private final int MAX_CONNECTIONS = 15;

	public ConnectionPool() {
		connections = new HashSet<>();
		for (int i = 0; i < MAX_CONNECTIONS; i++) {
			try {
				String URL = "jdbc:mysql://localhost:3306/coupon_system" + "?user=root" + "&password=Personas1996"
						+ "&useUnicode=true" + "&useJDBCCompliantTimezoneShift=true" + "&useLegacyDatetimeCode=false"
						+ "&serverTimezone=UTC";
				connections.add(DriverManager.getConnection(URL));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized Connection getConnection() {
		if (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		Iterator<Connection> iterator = connections.iterator();
		Connection connection = iterator.next();
		iterator.remove();
		return connection;
	}

	public synchronized void restoreConnections(Connection connection) {
		connections.add(connection);
		notifyAll();
	}

	public synchronized void closeConnections() {
		int counter = 0;
		while (counter < MAX_CONNECTIONS) {
			while (connections.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Iterator<Connection> iterator = connections.iterator();
				while (iterator.hasNext()) {
					try {
						Connection connection = iterator.next();
						iterator.remove();
						counter++;
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static synchronized ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}
}
