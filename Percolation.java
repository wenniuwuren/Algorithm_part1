/**
 * @author Yibin_Zhu 2013-03-25
 * 
 *         To model a percolation system
 */
public class Percolation {
    private int number;
    private int SIZE; // the lattice total size
    private boolean[] lattice; // sites state
    private WeightedQuickUnionUF ufVirtual, ufTrue;

    public Percolation(int N) { // create N-by-N grid, with all sites blocked
        number = N;
        SIZE = N * N;
        lattice = new boolean[SIZE];
        ufVirtual = new WeightedQuickUnionUF(SIZE);
        ufTrue     = new WeightedQuickUnionUF(SIZE);
        for (int i = 1; i < N; i++)           // Virtual Top and Bottom
            ufVirtual.union(0, i);
        for (int j = SIZE - N; j < SIZE - 1; j++) 
            ufVirtual.union(SIZE - 1, j);
        for (int i = 1; i < N; i++)         // Only Virtual Top
            ufTrue.union(0, i);
    }

    // open site (row i, column j) if it is not already
    public void open(int i, int j)  throws Exception { 
        validation(i, j);
        if (!isOpen(i, j)) { // mark the site as open
            int xyTo1DTemp = xyTo1D(i, j);
            lattice[xyTo1DTemp] = true;
            
            // links the site in question to its open neighbors
            if (xyTo1DTemp % number != 0) // leftneighbor
                if (isOpen(i, j - 1)) { 
                    ufVirtual.union(xyTo1DTemp, xyTo1DTemp - 1);
                    ufTrue.union(xyTo1DTemp, xyTo1DTemp - 1);
            }
            if ((xyTo1DTemp + 1) % number != 0) // right neighbor
                if (isOpen(i, j + 1)) { 
                    ufVirtual.union(xyTo1DTemp, xyTo1DTemp + 1);
                    ufTrue.union(xyTo1DTemp, xyTo1DTemp + 1);
                }
            if (xyTo1DTemp - number >= 0)    // up neighbor
                if (isOpen(i - 1, j)) { 
                    ufVirtual.union(xyTo1DTemp, xyTo1DTemp - number);
                    ufTrue.union(xyTo1DTemp, xyTo1DTemp - number);
                }
                if (xyTo1DTemp + number <= SIZE - 1) // beneath neighbor
                    if (isOpen(i + 1, j)) { 
                        ufVirtual.union(xyTo1DTemp, xyTo1DTemp + number);
                        ufTrue.union(xyTo1DTemp, xyTo1DTemp + number);
                }
        }
    }
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j)  throws Exception {  
        validation(i, j);
        if (lattice[xyTo1D(i, j)])
            return true;
        return false;
    }

    // is site (row i, column j)  full "open" site connected to the top
    public boolean isFull(int i, int j)  throws Exception {
        validation(i, j);
        if (isOpen(i, j) && ufTrue.connected(0, xyTo1D(i, j)))
            return true;
        return false;
    }

    public boolean percolates()  throws Exception { // does the system percolate?
        if (SIZE != 1 && ufVirtual.connected(0, SIZE - 1))
            return true;
        else if (SIZE == 1 && isOpen(1, 1))
            return true;
        return false;
    }

    private int xyTo1D(int i, int j) { // mapping 2D coordinates to 1D
        int result = j + number * (i - 1) - 1;
        return result;
    }
    private void validation(int i, int j)  throws Exception { //validate the x,y
        if (i < 1 || i > number || j < 1 || j > number)
            throw new IndexOutOfBoundsException("index out of bounds");
    }
}
