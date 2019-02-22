/*
Andre Hu
2/20/2019
*/
import java.util.Scanner;
import java.io.*;
   
public class deHuffman
{

   public static void main(String[] args) throws IOException
   {
      System.out.println("Messages:");
      System.out.println("fault | laughter | money | tj\n");
      System.out.print("Please choose a message: ");
      Scanner in = new Scanner(System.in);
      String fileName = in.nextLine();
      
      Scanner scheme = new Scanner(new File("scheme." + fileName + ".txt"));
      Scanner message = new Scanner(new File("message." + fileName + ".txt"));
      
      TreeNode a = huffmanTree(scheme);   
      System.out.println(dehuff(message.nextLine(), a));   
   }

   public static TreeNode huffmanTree(Scanner schemeScan)
   {
      TreeNode root = new TreeNode(null);
      TreeNode current = root;
      
      while(schemeScan.hasNext()){
      
         String s = schemeScan.nextLine();
         String letter = s.substring(0, 1); //value to put into node
         s = s.substring(1); //string of 0s and 1s   
         
         
         //traverses before end, creates node of node doesn't exist
         for(int i = 0; i < s.length() - 1; i++){
            if(s.charAt(i) == '0'){
               if(current.getLeft() == null){
                  current.setLeft(new TreeNode(null));
               }
               current = current.getLeft();
            
            }
            else if(s.charAt(i) == '1'){
               if(current.getRight() == null){
                  current.setRight(new TreeNode(null));
               }
               current = current.getRight();      
            }
         }
         
         //adding leafs
         if(s.charAt(s.length() - 1) == '0'){
            current.setLeft(new TreeNode(letter));
         }
         else
            current.setRight(new TreeNode(letter));
            
         //reset current back to root      
         current = root;
      }
      return current; 
   }

   public static String dehuff(String text, TreeNode root)
   {
      TreeNode current = root;
      String ret = "";
               
      for(int i = 0; i < text.length(); i++){
         if(text.charAt(i) == '0'){
            current = current.getLeft();
            
            if(current.getValue() != null){
               ret += current.getValue();
               current = root;
            }
         }
         else{
            current = current.getRight();
            if(current.getValue() != null){
               ret += current.getValue();
               current = root;
            }
         }
      }
      return ret;
   }
}
