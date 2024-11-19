import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JFrame implements ActionListener {

    JTextField title = new JTextField("Quiz");
    JTextField question = new JTextField();
    JButton playButton = new JButton("Play");
    JButton quitButton = new JButton("Quit");
    JButton buttonA = new JButton();
    JButton buttonB = new JButton();
    JButton buttonC = new JButton();
    JButton buttonD = new JButton();


    public GamePanel() {
        setTitle("Quiz");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(new Color(0, 50, 76));


        add(title);
        title.setEditable(false);
        title.setFocusable(false);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(null);
        title.setFont(new Font("Impact", Font.BOLD, 100));
        title.setBackground(new Color(0, 50, 76));
        title.setForeground(new Color(211, 211, 211));
        title.setBounds(150, 50, 400, 100);

        add(playButton);
        add(quitButton);
        playButton.addActionListener(this);
        quitButton.addActionListener(this);

        playButton.setBounds(250, 250, 200, 100);
        playButton.setBackground(new Color(211, 211, 211));
        playButton.setFont(new Font("Impact", Font.BOLD, 30));
        playButton.setFocusable(false);

        quitButton.setBounds(250, 370, 200, 100);
        quitButton.setBackground(new Color(211, 211, 211));
        quitButton.setFont(new Font("Impact", Font.BOLD, 30));
        quitButton.setFocusable(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitButton) {
            System.exit(0);
        }
        if (e.getSource() == playButton) {
            startGame();
        }
    }

    private void startGame() {
        playButton.setVisible(false);
        quitButton.setVisible(false);

        title.setText("Question 1");
        title.setFont(new Font("Impact", Font.BOLD, 70));

        add(question);
        question.setBounds(50, 200, 600, 100);
        question.setBorder(null);
        question.setEditable(false);
        question.setHorizontalAlignment(SwingConstants.CENTER);
        question.setBackground(new Color(0, 50, 76));
        question.setForeground(new Color(211, 211, 211));
        question.setFont(new Font("Impact", Font.BOLD, 30));
        question.setFocusable(false);
        question.setText("Is this a test question?");

        add(buttonA);
        add(buttonB);
        add(buttonC);
        add(buttonD);
        buttonA.addActionListener(this);
        buttonB.addActionListener(this);
        buttonC.addActionListener(this);
        buttonD.addActionListener(this);

        buttonA.setBounds(45, 350, 300, 100);
        buttonA.setBackground(new Color(211, 211, 211));
        buttonA.setFont(new Font("Impact", Font.BOLD, 30));
        buttonA.setText("Test A");
        buttonA.setFocusable(false);

        buttonB.setBounds(355, 350, 300, 100);
        buttonB.setBackground(new Color(211, 211, 211));
        buttonB.setFont(new Font("Impact", Font.BOLD, 30));
        buttonB.setText("Test B");
        buttonB.setFocusable(false);

        buttonC.setBounds(45, 470, 300, 100);
        buttonC.setBackground(new Color(211, 211, 211));
        buttonC.setFont(new Font("Impact", Font.BOLD, 30));
        buttonC.setText("Test C");
        buttonC.setFocusable(false);

        buttonD.setBounds(355, 470, 300, 100);
        buttonD.setBackground(new Color(211, 211, 211));
        buttonD.setFont(new Font("Impact", Font.BOLD, 30));
        buttonD.setText("Test D");
        buttonD.setFocusable(false);

    }
}
