
public class Subset {
        public static void main(String[] args) {
            int k = Integer.valueOf(args[0]); // prints out exactly k of strings
            RandomizedQueue<String> s = new RandomizedQueue<String>();
            while (!StdIn.isEmpty()) {
                String item = StdIn.readString();
                s.enqueue(item);
            }
            while (k > 0) { 
                StdOut.println(s.dequeue());
                k--;
            }
        }
}
