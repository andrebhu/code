import java.awt.*;

public class Cell{

   private Color myColor = Color.BLACK;
   boolean select = false;
   boolean alive;
   int neighbors;   
   int xLoc;
   int yLoc;
   
   public static final int size = 5;
   
   public Cell(){
      alive = false;
      neighbors = 0;
   }
   public Cell(int x, int y, int n, boolean a){
      alive = a;
      neighbors = n;
      xLoc = x;
      yLoc = y;
   }
   
   public void paint(Graphics g)
   {
      Graphics2D g2d = (Graphics2D) g;
   
      if(alive){
         g2d.setColor(Color.BLACK);	//black = alive
      } 
      else{
         g2d.setColor(Color.LIGHT_GRAY);	//gray = dead
      }
      
      g2d.fillRect(xLoc, yLoc, size, size); 
    
      if(select){
         g2d.setColor(Color.YELLOW);
         g2d.drawLine(xLoc, yLoc, xLoc + size, yLoc); //top
         g2d.drawLine(xLoc, yLoc, xLoc, yLoc + size); //left
         g2d.drawLine(xLoc + size, yLoc, xLoc + size, yLoc + size); //right
         g2d.drawLine(xLoc, yLoc + size, xLoc + size, yLoc + size);
      }
      else{
         g2d.setColor(Color.BLUE);
         g2d.drawLine(xLoc, yLoc, xLoc + size, yLoc); //top
         g2d.drawLine(xLoc, yLoc, xLoc, yLoc + size); //left
         g2d.drawLine(xLoc + size, yLoc, xLoc + size, yLoc + size); //right
         g2d.drawLine(xLoc, yLoc + size, xLoc + size, yLoc + size); //bottom
      }
   } 
   
   public void setSelect(Boolean b){
      select = b;
   }
   
   public void setStatus(Boolean b){
      alive = b;
   }
   
   public void addNeighbor(){
      neighbors++;
   }
   
   public void loseNeighbor(){
      neighbors--;
   }
   
   public void setNeighbor(int i){
      neighbors = i;
   }
   
   //if needed
   public int getX(){
      return xLoc;
   }
   
   public int getY(){
      return yLoc;
   }  
   
   public int getNeighbors(){
      return neighbors;
   }
   
   public boolean getStatus(){
      return alive;
   }   
   
}