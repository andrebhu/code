import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.*;
import java.util.ArrayList;
import java.io.*;
import java.util.*;
   
public class Conway extends JPanel implements KeyListener, ActionListener {
   private BufferStrategy strategy; 

   private Panel myPanel;
   private JFrame frame = new JFrame("Conway's Game of Life");
   
   public static int delay = 40; //for Thread.delay(delay)
   public final int dimensions = 150;
   public Cell[][] cells = new Cell[dimensions][dimensions];
   public final int SCREEN_WIDTH = 875;
   public final int SCREEN_HEIGHT = 875;     
   public final int displacement = 30;    //for grid position,
   public static int gen = 0; //generation number
   public static boolean advance = false; 
   public static int count = 0; //number of cells
   
   public static JButton export = new JButton("Export");
   public static JButton _import = new JButton("Import");
   public static JButton random = new JButton("Randomize");
   public static JButton restart = new JButton("Restart");
   
   public static int[] saves = new int[10]; //saves 10 generations of counts to determine if game reaches endstate

      
   public Conway() throws IOException{
   
     //setting up borderlayout/buttons 
      frame.getContentPane().setLayout(new BorderLayout());
      JPanel subPanel = new JPanel();
      subPanel.add(export);
      subPanel.add(_import);   
      subPanel.add(random);
      subPanel.add(restart);
      frame.add(subPanel, BorderLayout.SOUTH);
      frame.add(this, BorderLayout.CENTER);      
      
      //sets up standard features of JPanel
      frame.pack();   
      frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
      frame.setVisible(true);
      frame.setResizable(true);   
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
           
      //adding all the listeners               
      addKeyListener(this);  
      _import.addActionListener(this);
      export.addActionListener(this);
      random.addActionListener(this);
      restart.addActionListener(this);
      frame.addWindowListener(
               new WindowAdapter() { 
                  public void windowClosing(WindowEvent e) {System.exit(0);} 
               });
      frame.addMouseListener(
         new MouseAdapter() {
            public void mousePressed(MouseEvent e){  
            }
            public void mouseReleased(MouseEvent e){
            }   
            public void mouseClicked(MouseEvent e){ //adds/removes cells based on location, calibrated to index values, works differently on different screen sizes
               advance = false;
               int x = (e.getX() - displacement-1)/5-2;
               int y = (e.getY()-24-displacement-1)/5-3;
               if(x >= 0 && x < dimensions && y >= 0 && y < dimensions){
                  if(cells[x][y].getStatus() == false){
                     cells[x][y].setStatus(true);
                     addNearby(x, y);
                  }
                  else if(cells[x][y].getStatus() == true){
                     cells[x][y].setStatus(false);
                     loseNearby(x, y);
                  }
               }   
            }
            public void mouseEntered(MouseEvent e){
            }
            public void mouseExited(MouseEvent e){
            }
         
         });
               
      requestFocusInWindow();
          //beginning initialize cells 
      for(int i = 0; i < cells.length; i++){
         for(int j = 0; j < cells[i].length; j++){      
            cells[i][j] = new Cell(i * 5 + displacement, j * Cell.size + displacement, 0, false);
         }
      }           
       
   } 
   public void actionPerformed(ActionEvent e){
      if(e.getSource() == export){
         try{
            export();
         }
         catch(IOException o){   
         }   
      }
      else if(e.getSource() == _import){
         try{
            _import();
         }
         catch(IOException o){
         }
      } 
      else if(e.getSource() == random){
         random();
      }  
      else if(e.getSource() == restart){
         reset();
      }      
      //refocus back to keyboard works
      
      
      requestFocusInWindow();
   } 
     
   public void keyPressed(KeyEvent e){ 
   
      if(e.getKeyCode() == KeyEvent.VK_SPACE){ //pause
         if(advance){
            advance = false;   
         }  
         else
            advance = true;   
      }
               
      else if(e.getKeyCode() == KeyEvent.VK_RIGHT){ //advance one generation
         generation();
      } 
      else if(e.getKeyCode() == KeyEvent.VK_R){ //reset
         reset();
      }
            
   }   
   public void keyReleased(KeyEvent e){
   }
   public void keyTyped(KeyEvent e) {      
   }
   
  //50% chance of cell to become alive 
   public void random(){
      for(int i = 0; i < cells.length; i++){
         for(int j = 0; j < cells[i].length; j++){
            if(Math.random() > .5){
               cells[i][j] = new Cell(i * 5 + displacement, j * Cell.size + displacement, 0, false);
            }
            else{
               cells[i][j] = new Cell(i * 5 + displacement, j * Cell.size + displacement, 0, true);   
            }
         }
      }
      for(int i = 0; i < cells.length; i++){
         for(int j = 0; j < cells[i].length; j++){ 
            if(cells[i][j].getStatus() == true){
               addNearby(i, j);
            }
         }
      }
      gen = 0;
   }
   
   //blank state
   public void reset(){
      for(int i = 0; i < cells.length; i++){
         for(int j = 0; j < cells[i].length; j++){
            cells[i][j] = new Cell(i * 5 + displacement, j * Cell.size + displacement, 0, false);  
         }
      }
      gen = 0;
      advance = false;
   }
   
   public void generation(){
      gen++;
      //storing the ind exes of the cells to add
      ArrayList<Integer> addXValues = new ArrayList<Integer>();
      ArrayList<Integer> addYValues = new ArrayList<Integer>();
      
      //storing the indexes of the cells to remove
      ArrayList<Integer> loseXValues = new ArrayList<Integer>();   
      ArrayList<Integer> loseYValues = new ArrayList<Integer>();
      
      //rules
      for(int i = 0; i < cells.length; i++){
         for(int j = 0; j < cells[i].length; j++){
            //RULE 1, if a live cell has less than 2 neighbors, it dies
            if(cells[i][j].getStatus() == true && cells[i][j].getNeighbors() < 2){
               //System.out.println("Cell " + i + ", " + j + ": dies due to underpopulation. It had " + cells[i][j].getNeighbors() + " neighbors.");
               loseXValues.add(i);
               loseYValues.add(j);
            }
               //RULE 2, if a live cell has 2 or 3 neighbors, it lives
            else if(cells[i][j].getStatus() == true && cells[i][j].getNeighbors() == 2){
               //System.out.println("Cell " + i + ", " + j + ": does not change. It has " + cells[i][j].getNeighbors() + " neighbors.");
            }
            else if(cells[i][j].getStatus() == true && cells[i][j].getNeighbors() == 3){
               //System.out.println("Cell " + i + ", " + j + ": does not change. It has " + cells[i][j].getNeighbors() + " neighbors.");
            }
               //RULE 3, if a live cell has more than 3 neighbors, it dies
            else if(cells[i][j].getStatus() == true && cells[i][j].getNeighbors() > 3){
               //System.out.println("Cell " + i + ", " + j + ": dies due to overpopulation. It had " + cells[i][j].getNeighbors() + " neighbors.");
               loseXValues.add(i);
               loseYValues.add(j);
            }
               //RULE 4, if a dead cell has 3 neighbors, it becomes a live cell
            else if(cells[i][j].getStatus() == false && cells[i][j].getNeighbors() == 3){
               //System.out.println("Cell " + i + ", " + j + ": becomes alive due to reproduction.");
               addXValues.add(i);
               addYValues.add(j);
            }
         }
      }        
      
      //add/remove cells stored in addValues/loseValues
      for(int i = 0; i < addXValues.size(); i++){
         cells[addXValues.get(i)][addYValues.get(i)].setStatus(true);
         addNearby(addXValues.get(i), addYValues.get(i));
      }
      for(int i = 0; i < loseXValues.size(); i++){
         cells[loseXValues.get(i)][loseYValues.get(i)].setStatus(false);
         loseNearby(loseXValues.get(i), loseYValues.get(i));
      }
   }
   
   //with saves of number of cells for 10 generations, sorts them, if difference between least/greatest is less than 20 it is stable
   public boolean repeat(int[] arr){
      Arrays.sort(arr);
      if(arr[arr.length - 1] - arr[0] < 20){
         return true;
      }
      return false;
   }
   
   //modifying neighbor counts
   public void addNearby(int i, int j) throws ArrayIndexOutOfBoundsException{
      if(i > 0 && j > 0 && i < cells.length - 1 && j < cells[0].length - 1){ 
         cells[i - 1][j - 1].addNeighbor();
         cells[i - 1][j].addNeighbor();
         cells[i - 1][j + 1].addNeighbor();
         cells[i][j - 1].addNeighbor();
         cells[i][j + 1].addNeighbor();
         cells[i + 1][j - 1].addNeighbor();
         cells[i + 1][j].addNeighbor();
         cells[i + 1][j + 1].addNeighbor();
      }
      else{
         if(i == 0 && j == 0){ //top left
            cells[i + 1][j].addNeighbor();
            cells[i + 1][j + 1].addNeighbor();
            cells[i][j + 1].addNeighbor();
         }
         else if(i == 0 && j == cells[0].length - 1){ //bottom left
            cells[i][j - 1].addNeighbor();
            cells[i + 1][j - 1].addNeighbor();
            cells[i + 1][j].addNeighbor();      
         }
         else if(i == cells.length - 1 && j == 0){ //top right
            cells[i][j + 1].addNeighbor();
            cells[i - 1][j + 1].addNeighbor();
            cells[i - 1][j].addNeighbor();   
         }
         else if(i == cells.length - 1 && j == cells[0].length - 1){ //bottom right
            cells[i - 1][j - 1].addNeighbor();
            cells[i][j - 1].addNeighbor();
            cells[i - 1][j].addNeighbor();
         }
         else if(i == 0){ //left wall
            cells[i][j - 1].addNeighbor();
            cells[i][j + 1].addNeighbor();
            cells[i + 1][j - 1].addNeighbor();
            cells[i + 1][j].addNeighbor();
            cells[i + 1][j + 1].addNeighbor();
         }
         else if(j == 0){ //top wall
            cells[i - 1][j].addNeighbor();
            cells[i + 1][j].addNeighbor();
            cells[i - 1][j + 1].addNeighbor();
            cells[i][j + 1].addNeighbor();
            cells[i + 1][j + 1].addNeighbor();
         }
         else if(i == cells.length - 1){ //right wall
            cells[i][j - 1].addNeighbor();
            cells[i][j + 1].addNeighbor();
            cells[i - 1][j - 1].addNeighbor();
            cells[i - 1][j].addNeighbor();
            cells[i - 1][j + 1].addNeighbor();
         
         }
         else if(j == cells[0].length - 1){ //bottom wall
            cells[i - 1][j].addNeighbor();
            cells[i + 1][j].addNeighbor();
            cells[i - 1][j - 1].addNeighbor();
            cells[i][j - 1].addNeighbor();
            cells[i + 1][j - 1].addNeighbor();
         }
      }
   }
   //for removing neighbor counts
   public void loseNearby(int i, int j) throws ArrayIndexOutOfBoundsException{ 
      if(i > 0 && j > 0 && i < cells.length - 1 && j < cells[0].length - 1){ 
         cells[i - 1][j - 1].loseNeighbor();
         cells[i - 1][j].loseNeighbor();
         cells[i - 1][j + 1].loseNeighbor();
         cells[i][j - 1].loseNeighbor();
         cells[i][j + 1].loseNeighbor();
         cells[i + 1][j - 1].loseNeighbor();
         cells[i + 1][j].loseNeighbor();
         cells[i + 1][j + 1].loseNeighbor();
      }
      else{
      
         if(i == 0 && j == 0){ //top left
            cells[i + 1][j].loseNeighbor();
            cells[i + 1][j + 1].loseNeighbor();
            cells[i][j + 1].loseNeighbor();
         }
         else if(i == 0 && j == cells[0].length - 1){ //bottom left
            cells[i][j - 1].loseNeighbor();
            cells[i + 1][j - 1].loseNeighbor();
            cells[i + 1][j].loseNeighbor();      
         }
         else if(i == cells.length - 1 && j == 0){ //top right
            cells[i][j + 1].loseNeighbor();
            cells[i - 1][j + 1].loseNeighbor();
            cells[i - 1][j].loseNeighbor();   
         }
         else if(i == cells.length - 1 && j == cells[0].length - 1){ //bottom right
            cells[i - 1][j - 1].loseNeighbor();
            cells[i][j - 1].loseNeighbor();
            cells[i - 1][j].loseNeighbor();
         }
         else if(i == 0){ //left wall
            cells[i][j - 1].loseNeighbor();
            cells[i][j + 1].loseNeighbor();
            cells[i + 1][j - 1].loseNeighbor();
            cells[i + 1][j].loseNeighbor();
            cells[i + 1][j + 1].loseNeighbor();
         }
         else if(j == 0){ //top wall
            cells[i - 1][j].loseNeighbor();
            cells[i + 1][j].loseNeighbor();
            cells[i - 1][j + 1].loseNeighbor();
            cells[i][j + 1].loseNeighbor();
            cells[i + 1][j + 1].loseNeighbor();
         }
         else if(i == cells.length - 1){ //right wall
            cells[i][j - 1].loseNeighbor();
            cells[i][j + 1].loseNeighbor();
            cells[i - 1][j - 1].loseNeighbor();
            cells[i - 1][j].loseNeighbor();
            cells[i - 1][j + 1].loseNeighbor();
         
         }
         else if(j == cells[0].length - 1){ //bottom wall
            cells[i - 1][j].loseNeighbor();
            cells[i + 1][j].loseNeighbor();
            cells[i - 1][j - 1].loseNeighbor();
            cells[i][j - 1].loseNeighbor();
            cells[i + 1][j - 1].loseNeighbor();
         }
      }
   }
   
   
   //saves current layout into file named "save"
   public void export() throws IOException{
      advance = false;
      PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("save")));
      for(int i = 0; i < cells.length; i++){
         for(int j = 0; j < cells[i].length; j++){
            if(cells[j][i].getStatus()){
               pw.print("1");
            }
            else
               pw.print("0");
         }
         pw.println();
      }
      pw.close();
   }  
   //imports "save"
   public void _import() throws IOException{
      BufferedReader br = new BufferedReader(new FileReader("save")); 
      reset();
      String st; 
      int count = 0;
      while ((st = br.readLine()) != null) {
         for(int i = 0; i < st.length(); i++){
            if(st.charAt(i) == '1'){
               cells[i][count].setStatus(true);
               addNearby(i, count);
            }
            else if(st.charAt(i) == '0'){    
            }
         }
         count++;
      }
   }
   
   public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2d = (Graphics2D) g;  
      
      count = 0;    
      for(int i = 0; i < cells.length; i++){  //paints all the cells
         for(int j = 0; j < cells[i].length; j++){
            cells[i][j].paint(g2d);
            if(cells[i][j].getStatus()){
               count++;                     
            }
         }         
      }  
      if(advance){
         saves[gen%10]=count;         
      }
   
      //cell statistics for top left
      g2d.setFont(new Font("Courier", Font.PLAIN, 15)); 
      g2d.setColor(Color.LIGHT_GRAY);
      g2d.fillRect(0, 0, SCREEN_WIDTH, 30);
      g2d.setColor(Color.BLACK);
      if(repeat(saves)){
         g2d.drawString("Reached end state.", 150, 25);
      }
      g2d.drawString("Generation: " + gen, 0, 10);
      g2d.drawString("Count: " + count, 0, 25);
                
   }  

   public static void main(String[] args) throws InterruptedException, IOException{
      Conway app = new Conway();
      while(true){
         app.repaint();
         Thread.sleep(delay);   
         if(advance){   
            app.generation();
         }      
      }
   }
}