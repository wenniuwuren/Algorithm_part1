public class PercolationStats {
    private Percolation percolation;
    private double size; // total sites number
    private int times;
    private int[] sites;
    // The sample mean μ provides an estimate of the percolation threshold
    private double sampleMean;
    // the sample standard deviation σ measures the sharpness of the threshold
    private double sampleStanDevi;
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) throws Exception {
        if (N <= 0 || T <= 0)
            throw new IndexOutOfBoundsException("out of bounds");
        sites = new int[T];
        size = N * N;
        times = T;
        int randomX = 0; // random X axis
        int randomY = 0; // random Y axis
        
     // random open site and percolate T times  
        for (int i = 0; i < T; i++) {
            percolation = new Percolation(N);
            while (!percolation.percolates()) {
                randomX = StdRandom.uniform(1, N + 1);
                randomY = StdRandom.uniform(1, N + 1);
                if (!percolation.isOpen(randomX
                    , randomY)) { 
                    percolation.open(randomX, randomY);
                    sites[i]++;
                }
            }
         }
        sampleMean = mean();
        sampleStanDevi = stddev();
    }
   
    public double mean() { // sample mean of percolation threshold
        double total = 0;
        for (int i = 0; i < sites.length; i++) {
            total += sites[i];
        }
        sampleMean = total / size / times;
        return sampleMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double sum = sampleMean;
        sum = 0;
        for (int i = 0; i < sites.length; i++) {
//            sum += (Double.parseDouble(sites[i] + "") / size - sampleMean) 
//            * (Double.parseDouble(sites[i] + "") / size - sampleMean);
            sum += (sites[i]/size - sampleMean) 
                      * (sites[i]/size - sampleMean);
     }
        sampleStanDevi = Math.sqrt(sum / (times - 1));
        return sampleStanDevi;
    }
    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        double lowBound = 
            sampleMean - 1.96 * sampleStanDevi / Math.sqrt(times);
        return lowBound;
    }

    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {  
        double hiBound = sampleMean 
            + 1.96 * sampleStanDevi / Math.sqrt(times);
        return hiBound;
    }

    // test client, described below
    public static void main(String[] args)  throws Exception {   
        
        int N = StdIn.readInt(); // lattice size
        // repeating this computation experiment T times
        int T = StdIn.readInt();
        
        PercolationStats percolationStats = new PercolationStats(N, T);
         
        StdOut.println("mean                                   = "
                + percolationStats.mean());
        StdOut.println("stddev                                 = "
                + percolationStats.stddev());
        StdOut.println("95% confidence interval= "
                + percolationStats.confidenceLo()
                + " "
                + percolationStats.confidenceHi());
    }
}
