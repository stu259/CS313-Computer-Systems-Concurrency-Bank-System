import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Blair Ramsay on 09/03/2017.
 */


public class TestGenerator {
	
	List<String> pairs;
	List<String> generatedStrings;
	List<String> generatedID;

    public TestGenerator(int limit){
    	pairs = new ArrayList<String>();
    	generatedStrings = new ArrayList<String>();
    	generatedID = new ArrayList<String>();
    	generatePairs();
        SavingsAccount s = new SavingsAccount(99999999, "Savings Account"); //Creates a new Savings Account with "a" funds
        SavingsAccount s2 = new SavingsAccount(99999999, "Savings Account 2"); //Creates a new Savings Account with "a" funds
        for(int i = 0;i<limit;i++){
        	createThread(s, s2);
        }
        try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        checkForPairs();
    }

    public void createThread(SavingsAccount s, SavingsAccount s2){

        Random rnd=new Random();
        Random type=new Random();
        for (int k=0;k<2;k++) {
            System.out.println("---Thread " + (k+1) + "---");
            Thread t1 = new Thread(){
                public  void run(){
                    for (int i=0;i<1;i++) {
                        int j = type.nextInt(4);
                        int x = rnd.nextInt(9999);
//                        System.out.println("---Test #" + (i+1) + "---");
                        if (j == 0) {
                            withdrawTest(s, x);
                            generatedID.add(Long.toString(getID(s)));
//                            System.out.println("---Test #" + (i + 1) + " Complete---");
                            generatedStrings.add("withdraw");
                        }else if(j == 1) {
                            depositTest(s, x);
                            generatedID.add(Long.toString(getID(s)));
//                            System.out.println("---Test #" + (i + 1) + " Complete---");
                            generatedStrings.add("deposit");
                        }else if(j == 2) {
                            transferTest(s, s2, x);
                            generatedID.add(Long.toString(getID(s)));
//                            System.out.println("---Test #" + (i + 1) + " Complete---");
                            generatedStrings.add("transfer");
                        }
                        else if(j == 3){
                        	getBalance(s);
                        	generatedID.add(Long.toString(getID(s)));
//                        	System.out.println("---Test #" + (i + 1) + " Complete---");
                        	generatedStrings.add("get");
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
    
    public void getBalance(SavingsAccount s){
    	s.getBalance();
    }
    
    public long getID(SavingsAccount s){
    	long l = s.getID();
    	System.out.println(l);
    	return l;
    }
    
    private void generatePairs(){
    	List<String> types = new ArrayList<String>();
    	types.add("deposit");
    	types.add("transfer");
    	types.add("withdraw");
    	types.add("get");
    	
    	for(int i = 0;i<types.size();i++){
    		for(int j = 0;j<types.size();j++){
//    			if(!(i == j))
    				pairs.add(types.get(i) + "," + types.get(j));
    			
    		}
    	}
    	
    	
//    	for(int i = 0;i<pairs.size();i++){
//    		System.out.println(pairs.get(i));
//    	}

    }
    
    private void checkForPairs(){
    	List<String> copyPairs = pairs;
    	int pairsFound = 0;
    	for(int i = 0;i<(generatedStrings.size()/2);i++){
    		int j = i*2;
    		String compare = generatedStrings.get(j) + "," + generatedStrings.get(j+1);
    		if(copyPairs.contains(compare)){
    			pairsFound++;
    			copyPairs.remove(compare);
    			System.out.println(compare);
    		}
    	}
    	
    	System.out.println(pairsFound);
    }

}
