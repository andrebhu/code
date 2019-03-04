//Rev. Dr. Douglas R Oberle - Nicholas Hodgman 2015
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

/**************************************************
Given the Tree class, use an array of Trees (forest) to evolve them into a best fit

Tree Object:

Tree makeRandomTree() 				returns a random fractal
Tree mutate(int mutationRate)		given the current tree, return a mutation of the tree where each field can vary by mutationRate
int compareTo(Object other)		Trees compare to one another by how fit they are (one closer to the best fit is > one that is further away)

**************************************************/

public class TreePanel extends JPanel
{
   private static Tree [] forest;             //sample set of Trees (most fit is at forest[0], sorted by best fit)
   private static int SAMPLE_SIZE = 10;       //number of trees in population to mutate (2-25) 
   private static int MUTATION_RATE = 5;      //percentage a tree can mutate per field from one generation to the next (1-100)
   private static int numGenerations;         //count the number of generations
   private Timer t;							       //used to set the speed of generation refresh
   private static boolean evolve;             //have we started auto evolution?

   public TreePanel()
   {
      numGenerations = 0;
      evolve = false;
      forest = new Tree[SAMPLE_SIZE];
      for(int i=0; i<forest.length; i++)
         forest[i] = forest[i].makeRandomTree();
   
      setFocusable(true);
      addKeyListener(new Keyboard());
      addMouseListener(new Mouse());
   
      t = new Timer(0, new Listener());	   //0 delay between frames
      t.start(); 
   }
 
  //pre:  a and b are valid indicies of list
  //post: swaps list[a] and list[b]'s values
   public static void swap(Comparable [] list, int a, int b)
   {
      Comparable temp = list[a];
      list[a] = list[b];
      list[b] = temp;
   }

//post:  sorts list least to greatest
   public static void sort(Comparable [] list)
   {
      for(int i=0; i<list.length; i++)
      {
         int min = i;
         for(int j=i+1; j<list.length; j++)
            if(list[min].compareTo(list[j]) < 0)
               min = j;
         swap(list, i, min);
      }
   }
  
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)	//this is called for each timer iteration
      {
         if(evolve  && forest[0].howFit() < 0.99)
         {
         /*********************************************************
         WE WANT TO ADVANCE TO THE NEXT GENERATION IN HERE
         ********************************************************/
         //Simply call the method that advances one generation  
            advanceGeneration();
            repaint();
         }
      }
   }
   
  //post: given most fit at forest[0], repopulates forest with mutations of the most fit 
   private static void advanceGeneration()
   {
   /*********************************************************
     WE WANT THIS METHOD TO ADVANCE forest ONE GENERATION
    ********************************************************/
    //This method should re-fill the array by using the most fit tree of this generation  
    // to create a new generation (all other trees in the forest) made of mutated
    // versions of the most fit from the previous generation.  Be sure to sort the
    // array again after re-filling it, so that the most fit of the new generation
    // will be the parent of the next generation. (There is a sort method provided above.)
   for(int i = 0; i < forest.length; i++){
      forest[i] = forest[0].mutate(MUTATION_RATE);
   }
   sort(forest);
   numGenerations++;
   }

//post:  returns x clipped to two decimal places, used for Panel display
   private double clipNum(double x)
   {
      return ((int)(x*100))/100.0;
   }

/**
* Draws first tree in forest array to a Graphics object, as well as key commands
*
* @param g The awt Graphics object to draw itself on to
*/
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
   
      ArrayList<TreePart> points = forest[0].getPoints();
      for(int i = 0; i < points.size(); i++)
         points.get(i).paint(g);
   
   //draw the controls and stats**************
      Color previous=g.getColor();
      g.setColor(Color.white);
      g.fillRect(0,0,1500,50);
      g.setColor(Color.black);
      g.drawString("SampleSize ("+SAMPLE_SIZE+"):O+P     MutationRate("+MUTATION_RATE+"):K+L                                                          (Width):"+forest[0].getPixelWidth()+"        (Height):"+forest[0].getPixelHeight() + "     (Number Generations):"+numGenerations + "     (How fit):"+clipNum(forest[0].howFit()),0,20);   
      g.drawString("TrunkLength("+clipNum(forest[0].getLength())+"):Q+W     LengthChange("+clipNum(forest[0].getLengthChange())+"):E+R      TrunkWidth("+clipNum(forest[0].getWidth())+"):A+S     WidthChange("+clipNum(forest[0].getWidthChange())+"):D+F     Degrees("+clipNum(forest[0].getDegreesChange())+"): Z+X     NumBranches("+forest[0].getNumBranches()+"): C+V     Smaller Increments:SHIFT     Move:Arrow Keys     BackColor:B+N",0,40);  
      g.setColor(previous);
   //********************************
   }

   public class Mouse extends MouseAdapter
   {
   
      public void mouseClicked(MouseEvent e)
      {
         if(e.getButton()==MouseEvent.BUTTON1)           //create a new random fractal
         {
            forest[0] = forest[0].makeRandomTree();
            numGenerations = 0;
            evolve = false;
            repaint();
         }
         else if(e.getButton()==MouseEvent.BUTTON3)      //start evolution
         {
            evolve = !evolve;
            repaint();
         }
      
      }
   }


   private boolean shiftIsPressed=false;  //if shift is held it moves in finer increments

   public class Keyboard implements KeyListener
   {
   /**
   * Handles when a key is pressed
   *
   * @param e Standard KeyEvent
   */
      public void keyPressed(KeyEvent e)
      {
         requestFocusInWindow();
         double change=0;
         evolve = false;
      
         if(e.getKeyCode()==KeyEvent.VK_ESCAPE) //end the program
            System.exit(1);
      
         if(e.getKeyCode()==KeyEvent.VK_SPACE)	//advance one generation
         {
         /*********************************************************
         WE WANT TO ADVANCE TO THE NEXT GENERATION IN HERE
         ********************************************************/
         //Again, a simple method call
         advanceGeneration();
            repaint();
            return;
         }
         if(e.getKeyCode()==KeyEvent.VK_ENTER)  //toggle auto-evolution on
         {  
            evolve = true;
            return;
         }
         //The O key allows shrinking the generation size by 1.  SAMPLE_SIZE must reflect the size
         //  of the forest.  The forest should be recreated using the current seed to begin a new 
         //  generation that is one smaller.
         if(e.getKeyCode()==KeyEvent.VK_O)      //Sample Size (2-25)
         {
            if(SAMPLE_SIZE <= 2)
               return;
            SAMPLE_SIZE--;
         /*********************************************************
         WE WANT TO DECREASE forest by 1
         ********************************************************/
         //Re-create the array one smaller and copy all but the last (least fit)
         //  of the current generation to the new array.  Make sure at the end
         //  that the new array becomes forest so it is used going forward.
         Tree[] temp = new Tree[forest.length - 1];
         System.out.println("New array temp");
         for(int i = 0; i < temp.length; i++){
            temp[i] = forest[i];
         }
         forest = temp;
         System.out.println("forest = temp");
            repaint();
            return;
         }
         //The P key allows expanding the generation size by 1.  SAMPLE_SIZE must reflect the size
         //  of the forest.  The forest should be recreated using the current seed to begin a new 
         //  generation that is one larger.
      
         if(e.getKeyCode()==KeyEvent.VK_P)      //Sample Size (2-25)
         {
            if(SAMPLE_SIZE >= 25)
               return;
            SAMPLE_SIZE++;
         /*********************************************************
         WE WANT TO INCREASE forest by 1
         ********************************************************/
         //Re-create the array one larger, copy all of the current generation
         //  to the new array, then add a new tree mutated from the most fit
         //  to the new empty spot in forest.  Sort this new array, then make sure 
         //  that the new array becomes forest so it is used going forward.
         Tree[] temp = new Tree[forest.length + 1];
         for(int i = 0; i < forest.length; i++){
            temp[i] = forest[i];
         }
         temp[temp.length - 1] = forest[0].mutate(MUTATION_RATE);
         forest = temp;
            repaint();
            return;
         }
         if(e.getKeyCode()==KeyEvent.VK_K)      //Mutation Rate (1-100)
         {
            MUTATION_RATE--;
            if(MUTATION_RATE < 1)
               MUTATION_RATE = 1;   
            repaint();
            return;   
         }
         if(e.getKeyCode()==KeyEvent.VK_L)      //Mutation Rate (1-100)
         {
            MUTATION_RATE++;
            if(MUTATION_RATE > 100)
               MUTATION_RATE = 100;   
            repaint();
            return;   
         }
         if(e.getKeyCode()==KeyEvent.VK_SHIFT)
            shiftIsPressed=true;
      
         if(shiftIsPressed)
            change=.1;
         else
            change=5;
         if(e.getKeyCode()==KeyEvent.VK_Z)      //degreesChange
            forest[0].setDegreesChange(forest[0].getDegreesChange() + change);
         else if(e.getKeyCode()==KeyEvent.VK_X)
            forest[0].setDegreesChange(forest[0].getDegreesChange() - change);
      
         if(shiftIsPressed)
            change=forest[0].getNumBranches()/4;
         else
            change=forest[0].getNumBranches()/2;
         if(change <= 0)
            change = 1;   
         if(e.getKeyCode()==KeyEvent.VK_C)      //numBranches
            forest[0].setNumBranches(forest[0].getNumBranches() + (int)(change));
         else if(e.getKeyCode()==KeyEvent.VK_V)
            forest[0].setNumBranches(forest[0].getNumBranches() - (int)(change));
      
         if(shiftIsPressed)
            change=.01;
         else
            change=.025;
         if(e.getKeyCode()==KeyEvent.VK_E)      //lengthChange
            forest[0].setLengthChange(forest[0].getLengthChange() + change);
         else if(e.getKeyCode()==KeyEvent.VK_R)
            forest[0].setLengthChange(forest[0].getLengthChange() - change);
      
         if(shiftIsPressed)
            change=1.05;
         else
            change=1.1;
         if(e.getKeyCode()==KeyEvent.VK_Q)      //trunk length
            forest[0].setLength(forest[0].getLength()*change);
         else if(e.getKeyCode()==KeyEvent.VK_W)
            forest[0].setLength(forest[0].getLength()/change);
      
         if(shiftIsPressed)
            change=.01;
         else
            change=.025;
         if(e.getKeyCode()==KeyEvent.VK_D)      //withChange
            forest[0].setWidthChange(forest[0].getWidthChange() + change);
         else if(e.getKeyCode()==KeyEvent.VK_F)
            forest[0].setWidthChange(forest[0].getWidthChange() - change);
      
         if(shiftIsPressed)
            change=1.1; 
         else
            change=1.5;
         if(e.getKeyCode()==KeyEvent.VK_A)      //trunk width
            forest[0].setWidth(forest[0].getWidth()*change);
         else if(e.getKeyCode()==KeyEvent.VK_S)
            forest[0].setWidth(forest[0].getWidth()/change);
         
         else if(e.getKeyCode()==KeyEvent.VK_UP) //move
            forest[0].setInitialY(forest[0].getInitialY()-10);
         else if(e.getKeyCode()==KeyEvent.VK_DOWN)
            forest[0].setInitialY(forest[0].getInitialY()+10);
         else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
            forest[0].setInitialX(forest[0].getInitialX()+10);
         else if(e.getKeyCode()==KeyEvent.VK_LEFT)
            forest[0].setInitialX(forest[0].getInitialX()-10);
             
         else if(e.getKeyCode()==KeyEvent.VK_N) //random background color
         { 
            Color randomColor=new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
            setBackground(randomColor);
         }
         else if(e.getKeyCode()==KeyEvent.VK_B) //sets background color to light gray
            setBackground(Color.gray.brighter());
      
         forest[0].generateData();
         repaint();
      }
   
      public void keyReleased(KeyEvent e) 
      {
         requestFocusInWindow();
         if(e.getKeyCode()==KeyEvent.VK_SHIFT){
            shiftIsPressed=false;
         }
      }
   
      public void keyTyped(KeyEvent e){}  //not used
   
   
   
   }


}