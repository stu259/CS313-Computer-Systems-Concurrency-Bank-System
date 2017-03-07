import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
	double funds;						//Funds of an account object
	String accountName;					//Name of an account object
	Lock lock;
	
	Account(double initialFunds){
		funds = initialFunds;
		lock = new ReentrantLock();
	}

	double getBalance(){
		lock.lock();
		try {
			return funds;
		} finally {
			lock.unlock();
		}
	}

	String getName(){
		return accountName;
	}

	

	//Tests to see if the withdrawal amount is more than the balance
	boolean checkWithdrawal(double a){
		lock.lock();
		try {
			return a <= funds;
		} finally {
			lock.unlock();
		}

	}

}