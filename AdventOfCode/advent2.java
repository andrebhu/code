import java.util.*;
import java.io.*;


public class advent2 {
   public static void main(String[] args) throws Exception {
   
   	// Read in input.
      BufferedReader stdin = new BufferedReader(new FileReader("input.txt"));
      //StringTokenizer tok = new StringTokenizer(stdin.readLine());
      
      
      String line = "ababb";
       
       
      int cntTwo = 0;
      int cntThree = 0;   
      boolean two = false;
      boolean three = false;
      
      while((line = stdin.readLine()) != null)
      {   
         Map<Character,Integer> map = new HashMap<Character,Integer>();
         
         for (int i = 0; i < line.length(); i++) { //for each character, counts number of times occurs in HashMap map
            char c = line.charAt(i);   
            if (map.containsKey(c)) {
               int cnt = map.get(c);
               map.put(c, ++cnt);   
            } else {
               map.put(c, 1);
            }      
         }  
         
         System.out.println(map);
         
         two = false;
         three = false;
      
         
         for (Map.Entry<Character, Integer> entry: map.entrySet()) //iterates through map, finds largest Value(count) for Keys(characters)
         {
            if (entry.getValue() == 2){
               two = true;   
            }
            else if (entry.getValue() == 3){
               three = true;
            }
         }
         
         System.out.println(two + " " + three);
      
         if(two)
            cntTwo++;
         if (three)
            cntThree++;     
      }      
      
      System.out.println("Two = " + cntTwo);
      System.out.println("Three = " + cntThree);   
      System.out.println(cntTwo * cntThree);  
   }
}