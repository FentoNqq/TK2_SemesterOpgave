package db;

import model.Account;

public interface AccountDBIF {
	
	public Account findAccountByID(int accountID) throws Exception;
	public void transferBalance(int fromAccountID, int toAccountID, float amount) throws Exception;
	public void updateBalance(int accountID, float balance) throws Exception;

}
