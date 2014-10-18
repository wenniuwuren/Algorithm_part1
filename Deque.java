import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N;          // size of the Deque
    private Node first;     // first of Deque
    private Node last;  // last of Deque
    // helper linked list class
    private class Node {
        private Item item;
        private Node prior;
        private Node next;
    }

   public Deque() {       // construct an empty deque
       first = null;
       last = null;
       N = 0;
   }
   public boolean isEmpty() { // is the deque empty?
        return N == 0;  
   }
   public int size() {  // return the number of items on the deque
        return N;
   }
   
   public void addFirst(Item item) {    // insert the item at the front
       if (item == null) throw new NullPointerException("Item == Null");
       Node old = first;
       first = new Node();
       first.item  = item;
       first.next = old;
      // null Deque don't have oldfirst
       if (old != null) old.prior = first;  
       if (N == 0) last = first; //  if none node first == last
       N++;
   }
   public void addLast(Item item) {    // insert the item at the end
       if (item == null) throw new NullPointerException("Item==Null");
       Node old = last;
       last = new Node();
       last.item = item;
       last.prior = old;
       if (old != null) old.next = last;
       if (N == 0) first = last; //  if none node first == last
       N++;
   }
   public Item removeFirst() {       // delete and return the item at the front
       if (isEmpty()) throw new NoSuchElementException("Stack underflow");
       Item itemTemp = first.item;        // save item to return
       first.item = null;
       first = first.next;            // delete first node
       if (first != null) first.prior = null;    //last Node first is null         
       N--;
       if (N == 0) {
           first = null;
           last = null;
       }
       return itemTemp;
   }
   public Item removeLast() {           // delete and return the item at the end
       if (isEmpty()) throw new NoSuchElementException("Stack underflow");
       Item itemTemp = last.item;
       last.item = null;
       last = last.prior;
       if (last != null) last.next = null; //last Node last is null
       N--;
       if (N == 0) {
           first = null;
           last = null;
       }
       return itemTemp;
   }
 // return an iterator over items in order from front to end
   public Iterator<Item> iterator() {   
       return new DequeIterator();
   }
   private class DequeIterator implements Iterator<Item> {
       private Node current;
       public DequeIterator() {
           current = first;
       }
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("NoSuchElement");
            Item item = current.item;
            current = current.next;
            return item;
        }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("UnsupportedOperation");
    }
   }
   
   
}