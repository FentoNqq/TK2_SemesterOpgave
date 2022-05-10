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
		baseSelect += "where id = ?;";
		
		ResultSet rs = null;
		float balance = 0;
		
		try {
			PreparedStatement stmt = con.prepareStatement(baseSelect);
			stmt.setInt(1, accountID);
			rs = stmt.executeQuery();
			rs.next();
			balance = rs.getFloat("balance");
			stmt.close();
			Thread.sleep(25);
			con.commit();
			
			account = new Account(balance, accountID);
		} catch (SQLException e) {
			con.rollback();;
			throw e;
		}
		System.out.println(account.getBalance());
		return account;
	}

	@Override
	public void transferBalance(int fromAccountID, int toAccountID, float amount) throws Exception {
		DBConnection con = DBConnection.getInstance();
		con.startTransaction();
		Account fromAccount = null;
		Account toAccount = null;
		fromAccount = findAccountByID(fromAccountID);
		toAccount = findAccountByID(toAccountID);
		fromAccount.withdrawBalance(amount);
		toAccount.addBalance(amount);
		float fromBalance = fromAccount.getBalance();
		float toBalance = toAccount.getBalance();
		//System.out.println("withdrew " + amount + " from account " + fromAccountID);
		//System.out.println("deposited " + amount + " to account " + toAccountID);
		updateBalance(fromAccountID, fromBalance);
		updateBalance(toAccountID, toBalance);
		con.commitTransaction();
	}
	
	@Override
	public void updateBalance(int accountID, float balance) throws Exception {
		Connection con = DBConnection.getInstance().getConnection(isolationLevel);
		String baseUpdate = "UPDATE Tk2_Account SET balance = ? WHERE id ? = ?;";
		//baseUpdate += "set balance = " + balance + " ";
		//baseUpdate += "where id = " + accountID + ";";
		
//		String baseSelectBalance = "SELECT * FROM Tk2_Account ";
//		baseSelectBalance += "WHERE id = " + accountID + ";";
//		float currentBalance = 0;
//		ResultSet rs = null;
//		Statement statement = con.createStatement();
//		rs = statement.executeQuery(baseSelectBalance);
//		rs.next();
//		currentBalance = rs.getFloat("balance");
		
		
//		if(balance == currentBalance) {
			try {
				PreparedStatement stmt = con.prepareStatement(baseUpdate);
				stmt.setFloat(1, balance);
				stmt.setInt(2, accountID);
				stmt.executeUpdate();
				stmt.close();
			
				Thread.sleep(50);
				con.commit();
				System.out.println("Committed");
				
			} catch (SQLException e) {
				con.rollback();
				throw e;
			}
			} 
//		else { updateBalance(accountID, currentBalance);
//			System.out.println("prøver igen");
//		}
//	}
}
