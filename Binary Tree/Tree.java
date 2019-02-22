/*
Andre Hu
2/7/2019
*/

import java.io.*;
  
public class Tree
{
   private TreeNode myRoot;   
   public Tree()
   {
      myRoot = null;
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:adds x to the tree such that the tree is still an in-order Binary Search Tree
   
   public void add(Comparable x)
   {
      myRoot = addHelper(myRoot, x);
   }
   
   private TreeNode addHelper(TreeNode root, Comparable x)
   {
      if(root != null){
         if(root.getLeft() == null && root.getValue().compareTo(x) > 0){
            root.setLeft(new TreeNode(x, null, null));
         }
         else if(root.getRight() == null && root.getValue().compareTo(x) < 0){
            root.setRight(new TreeNode(x, null, null));
         } 
         //recursion
         else 
         {
            if(root.getValue().compareTo(x) > 0){
               addHelper(root.getLeft(), x);
            }
            else
               addHelper(root.getRight(), x); 
         } 
      }
      else{
         //first element
         root = new TreeNode(x, null, null);  
      } 
      return root;
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:removes x from the tree such that the tree is still an in-order Binary Search Tree
   
   public void remove(Comparable x)
   {
      System.out.print("Removing " + x);
      myRoot = removeHelper(myRoot, x);
   }
   
 
   private TreeNode removeHelper(TreeNode root, Comparable x)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null){
         if(contains(x) == true){
         /*
         temp is value to remove
         parent is temp's parent
         */
            TreeNode temp = searchHelper(root, x);
            TreeNode parent = searchParent(root, x);              
         /*
         first case: x is leaf
         
         isLeftNode/isRightNode is relative with parent to temp
         */
            boolean isLeftNode = false;
            boolean isRightNode = false;
            
            if(parent != null){
               isLeftNode = parent.getLeft() == temp;
               isRightNode = parent.getRight() == temp;
            }
            
            boolean hasLeftNode = temp.getLeft() != null;
            boolean hasRightNode = temp.getRight() != null;
            
            if(isLeaf(temp)){
               System.out.println(" using 1st case.");
               if(isLeftNode){
                  parent.setLeft(null);
               }
               if(isRightNode){
                  parent.setRight(null);
               }
            }
            
            /*
            second case: removing a node with 1 child
            
            hasLeftNode/hasRightNode to figure out which node to delete
            */  
            else if (oneKid(temp)){
               System.out.println(" using 2nd case.");
            
               if(isLeftNode){
                  if(hasLeftNode){
                     parent.setLeft(temp.getLeft());
                  }
                  if(hasRightNode){
                     parent.setLeft(temp.getRight());
                  }
               }
               if(isRightNode){
                  if(hasLeftNode){
                     parent.setRight(temp.getLeft());
                  }
                  if(hasRightNode){
                     parent.setRight(temp.getRight());
                  }
               }   
            }
            
            /*
            third case: removing a node with 2 children 
            
            temp2 is value from temp's left subtree to remove
            parent2 is temp2's parent
            */
            else{    
               System.out.println(" using 3rd case.");
            
               TreeNode temp2 = temp.getLeft();
               while(temp2.getRight() != null){
                  temp2 = temp2.getRight();
               }
               
               TreeNode parent2 = searchParent(root, temp2.getValue());
                        
               if(parent2.getRight().getValue().compareTo(temp2.getValue()) == 0){
                  parent2.setRight(null);
               }
               else
                  parent2.setLeft(null);   
               temp.setValue(temp2.getValue());     
            }   
         }   
         else{
            System.out.println(" ...does not exist!");
         }
      }
      
   //************************************************************           
      return root;
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:shows the elements of the tree such that they are displayed in prefix order
   
   public void showPreOrder()
   {
      System.out.println("Pre-Order:");
      preOrderHelper(myRoot);
      System.out.println("");
   }
   
   private void preOrderHelper(TreeNode root)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null){
         System.out.print(root.getValue() + " ");
         preOrderHelper(root.getLeft());
         preOrderHelper(root.getRight());   
      }
   //************************************************************  
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:shows the elements of the tree such that they are displayed in infix order
   
   public void showInOrder()
   {
      System.out.println("In-Order:");
      inOrderHelper(myRoot);
      System.out.println("");
   }
   
   private void inOrderHelper(TreeNode root)   
   {
      if(root!=null)
      {
         inOrderHelper(root.getLeft());
         System.out.print(root.getValue() + " ");    
         inOrderHelper(root.getRight());
      }
   }
      
   //pre: root points to an in-order Binary Search Tree
   //post:shows the elements of the tree such that they are displayed in postfix order
   
   public void showPostOrder()
   {
      System.out.println("Post Order:");
      postOrderHelper(myRoot);   
      System.out.println("");
   }
   
   private void postOrderHelper(TreeNode root)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null){
         postOrderHelper(root.getLeft());
         postOrderHelper(root.getRight());
         System.out.print(root.getValue() + " ");
      }
   //************************************************************  
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:returns whether or not x is found in the tree
   
   public boolean contains(Comparable x)
   {
      if (searchHelper(myRoot, x)==null){
         return false;
      }   
      return true;
   }
   
   private TreeNode searchHelper(TreeNode root, Comparable x)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null){
         if(root.getValue().compareTo(x) == 0){
            return root;
         }   
            
         //is this faster or is it unnecessary?
         if(root.getLeft() != null && root.getLeft().getValue().compareTo(x) == 0){
            return root.getLeft();
         }
         if(root.getRight() != null && root.getRight().getValue().compareTo(x) == 0){
            return root.getRight();
         }      
         
         //recursion        
         if(root.getValue().compareTo(x) > 0){
            if(root.getRight() != null){
               return searchHelper(root.getLeft(), x);
            }
         }
         if(root.getValue().compareTo(x) < 0){   
            if(root.getRight() != null){
               return searchHelper(root.getRight(), x);
            } 
         }          
      }
      return null;
   //************************************************************  
   }
      
   //pre: root points to an in-order Binary Search Tree
   //post:returns a reference to the parent of the node that contains x, returns null if no such node exists
   //THIS WILL BE CALLED IN THE METHOD removeHelper.  
   
   private TreeNode searchParent(TreeNode root, Comparable x)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null){
         if(root.getLeft() != null && root.getLeft().getValue().compareTo(x) == 0){
            return root;
         }
         if(root.getRight() != null && root.getRight().getValue().compareTo(x) == 0){
            return root;
         }
            
         //recursion
         if(root.getValue().compareTo(x) > 0){
            if(root.getRight() != null){
               return searchParent(root.getLeft(), x);
            }
         }
         if(root.getValue().compareTo(x) < 0){   
            if(root.getRight() != null){
               return searchParent(root.getRight(), x);
            } 
         }          
      }
   //************************************************************  
      return null;
   }
   
   //post: determines if root is a leaf or not O(1)
   public boolean isLeaf(TreeNode root)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null){
         if(root.getRight() == null && root.getLeft() == null){
            return true;
         }     
      }
   //************************************************************  
      return false;
   }
      
   //post: returns true if only one child O(1)
   private boolean oneKid(TreeNode root)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null){
         if(root.getRight() == null && root.getLeft() != null){
            return true;
         }
         else if(root.getRight() != null && root.getLeft() == null){
            return true;
         }
      }
   //************************************************************  
      return false;
   }
      
   //pre: root points to an in-order Binary Search Tree
   //post:returns the number of nodes in the tree
   
   public int size()
   {
      return sizeHelper(myRoot);
   }
   
   private int sizeHelper(TreeNode root)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null){
      
         if(root.getRight() == null && root.getLeft() == null){
            return 1;
         }     
         
      //recursion
         if(root.getRight() != null && root.getLeft() != null){
            return 1 + sizeHelper(root.getRight()) + sizeHelper(root.getLeft());
         }
         if(root.getRight() != null){
            return 1 + sizeHelper(root.getRight());
         }
         if(root.getLeft() != null){
            return 1 + sizeHelper(root.getLeft());
         }
      }   
   //************************************************************  
      return 0;
   }
         
   public int height()
   {
      return heightHelper(myRoot) - 1;
   }

   //pre: root points to an in-order Binary Search Tree
   //post:returns the height (depth) of the tree
   
   public int heightHelper(TreeNode root)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root == null){
         return 0;
      }
      else
      {
         int left = heightHelper(root.getLeft());
         int right = heightHelper(root.getRight());
      
         if(left > right)
            return left + 1;
         else
            return right + 1;      
      }         
   //************************************************************  
      
   }
   
   //EXTRA CREDIT
   //pre: root points to an in-order Binary Search Tree
   //post:returns true if p is an ancestor of c, false otherwise
      
   public boolean isAncestor(TreeNode root, Comparable p, Comparable c)
   {
      if(root != null){   
         if(root.getValue().compareTo(p) == 0){
            if(root.getRight().getValue().compareTo(c) == 0 || root.getLeft().getValue().compareTo(c) == 0){
               return true;
            }
         }
            
         //recursion
         if(root.getValue().compareTo(p) > 0){
            if(root.getRight() != null){
               return isAncestor(root.getLeft(), p, c);
            }
         }
         if(root.getValue().compareTo(p) < 0){   
            if(root.getRight() != null){
               return isAncestor(root.getRight(), p, c);
            } 
         }
      }
      return false;
   }
   
   //EXTRA CREDIT
   //pre: root points to an in-order Binary Search Tree
   //post:shows all elements of the tree at a particular depth
   
   public void print(int level){
      if(level <= height()){
         System.out.print("Level " + level + ": ");
         printLevel(myRoot, level);
      }
      else
         System.out.println("Level " + level + " does not exist!");
   }   
   
   public void printLevel(TreeNode root, int level)
   {
      if(level == 0 && root != null){
         System.out.print(root.getValue() + " ");
      }
      if(level > 0){
         if(root.getLeft() != null && root.getRight() != null){
            printLevel(root.getLeft(), level - 1);
            printLevel(root.getRight(), level - 1);
         }
         else if(root.getLeft() != null){
            printLevel(root.getLeft(), level - 1);
         }
         else
            printLevel(root.getRight(), level - 1);
      }      
   }
 
  //Nothing to see here...move along.
   private String temp;
   private void inOrderHelper2(TreeNode root)   
   {
      if(root!=null)
      {
         inOrderHelper2(root.getLeft());
         temp += (root.getValue() + ", "); 
         inOrderHelper2(root.getRight());
      }
   }

   public String toString()
   {
      temp="";
      inOrderHelper2(myRoot);
      if(temp.length() > 1)
         temp = temp.substring(0, temp.length()-2);
      return "[" + temp + "]";
   }
  
}