import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

public class Pavage extends Solver {

    private int height;
    private int width;
    private boolean[][] board;
    private JLabel[][] pavageLabels;
    private int pieceCount;
    private SortedSet<String> possibilities;
    private LinkedList<Piece> pieces;
    private LinkedList<Coordinate> pavage;
    private Color[] colors = {Color.CYAN, Color.GREEN, Color.BLUE, Color.DARK_GRAY, Color.MAGENTA, Color.GRAY, Color.ORANGE, Color.LIGHT_GRAY, Color.PINK, Color.BLACK, Color.RED, Color.YELLOW};

    private class Coordinate {

        public int x;
        public int y;

        public Coordinate(int _x, int _y) {
            x = _x;
            y = _y;
        }

    }

    private class Position {

        public int index; // piece number
        public int[] positions; // index of every position on the board

        public Position(int _index, int x, int y, int[][] offset) {
            index = _index;
            positions = new int[offset.length];
            for (int i = 0; i < offset.length; i++) {
                int _i = 0;
                for (Coordinate coordinate : pavage) {
                    if (coordinate.x == x + offset[i][0] && coordinate.y == y + offset[i][1]) {
                        positions[i] = _i;
                        break;
                    }
                    _i++;
                }
            }
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
                        pavage.add(new Coordinate(j, i));
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
        possibilities = new TreeSet<>(); // try to eliminate redundancy
        pieces.forEach(piece -> possibilities.addAll(piece.TestPossibilities()));
        secondaries = 0; // cover all square
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

    @Override
    protected void PrintResult(LinkedList<Node> result) {
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
            for (int i = pavage.size(); i < column; i++) // find index
                if (matrix[r][i]) {
                    index = i - pavage.size();
                    break;
                }
            for (int i = 0; i < pavage.size(); i++) // find position
                if (matrix[r][i]) {
                    Coordinate pos = pavage.get(i);
                    resultBoard[pos.x][pos.y] += index; // mark the square with a symbol
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
    protected void ShowResult() {
        JFrame window = new JFrame("Pavage Result Viewer");
        int windowWidth = 600;
        int windowHeight = (height / 9.0f > width / 16.0f) ? 700 : windowWidth * height / width + 80;
        window.setBounds(100, 50, windowWidth, windowHeight);
        window.setResizable(true);
        window.setLayout(new FlowLayout());

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setMinimumSize(new Dimension(windowWidth, 60));
        controlPanel.setPreferredSize(new Dimension(windowWidth, 60));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(25, 15, 25, 10));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;

        JLabel resultsLabel = new JLabel();
        switch (results.size()) {
            case 0:
                resultsLabel.setText("No solution!");
                break;
            case 1:
                resultsLabel.setText("There is only 1 solution.");
                break;
            default:
                resultsLabel.setText(String.format("There are %d solutions.", results.size()));
        }
        resultsLabel.setMinimumSize(new Dimension(550, 25));
        resultsLabel.setPreferredSize(new Dimension(550, 25));
        controlPanel.add(resultsLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0.6;

        JLabel chooseLabel = new JLabel(String.format("Please enter a number between 1 and %d:", results.size()));
        chooseLabel.setMinimumSize(new Dimension(300, 18));
        chooseLabel.setPreferredSize(new Dimension(300, 18));
        controlPanel.add(chooseLabel, constraints);

        constraints.gridx = 1;
        constraints.insets = new Insets(4, 20, 0, 20);

        JTextArea chooseText = new JTextArea("1");
        chooseText.setMinimumSize(new Dimension(40, 18));
        chooseText.setPreferredSize(new Dimension(40, 18));
        controlPanel.add(chooseText, constraints);

        constraints.gridx = 2;
        constraints.insets = new Insets(2, 20, 0, 20);

        JButton submit = new JButton("OK");
        submit.setMinimumSize(new Dimension(40, 25));
        submit.setPreferredSize(new Dimension(40, 25));
        if (results.size() > 0)
            submit.setEnabled(true);
        else {
            submit.setEnabled(false);
            chooseLabel.setText("Nothing to show.");
        }
        submit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = 0;
                try {
                    index = Integer.parseInt(chooseText.getText());
                    index--;
                    if (index < 0 || index >= results.size())
                        throw new ArrayIndexOutOfBoundsException();
                } catch (Exception error) {
                    error.printStackTrace();
                }
                LinkedList<Node> result = results.get(index);
                result.forEach(node -> {
                    int r = node.N - 1;
                    int _index = -1;
                    for (int i = pavage.size(); i < column; i++)
                        if (matrix[r][i]) {
                            _index = i - pavage.size();
                            break;
                        }
                    for (int i = 0; i < pavage.size(); i++)
                        if (matrix[r][i]) {
                            Coordinate pos = pavage.get(i);
                            pavageLabels[pos.x][pos.y].setBackground(colors[_index % 12]); // set different color
                        }
                });
            }
        });
        controlPanel.add(submit, constraints);

        JPanel boardPanel = new JPanel(new GridLayout(height, width));
        int pavageDimension;
        if (height > width) {
            boardPanel.setMinimumSize(new Dimension((windowHeight - 80) * width / height, windowHeight - 80));
            boardPanel.setPreferredSize(new Dimension((windowHeight - 80) * width / height, windowHeight - 80));
            pavageDimension = (windowHeight - 80) / height;
        }
        else {
            boardPanel.setMinimumSize(new Dimension(windowWidth, windowWidth * height / width));
            boardPanel.setPreferredSize(new Dimension(windowWidth, windowWidth * height / width));
            window.setBounds(100, 50, windowWidth, windowWidth * height / width + 120);
            pavageDimension = windowWidth / width;
        }
        controlPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 15));

        pavageLabels = new JLabel[width][height];

        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++) {
                JLabel pavageLabel = new JLabel();
                pavageLabels[i][j] = pavageLabel;
                if (!board[i][j])
                    pavageLabel.setBackground(Color.WHITE);
                pavageLabel.setOpaque(true);
                pavageLabel.setMinimumSize(new Dimension(pavageDimension, pavageDimension));
                pavageLabel.setPreferredSize(new Dimension(pavageDimension, pavageDimension));
                boardPanel.add(pavageLabel);
            }

        window.add(controlPanel);
        window.add(boardPanel);
        window.setVisible(true);
    }

}
