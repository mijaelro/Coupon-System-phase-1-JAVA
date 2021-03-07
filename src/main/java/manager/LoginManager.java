package manager;

import categories.ClientType;
import facades.AdminFacade;
import facades.ClientFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;

public class LoginManager {

	private static LoginManager instance;

	public static LoginManager getInstance() {
		if (instance == null) {
			synchronized (LoginManager.class) {
				if (instance == null) {
					instance = new LoginManager();
				}
			}

		}
		return instance;
	}

	private LoginManager() {

	}

	public ClientFacade login(String email, String password, ClientType clientType) {
		ClientFacade clientFacade = null;
		switch (clientType) {
		case ADMINISTRATOR:
			clientFacade = new AdminFacade();
			break;
		case COMPANY:
			clientFacade = new CompanyFacade();
			break;
		case CUSTOMER:
			clientFacade = new CustomerFacade();
			break;
		}
		if (clientFacade.logIn(email, password)) {
			return clientFacade;
		}
		return null;
	}
}
