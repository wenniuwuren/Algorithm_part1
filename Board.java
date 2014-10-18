import java.util.Iterator;

public class Board {
    
    private int[][] block;
    private int size;
    private int zeroI;
    private int zeroJ;
    
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {  
        
        size = blocks.length;
        block = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                block[i][j] = blocks[i][j];
                if (block[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
    }
     
    // board dimension N
    public int dimension() {                 
        return size;
    }
    
    // number of blocks out of place
    public int hamming() {                   
        
        int hamming = 0;
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j) {
                if ((i == size - 1) && (j == size - 1)) { // 0 不算入错位
                    continue;
                } else if (block[i][j] != size * i + j + 1) {
                    ++hamming;
                }
            }
        return hamming;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {            //  1 2 3          
        int manhattan = 0;              //  4 5 6  
        for (int i = 0; i < size; i++)  //  7 8 0
            for (int j = 0; j < size; j++) {
                if (block[i][j] == 0)
                    continue;
                int oj = block[i][j] % size;
                int oi = block[i][j] / size;
                if (oj == 0) {
                    oj = size - 1;
                    --oi;
                } else {
                    --oj;
                }
                manhattan += Math.abs(oi - i) + Math.abs(oj - j);
            }
         return manhattan;   
                
    }
    
    // is this board the goal board?
    public boolean isGoal() {               
        if (hamming() == 0) 
            return true;
        return false;
        
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    // use it to determine whether a puzzle is solvable
    public Board twin() {
        Board twin = new Board(this.block);
        boolean exchange = false;
        while (!exchange) {
            int i = StdRandom.uniform(size);
            int j = StdRandom.uniform(size);
            int ej = j;
            if (j == size - 1) {
                --ej;
            } else {
                ++ej;
            }
            if (block[i][j] != 0 && block[i][ej] != 0) { // 与零交换不算
                int temp = twin.block[i][j];
                twin.block[i][j] = block[i][ej];
                twin.block[i][ej] = temp;
                exchange = true;
            }
        }
        return twin;
    }

    // does this board equal y?
    public boolean equals(Object y) { 
        if (y == null)
            return false;
        if (!y.getClass().equals(this.getClass()))
            return false;
        Board another = (Board) y;
        if (this.size != another.size)
            return false;
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j) {
                if (this.block[i][j] != another.block[i][j])
                    return false;
            }
        return true;
    }
    
    
    // all neighboring boards
    public Iterable<Board> neighbors() { 
        Iterable<Board> iter = new NeighborIterable();
        return iter;
    }
    
    private class NeighborIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            NeighborIterator iter = new NeighborIterator();
            return iter;
        }
    }
    
    private class NeighborIterator implements Iterator<Board> {
        private Queue<Board> neighbors;

        public NeighborIterator() {
            neighbors = new Queue<Board>();
            if (zeroI > 0) {
                Board leftNeighbor = new Board(block);
                leftNeighbor.block[zeroI][zeroJ] 
                        = leftNeighbor.block[zeroI - 1][zeroJ];
                leftNeighbor.block[zeroI - 1][zeroJ] = 0;
                --leftNeighbor.zeroI;
                neighbors.enqueue(leftNeighbor);
            }
            if (zeroI < size - 1) {
                Board rightNeighbor = new Board(block);
                rightNeighbor.block[zeroI][zeroJ] 
                        = rightNeighbor.block[zeroI + 1][zeroJ];
                rightNeighbor.block[zeroI + 1][zeroJ] = 0;
                ++rightNeighbor.zeroI;
                neighbors.enqueue(rightNeighbor);
            }
            if (zeroJ > 0) {
                Board upperNeighbor = new Board(block);
                upperNeighbor.block[zeroI][zeroJ] 
                        = upperNeighbor.block[zeroI][zeroJ - 1];
                upperNeighbor.block[zeroI][zeroJ - 1] = 0;
                --upperNeighbor.zeroJ;
                neighbors.enqueue(upperNeighbor);
            }
            if (zeroJ < size - 1) {
                Board downNeighbor = new Board(block);
                downNeighbor.block[zeroI][zeroJ] 
                        = downNeighbor.block[zeroI][zeroJ + 1];
                downNeighbor.block[zeroI][zeroJ + 1] = 0;
                ++downNeighbor.zeroJ;
                neighbors.enqueue(downNeighbor);
            }
        }

        public boolean hasNext() {
            return (neighbors.isEmpty() == false);
        }

        public Board next() {
            return neighbors.dequeue();
        }

        public void remove() {

        }
    }
    
    // string representation of the board (in the output format specified below)
    public String toString() {              
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(String.format("%2d ", block[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    public static void main(String[] args) {
        int N = 3;
        int[][] blocks = new int[N][N];
        blocks[0][0] = 8;
        blocks[0][1] = 1;
        blocks[0][2] = 3;
        blocks[1][0] = 4;
        blocks[1][1] = 0;
        blocks[1][2] = 2;
        blocks[2][0] = 7;
        blocks[2][1] = 6;
        blocks[2][2] = 5;

        Board b = new Board(blocks);
        StdOut.println(b + "\nhamming : " + b.hamming() + "\nmanhattan: "
                + b.manhattan());

        StdOut.println(b.twin());
    }
    
    
}
