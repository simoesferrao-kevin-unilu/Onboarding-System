package lu.uni.database;

import lu.uni.user.Client;
import lu.uni.user.User;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.*;

public class Database {

    private Map<String, Client> clients;
    private static final Logger logger = LogManager.getLogger(Database.class);

    public Database() {
        this.clients = new HashMap<>();
    }

    public void saveClientData(Client client) {
        String encryptedId = encryptData(client.getId());
        clients.put(encryptedId, client);
        log("Client data saved for: " + client.getId());
    }

    public Client retrieveClientData(String clientId) {
        String encryptedId = encryptData(clientId);
        return clients.get(encryptedId);
    }

    public void log(String activity) {
        logger.info(activity);
    }

    public boolean checkAccess(User user) {
        return user.getId().length() > 5;
    }

    public String encryptData(String data) {
        return new StringBuilder(data).reverse().toString();
    }

    public String decryptData(String data) {
        return new StringBuilder(data).reverse().toString();
    }
}