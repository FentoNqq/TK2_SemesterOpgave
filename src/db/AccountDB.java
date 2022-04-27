package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Account;

public class AccountDB implements AccountDBIF {

	@Override
	public Account findAccountByID(int accountID, int branchID) throws DataAccessException {
		Connection con = DBConnection.getInstance().getConnection();
		Account account = null;
		String baseSelect = "select * from Tk2_Account ";
		baseSelect += "where id = " + accountID + " and fk_branchID = " + branchID + ";";
		
		ResultSet rs = null;
		float balance = 0;
		
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(baseSelect);
			rs.next();
			balance = rs.getFloat("balance");
			stmt.close();
			
			account = new Account(balance, accountID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public void transferBalance(int fromBranchID, int fromAccountID, int toAccountID, int toBranchID, float amount) throws DataAccessException {
		Account fromAccount = null;
		Account toAccount = null;
		fromAccount = findAccountByID(fromAccountID, fromBranchID);
		toAccount = findAccountByID(toAccountID, toBranchID);
		fromAccount.withdrawBalance(amount);
		toAccount.addBalance(amount);
		float fromBalance = fromAccount.getBalance();
		float toBalance = toAccount.getBalance();
		updateBalance(fromAccountID, fromBranchID, fromBalance);
		updateBalance(toAccountID, toAccountID, toBalance);
	}
	
	public void updateBalance(int accountID, int branchID, float balance) throws DataAccessException {
		Connection con = DBConnection.getInstance().getConnection();
		String baseUpdate = "update Tk2_Account ";
		baseUpdate += "set balance = " + balance + " ";
		baseUpdate += "where id = " + accountID + " and fk_branchId = " + branchID + ";";
		
		try {
			Statement stmt = con.createStatement();
			stmt.executeQuery(baseUpdate);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
