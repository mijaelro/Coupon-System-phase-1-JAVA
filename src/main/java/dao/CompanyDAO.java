package dao;

import entities.Company;

import java.util.List;

public interface CompanyDAO {

	boolean isCompanyExists(String email, String password);

	Company addCompany(Company company);

	Company updateCompany(Company company);

	Company deleteCompany(long companyId);

	List<Company> getAllCompanies();

	Company getOneCompany(long companyId);

}
