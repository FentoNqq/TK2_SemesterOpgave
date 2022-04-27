package db;

import model.Account;

public interface AccountDBIF {
	
	public Account findAccountByID(int accountID, int branchID) throws DataAccessException;
	public void transferBalance(int fromBranchID, int fromAccountID, int toAccountID, int toBranchID, float amount) throws DataAccessException;
	

}
