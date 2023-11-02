import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import users.*;
import games.Game;

public class GameCatalogApp extends JFrame {
    private UserManager userManager;  // Actualizare: Am adăugat UserManager

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> gameComboBox;
    private JTextArea gameListTextArea;

    public GameCatalogApp() {
        userManager = UserManager.getInstance();  // Actualizare: Am adăugat inițializarea UserManager

        setTitle("Game Catalog App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Autentificare
        JPanel loginPanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(signUpButton);

        // Interfața cu jocurile și funcționalitatea de mai jos
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        gameComboBox = new JComboBox<>();
        gameComboBox.addItem("The Witcher 3");
        gameComboBox.addItem("Minecraft");
        gameComboBox.addItem("CS:GO");
        JButton addButton = new JButton("Add Game");
        JButton removeButton = new JButton("Remove Game");
        gameListTextArea = new JTextArea(10, 20);
        gameListTextArea.setEditable(false);
        mainPanel.add(gameComboBox);
        mainPanel.add(addButton);
        mainPanel.add(removeButton);
        mainPanel.add(gameListTextArea);

        add(loginPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Ascultători și funcționalitate de mai jos
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty()) {
                    if (userManager.login(username, password)) {
                        updateGameList(userManager.getUserGameCatalog(username).getGames());
                    }
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty() && !password.isEmpty()) {
                    if (userManager.addUser(username, password)) {
                        updateGameList(userManager.getUserGameCatalog(username).getGames());
                    }
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedGame = gameComboBox.getSelectedItem().toString();
                String username = usernameField.getText();
                if (userManager.addGameToUserCatalog(username, new Game(selectedGame, 0.0, "Genre"))) {
                    updateGameList(userManager.getUserGameCatalog(username).getGames());
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedGame = gameComboBox.getSelectedItem().toString();
                String username = usernameField.getText();
                if (userManager.removeGameFromUserCatalog(username, selectedGame)) {
                    updateGameList(userManager.getUserGameCatalog(username).getGames());
                }
            }
        });
    }

    private void updateGameList(ArrayList<Game> games) {
        StringBuilder gameText = new StringBuilder();
        for (Game game : games) {
            gameText.append(game).append("\n");
        }
        gameListTextArea.setText(gameText.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameCatalogApp().setVisible(true);
            }
        });
    }
}
