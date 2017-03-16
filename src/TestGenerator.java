import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.text.StyledEditorKit.ForegroundAction;

/**
 * Created by Blair Ramsay on 09/03/2017.
 */


public class TestGenerator {
	
	List<String> pairs;
	List<String> foundPairs;
	List<String> generatedID;
	List<Boolean> locks;
	Map<String,String> idToString;
	Map<String,threadInfo> threadStuff;
	
	List<Thread> threads;

    public TestGenerator(double coverage){
    	//collections for storing predefined pairs, id's and id's to method executed
    	pairs = new ArrayList<String>();
    	generatedID = new ArrayList<String>();
    	idToString = new HashMap<String,String>();
    	locks = new ArrayList<Boolean>();
    	threads = new ArrayList<Thread>();
    	threadStuff = new HashMap<String, threadInfo>();
    	foundPairs = new ArrayList<String>();
    
    	//generate tests
        SavingsAccount s = new SavingsAccount(0, "Savings Account"); //Creates a new Savings Account with "a" funds
        SavingsAccount s2 = new SavingsAccount(0, "Savings Account 2"); //Creates a new Savings Account with "a" funds
                
        
        generatePairs();
        System.out.println(coverage);
        while((coverage > returnCoverage(foundPairs.size()))){
	        threads.clear();
	        s.clearIDs();
	        createThreads(s, s2);
	        startFirstThreads();
	        waitFor(2);
	        reRunThreads(2, 2, s,s2);
        
        
	        getIDs(s);
	        //checks pairs covered by tests
	        checkForPairs();
        }
        
        
//        getLocks(s);
//        printList(locks);
        
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Pairs Found : " + foundPairs.size());
        printList(foundPairs);
        System.out.println("");
        System.out.println("");
        System.out.println("Pairs not found: " + (pairs.size() - foundPairs.size()));
        List<String> copyPairs = new ArrayList<String>();
        copyPairs.addAll(pairs);
        for(String pair: foundPairs){
        	copyPairs.remove(pair);
        }
        printList(copyPairs);
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        System.out.println("Final coverage: " + returnCoverage(foundPairs.size()));
    }

    public void createThreads(SavingsAccount s, SavingsAccount s2){

        Random rnd=new Random();
        Random type=new Random();
        
        for (int k=0;k<2;k++) {
            Thread t1 = new Thread(){
                public  void run(){
                	String threadID = (Long.toString(Thread.currentThread().getId()));
//                	System.out.println(threadID);
                    for (int i=0;i<1;i++) {
                        int j = type.nextInt(4);
                        int x = rnd.nextInt(9999);
                        if (j == 0) {
                            withdrawTest(s, x);
                            idToString.put(threadID, "withdraw");
                            threadStuff.put(threadID, new threadInfo(j,x,"withdraw"));
                        }else if(j == 1) {
                            depositTest(s, x);
                            idToString.put(threadID, "deposit");
                            threadStuff.put(threadID, new threadInfo(j,x,"deposit"));
                        }else if(j == 2) {
                            transferTest(s, s2, x);
                            idToString.put(threadID, "transfer");
                            threadStuff.put(threadID, new threadInfo(j,x,"transfer"));
                        }
                        else if(j == 3){
                        	getBalance(s);                        	
                        	idToString.put(threadID, "get");
                        	threadStuff.put(threadID, new threadInfo(j,x,"get"));
                        }
//                        System.out.println("ThreadID: " + threadID + "  Method: " + idToString.get(threadID));
                    }
                }
            };
            threads.add(t1);
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
    
    private void startFirstThreads(){
    	for(int i = 0; i<threads.size();i++){
    		threads.get(i).start();
    	}
    }
    
    private void waitFor(long t){
    	try {
			TimeUnit.SECONDS.sleep(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void getIDs(SavingsAccount s){
    	List<Integer> id = s.getID();
    	for(int i=0; i<s.getID().size(); i++){
    		generatedID.add(String.valueOf(id.get(i)));
    	}
    }
    
    private void generatePairs(){
    	List<String> types = new ArrayList<String>();
    	//all methods which use the locks
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
    }
    
    private void checkForPairs(){
    	List<String> copyPairs = new ArrayList<String>();
    	copyPairs.addAll(pairs);
    	
    	for(String pair: foundPairs){
    		copyPairs.remove(pair);
    	}
    	
    	for(int i = 0;i<(generatedID.size() -1);i= i + 2){
    		//if thread id is repeated that means the thread is waiting.
    		if(generatedID.get(i).equals(generatedID.get(i+1))){
    			i++;	
    		}
	    	else{
	    		//make string from previous id and current id
	    		String compare = idToString.get(generatedID.get(i)) + "," + idToString.get(generatedID.get(i+1));
	    		if(copyPairs.contains(compare)){
	    			//new pair found
	    			System.out.println("------------------------------------------------");
	    			System.out.println("Pair Found: " + compare);
	    			System.out.println("------------------------------------------------");
	    			copyPairs.remove(compare);
	    			foundPairs.add(compare);
	    		}
	    	}
    	}
    	System.out.println("------------------------------------------------");
    	System.out.println("Current coverage as a percentage: " + returnCoverage(foundPairs.size()) + "%");
    	System.out.println("------------------------------------------------");
    }
    
    //method for printing out a list. Used for testing code
    private void printList(List<?> print){
    	for(int i = 0; i < print.size();i++){
    		System.out.println(print.get(i));
    	}
    }
    
    private double returnCoverage(int pairsCovered){
    	double total = (double) pairs.size();
    	double found = (double) pairsCovered;
    	return (found/total)*100;
    }
    
    private class threadInfo{
    	private int j;
    	private int x;
    	private String method;
    	
    	public threadInfo(int j, int x, String method){
    		this.method = method;
    		this.j = j;
    		this.x = x;
    	}
		public int getJ() {
			return j;
		}

		public int getX() {
			return x;
		}

		public String getMethod() {
			return method;
		}

    }
    
    private void reRunThreads(int numRuns, int randomNum, SavingsAccount s, SavingsAccount s2){
    	Thread a = threads.get(0);
    	Thread b = threads.get(1);
    	String aID =  String.valueOf((a.getId()));
    	String bID =  String.valueOf((b.getId()));
    	threadInfo aInfo = threadStuff.get(aID);
		threadInfo bInfo = threadStuff.get(bID);
//		System.out.println(threadStuff.get(aID));
//		System.out.println(threadStuff.get(bID));
		if(threadStuff.get(aID) == null || threadStuff.get(bID) == null){
			return;
		}
		
		if(aInfo.getMethod().equals("get"))
			System.out.println("ID : " + aID + ". Action: " + aInfo.getMethod());
		else
			System.out.println("ID : " + aID + ". Action: " + aInfo.getMethod() + ". Value: " + aInfo.getX());
		if(aInfo.getMethod().equals("get"))
			System.out.println("ID : " + bID + ". Action: " + bInfo.getMethod());
		else
			System.out.println("ID : " + bID + ". Action: " + bInfo.getMethod() + ". Value: " + bInfo.getX());
    	
    	
    	//re running the threads with different values
    	Random rnd=new Random();
    	for(int i = 0; i<(randomNum); i++){
    		int x = rnd.nextInt(9999);
    		for(int j = 0; j<(numRuns);j++){
	    		createThread(x, aInfo.getJ(), aInfo.getMethod(), s, s2);
	    		createThread(x, bInfo.getJ(), bInfo.getMethod(), s, s2);
	    		waitFor(1);
    		}
    	}
    }
    
    private void createThread(int x, int j, String method, SavingsAccount s, SavingsAccount s2){
    	Thread t1 = new Thread(){
            public  void run(){
            	String threadID = (Long.toString(Thread.currentThread().getId()));
                switch(method){
                case "withdraw":
                	withdrawTest(s, x);
                    idToString.put(threadID, "withdraw");
                    System.out.println("ID :" + threadID + ". Action: withdraw. Value: " + x);
                    break;
                case "deposit":
                	depositTest(s, x);
                    idToString.put(threadID, "deposit");
                    System.out.println("ID :" + threadID + ". Action: deposit. Value: " + x);
                    break;
                case "transfer":
                	transferTest(s, s2, x);
                    idToString.put(threadID, "transfer");
                    System.out.println("ID :" + threadID + ". Action: transfer. Value: " + x);
                    break;
                case "get":
                	getBalance(s);                        	
                	idToString.put(threadID, "get");
                	System.out.println("ID :" + threadID + ". Action: get.");
                	break;
                default:
                	return;
                }
            };
    	};
    	t1.start();
    }

    private void getLocks(SavingsAccount s){
    	locks.addAll(s.getLocksBool());
    	printList(locks);
    }

}
