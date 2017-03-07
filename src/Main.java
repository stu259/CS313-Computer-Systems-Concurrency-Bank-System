import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
public class Main {
  public static void main(String[] argv) throws Exception {
	  
	  //A number of Test Threads
	  //ThreadGroup tg = new ThreadGroup("tg");
	  
	  Thread t1 = new Thread(){
			public  void run(){
				try {
					Thread t = Thread.currentThread();
					t.setName("Test Thread #1");
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		
		Thread t2 = new Thread(){	
			public void run(){
				try {
					Thread t = Thread.currentThread();
					t.setName("Test Thread #2");
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Thread t3 = new Thread(){
			public void run(){
				try {
					Thread t = Thread.currentThread();
					t.setName("Test Thread #3");
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Thread t4 = new Thread(){	
			public void run(){
				try {
					Thread t = Thread.currentThread();
					t.setName("Test Thread #4");
					Thread.sleep(5000);
					t.setName("Still Test Thread #4");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Thread t5 = new Thread(){	
			public void run(){
				try {
					Thread t = Thread.currentThread();
					t.setName("Test Thread #5");
					Thread.sleep(6000);
					t.setName("Still Test Thread #5");
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
	//we create a table with a model that handles data insertion    
      
	    DefaultTableModel model = new DefaultTableModel();
	    JTable table = new JTable(model);
	    
	  ActionListener taskPerformer = e -> {
          model.setRowCount(0);

          //first we create a test thread
          Thread testThread = new Thread("test");
          testThread.start();


          boolean hasParent = true;
          ThreadGroup currentThreadGroup = testThread.getThreadGroup();


          //this while-loop finds root thread group
          while (hasParent) {
                if (currentThreadGroup.getParent() == null) {
                    hasParent = false;
                } else {
                    currentThreadGroup = currentThreadGroup.getParent();
                }
            }

              //array of threads that holds active threads
            Thread[] threads = new Thread[currentThreadGroup.activeCount() - 1];
            currentThreadGroup.enumerate(threads);

            // Create rows & filling them with data
            int num = 0;
            for(Thread t: threads){
            model.insertRow( num, new Object[] {t.getName(),t.getId(), t.getPriority(),t.getState(),t.isDaemon(),t.getThreadGroup().getName() });
                num++;
            }
      };
      
      Timer timer = new Timer(1000, taskPerformer);
      timer.setRepeats(true);
      timer.start();
      

      //columns
	  model.addColumn("Name");
	  model.addColumn("Id");
	  model.addColumn("Priority");
	  model.addColumn("State");
	  model.addColumn("isDaemon");
	  model.addColumn("Group Name");
      
		//table dimensions
		JFrame f = new JFrame();
		f.setSize(900, 500);
		f.add(new JScrollPane(table));
		f.setVisible(true);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//Starts our custom threads
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
  }
}