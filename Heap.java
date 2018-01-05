
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A heap data structure implementing the priority queue interface.
 * <p>An efficient, array-based priority queue data structure.
 * </p>
 *
 * @author Charles Graven, cssc0199
 */

public final class Heap<E> extends AbstractQueue<E> implements Queue<E>, Iterable<E> {

    final Comparator<E> comp;
    final List<E> storage;
    private int currentSize;
   
    /***
     * The collection constructor generates a new heap from the existing
     * collection using the enclosed item's natural ordering. Thus, these
     * items must support the Comparable interface.
     * @param col
     */
    public Heap(Collection<? extends E> col) {
    	this();
        for(E e : col)
        	offer(e);
    }

    /***
     * The default constructor generates a new heap using the natural order
     * of the objects inside. Consequently, all objects placed in this
     * container must implement the Comparable interface.
     */
    public Heap() {
        comp = (Comparator<E>) Comparator.naturalOrder();
        storage = new java.util.ArrayList<>();
    }

    /***
     * Generates a new Heap from the provided collection using the specified
     * ordering. This allows the user to escape the natural ordering or
     * provide one in objects without.
     * @param col the collection to use
     * @param orderToUse the ordering to use when sorting the heap
     */
    public Heap(Collection<? extends E> col, Comparator<E> orderToUse) {
        this(orderToUse);
        for(E e : col)
        	offer(e);
    }

    /***
     * Generates a new, empty heap using the Comparator object provided as
     * its parameter. Thus, items in this heap possess no interface
     * requirements.
     *
     * @param orderToUse
     */
    public Heap(Comparator<E> orderToUse) {
        comp = orderToUse;
        storage = new java.util.ArrayList<>();
    }

   /**
    * 
    * @param data
    * @param c
    */
    public static <T> void heapify(List<T> data, Comparator<T> c) {
    		
    	  int end = data.size();
    	  int start = (end/2); 
    	 
    	  while (start >= 0) {
    		  trickleDown(data, start, c, end-1);
    		  start--;
    	  }
    }
    
    /***
     * An IN-PLACE sort function using heapsort.
     *
     * @param data a list of data to sort
     */
    public static <T> void sort(List<T> data) {
    	
    	heapify(data, (Comparator<T>) Comparator.naturalOrder());
    	int size = data.size(); 
    	int i = 0;
    	for(i = size-1; i > 0; i--) {
    		swap(data,0,i);
    		trickleDown(data, 0,(Comparator<T>) Comparator.naturalOrder(),i-1);
    	}
    	Collections.reverse(data);
    }
    

    /***
     * An IN-PLACE sort function using heapsort.
     *
     * @param data a list of data to sort
     * @param order the comparator object expressing the desired order
     */
    public static <T> void sort(List<T> data, Comparator<T> order) {
    	heapify(data, order);
    	int size = data.size(); 
    	for(int i = size-1; i >= 1; i--) {
    		swap(data,0,i);
    		trickleDown(data, 0,order,i-1);
    	}
    	Collections.reverse(data);
    }

    /**
     * The iterator in this assignment provides the data to the user in heap
     * order. The lowest element will arrive first, but that is the only real
     * promise.
     *
     * @return an iterator to the heap
     */
    @Override
    public Iterator<E> iterator() {
        return storage.iterator();
    }

    /***
     * Provides the caller with the number of items currently inside the heap.
     * @return the number of items in the heap.
     */
    @Override
    public int size() {
        return currentSize;
    }

    /***
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions. Heaps
     * represent priority queues, so the first element in the queue must
     * represent the item with the lowest ordering (highest priority).
     *
     * @param e element to offer the queue
     * @return
     */
    @Override
    public boolean offer(E e) {
    	storage.add(currentSize, e);
        trickleUp(storage, comp, currentSize);
        currentSize++;
        return true;
    }

    /***
     * Retrieves and removes the head of this queue, or returns null if this
     * queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    @Override
    public E poll() {
    	if(currentSize==0) return null;
        E temp = storage.set(0, storage.get(currentSize-1));
        storage.remove(--currentSize);
        trickleDown(storage, 0,comp, currentSize-1);
        return temp;
    }

    /***
     * Retrieves, but does not remove, the head of this queue, or returns
     * null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    @Override
    public E peek() {
    	if(currentSize==0) return null;
        return (E)storage.get(0);
    }

    public static int getLeftChild(int index){
        return ((2*index)+1);
    }
    public static int getRightChild(int index){
        return ((2*index)+2);
    }
    public static int getParent(int index){
        return ((index-1)>>1);
    }

    public static int getNextChild(int parentIndex){
        return 0;
    }

    /**
     * 
     * @param store
     * @param index1
     * @param index2
     */
    public static <E> void swap(List<E> store,int index1, int index2) {
    	E temp = store.get(index1);
    	store.set(index1, store.get(index2));
    	store.set(index2, temp);
    }
    
    /**
     * 
     * @param store
     * @param com
     * @param size
     */
    private static <T> void trickleUp(List<T> store, Comparator<T> com, int size){
        int current = size;
        int parent = getParent(current);
        while(parent>=0 && com.compare(store.get(parent), store.get(current))>=0){
        	swap(store, current ,parent);
        	current = parent;
        	parent = getParent(parent);
        }
    }


     /**
      * 
      * @param store
      * @param start
      * @param com
      * @param size
      */
    private static <T> void trickleDown(List<T> store, int start, Comparator<T> com, int size){
    	int current = start;
    	int next = getNextChild(store, current,com,size);
    	
    	while(((next!=-1))&&
    			(com.compare(store.get(current), store.get(next))>0)) {
    		swap(store, current, next);
    		current = next;
    		next = getNextChild(store, current,com,size);
    	}
    }

    /**
     * Helper method for trickle down
     * Finds the next child to compare
     * @param store list to use
     * @param parent parent node
     * @param com comparator
     * @param size size of the list
     * @return the child that is desired
     */
    private static <T> int getNextChild(List<T> store,int parent, Comparator<T> com, int size) {
    	int left = getLeftChild(parent);
    	int right = getRightChild(parent);
    	
    	if(right<size) {
    		if(com.compare(store.get(right), store.get(left))>0) {
    			return left;
    		}
    		else {return right;}
    	} if (left<=size) {
    		return left;
    	}
    	return -1;
    }

}

