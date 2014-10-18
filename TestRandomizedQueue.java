
public class TestRandomizedQueue {
    public static void main(String args[]) {
        RandomizedQueue<String> randomizedQueue= new RandomizedQueue<String>();
        for (int i = 0; i < args.length; i++)
            randomizedQueue.enqueue(args[i]);
//        while(randomizedQueue.iterator().hasNext())     //不断地实例化，N不断被赋值，不会停止
//            System.out.println(randomizedQueue.iterator().next());
        for (String i : randomizedQueue) {
            System.out.print(i+" ");
        }
        System.out.println("--------------");
        for (String i : randomizedQueue) {
            System.out.print(i+" ");
        }
       
    }
}
