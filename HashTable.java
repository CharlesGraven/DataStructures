import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HashTable<K extends Comparable<K>, V> implements MapADT<K,V> {

	private List<Entry<K,V>>[] buckets;
	private int currentSize;
	private int TABLE_SIZE;
	private double MIN = .25;
	private double MAX = .9;
	
	/**
	 * Default Constructor
	 */
	public HashTable() {
		TABLE_SIZE = 123;
		buckets = new List[TABLE_SIZE];
		for(int i = 0; i < TABLE_SIZE; i++) {
			buckets[i] = new LinkedList<>();
		}
	}
	
	/**
	 * Constructor that takes in a size
	 * @param size of the hashtable
	 */
	public HashTable(int size) {
		TABLE_SIZE = size;
		buckets = new List[TABLE_SIZE];
		for(int i = 0; i < TABLE_SIZE; i++) {
			buckets[i] = new LinkedList<>();
		}
	}
	
	/**
	 * Inner class to help stor entris
	 * @param <K> key
	 * @param <V> value
	 */
	class Entry<K,V>{
		K k;
		V v;
		
		public Entry(K key, V value) {
			k = key;
			v = value;
		}
	}
	
	@Override
	public boolean contains(K key) {
		int getHash = (key.hashCode() & 0x7FFFFFFF) & TABLE_SIZE-1;
		for(Entry e : buckets[getHash]) {
			if(key.compareTo((K) e.k)==0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object add(K key, V value) {
		int getHash = (key.hashCode() & 0x7FFFFFFF) & TABLE_SIZE-1;
		if(contains(key)) {
			Entry e = null;
			for(int i = 0; i < buckets[getHash].size();i++) {
				Comparable temp = (Comparable) buckets[getHash].get(i).k;
				if(temp.compareTo(key)==0) {
					e = buckets[getHash].get(i);
				}
			}
			Object temp = e.v;
			e.v = value;
			
			return temp;
		}
		else {
			buckets[getHash].add(new Entry(key,value)); 
			currentSize++;
		
		if(((double)currentSize/TABLE_SIZE)>=MAX) {
			arrayCopier(4);
		}
			return null;
		}
	}

	@Override
	public boolean delete(K key) {
		int getHash = (key.hashCode() & 0x7FFFFFFF) & TABLE_SIZE-1;
		if(!contains(key)) {
			return false;
		}
		Entry e = null;
		for(int i = 0; i < buckets[getHash].size();i++) {
			Comparable temp = (Comparable) buckets[getHash].get(i).k;
			if(temp.compareTo(key)==0) {
				e = buckets[getHash].get(i);
			}
		}
		buckets[getHash].remove(e); //I dont know if this will work
		currentSize--;
		
		//BREAKS SO DONT UNDO
		//if(((double)currentSize/TABLE_SIZE)<=MAX) {
		//	arrayCopier(.5);
		//}
		return true;
	}

	@Override
	public V getValue(K key) {
		int getHash = (key.hashCode() & 0x7FFFFFFF) & TABLE_SIZE-1;
		if(!contains(key)) { //new
			return null;
		}
		for(Entry e : buckets[getHash]) {
			if(key.compareTo((K) e.k)==0) {
				return (V) e.v;
			}
		}
		return null;
	}

	@Override
	public K getKey(V value) {
		for(int i = 0; i < buckets.length; i++) {
			for(Entry e : buckets[i]) {
				if(e.v.equals(value)) {
					return (K)e.k;
				}
			}
		}
		return null;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public boolean isEmpty() {
		if(currentSize==0) return true;
		return false;
	}

	@Override
	public void clear() {
		for(int i = 0; i < buckets.length; i++) {
			buckets[i].clear();
		}
		currentSize = 0;
	}

	@Override
	public Iterator keys(){
		return new HashIteratorKeys();
	}

	@Override
	public Iterator values() {
		return new HashIteratorValues();
	}
	
	/**
	 * Makes the hashtable larger since the
	 * hashtable can get really large in size. Right
	 * now shrinking does not work
	 * @param percentage to grow or shrink the hashtable
	 */
	private void arrayCopier(double percentage) {
		int newSize = (int)(TABLE_SIZE*percentage);
		TABLE_SIZE = newSize;
		
		List<Entry<K,V>>[] oldStorage = buckets;
		
		buckets = new List[TABLE_SIZE];
		for(int i = 0; i < TABLE_SIZE; i++) {
			buckets[i] = new LinkedList<>();
		}
		
		//Does not work for delete
		for(int i = 0; i < oldStorage.length; i++) {
			for(Entry e : oldStorage[i]) {
				int getHash = (e.k.hashCode() & 0x7FFFFFFF) & TABLE_SIZE-1;
				buckets[getHash].add(new Entry(e.k,e.v));
			}
		}
	
    }
	
	class HashIteratorKeys implements Iterator<K>{

		List<K> keys;
		List<Entry<K,V>>[] instance;
		
		public HashIteratorKeys() {
			keys = new ArrayList<K>();
			instance = buckets; 
			
			for(int i = 0; i < instance.length; i++) {
				for(Entry e : instance[i]) {
					keys.add((K) e.k);
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			return !keys.isEmpty(); 
		}
		
		@Override
		public K next() {
			return keys.remove(0);
		}
		
	}
	
	class HashIteratorValues implements Iterator<V>{

		List<V> values;
		List<Entry<K,V>>[] instance;
		
		public HashIteratorValues() {
			values = new ArrayList<V>();
			instance = buckets; 
			
			for(int i = 0; i < instance.length; i++) {
				for(Entry e : instance[i]) {
					values.add((V) e.v);
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			return !values.isEmpty(); 
		}
		
		@Override
		public V next() {
			return values.remove(0);
		}
		
		
	}
	
	public int getTableSize() {
		return TABLE_SIZE;
	}
}