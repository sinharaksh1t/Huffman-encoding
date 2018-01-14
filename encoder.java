import java.io.*;
import java.util.*;

public class encoder {

        int count;
        HashMap<Integer, Integer> freqTable = new HashMap<Integer, Integer>();
        FourWayHeap heap = new FourWayHeap(1000000);
        HashMap<Integer, String> symbolTable = new HashMap<Integer, String>();                
        Scanner scan = null;
        byte[] huffCode;

        public encoder(String path) // path in String
		{
			try{
				scan = new Scanner (new File(path));
			}
			catch(Exception e){
				System.out.println("could not find file.");
			}
			while(scan.hasNext())
			{
				int element = scan.nextInt();
				if(freqTable.containsKey(element))
				{
					freqTable.put(element, freqTable.get(element)+1);
				}
				else
				{
					freqTable.put(element, 1);
				}
			}
			scan.close();
		}

		private Node createTree() //creating a Huffman Tree
		{
			createHeap(freqTable);
			int n = heap.getSize();
			for(int i = 0; i < (n-1); ++i) 
			{
				Node z = new Node();
				z.setLeft(heap.delMin());
				z.setRight(heap.delMin());
				z.setFreq(z.getLeft().getFreq() + z.getRight().getFreq());
				heap.insert(z);
			}
			return heap.getMin();
        }
		
		private void createHeap(HashMap<Integer, Integer> freqTable) //called by createTree
		{
			Iterator it = freqTable.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry pair = (Map.Entry)it.next();
				Node node = new Node((int)pair.getValue(), (int)pair.getKey());
				node.setToLeaf();
				heap.insert(node);
			}
        }
		
		private void generateCode(Node node, String code) //call in main once nodes are all made || symbol table is being created in this method
		{
	        if(node != null) 
	        {
                if(node.isLeaf())
                	symbolTable.put(node.getKey(), code);	
                else 
                {
                    generateCode(node.getLeft(), code + "0");
                    generateCode(node.getRight(), code + "1");
                }
	        }
	    }
		
		private void generateByteString(String path) //call this in main method
		{
			try{
				scan = new Scanner (new File(path));
			}
			catch(Exception e){
				System.out.println("could not find file.");
			}
			
			StringBuffer sb = new StringBuffer();
			while(scan.hasNext())
			{
				int element = scan.nextInt();
				if(symbolTable.containsKey(element))
				{
					sb.append(symbolTable.get(element));
				}
			}
			scan.close();
			huffCode = getBitSet(sb.toString()); //written into a bin file using fileoutputstream 
		}
		
		private byte[] getBitSet(String huff_code) 
		{
			BitSet buffer = new BitSet(Integer.MAX_VALUE);
			int bitIndex = -1;
			for (char c : huff_code.toCharArray()) {
				++bitIndex;
				if (c == '1') {
					buffer.set(bitIndex, true);

				} else if (c == '0') {
					buffer.set(bitIndex, false);

				} else {
					continue;
				}
			}
			return buffer.toByteArray();
		}
		
		private void makeEncodedBin(String path)
		{
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(path+"encoded.bin");
				fos.write(huffCode);
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally 
			{
	            if (fos!= null) {
	                try {
	                    fos.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		}
		
		public void makeCodeTable(String path)
		{
			BufferedWriter bufferedWriter = null;
			try {	
				bufferedWriter = new BufferedWriter(new FileWriter(path+"code_table.txt"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Iterator it = symbolTable.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry pair = (Map.Entry)it.next();
				try {
					    bufferedWriter.append(pair.getKey()+" "+pair.getValue()+"\n");
					} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
			
			try {
				bufferedWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void main(String args[])
		{
			long startTime = System.nanoTime();
			String filePath = "";
			String path = filePath+args[0];
			encoder encoderObj = new encoder(path);
			Node root = encoderObj.createTree();
			encoderObj.generateCode(root, "");
			encoderObj.generateByteString(path);
			encoderObj.makeEncodedBin(filePath);
			encoderObj.makeCodeTable(filePath);
			long elapsedTime = System.nanoTime() - startTime;
		}
}