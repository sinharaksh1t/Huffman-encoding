import java.util.*;

public class FourWayHeap {
	private Node[] FourWayHeap;	
	private int size = 0;

	public FourWayHeap(int size) {
		FourWayHeap = new Node[size+3];
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean isFull() {
		return size == FourWayHeap.length;
	}

	private int parent(int i) {
		return (i/4)+2;
	}

	private int kthChild(int i, int k) {
		return (4*i) + k-9;
	}

	public void insert(Node element) {
		if (!isFull()) {
			FourWayHeap[size+3] = element;
			size++;
			minHeapifyUp(size+2);
			
		}
	}
	private void minHeapifyUp(int childInd) {
		Node tmp = FourWayHeap[childInd];
		while (childInd > 3 && tmp.getFreq() < FourWayHeap[parent(childInd)].getFreq()) {
			FourWayHeap[childInd] = FourWayHeap[parent(childInd)];
			childInd = parent(childInd);
		}
		FourWayHeap[childInd] = tmp;
	}
	public Node getMin() {
		if (isEmpty())
            throw new NoSuchElementException("Underflow Exception");           
        return FourWayHeap[3];
	}

	public Node delMin() {
		if (!isEmpty()) {
			Node removed = FourWayHeap[3];
			FourWayHeap[3] = FourWayHeap[size + 2];
			size--;
			minHeapifyDown(3);

			return removed;
		}
		return null;
	}

	private void minHeapifyDown(int ind) {
		int child;
		Node tmp = FourWayHeap[ind];
		while (kthChild(ind, 1) < size+3) {
			child = minChild(ind);
			if (FourWayHeap[child].getFreq() < tmp.getFreq())
				FourWayHeap[ind] = FourWayHeap[child];
			else
				break;
			ind = child;
		}
		FourWayHeap[ind] = tmp;
	}

	private int minChild(int ind) {
		int bestChild = kthChild(ind, 1);
		int k = 2;
		int pos = kthChild(ind, k);
		while ((k <= 4) && (pos < size+3)) {				
			if (FourWayHeap[pos].getFreq() < FourWayHeap[bestChild].getFreq())
				bestChild = pos;
			pos = kthChild(ind, ++k);
		}
		return bestChild;
	}

	public void print(Node x,String codeStr) {
		
			System.out.println("Parent: "+x.getFreq()+" LChild: "+x.getLeft().getFreq()+" RChild: "+x.getRight().getFreq()+"Code till now: "+codeStr);
			if(x.isLeaf())
				System.out.println("Codeeeee: "+codeStr);
			if(!x.getLeft().isLeaf())
			{
				
				print(x.getLeft(),codeStr+"0");
			}
			else{
				System.out.println("Codeeeee: "+codeStr+"0");
			}
			if(!x.getRight().isLeaf())
			{
				print(x.getRight(),codeStr+"1");
			}
			else{
				System.out.println("Codeeeee: "+codeStr+"1");
			}
	}
}
