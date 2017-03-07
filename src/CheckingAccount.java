import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CheckingAccount extends Account {

	private int activityCount;					//Counts the number of activities on the account
	private double penaltyCharge;				//Penalty to be applied
	private boolean activityLimitReached;
	private Lock lock;
	final Condition notEoungh;

	CheckingAccount(double a, String name) {
		super(a);
		accountName = name;
		lock = new ReentrantLock();
		notEoungh = lock.newCondition();
	}

	//Deposits funds into the account
	void depositFunds(double amount){
		lock.lock();
		try {
			System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName() ,funds);
			System.out.printf("Thread with ID %d, depositing: %.2f into %s\n", Thread.currentThread().getId(), amount, getName());
			funds += amount;
			System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
			notEoungh.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	boolean isActivityLimitReached() {
		lock.lock();
		try {
			return (activityLimitReached);
		} finally {
			lock.unlock();
		}
	}

	double getPenaltyCharge() {
		lock.lock();
		try {
			return penaltyCharge;
		} finally {
			lock.unlock();
		}
	}

	public int getActivityCount() {
		return activityCount;
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
	//Withdraws funds from an account
	void  withdrawFunds(double amount) {
		lock.lock();
		try{
		System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
			if (checkWithdrawal(amount)) {
				if(amount > funds){
					System.out.println("waiting");
					try {
						notEoungh.await(2, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(amount > funds){
					System.out.printf("Thread with ID %d, %s Insufficient funds cannot withdraw\n", Thread.currentThread().getId(), getName());
				} else {
					funds -= amount;
					System.out.printf("Thread with ID %d, withdrawing: %.2f from %s\n", Thread.currentThread().getId(), amount, getName());

					activityLimit();

					if (activityLimitReached) {
						calculatePenalty();
						System.out.printf("Lower Limit Reached, withdrawing penalty of %.2f\n", penaltyCharge);
						funds -= penaltyCharge;
						System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);

					} else {
						System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
					}
				}
		} else {
			System.out.println("Insuficcient Funds");
		}
	}finally{
		lock.unlock();
	}

}

	//Activity limit is true if activity could is greater than 30
	void activityLimit() {
		lock.lock();
		try {
			activityLimitReached = activityCount >= 30;
		} finally {
			lock.unlock();
		}
	}

	//Calculates the penalty to be applied
	void  calculatePenalty() {
		lock.lock();
		try {
			penaltyCharge = (funds * 0.2);
		} finally {
			lock.unlock();
		}
	}


}