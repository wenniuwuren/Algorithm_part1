import java.util.TreeSet;


public class PointSET {
    
   private TreeSet<Point2D> pointSet;    // Store points
   private int N;               // number of points
   
   // construct an empty set of points
   public PointSET() {
       
       pointSet = new TreeSet<Point2D>();
       N = 0;
   }
   
   // is the set empty?
   public boolean isEmpty() { 
       return N == 0;
   }
   
   // number of points in the set
   public int size() {                              
       return N;
   }
   
   // add the point p to the set (if it is not already in the set)
   public void insert(Point2D p) {
       if (!pointSet.contains(p)) {
           pointSet.add(p);
           N++;
       }
   }
   
   // does the set contain the point p?
   public boolean contains(Point2D p) {
       return pointSet.contains(p);
   }   
   
   // draw all of the points to standard draw
   public void draw() {
       StdDraw.setPenColor(StdDraw.BLACK);
       StdDraw.setPenRadius(.01);
       for (Point2D p : pointSet)
           p.draw();
       StdDraw.show(0);
   }
   
   // all points in the set that are inside the rectangle
   public Iterable<Point2D> range(RectHV rect) {
       Stack<Point2D> stack = new Stack<Point2D>();
       
       for (Point2D p : pointSet) {
           if (rect.contains(p)) {
               stack.push(p);
           }
       }
       
       return stack;
   }     
   
   
   // a nearest neighbor in the set to p; null if set is empty
   public Point2D nearest(Point2D p) {
       
       if (N == 0) return null;
       
       Point2D neighbor = null;
       for (Point2D point : pointSet) {
           if (neighbor == null) {
               neighbor = point;
           }
           
           if (point.distanceSquaredTo(p) < neighbor.distanceSquaredTo(p)) {
               neighbor = point;
           }
       }
       
       return neighbor;
       
   }              
   
   /*
   public static void main(String args[]) {
       RectHV rectHV = new RectHV(0.1, 0.1, 0.25, 3);
       PointSET p = new PointSET();
       p.insert(new Point2D(0.1,0.1));
       p.insert(new Point2D(0.2,0.2));
       p.insert(new Point2D(0.3,0.3));
       p.insert(new Point2D(0.4,0.4));
       System.out.println("size = "+p.N);
       System.out.println("nearest = "+p.nearest(new Point2D(0.05,0.05)).toString());
       for (Point2D s : p.range(rectHV))
           System.out.println("range = "+ s.toString());
   }
   */
}
