import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int N;            // number of elements on randomized queue
    private int i;
   public RandomizedQueue() { // construct an empty randomized queue
       a = (Item[]) new Object[2];
   }
   public boolean isEmpty() {           // is the queue empty?
       return N == 0;
   }
   public int size() {        // return the number of items on the queue
       return N;
   }
// resize the underlying array holding the elements
   private void resize(int capacity) {
       assert capacity >= N;
       Item[] temp = (Item[]) new Object[capacity];
       for (i = 0; i < N; i++) {
           temp[i] = a[i];
       }
       a = temp;
   }
   public void enqueue(Item item)  {     // add the item
       if (item == null) throw new NullPointerException("Item==Null");
       if (N == a.length) resize(2 * a.length);
       a[N++] = item;
   }
   public Item dequeue() {             // delete and return a random item
       if (isEmpty()) throw new NoSuchElementException("Stack underflow");
       int random = StdRandom.uniform(N);
       Item itemTemp = a[random];
//       for (i = random; i < N - 1; i++) {  
//           a[i] = null; // remove random , move forward the item afer random
//           if (i < N - 1)
//               a[i] = a[i+1];
//           else throw new IndexOutOfBoundsException("IndexOutOfBounds");
//       }
       a[random] = a[N -1];
       a[N - 1] = null;
       N--;
       // shrink size of array if necessary
       if (N > 0 && N == a.length/4) resize(a.length/2);
       return itemTemp;
   }
   public Item sample() {              // return (but do not delete) a random item
       if (isEmpty()) throw new NoSuchElementException("Stack underflow");
       return a[StdRandom.uniform(N)];
   }
// return an independent iterator over items in random order
   public Iterator<Item> iterator() { 
       return new RandomizedQueueIterator();
   }
   private class RandomizedQueueIterator implements Iterator<Item> {
       private int number;
       private int random;
       private Item[] b;
       public RandomizedQueueIterator() {
           number = N;
           b = (Item[]) new Object[a.length];
           for (i = 0; i < a.length; i++) {
               b[i] = a[i];
           }
       }
       
        @Override
        public boolean hasNext() {
            return number > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Stack underflow");
            Item itemTemp;
            random = StdRandom.uniform(number);
            itemTemp = b[random];
            b[random] = b[number -1];
            b[number - 1] = itemTemp;
            number--;
            return itemTemp;
        }
    
        @Override
        public void remove() {
            throw new UnsupportedOperationException("UnsupportedOperation");
        }
   }
   
   
}
