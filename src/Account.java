import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
	double funds;						//Funds of an account object
	String accountName;					//Name of an account object
	ReentrantLock lock;
	long currentId;
	
	Account(double initialFunds){
		funds = initialFunds;
		lock = new ReentrantLock();
		currentId = 0;
	}
	
	public long getID(){
		return currentId;
	}

	double getBalance(){
		lock.lock();
		if(lock.isHeldByCurrentThread()){
			currentId = Thread.currentThread().getId();
		}
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