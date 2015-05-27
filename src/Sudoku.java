import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.LinkedList;

public class Sudoku extends Solver {

    private int dimBoard; // board dimension
    private int dim = 0; // small box dimension
    private int[][] board;
    private JLabel[][] numberLabels;

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
        JFrame window = new JFrame("Sudoku Result Viewer");
        int height = 675;
        int width = 600;
        window.setBounds(100, 50, width, height);
        window.setResizable(true);
        window.setLayout(new FlowLayout());

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setMinimumSize(new Dimension(width, 55));
        controlPanel.setPreferredSize(new Dimension(width, 55));
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
        resultsLabel.setMinimumSize(new Dimension(width, 25));
        resultsLabel.setPreferredSize(new Dimension(width, 25));
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
        else
            submit.setEnabled(false);
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
                    int i = (node.N - 1) % (dimBoard * dimBoard);
                    Position pos = new Position(i);
                    int val = (node.N - 1) / (dimBoard * dimBoard); // get the number from row number
                    numberLabels[pos.row][pos.column].setText(String.valueOf(val + 1));
                });
            }
        });
        controlPanel.add(submit, constraints);

        JPanel boardPanel = new JPanel(new GridLayout(dimBoard, dimBoard));
        boardPanel.setMinimumSize(new Dimension(width, height - 75));
        boardPanel.setPreferredSize(new Dimension(width, height - 75));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 15));

        numberLabels = new JLabel[dimBoard][dimBoard];

        for (int i = 0; i < dimBoard; i++)
            for (int j = 0; j < dimBoard; j++) {
                JLabel numberLabel = new JLabel();
                numberLabels[i][j] = numberLabel;
                if (board[i][j] == -1) {
                    numberLabel.setBackground(Color.getHSBColor(0.3f, 0.2f, 1f));
                    numberLabel.setForeground(Color.getHSBColor(1f, 0.7f, 0.4f));
                }
                else {
                    numberLabel.setText(String.valueOf(board[i][j] + 1));
                    numberLabel.setBackground(Color.LIGHT_GRAY);
                }
                numberLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20 * 16 / dimBoard));
                numberLabel.setOpaque(true);
                if (i == 0) {
                    if (j == 0)
                        numberLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
                    else
                        numberLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.DARK_GRAY));
                }
                else {
                    if (j == 0)
                        numberLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.DARK_GRAY));
                    else
                        numberLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.DARK_GRAY));
                }
                numberLabel.setHorizontalAlignment(JLabel.CENTER);
                numberLabel.setVerticalAlignment(JLabel.CENTER);
                numberLabel.setMinimumSize(new Dimension(width / dimBoard, width / dimBoard));
                numberLabel.setPreferredSize(new Dimension(width / dimBoard, width / dimBoard));
                boardPanel.add(numberLabel);
            }

        window.add(controlPanel);
        window.add(boardPanel);
        window.setVisible(true);
    }

}