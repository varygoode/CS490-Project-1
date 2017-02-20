package project1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Rob Vary
 */
public class Project1 
{
    private static final int MYTHREADS = 30;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //get the singleton heap fired up
        MinHeap heap = MinHeap.getInstance();
        
        //prepare an empty array of process nodes so we have a max
        ProcessNode[] heapArray = new ProcessNode[30];
        
        //initialize the MinHeap singleton
        heap.init(heapArray, 30, 0);
        
        //this is to run the threads in parallel instead of sequentially
        ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
        Runnable consumer1 = new ConsumerProcess();
        executor.execute(consumer1);
        Runnable consumer2 = new ConsumerProcess();
        executor.execute(consumer2);
        
        //the creator process object takes in a list of the consumers to stop when done
        ArrayList<ConsumerProcess> consumers = new ArrayList<ConsumerProcess>(){
            {
                add((ConsumerProcess)consumer1);
                add((ConsumerProcess)consumer2);
            }
        };
        
        Runnable creator = new CreatorProcess(consumers);
        executor.execute(creator);
        
        executor.shutdown();
        
        while(!executor.isTerminated())
        {
            //all the stuff to do is being done in the threads
            //this just ensures the program doesn't quit early
        }
        
        //show what our cutoff condition made us miss
        System.out.println("\n\nHeap after cutoff condition:");
        MinHeap.getInstance().printHeap();
    }    
}