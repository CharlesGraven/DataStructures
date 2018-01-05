import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BinarySearchTree<K extends Comparable<K>, V> implements MapADT<K,V> {

	private int currentSize;
	private Node head; 
	
	/**
	 * Default constructor
	 */
	public BinarySearchTree() {
		currentSize = 0;
		head = null;
	}

	/**
	 * Node class to handle key and value pairs
	 * 
	 * @param <K>
	 *            Key
	 * @param <V>
	 *            Value
	 */
	class Node<K, V>{

		public Node(K key, V value) {
			k = key;
			v = value;
		}

		Node left;
		Node right;
		K k;
		V v;

	}

	/**
	 * Searches for a specific node in the tree
	 * @param key, the key to use for searching
	 * @return the node found
	 */
	private Node searchNode(K key) {
		Node next = head;
		while (next != null) {
			if (((Comparable) key).compareTo(next.k) == 0) {
				return next;
			} else if (((Comparable) key).compareTo(next.k) > 0)
				next = next.right;
			else
				next = next.left;
		}
		return null;
	}

	/**
	 * Searches to see if a key is in the bns
	 * 
	 * @param key to search for
	 * @return the value associated with the key
	 */
	private V search(K key) {
		Node next = head;
		while (next != null) {
			if (((Comparable) key).compareTo(next.k) == 0)
				return (V) next.v;
			else if (((Comparable) key).compareTo(next.k) > 0)
				next = next.right;
			else
				next = next.left;
		}
		return null;
	}

	@Override
	public boolean contains(K key) {
		if (search(key) == null)
			return false;
		return true;
	}

	@Override
	public V add(K key, V value) {
		if (contains(key)) {
			Node node = searchNode(key);
			Object temp = node.v;
			node.v = value;
			return (V)temp;
		} else if (head == null) {
			head = new Node<>(key, value);
			currentSize++;
			return null;
		} else {
			insert(key, value, head);
		}
		currentSize++;
		return null;
	}

	/**
	 * Helper method to make my add more readable Goes down and looks for a null to
	 * enter the node into the BNS
	 * 
	 * @param key the key to be inseted
	 * @param value the value to be inserted
	 * @param parent the parent of the current node
	 * @return the key thats being added
	 */
	private K insert(K key, V value, Node parent) {
		if (((Comparable) key).compareTo(parent.k) > 0) {
			if (parent.right == null) {
				parent.right = new Node(key, value);
				return (K) parent.right.k;
			} else {
				insert(key, value, parent.right);
			}
		} else {
			if (parent.left == null) {
				parent.left = new Node(key, value);
				return (K) parent.left.k;
			} else {
				insert(key, value, parent.left);
			}
		}
		return null;
	}

	/**
	 * A recursive helper methodd for delete that will
	 * cover all options of node deletion
	 * @param key, the key to be deleted
	 * @param parent, the parent of current
	 * @param current, the node being checked
	 * @param left, if we are traveling left in the tree
	 * @return true if it is successfully deleted
	 */
	private boolean remove(K key, Node parent, Node current, boolean left) {
		int comp = ((Comparable) key).compareTo(current.k);
		
		if (comp > 0) {
			return remove(key, current, current.right, false);
		}
		else if (comp < 0) {
			return remove(key, current, current.left, true);
		} 
		else {
			if (current.right == null && current.left == null) {
				if (parent == null) { // head case and current is head, so dont call the parent
					head = null;
				} else {
					if (left) {
						parent.left = null;
					} else {
						parent.right = null;
					}
				}
			} else if (current.right != null && current.left != null) {
	
				Node newGuy = current.right;
				Node stepparent = current; //evaluate this, could be null if its set to parent, with current it cant be
				
				boolean goingLeft = false;
				while (newGuy.left != null) {
					stepparent = newGuy;
					newGuy = newGuy.left;
					goingLeft = true;
				}
				
				if(goingLeft) {
					stepparent.left = stepparent.left.right;
				}else {
					stepparent.right = stepparent.right.right;
				}
				
				newGuy.left = current.left;
				newGuy.right = current.right;
				
				if(parent == null) {
					head = newGuy;
				}else if(left) {
					parent.left = newGuy;
				}else {
					parent.right = newGuy;
				}
			} else {
				if (parent == null) { 
					if (current.left == null) {
						head = current.right;
					} else {
						head = current.left;
					}
				} else {
					if (left) {
						if (current.left == null) {
							parent.left = current.right;
						} else {
							parent.left = current.left;
						}
					} else {
						if (current.left == null) {
							parent.right = current.right;
						} else {
							parent.right = current.left;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean delete(K key) {
		if(!contains(key)) {
			return false;
		}else {
			currentSize--;
			return remove(key, null, head, false);
		}
	}

	@Override
	public V getValue(K key) {
		return search(key);
	}

	private List<Node> builtList = new ArrayList<Node>();
	
	private void build(Node top) {
		if (top.left != null) {
			build(top.left);
		}

		builtList.add(top);

		if (top.right != null) {
			build(top.right);
		}
		
	}
	
	@Override
	public K getKey(Object value) {
		build(head);
		for(Node node : builtList) {
			if((node.v.equals(value))){
				return (K)node.k;
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
		if (head == null) {
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		head = null;
		currentSize = 0;
	}

	@Override
	public Iterator keys() {
		return new BSTIteratorKey(head);
	}

	@Override
	public Iterator values() {
		return new BSTIteratorValue(head);
	}

	class BSTIteratorKey implements Iterator<K> {

		List<K> bst;

		public BSTIteratorKey(Node h) {
			bst = new ArrayList<K>();
			if(isEmpty()) {
				//Dont doThis
			}else {
				doThis(h);
			}
		}

		public void doThis(Node top) {
			if (top.left != null) {
				doThis(top.left);
			}

			bst.add((K) top.k);

			if (top.right != null) {
				doThis(top.right);
			}
		}

		@Override
		public boolean hasNext() {
			return !bst.isEmpty();
		}

		@Override
		public K next() {
			if (hasNext()) {
				return (bst.remove(0));
			}
			return null;
		}

	}

	class BSTIteratorValue implements Iterator<V> {

		List<V> bst;

		public BSTIteratorValue(Node h) {
			bst = new ArrayList<V>();
			if(isEmpty()) {
				//Dont doThis
			}else {
				doThis(h);
			}
		}

		public void doThis(Node top) {
			if (top.left != null) {
				doThis(top.left);
			}

			bst.add((V) top.v);

			if (top.right != null) {
				doThis(top.right);
			}
		}

		@Override
		public boolean hasNext() {
			return !bst.isEmpty();
		}

		@Override
		public V next() {
			if (hasNext()) {
				return (bst.remove(0));
			}
			return null;
		}

	}

}