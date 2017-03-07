import java.util.Random;

public class Driver{
	

	public static void main(String[] args) {


		SavingsAccount s = new SavingsAccount(0, "Savings Account"); //Creates a new Savings Account with �1000 funds
		CheckingAccount c = new CheckingAccount(1500, "Checking Account"); //Creates a new Checking Account with �1000 funds
		CODAccount cod = new CODAccount(3000, "COD Account"); //Creates a new Checking Account with �1000 funds
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
		/*
		Thread t16 = new Thread(){
			public  void run(){
				s.withdrawFunds(rnd.nextInt(5000));
			}
		};

		Thread t17 = new Thread(){
			public  void run(){
				s.depositFunds(9999);
			}
		};
*/
//		Thread t1 = new Thread(){
//			public  void run(){
//				s.transferAmount(2000, s, c);
//			}
//		};
//
//
//		Thread t2 = new Thread(){
//			public void run(){
//				s.depositFunds(1000);
//			}
//		};


//		Thread t11 = new Thread(){
//			public void run(){
//				cod.withdrawFunds(200);
//			}
//		};
//
//		Thread t12 = new Thread(){
//			public void run(){
//				cod.depositFunds(5);
//			}
//		};
//
//		Thread t13 = new Thread(){
//			public void run(){
//				cod.withdrawFunds(200);
//			}
//		};
//
//		Thread t14 = new Thread(){
//			public void run(){
//				cod.depositFunds(1000);
//			}
//		};
		/*
		Thread t3 = new Thread(){	
			public void run(){
				s.depositFunds(50);
				s.depositFunds(20);
				s.withdrawFunds(500);
				s.depositFunds(20);
				s.getBalance();
			}
		};

		Thread t4 = new Thread(){	
			public void run(){
				s.depositFunds(50);
				s.depositFunds(20);
				s.withdrawFunds(500);
				s.depositFunds(20);
				s.getBalance();
			}
		};

		Thread t5 = new Thread(){	
			public void run(){
				s.depositFunds(50);
				s.depositFunds(20);
				s.withdrawFunds(500);
				s.depositFunds(20);
				s.getBalance();
			}
		};
		*/

//		Thread t6 = new Thread(){
//			public void run(){
//				s.withdrawFunds(4);
//			}
//		};
		
//		Thread t7 = new Thread(){
//			public void run(){
//				s.withdrawFunds(5);
//			}
//		};
		
//		Thread t8 = new Thread(){
//			public void run(){
//				s.withdrawFunds(6);
//			}
//		};
		
//		Thread t9 = new Thread(){
//			public void run(){
//				s.withdrawFunds(7);
//			}
//		};
		
//		Thread t10 = new Thread(){
//			public void run(){
//				s.withdrawFunds(8);
//			}
//		};


	//	t1.start();
	//	t2.start();

//		t11.start();
//		t12.start();
//		t13.start();
//		t14.start();
		//t3.start();
		//t4.start();
		//t5.start();
//		t6.start();
//		t7.start();
//		t8.start();
//		t9.start();
//		t10.start();
//		t16.start();
//		t17.start();
	}
}