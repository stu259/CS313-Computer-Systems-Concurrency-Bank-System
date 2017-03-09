import java.util.Random;

/**
 * Created by Blair Ramsay on 09/03/2017.
 */


public class TestGenerator {

    public TestGenerator(){
        SavingsAccount s = new SavingsAccount(0, "Savings Account"); //Creates a new Savings Account with "a" funds
        SavingsAccount s2 = new SavingsAccount(0, "Savings Account 2"); //Creates a new Savings Account with "a" funds
        createThread(s, s2);
    }

    public void createThread(SavingsAccount s, SavingsAccount s2){

        Random rnd=new Random();
        Random type=new Random();
        for (int k=0;k<4;k++) {
            System.out.println("---Thread " + (k+1) + "---");
            Thread t1 = new Thread(){
                public  void run(){
                    for (int i=0;i<5;i++) {
                        int j = type.nextInt(3);
                        int x = rnd.nextInt(9999);
                        System.out.println("---Test #" + (i+1) + "---");
                        if (j == 0) {
                            withdrawTest(s, x);
                            System.out.println("---Test #" + (i + 1) + " Complete---");
                        }else if(j == 1) {
                            depositTest(s, x);
                            System.out.println("---Test #" + (i + 1) + " Complete---");
                        }else if(j == 2) {
                            transferTest(s, s2, x);
                            System.out.println("---Test #" + (i + 1) + " Complete---");
                        }
                    }
                }
            };
            t1.start();
            System.out.println("---Thread " + (k+1) + " Complete---");
        }
    }

    public void depositTest(SavingsAccount s, Integer amount){
        s.depositFunds(amount);
    }

    public void withdrawTest(SavingsAccount s, Integer amount){
        s.withdrawFunds(amount);
    }

    public void transferTest(SavingsAccount s, SavingsAccount s2, Integer amount){
        s.transferAmount(amount, s, s2);
    }

}
