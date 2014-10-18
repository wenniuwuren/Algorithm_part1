import java.util.Comparator;
/*************************************************************************
 * Name: Yibin_Zhu
 * Email: wenniuwuren@gmail.com
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

public class Point implements Comparable<Point> {
 
   // compare points by slope to this point
   public final Comparator<Point> SLOPE_ORDER = new BySlopeOrder();
   
   private final int x;                              // x coordinate
   private final int y;                              // y coordinate
   
   // construct the point (x, y)
   public Point(int x, int y) {                         
       this.x = x;
       this.y = y;
   }
   
   
   private class BySlopeOrder implements Comparator<Point> {
       
       @Override
       public int compare(Point p1, Point p2) {
           double slope1 = slopeTo(p1);
           double slope2 = slopeTo(p2);
           if (slope1 < slope2) return -1;
           else if (slope1 > slope2) return 1;
           return 0;
       }
  }
   
  // plot this point to standard drawing
   public void draw() {                             
       StdDraw.point(x, y);
   }
   
   // draw the line segment from this point to that point
   public void drawTo(Point that) {                   
       StdDraw.line(this.x, this.y, that.x, that.y);
   }
   
   // string representation
   public String toString() {                         
       return "(" + x + ", " + y + ")";
   }

   // is this point lexicographically smaller than that point?
   public int compareTo(Point that) {              
       
       if (this.y < that.y 
              || this.y == that.y && this.x < that.x) return -1;
       else if (this.y == that.y && this.x == that.x) return 0;
       else return 1;
   }
   
// the slope between this point and that point
   public double slopeTo(Point that) {           
       
       //  the slope of a degenerate line segment (between a point and itself) 
       // as negative infinity
       if (that.x == this.x && that.y == this.y) 
           return Double.NEGATIVE_INFINITY;
       
       // the slope of a horizontal line segment as positive zero
       else if (that.y == this.y) return +0.0; 
       
       // the slope of a vertical line segment as positive infinity
       else if (that.x == this.x) return Double.POSITIVE_INFINITY;
       
       return (double) (that.y - this.y) / (that.x - this.x);
   }
   
   
   // unit test
   public static void main(String[] args) {
       /* YOUR CODE HERE */
       //Point p = new Point(1,1);
       //System.out.println(p.slopeTo(new Point(1,3)));
       //System.out.println(p.slopeTo(new Point(3,3)));
       //System.out.println(p.slopeTo(p));
       
   }
}
