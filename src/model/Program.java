package model;

import db.AccountDB;
import db.AccountDBIF;

public class Program extends Thread {

	public static void main(String[] args) {
		AccountDBIF accountDB = new AccountDB();
		
		Thread t1 = new Thread("T1" ) {
			
			public void run() {
				try {
					accountDB.transferBalance(3, 2, 50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		Thread t2 = new Thread("T2") {
			
			public void run() {
				try {
					accountDB.transferBalance(1, 2, 100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t1.start();
		t2.start();
	}
	

}
