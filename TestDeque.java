public class TestDeque {
    public static void main (String args[]) {
        Deque<Long> deque = new Deque<Long>();
        //int size = args.length;
//        for (int i = 0; i < size; i++)
//            deque.addLast(args[i]);
        long a = 1056000;
        for (long i = 0; i < a; i++)
          deque.addLast(i);
        for (Long s : deque) {
            System.out.println(s);
        }
   
    }
}
