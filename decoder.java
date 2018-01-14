import java.io.*;
import java.util.*;

public class decoder {

	HashMap<String, Integer> codeTable = new HashMap<String, Integer>();
	FourWayHeap heap = new FourWayHeap(1000000);
	Node root = new Node();
	Scanner scan = null;

	public void decode(String file1,String file2) {
		readSymbols(file2); //Read the code table file and construct the hashtable
		constructDecodeTree(); // construct the tree using the code hashtable
		decompress(file1); //Read endcoded file and drcompress
	}
	

	private void decompress(String str1) {
		StringBuffer decodedString = new StringBuffer();
		//Read endcoded.bin
		File file = new File(str1);
		//init array with file length
		//Byte array
		byte[] bytesArray = new byte[(int) file.length()];
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			fis.read(bytesArray); //read file into bytes[]
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//extract each bit and traverse the huffman tree to get the number - call to function decompressToGetContent
		decodedString = decompressToGetContent(bytesArray);
		
		//Write to decoded.txt
		BufferedWriter bwr;
		try {
			bwr = new BufferedWriter(new FileWriter(new File("decoded.txt")));
			bwr.write(decodedString.toString());
	        bwr.flush();
	        bwr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readSymbols(String str2) 
	{
		//Read the code_table.txt file line by line
		try{
				FileInputStream fstream = new FileInputStream(str2);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				String[] tokens;
				while ((strLine = br.readLine()) != null)   
				{
					//split the string
					tokens = strLine.split(" ");
					//construct the hash table (Key - Code of string type, Value - number of integer type)
					codeTable.put(tokens[1],Integer.parseInt(tokens[0]));
				}
				in.close();
		   }
		catch (Exception e){
		     e.printStackTrace();
		   }
	}

	private StringBuffer decompressToGetContent(byte[] bytes) {
		StringBuffer output = new StringBuffer();
		Node currentNode = root;
		if (bytes != null) {
			for (byte b : bytes) {
				for (int i = 0; i < 8; i++) {
					int currentBit = (b << (7 - i)) & 0x80;
					Node node = new Node();
					if (currentBit == 0x80) {
						node = currentNode.getRight();
					} else {
						node = currentNode.getLeft();
					}
					if (node.isLeaf()) {
						output.append(node.getKey());
						output.append("\n");
						currentNode = root;
					} else {
						currentNode = node;
					}
				}
			}
		}
		return output;
	}

	private void constructDecodeTree() {
		Iterator iterator = codeTable.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> obj = (Map.Entry<String, Integer>) iterator.next();
			constructDecodeTreeBranch(root, obj.getKey(), obj.getKey(), obj.getValue());
		}
	}

	private void constructDecodeTreeBranch(Node parentNode, String remainingCode, String fullCodeUntilNode, int num) 
	{
		int len = remainingCode.length();
		if (len > 1) {
			String subRemainingCode = remainingCode.substring(1, len);
			String currCode = remainingCode.substring(0, 1);
			if (currCode.equals("0")) 
			{
				if (parentNode.getLeft() == null) {
					Node left = new Node();
					left.setParent(parentNode);
					parentNode.setLeft(left);
				} else {
				}
				constructDecodeTreeBranch(parentNode.getLeft(), subRemainingCode, fullCodeUntilNode, num);
			} 
			else 
			{
				if (parentNode.getRight() == null) {
					Node right = new Node();
					right.setParent(parentNode);
					parentNode.setRight(right);
				} else {
				}
				constructDecodeTreeBranch(parentNode.getRight(), subRemainingCode, fullCodeUntilNode, num);
			}
		} else {
			String subRemainingCode = remainingCode.substring(1, len);
			String currCode = remainingCode.substring(0, 1);
			if (currCode.equals("0")) {
				if (parentNode.getLeft() == null) {
					Node left = new Node();
					left.setParent(parentNode);
					left.setCode(fullCodeUntilNode);
					left.setKey(num);
					left.setToLeaf();
					parentNode.setLeft(left);
				}
			} else {
				if (parentNode.getRight() == null) {
					Node right = new Node();
					right.setParent(parentNode);
					right.setCode(fullCodeUntilNode);
					right.setKey(num);
					right.setToLeaf();
					parentNode.setRight(right);
				}
			}
		}
	}
	
	public static void main(String args[])
	{
		long startTime = System.nanoTime();
		String str1 = args[0].toString();
		String str2 = args[1].toString();
		decoder decoderObj = new decoder();
		decoderObj.decode(str1,str2);
		long elapsedTime = System.nanoTime() - startTime;
	}
}
