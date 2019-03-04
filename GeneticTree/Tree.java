//Rev. Dr. Douglas R Oberle - Nicholas Hodgman 2015
import java.awt.Color;
import java.util.ArrayList;

public class Tree implements Comparable
{
   private ArrayList<TreePart> points; 	//an ArrayList of all of the points
   private int[] generations=new int[50]; //spot 0= number of 1 generation points, 1 = numbner of 2 generation  points
   private int initialX, initialY; 			//the starting x and y coordinates
   private double degreesChange;          //degrees the children will diverge from the parents direction
   private Color initialColor; 				//the color it will start out with
   private double length; 					   //the distance of a branch
   private double lengthChange; 			   //0-1 percent change from one branch length to its child
   private double width;                  //the width of a branch
   private double widthChange;            //0-1 percent change from one branch width to its child
   private int numBranches;               //number of points from which branches can extend (number of fractal iterations)
   private int maxX,minX,maxY,minY;       //min and max X and Y coordinates 
   private int leafColor;                 //picks a random leaf color (GREEN, YELLOW, ORANGE, RED or BROWN) 
   private static Tree bestTree = makeBestTree();          //model for the most fit tree
   
/**
   * Constructor for the Tree
   *
   * @param x  Starting x position for the fractal
   * @param y  Starting y position for the fractal
   * @param numBranches  Number of iterations / branches created
   * @param degChange Degrees the children will diverge from the parents direction
   * @param dist The distance the first point will travel in the Fractal
   * @param dChange The percent of a parents distance the child point will travel
   * @param w The starting width of the Fractal
   * @param wChange The percent of a parents width a child will have
   * @param lColor The leaf color where 0-GREEN, 1-YELLOW, 2-ORANGE, 3-RED, 4-BROWN
*/
   public Tree(int x, int y, int numBranch, double degChange, double dist, double dChange, double w, double wChange, int lColor)
   {
      initialX=x; 
      initialY=y;
      numBranches = numBranch;
      degreesChange=degChange;
      initialColor=new Color(50, 50, 35);    //gray-brown trunk
      length=dist;
      lengthChange=dChange;
      width=w;
      widthChange=wChange;
      leafColor = lColor;
      points=new ArrayList<TreePart>();
      generateData();
      shiftTree();
   }
   
   //post:  returns a random fractal
   public static Tree makeRandomTree()
   {
      int startingX=650; 					
      int startingY=500; 						
      int numBranches = (int)(Math.random()*20000)+10;            //10000
      double degreesChange=(Math.random()*180+10); 			      //20
      int initialTrunkLength=(int)(Math.random()*161+40); 	      //200 
      double percentLengthRemaining=(Math.random()*41 + 50)/100.0;//.75
      double initialTrunkWidth=(Math.random()*100+10);  		      //80
      double percentWidthRemaining=(Math.random()*41 + 50)/100.0; //.7
      int leafColor = (int)(Math.random()*10);    
      return new Tree(startingX, startingY, numBranches, degreesChange, initialTrunkLength, percentLengthRemaining, initialTrunkWidth, percentWidthRemaining, leafColor);
   }

   //post: given the current tree (this), return a mutation of the tree where each field can vary by mutationRate
   public Tree mutate(int mutationRate)
   {
      int startingX=650; 					
      int startingY=500; 						
      int numBranches               = (int)(varyByRate(mutationRate, this.numBranches));
      double degreesChange          = varyByRate(mutationRate, this.degreesChange); 			    
      double initialTrunkLength     = varyByRate(mutationRate, this.length); 	      
      double percentLengthRemaining = varyByRate(mutationRate, this.lengthChange);
      double initialTrunkWidth      = varyByRate(mutationRate, this.width);  		      
      double percentWidthRemaining  = varyByRate(mutationRate, widthChange);
      int leafColor = (int)(Math.random()*10);    
      return new Tree(startingX, startingY, numBranches, degreesChange, initialTrunkLength, percentLengthRemaining, initialTrunkWidth, percentWidthRemaining, leafColor);
   }

   //post: returns the percentage this tree is to the best fit tree
   public double howFit()
   {
      double numBranchPerc    = percentDifferent(bestTree.numBranches,this.numBranches);
      double degreeChangePerc = percentDifferent(bestTree.degreesChange,this.degreesChange);
      double trunkLengthPerc  = percentDifferent(bestTree.length,this.length);
      double lengthChangePerc = percentDifferent(bestTree.lengthChange, this.lengthChange);
      double trunkWidthPerc   = percentDifferent(bestTree.width, this.width);
      double widthChangePerc  = percentDifferent(bestTree.widthChange, this.widthChange);
      
      return (numBranchPerc + degreeChangePerc + trunkLengthPerc + lengthChangePerc + trunkWidthPerc + widthChangePerc)/6.0;
   }

      //post:  Trees compare to one another by how fit they are (one closer to the best fit is > one that is further away)
   public int compareTo(Object other)
   {
      Tree that = (Tree)(other);
      if(this.howFit() < that.howFit())
         return -1;
      if(this.howFit() > that.howFit())
         return 1;
      return 0;
   }
   
  //post:  returns a model of the most fit tree
   public static Tree makeBestTree()
   {
      int startingX=650; 						//starting x coordinate
      int startingY=725; 						//starting y coordinate
      int numBranches = 10000;            //number of points from which branches can extend (number of fractal iterations)
      double degreesChange=20; 				//how much of a different angle to the left and right of their parents the new point will come
      int initialTrunkLength=200; 			//how far the first line will be 
      double percentLengthRemaining=.75; 	//what percent of the parents length the children will travel
      double initialTrunkWidth=80;  		//how wide the trunk will be initially
      double percentWidthRemaining=.7; 	//what percent of the parents width the children will have
      int leafColor = (int)(Math.random()*10);
      return new Tree(startingX, startingY, numBranches, degreesChange, initialTrunkLength, percentLengthRemaining, initialTrunkWidth, percentWidthRemaining, leafColor);
   }

   //post: given rate and n, returns a value that can vary by rate percent higher or lower
   private static double varyByRate(double rate, double n)
   {
      double variation = (n * (Math.random()*rate/100.0));
      if(Math.random() < .5 || (n - variation <= 0))
         return n + variation;
      return  n - variation;
   }
   
   //post: returns the percentage difference between a and b
   private static double percentDifferent(double a, double b)
   {
      return (a - Math.abs(a - b)) / a;
   }

    //post: push tree up or down if part of it clips off the screen
   private void shiftTree()
   {
      boolean changeMade = false;
      while(minY < 50 && maxY < 735)
      {
         minY++;
         initialY++;
         maxY++;
         changeMade = true;
      }
      while(minY >= 50 && maxY >= 735)
      {
         minY--;
         initialY--;
         maxY--;
         changeMade = true;
      }
      if(changeMade)
         generateData();
   }

  //post: Clears and populates the Objects data and recalculates the statistics on the points
   public void generateData()
   {
   //reset all statistics **************
      maxX=Integer.MIN_VALUE;
      maxY=Integer.MIN_VALUE;
      minY=Integer.MAX_VALUE;
      minX=Integer.MAX_VALUE;
      points.clear();
      generations=new int[50];
   //*************************************
   
      points.add(new TreePart(initialX, initialY, initialColor, 270, degreesChange, length, lengthChange, width, widthChange, 1, leafColor)); //adds the first point to the arrayList
      for(int i=0; i<numBranches; i++)            //loops through, adding children
      {   
         TreePart[] children=points.get(i).generateChildren(); //creates an array holding two new children
         Color color=points.get(i).getColor();  //gets the parents color
         color=darken(color);                   //creates the childrens color
         children[0].setColor(color);           //sets both of the childrens color
         children[1].setColor(color);
         points.add(children[0]);               //adds each of the children to the list
         points.add(children[1]);
      }
      for(int i=0; i<points.size(); i++)        //calculates the statistics
      { 
         if(points.get(i).getX() > maxX) 
            maxX=points.get(i).getX();
         if(points.get(i).getX() < minX)
            minX=points.get(i).getX();
         if(points.get(i).getY() > maxY)
            maxY=points.get(i).getY();
         if(points.get(i).getY() < minY)
            minY=points.get(i).getY();
         generations[points.get(i).getGeneration()]++;
      }
   }

//post: returns a Color that is a darker shade than color, used for variation in leaf colors
   private Color darken(Color color)
   {
      return new Color(Math.abs((color.getRed()-7)%256), Math.abs((color.getGreen()-7)%256), Math.abs((color.getBlue()-7)%256)); 
   }

   public ArrayList<TreePart> getPoints()
   {
      return points;
   }
   
   public double getDegreesChange()
   {
      return degreesChange;
   }
   
   public void setDegreesChange(double n)
   {
      degreesChange = n;
   }

   public double getLength()
   {
      return length;
   }
   
   public void setLength(double n)
   {
      length = n;
      if(length < 1)
         length = 1;
   }

   public double getLengthChange()
   {
      return lengthChange;
   }
   
   public void setLengthChange(double n)
   {
      lengthChange = n;
      if(lengthChange < 0)
         lengthChange = 0;
   }

   public double getWidth()
   {
      return width;
   }

   public void setWidth(double n)
   {
      width = n;
      if(width < 1)
         width = 1;
   }

   public double getWidthChange()
   {
      return widthChange;
   }
   
   public void setWidthChange(double n)
   {
      widthChange = n;
      if(widthChange < 0)
         widthChange = 0;
   }

   public int getPixelWidth()
   {
      return (maxX-minX); 
   }

   public int getPixelHeight()
   {
      return (maxY-minY); 
   }

   public int getNumBranches()
   {
      return numBranches;
   }
   
   public void setNumBranches(int n)
   {
      numBranches = n;
      if(numBranches < 1)
         numBranches = 1;
      else
         if(numBranches > 300000)
            numBranches =  300000;
   }

   public int getInitialX()
   {
      return initialX;
   }

   public void setInitialX(int n)
   {
      initialX = n;
   }

   public int getInitialY()
   {
      return initialY;
   }

   public void setInitialY(int n)
   {
      initialY = n;
   }

   public void setLeafColor(int n)
   {
      leafColor = n;
   }

   public int getMaxX()
   {
      return maxX;
   }
   
   public int getMinX()
   {
      return minX;
   }

   public int getMaxY()
   {
      return maxY;
   }
   
   public int getMinY()
   {
      return minY;
   }

}