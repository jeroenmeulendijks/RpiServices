package nl.meul.rpiservices;

import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

public abstract class Service {

    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());

    /** Start the service
     */
    abstract void start();

    /** Stop the service
     */
    abstract void stop();

    /** Get the status of the service. Ex. running or stopped
     * @return String with the status of the service
     */
    abstract String getStatus();

    /** Get the description of the service. Ex. running or stopped
     * @return String with the description of the service
     */
    abstract String getDescription();

    /** Get status of the service
     * @param command Command to execute to retrieve the status
     * @return String with the status of the service
     */
    protected static String runCommand(String command) {
        String result = "";

        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);
            p.waitFor();
            try (BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;

                while ((line = b.readLine()) != null) {
                    result = line;
                }
            }
        }
        catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Failed to get status: {0}", e.getMessage());
        }

        return result;
    }
}
