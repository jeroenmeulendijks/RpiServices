package nl.meul.rpiservices;

public class Couchpotato extends Service {

    /** Get the status of the service. Ex. running or stopped
     * @return String with the status of the service
     */
    @Override
    public String getStatus() {
        return Service.runCommand("sudo service couchpotato status");
    }

    /** Start the service
     */
    @Override
    public void start() {
        Service.runCommand("sudo start couchpotato");
    }

    /** Stop the service
     */
    @Override
    public void stop() {
        Service.runCommand("sudo stop couchpotato");
    }

    @Override
    public String getDescription() {
        return "Couchpotato";
    }
}
