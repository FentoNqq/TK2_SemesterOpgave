package model;

public class Account {

	private float balance;
	private int id;
	
	public Account(float balance, int id) {
		this.balance = balance;
		this.id = id;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}
	
	public void withdrawBalance(float amount) {
		balance -= amount;
	}
	
	public void addBalance(float amount) {
		balance += amount;
	}
}
