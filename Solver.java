import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private Board initialBoard;
    private int answerMoves;     // 最小移动步数
    private SearchNode answerLastNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.answerMoves = -1;
        this.answerLastNode = null;

        MinPQ<SearchNode> searchNodes = InitSearchNode(initial);
        Board twin = initial.twin();
        MinPQ<SearchNode> twinSearch = InitSearchNode(twin);

        while (!searchNodes.isEmpty() && !twinSearch.isEmpty()) {
            
            // 根据曼哈顿+移动距离的优先级出队列    数小优先级高
            SearchNode minSearchNode = searchNodes.delMin(); 
            SearchNode twinMin = twinSearch.delMin();
            
            if (minSearchNode.board.isGoal()) {
                this.answerMoves = minSearchNode.moves;
                this.answerLastNode = minSearchNode;
                return;
            } else if (twinMin.board.isGoal()) { // 当同行交换时，infeasible
                this.answerMoves = -1;
                this.answerLastNode = null;
                return;
            } else {
                ExtendSearchNode(searchNodes, minSearchNode);
                ExtendSearchNode(twinSearch, twinMin);
            }
        }
    }
    
    private class SearchNode {
        public Board board;
        public int moves;       // 总移动步长
        public SearchNode prev; // 指向父搜索节点
    }

    private class SearchNodeComp implements Comparator<SearchNode> {
        public int compare(SearchNode sn1, SearchNode sn2) {
            // 优先级为曼哈顿长度+移动步长
            int priority1 = sn1.board.manhattan() + sn1.moves;
            int priority2 = sn2.board.manhattan() + sn2.moves;
            if (priority1 < priority2)
                return -1;
            else if (priority1 == priority2)
                return 0;
            else
                return 1;
        }
    }

    private MinPQ<SearchNode> InitSearchNode(Board init) {
        SearchNodeComp snComp = new SearchNodeComp();
        MinPQ<SearchNode> searchNodes = new MinPQ<SearchNode>(1, snComp);
        SearchNode initNode = new SearchNode();
        initNode.board = init;
        initNode.moves = 0;
        initNode.prev = null;
        searchNodes.insert(initNode);
        return searchNodes;
    }

    // 下一个搜索节点
    private void ExtendSearchNode(MinPQ<SearchNode> searchNodes,
            SearchNode curSearchNode) {
        Iterable<Board> iterable = curSearchNode.board.neighbors();
        Iterator<Board> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            SearchNode newSearchNode = new SearchNode();
            newSearchNode.board = iterator.next();
            newSearchNode.moves = curSearchNode.moves + 1;
            newSearchNode.prev = curSearchNode;
            /* don't enqueue a neighbor if its board is the same as 
                the board of the previous search node. ----------
                --!newSearchNode.board.equals(curSearchNode.prev.board) */
            if (curSearchNode.prev == null
                    || !newSearchNode.board.equals(curSearchNode.prev.board))
                searchNodes.insert(newSearchNode); // neighbor插入优先队列
        }
    }
    
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return (answerMoves != -1);
    }
    
    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        return answerMoves;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return new SolutionIterable();
    }
    
    private class SolutionIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new SolutionIterator();
        }
    }
    
    private class SolutionIterator implements Iterator<Board> {

        private Stack<Board> solutionSequence;

        private SolutionIterator() {
            // 用Stack 输出时就是从起点到终点  
            solutionSequence = new Stack<Board>(); 
            SearchNode curSN = answerLastNode;
            while (curSN != null) {
                solutionSequence.push(curSN.board);
                curSN = curSN.prev;
            }
        }

        public boolean hasNext() {
            return (!solutionSequence.isEmpty());
        }

        public Board next() {
            return solutionSequence.pop();
        }

        public void remove() {
        }
    }


    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}