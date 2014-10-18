
public class KdTree {
    
    private int N; // Store the points
    private Node root; // the root of KdTree
    private boolean orientation; // true == vertical
    
    // construct an empty set of points
    public KdTree() {                              
        N = 0;
        orientation = true;
    }
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean isVertical;
        
        public Node(Point2D p, RectHV rect, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.isVertical = isVertical;
        }
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
    /*
     Search and insert. The algorithms for 
     search and insert are similar to those for BSTs, 
     but at the root we use the x-coordinate 
     (if the point to be inserted has a smaller 
     x-coordinate than the point at the root,
      go left; otherwise go right); 
     then at the next level, we use the y-coordinate 
     (if the point to be inserted has a smaller y-coordinate 
     than the point in the node
     , go left; otherwise go right);
      then at the next level the x-coordinate, and so forth.
     */
    public void insert(Point2D p) {
        RectHV rectangle = new RectHV(0, 0, 1, 1);
        if(!contains(p)) { // 点不存在则插入
            root = insert(root, orientation, p, rectangle);
            N++;
        }
    }   
    private Node insert(Node x, boolean orientation, Point2D p, RectHV r) {
        
        if (x == null) {
            return new Node(p, r, orientation);
        }
            if (orientation) { // even level
                if      (p.x() < x.p.x()) {
                    r = new RectHV(r.xmin(), r.ymin(), x.p.x(), r.ymax());
                    x.lb = insert(x.lb, !orientation, p, r);
                }
                else if (p.x() >= x.p.x()) {
                    r = new RectHV(x.p.x(), r.ymin(), r.xmax(), r.ymax());
                    x.rt = insert(x.rt, !orientation, p, r);
                }
            } else {    // odd level
                if      (p.y() < x.p.y()) {
                    r = new RectHV(r.xmin(), r.ymin(), r.xmax(), x.p.y());
                    x.lb = insert(x.lb, !orientation, p, r);
                }
                else if (p.y() >= x.p.y()) {
                    r = new RectHV(r.xmin(), x.p.y(), r.xmax(), r.ymax());
                    x.rt = insert(x.rt, !orientation, p, r);
                }
            }
        return x;
    }
    
    
    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return contains(root, orientation, p);
    }   
    private boolean contains(Node x,boolean orientation, Point2D p) {
        if (x == null) return false;
        if (x.p.y() == p.y() && x.p.x() == p.x())  return true;
        else {
            if (orientation) { // even level
                if (p.x() < x.p.x()) return contains(x.lb, !orientation, p);
                else if (p.x() >= x.p.x()) return contains(x.rt, !orientation, p);
            } else {    // odd level
                if (p.y() < x.p.y()) return contains(x.lb, !orientation, p);
                else if (p.y() >= x.p.y()) return contains(x.rt, !orientation, p);
            }
        }
        return false;
    }
    
    // draw all of the points to standard draw
    public void draw() {
        StdDraw.setScale(0, 1);
        StdDraw.line(0, 0, 1, 0); // 画出最大的边框
        StdDraw.line(0, 0, 0, 1);
        StdDraw.line(0, 1, 1, 1);
        StdDraw.line(1, 0, 1, 1);
        draw(root);
        
    }   
    private void draw(Node root) {
        if (root != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            root.p.draw();
            StdDraw.setPenRadius();
        
            if (root.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(root.p.x(), root.rect.ymin()
                        , root.p.x(), root.rect.ymax());
                
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(root.rect.xmin(), root.p.y()
                        , root.rect.xmax(), root.p.y());
            }
        
            draw(root.lb);
            draw(root.rt);
        }
    }
    
    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> s = new Stack<Point2D>();
        return range(root, rect, s);
    }   
    private Stack<Point2D>  range(Node x, RectHV rect, Stack<Point2D> s) {
        if (x == null) return null;
        //System.out.println("rect="+rect.toString());
        if (rect.contains(x.p)) s.push(x.p);
        
        if (x.isVertical) {
            //与点所在的分割线相交
            if (rect.intersects(
                    new RectHV(x.p.x(), x.rect.ymin()
                            , x.p.x(),x.rect.ymax()))) {
               if (x.lb != null)
                   range(x.lb, rect, s);
               if (x.rt != null)
                   range(x.rt, rect, s);
            } 
            else if (x.lb != null && rect.intersects(x.lb.rect))
                    range(x.lb, rect, s);
            else if (x.rt != null && rect.intersects(x.rt.rect))
                    range(x.rt, rect, s);
            
        } else if(!x.isVertical){
            if (rect.intersects(
                    new RectHV(x.rect.xmin(), x.p.y()
                            , x.rect.xmax(),x.p.y()))) {
                if (x.lb != null)
                    range(x.lb, rect, s);
                if (x.rt != null)
                    range(x.rt, rect, s);
            } 
            else if (x.lb != null && rect.intersects(x.lb.rect))
                range(x.lb, rect, s);
            else if (x.rt != null && rect.intersects(x.rt.rect))
                range(x.rt, rect, s);
        } 
            
        return s;
    }
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        Point2D nearestPoint = root.p;
        
        return nearest(root, p, nearestPoint);
    }   
    private Point2D nearest(Node x, Point2D p, Point2D nearestPoint) {
        if (x.p == null) return null;  
        if (x.p.equals(p)) return x.p; // 相等则这点就是最近的
        
        if      (x.lb == null && x.rt == null) {
            if (p.distanceSquaredTo(nearestPoint) < p.distanceSquaredTo(x.p))
                return nearestPoint;
            else {
                nearestPoint = x.p;
                return nearestPoint;
            }
           
        }
        else if (x.lb != null 
                && p.distanceSquaredTo(x.p) 
                < p.distanceSquaredTo(x.lb.p)) {
            nearestPoint = x.p;
            if (x.rt == null) return nearestPoint;
            return nearest(x.rt, p, nearestPoint);
        }
        else if (x.rt != null 
                && p.distanceSquaredTo(x.p) 
                < p.distanceSquaredTo(x.rt.p)) {
            nearestPoint = x.p;
            if (x.lb == null) return nearestPoint;
            return nearest(x.lb, p, nearestPoint);
        }
       
        return nearestPoint;
    }
    
    // unit test
    /*
    public static void main(String args[]) {
        KdTree kdTree = new KdTree();
        Point2D p1 = new Point2D(0.1,0.1);
        Point2D p2 = new Point2D(0.2,0.2);
        Point2D p22 = new Point2D(0.2,0.2);
        Point2D p4 = new Point2D(0.15,0.15);
        Point2D p5 = new Point2D(0.5,0.5);
        Point2D p6 = new Point2D(0.05,0.05);
        kdTree.insert(p1);
        kdTree.insert(p2);
        kdTree.insert(p22);
        kdTree.insert(p4);
        kdTree.insert(p5);
        kdTree.insert(p6);
        System.out.println("contains p1 = "+kdTree.contains(p1));
        System.out.println("contains p2 = "+kdTree.contains(p2));
        System.out.println("contains p3 = "+kdTree.contains(new Point2D(0.3,0.3)));
        System.out.println("contains p4 = "+kdTree.contains(p4));
        System.out.println("contains p5 = "+kdTree.contains(p5));
        System.out.println("contains p6 = "+kdTree.contains(p6));
        System.out.println("size = "+kdTree.N);
        System.out.println("range = "+kdTree.range(new RectHV(0, 0, 0.15, 0.15)));
        System.out.println("nearest = "+kdTree.nearest(new Point2D(0.35, 0.35)));
        kdTree.draw();
    }
    
    public static void main(String args[]) {
        String filename = args[0];
        In in = new In(filename);
        
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        System.out.println("nearest = "+kdtree.nearest(new Point2D(0.81, 0.30)));
        kdtree.draw();
    }
    */
}
