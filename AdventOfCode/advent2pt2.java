import java.util.*;
import java.io.*;

public class advent2pt2 {
   public static void main(String[] args) throws Exception {
   
   	// Read in input.
      BufferedReader stdin = new BufferedReader(new FileReader("input.txt"));
      //StringTokenizer tok = new StringTokenizer(stdin.readLine());
      
      
      String line = "ababb";
      ArrayList<String> strings = new ArrayList<String>();
      
      while((line = stdin.readLine()) != null)
      {   
         strings.add(line);   
      }   
      //250 lines
      //string 26 long
      int count = 0;
      for(int i = 0; i < 26; i++){
         for(int j = 0; j < strings.size(); j++){
            for(int k = j + 1; k < strings.size(); k++){
               StringBuilder a = new StringBuilder(strings.get(j));
               StringBuilder b = new StringBuilder(strings.get(k));
               a.deleteCharAt(i);
               b.deleteCharAt(i);
               
               String resA = a.toString();
               String resB = b.toString();
               
               if(resA.equals(resB)){
               System.out.println(a.toString());
               System.out.println(b.toString());
                  strings.remove(j);
                  strings.remove(k);  
                  count++;
                  System.out.println(i);
               }
            }
         }
      }
      
      System.out.println(count);
   }
}