package Authentication;


import Enums.UserProfiles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AuthSystem {
    private final String filePath;
    private final Gson gson;
    private static final Type USER_MAP_TYPE = new TypeToken<HashMap<String, User>>(){}.getType(); // this line is to tell Gson what kinda data were saving
    //in the file (por exemplo neste caso as keys sao strings (user ids) e os valores são os users
    //Se não fizéssemos isto assim, o tipo do hashmap ia ficar genérico e não ia ser string:User

    public AuthSystem() {
        // Create a 'data' directory in the project root
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Set the file path to be in the data directory
        this.filePath = "data" + File.separator + "users.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                // Make sure the file's parent directory exists
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs(); //make the directory if it doesnt exists
                }

                Map<String, User> emptyMap = new HashMap<>(); //create user hashmap
                saveUsers(emptyMap); //save it(if the file didnt exist, it saves an empty map)
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Map<String, User> loadUsers() throws IOException {
        try (Reader reader = new FileReader(filePath)) {
            Map<String, User> users = gson.fromJson(reader, USER_MAP_TYPE); //vai buscar os users ao file, TENDO EM ONTA O TIPO QUE CRIÁMSO
            return users != null ? users : new HashMap<>();
        }
    }

    private void saveUsers(Map<String, User> users) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(users, writer);//escreve os users para file
        }
    }

    public synchronized RegisterResult register(String usernameId, String name, UserProfiles profile, String password) {
        User newUser = null;
        try {
            Map<String, User> users = loadUsers(); //vamos buscar os users existentes

            if (users.containsKey(usernameId)) {
                return new RegisterResult(false, "User ID already exists", null);
            } //se já existir esse ID, insucesso
            newUser = new User(usernameId,name, profile, password);
            users.put(usernameId, newUser); //insere o novo user no hashmap
            saveUsers(users); //guarda o hashmap de novo (OVERWRITE DO FILE)
            return new RegisterResult(true, "Registration successful", newUser);

        } catch (IOException e) {
            e.printStackTrace();
            return new RegisterResult(false, "Error during registration", newUser);
        }
    }

        public synchronized LoginResult login(String userId, String password) {
        try {
            Map<String, User> users = loadUsers(); //vamos buscar os users
            User user = users.get(userId); //buscar o user que está a tentar dar login

            if (user == null) {
                return new LoginResult(false, null);
            }

            if (user.getPassword().equals(password)) {
                return new LoginResult(true, user);
            }

            return new LoginResult(false, null);

        } catch (IOException e) {
            e.printStackTrace();
            return new LoginResult(false, null);
        }
    }

    public UserProfiles getUserProfile(String userId) { //para ir bucar o profile do user, might be useful later para entrar em channels(?)
        try {
            Map<String, User> users = loadUsers();
            User user = users.get(userId);
            return user != null ? user.getProfile() : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}