/*
 * File: EnigmaModel.java
 * ----------------------
 * This is the starter file for a class that models the German Enigma
 * machine from World War II.
 *
 * // Remember to correct this comment when you implement the class
 */

public class EnigmaModel {

/* Private instance variables */

   // Add any instance variables you need here
   private int rOrder;
   private int[] rSetting;
   private String fastRotor;
   private String midRotor;
   private String slowRotor;
 /**
 * Creates a new object that models the operation of an Enigma machine.
 * By default, the rotor order is initialized to 123, which indicates
 * that stock rotors 1, 2, and 3 are used for the slow, medium, and
 * fast rotor positions, respectively.  The rotor setting is initialized
 * to "AAA".
 */

   public EnigmaModel() {
      rOrder = 123;
      rSetting = new int[] {1, 1, 1};
   }

/**
 * Sets the rotor order for the Enigma machine.  The rotor order is
 * specified as a three-digit integer giving the numbers of the stock
 * rotors to use.  For example, calling setRotorOrder(513) uses stock
 * rotor 5 as the slow rotor, stock rotor 1 as the medium rotor, and
 * stock rotor 3 as the fast rotor.  This method returns true if the
 * argument specifies a legal rotor order (three digits in the range
 * 1 to 5 with no duplication) and false otherwise.
 *
 * @param order A three-digit integer specifying the rotor order
 * @return A Boolean value indicating whether the rotor order is legal
 */

   public boolean setRotorOrder(int order) {
      int temp = order;
      int[] used = new int[5];
      while (temp > 0)
      {
         int digit = temp % 10;
         if (digit > 0 && digit < 6)
         {
            if (used[digit - 1] == 0)
            {
               used[digit - 1] = 1;
            }
            else
            {
               return false;  //a number was re-used
            }
         }
         else
         {
            return false;     //a digit out of bounds
         }
         temp = temp / 10;
      }
      rOrder = order;
      
      
      
      //need to assign fast/mid/slow rotors   
      int t = rOrder % 10;
           
      if(t == 1){      
         fastRotor = STOCK_ROTOR_1;
      }
      else if(t == 2){
         fastRotor = STOCK_ROTOR_2;
      }
      else if(t == 3){
         fastRotor = STOCK_ROTOR_3;
      }
      else if(t == 4){
         fastRotor = STOCK_ROTOR_4;
      }
      else{
         fastRotor = STOCK_ROTOR_5;
      }
      
      t = rOrder / 10;
      t = t % 10;
      
      if(t == 1){      
         midRotor = STOCK_ROTOR_1;
      }
      else if(t == 2){
         midRotor = STOCK_ROTOR_2;
      }
      else if(t == 3){
         midRotor = STOCK_ROTOR_3;
      }
      else if(t == 4){
         midRotor = STOCK_ROTOR_4;
      }
      else{
         midRotor = STOCK_ROTOR_5;
      }
      
      t = rOrder / 100;      
      t = t % 10;   
      
      if(t == 1){      
         slowRotor = STOCK_ROTOR_1;
      }
      else if(t == 2){
         slowRotor = STOCK_ROTOR_2;
      }
      else if(t == 3){
         slowRotor = STOCK_ROTOR_3;
      }
      else if(t == 4){
         slowRotor = STOCK_ROTOR_4;
      }
      else{
         slowRotor = STOCK_ROTOR_5;
      }
      return true;
   }

/**
 * Establishes the rotor setting for the Enigma machine.  A legal rotor
 * setting must be a string of three uppercase letters.  This method
 * returns true if the argument is a legal setting and false otherwise.
 *
 * @param str The rotor settings
 * @return A Boolean value indicating whether the rotor setting is legal
 */

   public boolean setRotorSetting(String setting) {
      String[] turns = setting.trim().split(" ");
      int[] settings = new int[3];
      if (turns.length != 3)
         return false;     //A legal key must have three separated numbers
      
      for (int i = 0; i < turns.length; i++)
      {
         try {
            int z = Integer.parseInt(turns[i]);
            if (z < 0 || z > 25)
               return false;
            settings[i] = z;
         }
         catch (NumberFormatException e)
         {
            return false;
         }
      }
      rSetting = settings;
      
      //actually turning the rotors
      for(int i = 0; i < rSetting[0]; i++){
         //System.out.println("Rotating slow rotor " + (i + 1) + " times...");
         slowRotor = advanceRotor(slowRotor);
         
      }
      for(int i = 0; i < rSetting[1]; i++){
         //System.out.println("Rotating mid rotor " + (i + 1) + " times...");
         midRotor = advanceRotor(midRotor);
      }
      for(int i = 0; i < rSetting[2]; i++){
         //System.out.println("Rotating fast rotor " + (i + 1) + " times...");
         fastRotor = advanceRotor(fastRotor);
      }
      System.out.println("Slow rotor: " + slowRotor);
      System.out.println("Mid rotor: " + midRotor);
      System.out.println("Fast rotor: " + fastRotor);  
      
      return true; 
   }

   public int[] getRotorSetting() {
      return rSetting;  
   }

   public String encrypt(String plaintext) {
   
      plaintext = plaintext.toUpperCase();
      String ciphertext = "";
      String a, b, c, d, e, f, g;
         
      for(int i = 0; i < plaintext.length(); i++){  
      
         if(plaintext.charAt(i) >= 65 && plaintext.charAt(i) <= 90){
            rSetting[2]++;
            fastRotor = advanceRotor(fastRotor);
                    
         //three rotors reset
            if(rSetting[0] % 25 == 0 && rSetting[1] % 25 == 0 && rSetting[2] % 26 == 0){ 
               rSetting[0] = 0;
               rSetting[1] = 0;
               rSetting[2] = 0;
            
               slowRotor = advanceRotor(slowRotor);
               midRotor = advanceRotor(midRotor);
            }
            //two rotors reset
            else if(rSetting[1] % 25 == 0 && rSetting[2] % 26 == 0){
               rSetting[0] += 1;
               rSetting[1] = 0;
               rSetting[2] = 0;
            
               slowRotor = advanceRotor(slowRotor);
               midRotor = advanceRotor(midRotor);
            }
            //one rotor resets
            else if(rSetting[2] % 26  == 0){
               rSetting[1] += 1;
               rSetting[2] = 0;
            
               midRotor = advanceRotor(midRotor);
            }  
            
         //encryption   
            a = LetterSubstitutionCipher.encrypt(String.valueOf(plaintext.charAt(i)), fastRotor);
            b = LetterSubstitutionCipher.encrypt(a, midRotor);
            c = LetterSubstitutionCipher.encrypt(b, slowRotor);
            d = LetterSubstitutionCipher.encrypt(c, REFLECTOR);   
            e = LetterSubstitutionCipher.encrypt(d, LetterSubstitutionCipher.invertKey(slowRotor));
            f = LetterSubstitutionCipher.encrypt(e, LetterSubstitutionCipher.invertKey(midRotor));
            g = LetterSubstitutionCipher.encrypt(f, LetterSubstitutionCipher.invertKey(fastRotor));
            ciphertext += g;
         }
         else{
            ciphertext += plaintext.charAt(i);
         }
      }
      return ciphertext; 
   }

   private static String advanceRotor(String input){
      String first = "";
      String result = "";
      
      for(int i = 1; i < input.length(); i++){
         first += input.charAt(i);
      }        
      first += input.charAt(0);
   
   
      for(int i = 0; i < first.length(); i++){
         char c = first.charAt(i);
         if(c == 'A'){
            result += 'Z';
         }
         else
            result += (char)(c - 1);
      }
      return result;
   }
   
   private static final String STOCK_ROTOR_1 = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
   private static final String STOCK_ROTOR_2 = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
   private static final String STOCK_ROTOR_3 = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
   private static final String STOCK_ROTOR_4 = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
   private static final String STOCK_ROTOR_5 = "VZBRGITYUPSDNHLXAWMJQOFECK";
   private static final String REFLECTOR = "IXUHFEZDAOMTKQJWNSRLCYPBVG";
}
