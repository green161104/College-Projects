    package Server;

    import Authentication.UserInfo;

    import java.io.*;
    import java.net.InetAddress;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Objects;

    public class UDPGroupChat {
        private String logFile; // Optional, for logging group activity
        private List<UserInfo> loggedUsers; // List of users in this group
        private int groupCode; // Group ID (0-3: general, low, medium, high)
        private InetAddress groupAddress; // Multicast address (if needed)

        public UDPGroupChat(int groupCode, InetAddress groupAddress) {
            this.groupCode = groupCode;
            this.groupAddress = groupAddress;
            this.loggedUsers = new ArrayList<>();
            this.logFile = "group_" + groupCode + "_log.txt"; // Example log filename
        }

        // Add a user to the group
        public boolean addUser(UserInfo newClient) {
            if (loggedUsers.contains(newClient)) {
                return false;  // User already exists
            }
            loggedUsers.add(newClient);
            return true;
        }

        public  boolean containsUser(UserInfo user){
            if(loggedUsers.contains(user)){
                return true;
            }
            return false;
        }

        // Remove a user from the group
        public boolean removeUser(UserInfo user) {
            return loggedUsers.remove(user);
        }

        // Get logged users
        public List<UserInfo> getLoggedUsers() {
            return loggedUsers;
        }

        public int getGroupCode() {
            return groupCode;
        }

        public InetAddress getGroupAddress() {
            return groupAddress;
        }

        public String getLogFile() {
            return logFile;
        }

        public void loadLogFile(){

            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                List<String> logEntries = new ArrayList<>();

                // Read all lines from the log file
                while ((line = reader.readLine()) != null) {
                    logEntries.add(line);
                }

                // Optional: You could do something with the log entries here
                // For example, print them or process them further
                for (String entry : logEntries) {
                    System.out.println("Log Entry: " + entry);
                }

            } catch (IOException e) {
                System.err.println("Error reading log file: " + e.getMessage());
            }
        }


        // Write a message to the log file
        public void writeMessageToLog(String sender, String message) {
            String logEntry = String.format("[%s] %s: %s", java.time.LocalDateTime.now(), sender, message);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(logEntry);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Failed to write to log file: " + e.getMessage());
            }
        }
    }
