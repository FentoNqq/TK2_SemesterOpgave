package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Account;

public class AccountDB implements AccountDBIF {
	
	private int isolationLevel = Connection.TRANSACTION_REPEATABLE_READ;

	@Override
	public Account findAccountByID(int accountID) throws Exception {
		Connection con = DBConnection.getInstance().getConnection(isolationLevel);
		Account account = null;
		String baseSelect = "select * from Tk2_Account ";
		baseSelect += "where id = " + accountID + ";";
		
		ResultSet rs = null;
		float balance = 0;
		
		try {
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(baseSelect);
			rs.next();
			balance = rs.getFloat("balance");
			stmt.close();
			Thread.sleep(25);
			con.commit();
			
			account = new Account(balance, accountID);
		} catch (SQLException e) {
			con.rollback();;
			throw e;
		} finally {
			con.setAutoCommit(true);
		}
		return account;
	}

	@Override
	public void transferBalance(int fromAccountID, int toAccountID, float amount) throws Exception {
		Account fromAccount = null;
		Account toAccount = null;
		fromAccount = findAccountByID(fromAccountID);
		toAccount = findAccountByID(toAccountID);
		fromAccount.withdrawBalance(amount);
		toAccount.addBalance(amount);
		float fromBalance = fromAccount.getBalance();
		float toBalance = toAccount.getBalance();
		System.out.println("withdrew " + amount + " from account " + fromAccountID);
		System.out.println("deposited " + amount + " to account " + toAccountID);
		updateBalance(fromAccountID, fromBalance);
		updateBalance(toAccountID, toBalance);
	}
	
	@Override
	public void updateBalance(int accountID, float balance) throws Exception {
		Connection con = DBConnection.getInstance().getConnection(isolationLevel);
		String baseUpdate = "update Tk2_Account ";
		baseUpdate += "set balance = " + balance + " ";
		baseUpdate += "where id = " + accountID + ";";
		
		try {
			con.setAutoCommit(false);
			PreparedStatement stmt = con.prepareStatement(baseUpdate);
			stmt.executeUpdate();
			stmt.close();
			
			Thread.sleep(50);
			con.commit();
			System.out.println("Committed");
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(true);
		}
	}

}
