//Nicholas Roethel
//Feb. 7 2019


import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.lang.Math;
import java.util.Random;
import java.util.*;

public class RedBlackBST {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST
    public static int reds = 0; //keeps track of the amount of red nodes
    // BST helper node data type
    private class Node {
        private int key;           // key
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(int key, boolean color, int size) {
            this.key = key;
            this.color = color;
            this.size = size;
        }
    }

    public RedBlackBST() {

    }
   /***************************************************************************
    *  Node helper methods.
    ***************************************************************************/
    // is node x red; false if x is null ?
   private boolean isRed(Node x) {
    if (x == null) return false;
    return x.color == RED;
}

public static int percentRed(int size, int reds){ //returns what percent of reds there are

 float tmp = ((float)reds*100)/(float)size;
 return Math.round(tmp);


}

    // number of node in subtree rooted at x; 0 if x is null
private int size(Node x) {
    if (x == null) return 0;
    return x.size;
} 


    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

   /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
   public boolean isEmpty() {
    return root == null;
}

   /***************************************************************************
    *  Red-black tree insertion.
    ***************************************************************************/

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public void put(int key) {

        root = put(root, key);
        if(root.color == RED){
            reds --; //decrease reds by one
        }
        root.color = BLACK;
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, int key) { 

        if (h == null){
         reds ++; //increase reds by 1
         return new Node(key, RED, 1);
     }

        int cmp = key - h.key;
        if      (cmp < 0) h.left  = put(h.left,  key); 
        else if (cmp > 0) h.right = put(h.right, key); 
        else              h.key   = key;

        // fix-up any right-leaning links

        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
        h.size = size(h.left) + size(h.right) + 1;

        return h;
    }


   /***************************************************************************
    *  Red-black tree helper functions.
    ***************************************************************************/

    // make a left-leaning link lean to the right
   private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
    Node x = h.left;
    h.left = x.right;
    x.right = h;
    x.color = x.right.color;
    x.right.color = RED;
    x.size = h.size;
    h.size = size(h.left) + size(h.right) + 1;
    return x;
}

    // make a right-leaning link lean to the left
private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
    Node x = h.right;
    h.right = x.left;
    x.left = h;
    x.color = x.left.color;
    x.left.color = RED;
    x.size = h.size;
    h.size = size(h.left) + size(h.right) + 1;
    return x;
}

    // flip the colors of a node and its two children
private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));''
    reds --; //decrease reds by one

    h.color = !h.color;
    h.left.color = !h.left.color;
    h.right.color = !h.right.color;
}


    /**
     * Unit tests the {@code RedBlackBST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) { 

        RedBlackBST st = new RedBlackBST();
        RedBlackBST two = new RedBlackBST();
        RedBlackBST three = new RedBlackBST();

        if (args.length != 0){// if a file exists
            try { //try to read it
                File file = new File(args[0]);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String[] nums = br.readLine().split("\\s+");
                int i = 0; 
                while(i<nums.length){
                    st.put(Integer.valueOf(nums[i])); //fill the tree with numbers from the files
                    i++;
                }

                int ans = percentRed(st.size(),reds);
                System.out.println("Reading input values from " + args[0]);
                System.out.printf("Percent of Red Nodes: %d\n",ans);
            }

            catch (IOException e) {
                e.printStackTrace();

            } 



        }

        else{ //if no test file is inputed
            int var = 10000; // n = 10^4
            ArrayList<Integer> arrlist = new ArrayList<>();
            for (int x = 0; x<100; x++){ //insert random variables 100 times
                for(int i = 0; i<var; i++){
                    arrlist.add(i+1);           
                }
            }
            Collections.shuffle(arrlist); //randomize them
            for(int y: arrlist){
                st.put(y); // insert in the tree
            }
            int ans = percentRed(st.size(),reds);
            System.out.println("Reading input values from 100 tests of n = 10^4");
            System.out.printf("Percent of Red Nodes: %d\n",ans);
            arrlist.clear(); //reset the list
            reds = 0; //reset the reds

            var = 100000; // n = 10^5
            for (int x = 0; x<100; x++){ //insert random variables 100 times
                for(int i = 0; i<var; i++){
                    arrlist.add(i+1);           
                }
            }
            Collections.shuffle(arrlist); //randomize them
            for(int y: arrlist){
                two.put(y); // insert in the tree
            }
            ans = percentRed(two.size(),reds);
            System.out.println("Reading input values from 100 tests of n = 10^5");
            System.out.printf("Percent of Red Nodes: %d\n",ans);
            arrlist.clear(); //reset the list
            reds = 0; //reset the reds

            Random random = new Random();
            int tmp = 0;
            var = 1000000; // n = 10^6
            for (int x = 0; x<100; x++){ //insert random variables 100 times
                for(int i = 0; i<var; i++){
                    tmp = random.nextInt(1000000) + 1;
                    three.put(tmp);      
                }
            }
            
            ans = percentRed(three.size(),reds);
            System.out.println("Reading input values from 100 tests of n = 10^6");
            System.out.printf("Percent of Red Nodes: %d\n",ans);
        }
    }
}





/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/