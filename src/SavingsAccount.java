import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SavingsAccount extends Account{

	private boolean lowerLimitReached; //Boolean value stating whether or not the Lower Limit has been reached
	private double penaltyCharge; //The value of the penalty charge to be applied
	private ReentrantLock lock;
	private final Condition notEoungh;


	//Savings Account Constructor
	//Takes an amount and a name as parameters
	SavingsAccount(double a, String name) {
		super(a); //Account is a supertype of savings account
		accountName = name;
		lock = new ReentrantLock();
		notEoungh = lock.newCondition();
	}
	
	//Transfers funds between 2 accounts
	//Takes an amount and 2 accounts as parameters
    void transferAmount(double a1, Account a, Account b){
//        System.out.println("Calling Transfer");
        lock.lock();
        if(lock.isHeldByCurrentThread()){
			currentId.add((int)Thread.currentThread().getId());
		}
        try {
            if(a1 > a.funds){
//                System.out.println("waiting");
                try {
                    notEoungh.await(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if(a1 > a.funds){
//                System.out.printf("Thread with ID %d, %s Insufficient funds cannot transfer\n", Thread.currentThread().getId(), getName());
            } else {
//                System.out.printf("Thread with ID %d, Transferring: %.2f from %s to %s\n", Thread.currentThread().getId(), a1, a.getName(), b.getName());
                a.funds = a.funds - a1;
                b.funds = b.funds + a1;
//                System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), a.getName(), a.getBalance());
//                System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), b.getName(), b.getBalance());
            }
        } finally {
            lock.unlock();
        }
    }
		
	//Deposits funds into an account
    //Takes an amount as a parameter
	void depositFunds(double amount){
//        System.out.println("Calling Deposit");
		lock.lock();
		if(lock.isHeldByCurrentThread()){
			currentId.add((int)Thread.currentThread().getId());
		}
		try {
//			System.out.printf("Thread with ID %d, depositing: %.2f into %s\n", Thread.currentThread().getId(), amount, getName());
			funds += amount;
//			System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
			notEoungh.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	
	//Withdraws funds from an account
    //Takes an amount as a parameter
	void withdrawFunds(double amount) {
//        System.out.println("Calling Withdraw");
		lock.lock();
		if(lock.isHeldByCurrentThread()){
			currentId.add((int)Thread.currentThread().getId());
		}
		try{
//			System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
			if(amount > funds){
//				System.out.println("waiting");
				try {
					notEoungh.await(2, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if(amount > funds){
//				System.out.printf("Thread with ID %d, %s Insufficient funds cannot withdraw\n", Thread.currentThread().getId(), getName());
			} else {
				funds -= amount;
				//lowerLimit();
				if (lowerLimitReached) {
					//calculatePenalty();
//					System.out.printf("Lower Limit Reached, withdrawing penalty of " + penaltyCharge);
					funds -= penaltyCharge;
//					System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
				} else {
//					System.out.printf("Thread with ID %d, %s current funds: %.2f\n", Thread.currentThread().getId(), getName(), funds);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	//Sets the LowerLimit boolean to true/false
	//Lower limit is true if funds gets below £100
	private void lowerLimit() {
		lock.lock();
		try {
			lowerLimitReached = funds < 100;
		} finally {
			lock.unlock();
		}
	}

	//Calculates the penalty charge to be applied
	private void calculatePenalty() {
		lock.lock();
		try {
			penaltyCharge = (funds * 0.1);
		} finally {
			lock.unlock();
		}
	}

}