import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.LinkedList;

public class Queens extends Solver {

    private int dimBoard;
    private JLabel[][] numberLabels;

    public Queens() {
        this(new InputStreamReader(System.in));
    }

    public Queens(String file) throws FileNotFoundException {
        this(new FileReader(file));
    }
    public Queens(Reader stream){
        BufferedReader reader = new BufferedReader(stream);
        try {
            dimBoard = Integer.parseInt(reader.readLine());
            row = dimBoard * dimBoard;
            column = dimBoard * 6 - 2;
            matrix = new boolean[row][column];
            secondaries = column - 4 * dimBoard;
            for(int i = 0; i < dimBoard; i++){
                for(int j = 0; j < dimBoard; j++){
                    matrix[i * dimBoard + j][i] = true;
                    matrix[i * dimBoard + j][dimBoard + j] = true;
                    matrix[i * dimBoard + j][2 * dimBoard + i - j + dimBoard - 1] = true;
                    matrix[i * dimBoard + j][4 * dimBoard - 1 + i + j] = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void PrintResult(LinkedList<Node> result){
        result.forEach(node -> System.out.format("%d %d\n", (node.N - 1) / dimBoard, (node.N - 1) % dimBoard));
    }

    @Override
    protected void ShowResult() {
        JFrame window = new JFrame("Queens Result Viewer");
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
                for(int i = 0; i < dimBoard; i++)
                    for(int j = 0; j < dimBoard; j++)
                        numberLabels[i][j].setText("");
                result.forEach(node -> {
                    int i = node.N - 1;
                    numberLabels[i / dimBoard][i % dimBoard].setText("Q");
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
                numberLabel.setBackground(Color.getHSBColor(0.3f, 0.2f, 1f));
                numberLabel.setForeground(Color.getHSBColor(1f, 0.7f, 0.4f));
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