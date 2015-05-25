import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Pavage extends Solver {

    private int height;
    private int width;
    private boolean[][] board;
    private int pieceCount;
    private LinkedList<Position> possibilities;
    private LinkedList<Piece> pieces;

    private class Position {

        public int index; // piece number
        public int[] positions; // index of every position on the board

        public Position(int _index, int x, int y, int[][] offset) {
            index = _index;
            positions = new int[offset.length];
            for (int i = 0; i < offset.length; i++) {
                positions[i] = x + offset[i][0] + (y + offset[i][1]) * width;
            }
        }

    }

    private class Piece {

        private int index;
        private int size;
        private int[][][][] offset;

        public Piece(int _h, int _w, String[] _data, int _index) {
            int h = _h;
            int w = _w;
            index = _index;
            size = 0;
            LinkedList<Integer> xOffset = new LinkedList<>();
            LinkedList<Integer> yOffset = new LinkedList<>();
            for (int y = 0; y < h; y++)
                for (int x = 0; x < w; x++)
                    if (_data[y].charAt(x) == '*') {
                        size++;
                        xOffset.add(x);
                        yOffset.add(y);
                    }
            offset = new int[2][4][size][2]; // flip, rotation, size, offset (x, y)
            for (int i = 0; i < size; i++) {
                int x = xOffset.pollFirst();
                int y = yOffset.pollFirst();
                offset[0][0][i][0] = x; // rotate
                offset[0][0][i][1] = y;
                offset[0][1][i][0] = -y;
                offset[0][1][i][1] = x;
                offset[0][2][i][0] = -x;
                offset[0][2][i][1] = -y;
                offset[0][3][i][0] = y;
                offset[0][3][i][1] = -x;
                for (int j = 0; j < 4; j++) {
                    offset[1][j][i][0] = offset[0][j][i][1]; // flip
                    offset[1][j][i][1] = offset[0][j][i][0];
                }
            }
        }

        private boolean HaveSpace(int x, int y, int flip, int rotation) {
            for (int i = 0; i < size; i++) {
                int _x = offset[flip][rotation][i][0] + x;
                int _y = offset[flip][rotation][i][1] + y;
                if (_x < 0 || _x >= width || _y < 0 || _y >= height)
                    return false;
            }
            return true;
        }

        public LinkedList<Position> TestPossibilities() {
            LinkedList<Position> possibilities = new LinkedList<>();
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    for (int flip = 0; flip < 2; flip++)
                        for (int rotation = 0; rotation < 4; rotation++)
                            if (HaveSpace(x, y, flip, rotation))
                                possibilities.add(new Position(index, x, y, offset[flip][rotation]));
            return possibilities;
        }

    }

    public Pavage(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                height = Integer.parseInt(reader.readLine());
                width = Integer.parseInt(reader.readLine());
                board = new boolean[height][width];
                for (int i = 0; i < height; i++) {
                    String tmp = reader.readLine();
                    for (int j = 0; j < width; j++)
                        if (tmp.charAt(j) == '*')
                            board[i][j] = true;
                }
                pieceCount = Integer.parseInt(reader.readLine());
                pieces = new LinkedList<>();
                for (int i = 0; i < pieceCount; i++) {
                    int w = Integer.parseInt(reader.readLine());
                    int h = Integer.parseInt(reader.readLine());
                    String[] _data = new String[h];
                    for (int j = 0; j < h; j++)
                        _data[j] = reader.readLine();
                    pieces.add(new Piece(h, w, _data, i));
                }
                MakeEMC();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void MakeEMC() {
        possibilities = new LinkedList<>();
        pieces.forEach(piece -> possibilities.addAll(piece.TestPossibilities()));
        secondaries = 0;
        row = possibilities.size();
        column = pieceCount + height * width;
        matrix = new boolean[row][column];
        for (int i = 0; i < row; i++) {
            Position position = possibilities.pollFirst();
            int index = position.index;
            matrix[i][index] = true;
            for (int j : position.positions) {
                matrix[i][pieceCount + j] = true;
            }
        }
    }

    @Override
    protected void PrintResult() {

    }

}
