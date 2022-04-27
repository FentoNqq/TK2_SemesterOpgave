package db;

import model.Account;

public interface AccountDBIF {
	
	public Account findAccountByID(int accountID) throws DataAccessException;
	public void transferBalance(int fromAccountID, int toAccountID, float amount) throws DataAccessException;
	

}
