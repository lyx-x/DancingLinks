package solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Sudoku extends Solver {

    int dimBoard; // board dimension
    int dim = 0; // small box dimension
    int[][] board;

    /*
        There are dimBoard^2 possible positions with different row, column and box
        Every blank can contain up to dimBoard different numbers
     */

    class Position {

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

    public Sudoku(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void PrintResult() {
        results.forEach(result -> {
            System.out.println("Sudoku Result:");
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
        });
    }


}
