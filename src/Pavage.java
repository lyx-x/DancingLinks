import javafx.util.Pair;
import java.io.*;
import java.util.*;

public class Pavage extends Solver {

    private int height;
    private int width;
    private boolean[][] board;
    private int pieceCount;
    private SortedSet<String> possibilities;
    private LinkedList<Piece> pieces;
    private LinkedList<Pair<Integer,Integer>> pavage;

    private class Position {

        public int index; // piece number
        public int[] positions; // index of every position on the board

        public Position(int _index, int x, int y, int[][] offset) {
            index = _index;
            positions = new int[offset.length];
            for (int i = 0; i < offset.length; i++)
                positions[i] = pavage.indexOf(new Pair<>(x + offset[i][0], y + offset[i][1]));
        }

        @Override
        public String toString() {
            int[] _row = new int[pieceCount + pavage.size()];
            _row[index + pavage.size()] = 1;
            for (int i : positions)
                _row[i] = 1;
            StringBuilder sb = new StringBuilder();
            for (int i : _row)
                sb.append(i);
            return sb.toString();
        }

    }

    private class Piece {

        private int index;
        private int size;
        private int[][][][] offset;

        public Piece(int h, int w, String[] _data, int _index) {
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
                if (!board[_x][_y])
                    return false;
            }
            return true;
        }

        public SortedSet<String> TestPossibilities() {
            SortedSet<String> possibilities = new TreeSet<>();
            for (int x = 0; x < width; x++)
                for (int y = 0; y < height; y++)
                    for (int flip = 0; flip < 2; flip++)
                        for (int rotation = 0; rotation < 4; rotation++)
                            if (HaveSpace(x, y, flip, rotation))
                                possibilities.add(new Position(index, x, y, offset[flip][rotation]).toString());
            return possibilities;
        }

    }

    public Pavage() {
        this(new InputStreamReader(System.in));
    }

    public Pavage(String file) throws FileNotFoundException {
        this(new FileReader(file));
    }

    public Pavage(Reader stream) {
        BufferedReader reader = new BufferedReader(stream);
        try {
            width = Integer.parseInt(reader.readLine());
            height = Integer.parseInt(reader.readLine());
            board = new boolean[width][height];
            pavage = new LinkedList<>();
            for (int i = 0; i < height; i++) {
                String tmp = reader.readLine();
                for (int j = 0; j < width; j++)
                    if (tmp.charAt(j) == '*') {
                        board[j][i] = true;
                        pavage.add(new Pair<>(j, i));
                    }
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
    }

    private void MakeEMC() {
        possibilities = new TreeSet<>();
        pieces.forEach(piece -> possibilities.addAll(piece.TestPossibilities()));
        secondaries = 0;
        row = possibilities.size();
        column = pieceCount + pavage.size();
        matrix = new boolean[row][column];
        int i = 0;
        for (String position : possibilities) {
            for (int j = 0; j < position.length(); j++) {
                matrix[i][j] = (position.charAt(j) == '1');
            }
            i++;
        }
    }

    private void PrintResult(LinkedList<Node> result) {
        char[][] resultBoard = new char[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (board[i][j])
                    resultBoard[i][j] = 'a';
                else
                    resultBoard[i][j] = ' ';
        result.forEach(node -> {
            int r = node.N - 1;
            int index = -1;
            for (int i = pavage.size(); i < column; i++)
                if (matrix[r][i]) {
                    index = i - pavage.size();
                    break;
                }
            for (int i = 0; i < pavage.size(); i++)
                if (matrix[r][i]) {
                    Pair<Integer, Integer> pos = pavage.get(i);
                    resultBoard[pos.getKey()][pos.getValue()] += index;
                }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                sb.append(resultBoard[j][i]);
            sb.append('\n');
        }
        System.out.println(sb.toString());
    }

    @Override
    protected void PrintAllResult() {
        System.out.println("Pavage Result:");
        results.forEach(this::PrintResult);
    }

    @Override
    protected void PrintOneResult(int i) {
        System.out.println("Pavage Result 1:");
        PrintResult(results.get(i));
    }

    @Override
    protected void ShowResult() {

    }

}
