import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Blair Ramsay on 09/03/2017.
 */


public class TestGenerator {
	
	List<String> pairs;
	List<String> generatedID;
	Map<String,String> idToString;
	Map<String,threadInfo> thread_Info;
	SavingsAccount s;
    SavingsAccount s2;
	
	List<Thread> threads;

    public TestGenerator(int limit){
    	//collections for storing predefined pairs, id's and id's to method executed
    	pairs = new ArrayList<String>();
    	generatedID = new ArrayList<String>();
    	idToString = new HashMap<String,String>();
    	threads = new ArrayList<Thread>();
    	thread_Info = new HashMap<String,threadInfo>();
    
    	//generate tests
        s = new SavingsAccount(0, "Savings Account"); //Creates a new Savings Account with "a" funds
        s2 = new SavingsAccount(0, "Savings Account 2"); //Creates a new Savings Account with "a" funds
        for(int i = 0;i<limit;i++){
        	threads.clear();
        	createThreads(s, s2);
        	waitFor(2);
        	startThreads(5);     
        	waitFor(1);
        }
        
        
        //creates all pairs
        generatePairs();
        getIDs(s);
        //checks pairs covered by tests
        checkForPairs();
    }

    public void createThreads(SavingsAccount s, SavingsAccount s2){

        Random rnd=new Random();
        Random type=new Random();
        
        for (int k=0;k<2;k++) {
        	int j = type.nextInt(4);
            int x = rnd.nextInt(9999);
            
            Thread t1 = new Thread(){
                public  void run(){
                	String threadID = (Long.toString(Thread.currentThread().getId()));
                	System.out.println(threadID);
                    for (int i=0;i<1;i++) {
                        if (j == 0) {
                            withdrawTest(s, x);
                            idToString.put(threadID, "withdraw");
                            thread_Info.put(threadID, new threadInfo(j,x,"withdraw"));
                        }else if(j == 1) {
                            depositTest(s, x);
                            idToString.put(threadID, "deposit");
                            thread_Info.put(threadID, new threadInfo(j,x,"deposit"));
                        }else if(j == 2) {
                            transferTest(s, s2, x);
                            idToString.put(threadID, "transfer");
                            thread_Info.put(threadID, new threadInfo(j,x,"transfer"));
                        }
                        else if(j == 3){
                        	getBalance(s);                        	
                        	idToString.put(threadID, "get");
                        	thread_Info.put(threadID, new threadInfo(j,x,"get"));
                        }
//                        System.out.println("ThreadID: " + threadID + "  Method: " + idToString.get(threadID));
                    }
                }
            };
            threads.add(t1);
        }
        threads.get(0).run();
        threads.get(1).run();
    }
    
    private void createThread(int x, int j, String method, SavingsAccount s, SavingsAccount s2){
    	Thread t1 = new Thread(){
            public  void run(){
            	String threadID = (Long.toString(Thread.currentThread().getId()));
                switch(method){
                case "withdraw":
                	withdrawTest(s, x);
                    idToString.put(threadID, "withdraw");
                    break;
                case "deposit":
                	depositTest(s, x);
                    idToString.put(threadID, "deposit");
                    break;
                case "transfer":
                	transferTest(s, s2, x);
                    idToString.put(threadID, "transfer");
                    break;
                case "get":
                	getBalance(s);                        	
                	idToString.put(threadID, "get");
                	break;
                default:
                	return;
                }
            };
    	};
    	t1.start();
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

		public void setJ(int j) {
			this.j = j;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
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
    
    private void startThreads(int numRuns){
    	Thread a = threads.get(0);
    	Thread b = threads.get(1);
    	String aID =  String.valueOf((a.getId()));
    	String bID =  String.valueOf((b.getId()));
    	threadInfo aInfo = thread_Info.get(aID);
		threadInfo bInfo = thread_Info.get(bID);
		System.out.println("ID a: " + aInfo + "  ID b: " + bInfo);
    	for(String key: thread_Info.keySet()){
    		System.out.println(key);
    		System.out.println(thread_Info.get(key));
    	}
    	getIDs(s);
    	
    	for(int i = 0; i<(numRuns - 1); i++){
    		createThread(aInfo.getX(), aInfo.getJ(), aInfo.getMethod(), s, s2);
    		createThread(bInfo.getX(), bInfo.getJ(), bInfo.getMethod(), s, s2);
    		waitFor(1);
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
    		System.out.println(id.get(i));
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
    	List<String> foundPairs = new ArrayList<String>();
    	
    	copyPairs.addAll(pairs);
    	
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
	    			copyPairs.remove(compare);
	    			foundPairs.add(compare);
	    		}
	    	}
    	}    	
    	System.out.println(foundPairs.size());
    	printList(foundPairs);
    	System.out.println("Coverage as a percentage: " + returnCoverage(foundPairs.size()) + "%");
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

}
