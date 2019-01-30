/*
 *  Assignment by Eric Roberts
 *
 */
import java.util.Scanner;
public class LetterSubstitutionCipher
{
   public static void main(String[] args)
   {
      Scanner in = new Scanner(System.in);
      
      System.out.println("Please enter 26-letter key: ");
      //String key = in.nextLine();
      String key = "QWERTYUIOPASDFGHJKLZXCVBNM";
      System.out.println("Key: " + key);
      
      System.out.println("Please enter plaintext message: ");
      String plaintext = in.nextLine();
      String ciphertext = encrypt(plaintext, key);
      System.out.println("Ciphertext: " + ciphertext);
   }
   
   /**
    *  Encrypts a message according to the key.  All letters in the string are
    *  converted to uppercase.  Characters which are not letters are copied
    *  to the result unchanged.
    *
    */
   public static String encrypt(String str, String key)
   {   
      String result = "";
      str = str.toUpperCase();
      for (int i = 0; i < str.length(); i++)
      {
         if(str.charAt(i) >= 65 && str.charAt(i) <= 90){
            char ch = str.charAt(i);
            if (Character.isLetter(ch))
            {
               ch = key.charAt(ch - 'A');
            }
            result += ch;
         }
         else
            result += str.charAt(i);
      }
      return result;
   }

/**
 * Inverts a key for a letter-substitution cipher, where a key is a
 * 26-letter string that shows how each letter in the alphabet is
 * translated into the encrypted message.  For example, if the key is
 * "LZDRXPEAJYBQWFVIHCTGNOMKSU", that means that 'A' (the first letter
 * in the alphabet) translates to 'L' (the first letter in the key),
 * 'B' translates to 'Z', 'C' translates to 'D', and so on.  The inverse
 * of a key is a 26-letter that translates in the opposite direction.
 * As an example, the inverse of "LZDRXPEAJYBQWFVIHCTGNOMKSU" is
 * "HKRCGNTQPIXAWUVFLDYSZOMEJB".
 *
 * @param key The original key
 * @return The key that translates in the opposite direction
 *
 * NOTE: WHEN FINISHED TESTING, COPY THIS CODE INTO THE invertKey METHOD FOR
 * THE ENIGMA MODEL CLASS.  THEY SHOULD BE THE SAME.
 */

   public static String invertKey(String key) {
      String result = "";
      String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      char[] map = new char[26];
      
      for(int i = 0; i < 26; i++){
         char k = key.charAt(i);      
         int index = k - 'A';        
         map[index] = alphabet.charAt(i);          
      }
      
      for(int i = 0; i < 26; i++){
         result += map[i];
      }   
      return result;
   }  
   
   private static boolean checkKey(String key){
   
      return false;
   }
}
   
