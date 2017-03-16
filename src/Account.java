import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
	double funds;						//Funds of an account object
	String accountName;					//Name of an account object
	ReentrantLock lock;
	List<Integer> currentId;
	List<Boolean> holdingLock;
	
	Account(double initialFunds){
		funds = initialFunds;
		lock = new ReentrantLock();
		currentId = new ArrayList<Integer>();
	}
	
	public List<Integer> getID(){
		return currentId;
	}
	
	public void clearIDs(){
		currentId.clear();
	}
	
	public List<Boolean> getLocksBool(){
		return holdingLock;
	}
	
	public void clearLocks(){
		holdingLock.clear();
	}

	double getBalance(){
		lock.lock();
		if(lock.isHeldByCurrentThread()){
			currentId.add((int)Thread.currentThread().getId());
		}
//		holdingLock.add(Thread.currentThread().holdsLock(lock));
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