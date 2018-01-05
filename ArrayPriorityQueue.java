
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * An array-based priority queue implementation.
 * <p>
 * A first-in-first-out (FIFO) data container, the basic queue represents
 * a line arranged in the order the items arrive. This queue differs from
 * the standard implementation in that it requires all enclosed objects
 * support comparison with like-type objects (Comparable), and it
 * automatically arranges the items in the queue based upon their ranking.
 * Lower items appear closer to the queue's front than larger items.
 * </p>
 * Due to their nature, queues must support rapid enqueue (offer) and
 * dequeue (poll) operations. That is, getting in and out of line must
 * occur rapidly. In this priority queue, the poll operation shall occur
 * in constant time, but because the queue requires arrangement, adding
 * to the circular-array based priority queue may produce slower timings,
 * for the data structure must use binary-search to locate where the new
 * item belongs in relation to everything else, and it must then shift
 * the existing contents over to make room for the addition.
 * </p>
 * <p>
 * In the likely event two objects share the same priority, new items
 * added to the queue shall appear after existing entries. When a standard
 * priority customer enters the line, it should naturally go behind the
 * last standard customer currently in line.
 * </p>
 * <p>
 * Although technically a Collection object, the queue does not support
 * most of the standard Collection operations. That is, one may not add
 * or remove from the Queue using the collection methods, for it breaks
 * the abstraction. One may, however, use a Queue object as an input
 * parameter to any other Collection object constructor.
 * </p>
 *
 * @param <E> Object to store in container. It must support comparisons with
 *            other objects of the same type.
 * @author Charles Graven, cssc0199@edoras.sdsu.edu
 */
public final class ArrayPriorityQueue<E extends Comparable<? extends E>>
        extends AbstractCollection<E> implements Queue<E> {
	
    private List<E> queue = new CircArrayList<>();

    /**
     * Builds a new, empty priority queue.
     */
    public ArrayPriorityQueue() {
    	super();
    }

    /**
     * Builds a new priority queue containing every item in the provided
     * collection.
     *
     * @param col the Collection containing the objects to add to this queue.
     */
    public ArrayPriorityQueue(Collection<? extends E> col) {
    	for(E e: col)
    		offer(e);
    }

    /**
     * Reports the number of items in this queue.
     *
     * @return the number of items in this queue.
     * @implNote it is INCORRECT to track this as a field in this class.
     */
    @Override
    public int size() {
        return queue.size();
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions. When using a
     * capacity-restricted queue, this method is generally preferable to add
     * (E), which can fail to insert an element only by throwing an exception.
     *
     * @param e the element to add
     * @return true if element added to queue, false otherwise
     */
    @Override
    public boolean offer(E e) {
    	
    	if(size()==0) {
    		queue.add(0, e);
    	}else {
    		int insertIndex = binarySearch((Comparable<E>)e);
    		queue.add(insertIndex,e);
    	}
        return true;
    }

    /**
     * Retrieves and removes the head of this queue. This method differs from
     * poll only in that it throws an exception if this queue is empty.
     *
     * @return the queue's head
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E remove() {
        return queue.remove(0);
    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this
     * queue is empty.
     *
     * @return this queue's head or null if this queue is empty.
     */
    @Override
    public E poll() {
        if(queue.size()==0)
        	return null;
    	return queue.remove(0);
    }

    /**
     * Retrieves, but does not remove, the head of this queue. This method
     * differs from peek only in that it throws an exception if this queue is
     * empty.
     *
     * @return the head of this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    @Override
    public E element() {
    	if(size()==0)
        	throw new java.util.NoSuchElementException();
        return queue.get(0);
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns
     * null if this queue is empty.
     *
     * @return the head of this queue or null if this queue is empty
     */
    @Override
    public E peek() {
    	if(queue.size()==0)
        	return null;
        return queue.get(0);
    }
    
    /**
     * A generic non-recursive binary search which will return the
     * proper index at which an Object should be placed even if not
     * found in the List.
     * 
     * @param e Object to Search for
     * @return the spot at which the object should be inserted
     */
    public int binarySearch(Comparable<E> e) {
        int low = 0;
        int high = queue.size()-1;
        int index = 0;
        
        while (low <= high) {
            int mid = (low+high) / 2;
            int cmp = e.compareTo(queue.get(mid));
            if (cmp == 0) {
                index = mid;
                return index;
            } else if (cmp < 0) {
                high = mid-1;
                index = mid;
            } else {
                low = mid+1;
                //if(e.compareTo(queue.get(size()-1))>1)
                if(e.compareTo(queue.get(high))>=1) {
                	index = mid+1;
                }else
                	index = mid; 
            }
        } 
        
        return index;
    }
	
    @Override
    public Iterator<E> iterator() {
    	return queue.iterator();
    }
    
    public E get(int index) {
    	return queue.get(index);
    }

}