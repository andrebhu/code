import java.util.*;
import java.io.*;

public class advent1 {
   public static void main(String[] args) throws Exception {
   
   	// Read in input.
      BufferedReader stdin = new BufferedReader(new FileReader("input.txt"));
      //StringTokenizer tok = new StringTokenizer(stdin.readLine());
   	
      String line;
       
      ArrayList<Integer> values = new ArrayList<Integer>();
      
      
      /*
      Places all the values into an arraylist, values      
      */
      while((line = stdin.readLine()) != null){ 
         if(line.substring(0, 1).equals("-"))
         {
            values.add(Integer.parseInt(line.substring(1)) * -1);
            
         }
         else{
            values.add(Integer.parseInt(line.substring(1)));
         }     
      } 
      
      /*
      Loops through values, stores sums into store, checks if duplicate occurs
      */      
      ArrayList<Integer> store = new ArrayList<Integer>();
      int sum = 0;
      int i = 0;
      boolean notFound = true;
      
      while(notFound){
         if(i == values.size()){
            i = 0;
         }   
         else{
            sum += values.get(i);
            
            if(!store.contains(sum)){
               store.add(sum);
            }
            else{
               notFound = false;
               System.out.println(sum);   
            }   
            i++;
         }
      }  
   }
}
