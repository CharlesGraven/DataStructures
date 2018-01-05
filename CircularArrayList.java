
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * A circular version of an array list.
 * <p>Operates as a standard java.util.ArrayList, but additions and removals
 * from the List's front occur in constant time, for its circular nature
 * eliminates the shifting required when adding or removing elements to the
 * front of the ArrayList.
 * </p>
 *
 * @author Charles Graven, cssc0199
 */
public final class CircArrayList<E> extends AbstractList<E> implements
        List<E>, RandomAccess {

    private E[] storage;

    private static final int DEFAULT_SIZE = 16;
    
    private int curSize;

    private int frontCursor;
    
    private int backCursor;
    
    private static final double HIGH_BOUND = .9;
    
    private static final double LOW_BOUND = .25;
    
    /**
     * Builds a new, empty CirArrayList.
     */
    public CircArrayList() {
        super();
    	curSize = 0;
        frontCursor = 0;
        backCursor = 0;
        storage = (E[])new Object[DEFAULT_SIZE];
    }

    /**
     * Constructs a new CirArrayList containing all the items in the input
     * parameter.
     *
     * @param col the Collection from which to base
     */
    public CircArrayList(Collection<? extends E> col) {
    	curSize = 0;
    	storage = (E[])new Object[(col.size())];
    	
    	for(E e : col){
    		storage[curSize] = e;
    		curSize++;
    	}
    	
    	backCursor = col.size()-1;
    	frontCursor = 0;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index (0 based) of the element to return.
     * @return element at the specified position in the list.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0
     *                                   || index >= size())
     */
    @Override
    public E get(int index) {
        if(index<0 || index>=size())
        	throw new IndexOutOfBoundsException();
       
        return storage[toCircleIndex(index)];
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of the element to replace
     * @param value element to be stored at the specified position
     * @return element previously at the specified position
     * @throws IndexOutOfBoundsException if index is out of the range (index < 0
     *                                   || index >= size())
     */
    @Override
    public E set(int index, E value) {
    	if(index<0 || index>=size())
        	throw new IndexOutOfBoundsException();
    	E temp = storage[(toCircleIndex(index))];
    	
    	storage[toCircleIndex(index)] = value;
    	
        return temp;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param value element to be inserted
     */
    @Override
    public void add(int index, E value) {
   
    	if(index==0)
        	addFirst(value);
        else if(index==size())
        	addLast(value);
        else 
        	addMiddle(index, value);
        
    	curSize++;
    	
        //See if this works?
        if((size()/((double)storage.length))>=HIGH_BOUND) {
        	storage = arrayCopier(2);
        }
        
    }

    /**
     * Removes the element at the specified position in this list.  Shifts
     * any subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index index of element to remove
     * @return the element previously at the specified position.
     */
    @Override
    public E remove(int index) {
    	
    	if(size()==0)
    		throw new IndexOutOfBoundsException();
    	
    	if((size()/((double)storage.length))<=LOW_BOUND) {
        	storage = arrayCopier(.5);
        }
    	if(index==0){
        	return removeFirst();
        }
        else if(index==size()-1){
        	return removeLast();
        }else{
        	return removeMiddle(index);
        }
    }

    /**
     * Reports the number of items in the List.
     *
     * @return the item count.
     */
    @Override
    public int size() {
        return curSize;
    }
    
    /**
     * Adds to the front of the array in O(1)
     * this is done by keeping track of a cursor
     * 
     * @param value element to be inserted
     */
    public void addFirst(E value){

    	if(size()==0){
    		storage[frontCursor]=value;
    	}else if(frontCursor==0){
    		frontCursor = storage.length-1;
    		storage[frontCursor] = value;
    	}else{ 
    		--frontCursor;
    		storage[frontCursor] = value;
    	}
    }
    
    /**
     * Adds to the back of the array in O(1)
     * this is done by keeping track of a cursor
     * 
     * @param value element to be inserted
     */
    public void addLast(E value){
    	if(size()==0){
    		storage[0] = value;
    		return;
    	}else if(backCursor==storage.length-1){
    		backCursor = 0;
    		storage[backCursor] = value;
    	}
    	else{
    		backCursor++;
    		storage[backCursor] = value;
    	}
    }
    
    /**
     * Adds an element to the middle of a circular array
     * and deals with the fixing of indexes
     * 
     * @param index index at which the specified element is to be inserted
     * @param value element to be inserted
     */
    public void addMiddle(int index, E value){
 
    	SortAdd(index);
    	storage[toCircleIndex(index)] = value;
    	if(backCursor==storage.length-1)
    			backCursor = 0;
    	else
    		backCursor++;
    }
    
    /**
     * When added to the middle all the indexes
     * to the right of the index need to be fixed
     * 
     * @param index at which the array should be fixed from
     */
    public void SortAdd(int index){
    	
    	for(int i = size(); index<i; i--){
    		storage[toCircleIndex(i)] = storage[toCircleIndex(i-1)];
    	}
    }
    
    /**
     * Removes the middle element of an array
     * and fixes the gap made by the removal
     * 
     * @param index where to remove from
     * @return element removed
     */
    public E removeMiddle(int index){
    	--curSize;
    	E temp = get(index);
    	sortRemove(index);
    	if(backCursor==0)
			backCursor = storage.length-1;
    	else
    		--backCursor;
    	return temp;
    }
    
    /**
     * When removed in the middle all the indexes
     * to the left of the index need to be fixed
     * 
     * @param index at which the array should be fixed from
     */
    public void sortRemove(int index){
    	for(int i = index; size()>i; i++){
    		storage[toCircleIndex(i)] = storage[toCircleIndex(i+1)];
    	}
    }
    
    /**
     * Removes the first element of the circular
     * aray by moving the front cursor
     */
    public E removeFirst(){
    	--curSize;
    	if(frontCursor==storage.length-1){
    		frontCursor = 0;
    		return storage[storage.length-1];
    	}
    		
    		return storage[frontCursor++];
    }
    
    /**
     * Removes the last element of the circular
     * array by moving the back cursor
     */
    public E removeLast(){
    	--curSize;
    	if(backCursor==0){
    		backCursor = storage.length;
    		return storage[0];
    	}
    		return storage[backCursor--];
    }
    
    /**
     * Turns the desired index into an
     * index that is useful for the circle array
     * 
     * @param arrIndex
     * @return proper circle index
     */
    private int toCircleIndex(int arrIndex){
    	int j = frontCursor + arrIndex;
    	
    	if(j>(storage.length-1)){
    		return j - storage.length;
    	}
    	return j;
    }
  
    /**
     * Takes a circular array and gives it a larger
     * or smaller array based on the percentage change
     * also resets the cursors for the circular array
     * 
     * @param percentage to change the array by
     * @return the same array with a different size
     */
    private E[] arrayCopier(double percentage) {
    	E[] newStorage = (E[])new Object[(int)(storage.length*percentage)];
    	for(int i = 0; i < size(); i++){
    			newStorage[i] = get(i);
    	}
    	frontCursor = 0;
    	backCursor = size()-1;
  
    	return newStorage;
    }
    
}