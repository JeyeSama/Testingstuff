import java.io.Serializable;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class UserManager implements Serializable {
    private static UserManager instance;
    private HashMap<String, String> userCredentials;
    private HashMap<String, users.UserGameCatalog> userCatalogs;

    private UserManager() {
        userCredentials = new HashMap<>();
        userCatalogs = new HashMap<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean addUser(String username, String password) {
        if (!userCredentials.containsKey(username)) {
            userCredentials.put(username, password);
            userCatalogs.put(username, new UserGameCatalog());
            return true;
        }
        return false;
    }

    public boolean login(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    public UserGameCatalog getUserGameCatalog(String username) {
        return userCatalogs.get(username);
    }

    public boolean addGameToUserCatalog(String username, Game game) {
        UserGameCatalog userCatalog = userCatalogs.get(username);
        return userCatalog.addGame(game);
    }

    public boolean removeGameFromUserCatalog(String username, Game game) {
        UserGameCatalog userCatalog = userCatalogs.get(username);
        return userCatalog.removeGame(game);
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("UsersData.txt"))) {
            oos.writeObject(userCredentials);
            oos.writeObject(userCatalogs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("UsersData.txt"))) {
            userCredentials = (HashMap<String, String>) ois.readObject();
            userCatalogs = (HashMap<String, UserGameCatalog>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
