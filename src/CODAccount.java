import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CODAccount extends Account{

	private int currentPeriod = 5;		//Hard coded value for current time period in years
	private int totalPeriod = 10;		//Hard coded value for total time period in years
	private boolean timePenalty;
	private double penaltyCharge;
	private Lock lock;
	final Condition notEoungh;

	CODAccount(double a, String name) {
		super(a);
		accountName = name;
		lock = new ReentrantLock();
		notEoungh = lock.newCondition();
	}
	
	//Transfers funds between 2 accounts
		void transferAmount(double a1, Account a, Account b){
			lock.lock();
			try {
				if(a1 > a.funds){
					System.out.println("waiting");
					try {
						notEoungh.await(2, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if(a1 > a.funds){
					System.out.printf("Thread with ID %d, %s Insufficient funds cannot withdraw\n", Thread.currentThread().getId(), getName());
				} else {
					System.out.printf("Thread with ID %d, Transferring: %.2f from %s to %s\n", Thread.currentThread().getId(), a1, a.getName(), b.getName());
					a.funds = a.funds - a1;
					b.funds = b.funds + a1;
					System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), a.getName(), a.getBalance());
					System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), b.getName(), b.getBalance());
				}
			} finally {
				lock.unlock();
			}
		}

	void depositFunds(double amount) {
		lock.lock();
		try {
			System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
			System.out.printf("Thread with ID %d, depositing: %.2f into %s\n", Thread.currentThread().getId(), amount, getName());
			funds += amount;
			System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
			notEoungh.signalAll();
		} finally {
			lock.unlock();
		}
	}

	//Withdraws funds from an account
	void withdrawFunds(double amount) {
		lock.lock();
		try {
			System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
			if(amount > funds){
				System.out.println("waiting");
				try {
					notEoungh.await(2, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(amount > funds){
				System.out.printf("Thread with ID %d, %s Insufficient funds cannot withdraw\n", Thread.currentThread().getId(), getName());
			} else {
				funds -= amount;
				lowerLimit();

				if (timePenalty) {
					calculatePenalty();
					System.out.printf("Lower Limit Reached, withdrawing penalty of %.2f\n", penaltyCharge);
					funds -= penaltyCharge;
					System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);

				} else {
					System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	void lowerLimit() {
		lock.lock();
		try {
			timePenalty = currentPeriod < totalPeriod;
		} finally {
			lock.unlock();
		}
	}

	void calculatePenalty() {
		lock.lock();
		try {
			penaltyCharge = (funds * 0.2);
		} finally {
			lock.unlock();
		}
	}

}