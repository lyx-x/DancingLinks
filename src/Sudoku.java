import java.io.*;
import java.util.LinkedList;

public class Sudoku extends Solver {

    private int dimBoard; // board dimension
    private int dim = 0; // small box dimension
    private int[][] board;

    /*
        There are dimBoard^2 possible positions with different row, column and box
        Every blank can contain up to dimBoard different numbers
     */

    private class Position {

        int row, column, box;
        int index;

        public Position(int _index) {
            index = _index;
            row = index / dimBoard;
            column = index % dimBoard;
            box = (row / dim) * dim + (column / dim);
        }

        public void SetMatrix(int val) {
            int _row = val * dimBoard * dimBoard + index; // different number starts at different rows
            matrix[_row][index] = true;
            int _column = dimBoard * dimBoard + val * dimBoard * 3; // different number starts at different columns
            matrix[_row][_column + row] = true;
            matrix[_row][_column + dimBoard + column] = true;
            matrix[_row][_column + dimBoard * 2 + box] = true;
        }

    }

    public Sudoku() {
        this(new InputStreamReader(System.in));
    }

    public Sudoku(String file) throws FileNotFoundException {
        this(new FileReader(file));
    }

    public Sudoku(Reader stream) {
        BufferedReader reader = new BufferedReader(stream);
        try {
            dimBoard = Integer.parseInt(reader.readLine());
            while (dim * dim < dimBoard)
                dim++;
            board = new int[dimBoard][dimBoard];
            for (int i = 0; i < dimBoard; i++) {
                String[] tmp = reader.readLine().split(" ");
                for (int j = 0; j < dimBoard; j++)
                    if (tmp[j].equals("*"))
                        board[i][j] = -1;
                    else
                        board[i][j] = Integer.parseInt(tmp[j]);
            }
            MakeEMC();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void MakeEMC() {
        secondaries = 0;
        row = dimBoard * dimBoard * dimBoard;
        column = dimBoard * dimBoard * 4; // dimBoard ^ 2 + dimBoard * 3 * dimBoard
        matrix = new boolean[row][column];
        for (int i = 0; i < dimBoard * dimBoard; i++) {
            Position pos = new Position(i);
            if (board[pos.row][pos.column] == -1) // there are dimBoard possibilities
                for (int j = 0; j < dimBoard; j++)
                    pos.SetMatrix(j);
            else
                pos.SetMatrix(board[pos.row][pos.column]); // there is only one possibility
        }
    }

    private void PrintResult(LinkedList<Node> result) {
        result.forEach(node -> {
            int index = (node.N - 1) % (dimBoard * dimBoard);
            Position pos = new Position(index);
            int val = (node.N - 1) / (dimBoard * dimBoard); // get the number from row number
            board[pos.row][pos.column] = val;
        });
        for (int i = 0; i < dimBoard; i++) {
            for (int j = 0; j < dimBoard; j++) {
                System.out.print(board[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void PrintAllResult() {
        System.out.println("Sudoku Result:");
        results.forEach(this::PrintResult);
    }

    @Override
    protected void PrintOneResult(int i) {
        System.out.println("Sudoku Result 1:");
        PrintResult(results.get(i));
    }

    @Override
    protected void ShowResult() {

    }

}