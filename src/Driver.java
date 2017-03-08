import java.util.Random;

public class Driver{
	

	public static void main(String[] args) {


		SavingsAccount s = new SavingsAccount(0, "Savings Account"); //Creates a new Savings Account with �1000 funds
		Random rnd=new Random();
		Random type=new Random();

		for (int k=0;k<4;k++) {
			Thread t15 = new Thread(){
				public  void run(){
					for (int i=0;i<5;i++) {
						int j = type.nextInt(2);
						if (j == 0) 
							s.withdrawFunds(rnd.nextInt(5000));
						else
							s.depositFunds(rnd.nextInt(9999));
					}
				}
			}; 
			t15.start();
		}
	}
}