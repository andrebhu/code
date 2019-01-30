/*
 * File: EnigmaSimulator.java
 * --------------------------
 * This is the starter file for an Enigma simulator that uses the console to
 * enter the rotor order, rotor settings, and plaintext.
 *
 * // Remember to correct this comment when you write the actual program
 */
import java.util.Scanner;
public class EnigmaSimulator {

   public static void main(String[] args) {
      // Fill in the implementation along with any helper methods
      Scanner in = new Scanner(System.in);
      EnigmaModel machine = new EnigmaModel();
      boolean getInfo = false;
      
      while (!getInfo)
      {
         System.out.println("Enter rotor order: ");
         int rOrder = in.nextInt();
         in.nextLine();
         if (!machine.setRotorOrder(rOrder))
            System.out.println("Please re-enter, " + rOrder + " is not valid");
         else
            getInfo = true;
      }
      getInfo = false;
      while (!getInfo)
      {
         System.out.println("Enter rotor setting: ");
         String rSetting = in.nextLine();
         if (!machine.setRotorSetting(rSetting))
             System.out.println("Please re-enter, " + rSetting + " is not a valid setting");
         else
             getInfo = true;
      }
      
      System.out.println("Enter plaintext line: ");
      String message = in.nextLine();
      System.out.println("The encoded ciphertext: " + machine.encrypt(message));
      

   }

}
