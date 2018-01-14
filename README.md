# Huffman Encoding
Class Project for course - Advanced Data Structures (COP5536)
This project report details the implementation of a java program that reads an input file, encodes the given values using the Huffman Encoding algorithm and later outputs the decoded contents back into a file.

## Project Description
This project consists of two sections. In the first section, we implement 3 types of heap structures – Binary Heap, 4-Way Cache Optimized Heap and Pairing Heap – to find out the one with the best performance and least run time. The winner out of these 3 is chosen to run the Huffman code program. Once that is done, the Huffman code program is used to write the Huffman encoder and decoder.
For the second section, we used the best performer from the previous section as the underlying heap data structure. Using this heap data structure, we created Huffman tree in an encoder file that converts the numbers from the given input file into a series of Huffman codes. Once that is done, we use the files generated out of the encoder program and provide that as an input to our decoder program to get back a decoded file which is identical to the initial input file.

## Performance Analysis
Initially I created a class Freq_table.java where I calculate the frequency table and generate the Huffman tree. Huffman tree was created using all the 3 heap data structures mentioned above. Implementation was done in all the 3 heap structures in the same way. I started off by creating a min Heap. Two nodes were fetched from this min Heap one at a time by calling getMin() function which gave me the minimum values out of the entire heap. I used these two nodes, combined into one and formed a new node of the Huffman tree whose frequency was a sum of the two extracted nodes. At the end of this execution, only one node in the Heap was left which was the root of the Huffman Tree.
* Binary Heap – This heap structure performed second best out of the 3 heap implementations. In a binary heap, each node has only two children and the tree made in the binary heap always conforms to the following two properties – the tree formed is a complete binary tree and for every node, the values of the children are always greater than the parent (for Min Heap).
* 4-Way Cache Optimized Heap – The fastest data structure used for tree creation. The property that this heap structure follows is that every node has a maximum of 4 children. In addition, since this is a cache optimized heap, there occurs no cache misses when the children nodes are accessed by the parent. As a result of this optimization property, the comparison completes in one cycle. Also, since this heap structure can have 4 children, the size of this heap is very less and thus, turns out to give the best performance.
* Pairing Heap – Pairing Heap took the longest time for tree creation. This is because of the way it is implemented which makes it have no direct relationship with its children as compared to Binary or 4-way heap structures. So, for each comparison children are visited based on the left or right links of the children and children reference of the parent.
Different instances of timings captured for Tree creation by each Heap Data Structure is displayed in the image below -
![image](https://user-images.githubusercontent.com/28585848/34919364-a23687ca-f988-11e7-8e38-424fda9e9e71.png)
![image](https://user-images.githubusercontent.com/28585848/34919365-a875a92c-f988-11e7-9d17-b25fb2b02265.png)
![image](https://user-images.githubusercontent.com/28585848/34919370-af4b4d7e-f988-11e7-979d-a10a18fa7b1c.png)
Looking at the above screenshots, it is evident that 4-Way Cache Optimized Heap has the best execution time out of the three heap data structures.
Therefore, that is what we use to create the final Huffman Tree.

## Decoding Algorithm
binary encoded file and the code table text file is fed to the decoded class for the decoding process.
First step is to create a tree using the code table file.
Decode Tree – Decode Tree is created following the procedure listed below:
1. All the entries from code table file are read in a HashMap. This contains the Huffman code as String in its keys and the corresponding number as integer into the values field.
2. Each entry of Map is read one by one. The code string is read character by character. For each string, the process starts from root of the tree, which contains the maximum integer value. Based on the current value of the character, node is visited/created as follows -
a) If current character = 0, then traverse left if a left node exists. If not, a new left node is created.
b) If current character = 1, then traverse right if a right node exists. If not, a new right node is created.
3. This process ends when all the rows of the map are traversed. The end of this traversal and its corresponding linkage marks the creation of the decode tree.
The complexity of this process is O(nh) where n is number of elements and h is the height of the tree.
Retrieving Values – Now that we have the decode tree already created, the next step is to read the encoded file. The contents of the file are stored in a byte array. Now each element of this array, which is in Hex, is converted to binary. We start the tree traversal from the root of the tree, going left if the character is ‘0’ or to the right if the character is ‘1’. As soon as a leaf node is encountered, the value of the leaf node is extracted and written into the decoded.txt file. If the node encountered is not a leaf node, the tree traversal process is continued until a leaf node is found.
In worst case scenario, the tree traversal for each value could happen right from the root level to the leaf node every single time. This would mean that the complete tree height is traversed n times for the n elements. So the total complexity would be O(nh).
Thus, making total decoding complexity as – O(nh+nh) = O(nh).
n – number of elements in the file.
h – height of the tree.

## Execution
The make file can simply be executed using the command "make" on the terminal.
