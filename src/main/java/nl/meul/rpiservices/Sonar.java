package nl.meul.rpiservices;

public class Sonar extends Service {

    /** Get the status of the service. Ex. running or stopped
     * @return String with the status of the service
     */
    @Override
    public String getStatus() {
        return Service.runCommand("sudo service nzbdrone status");
    }

    /** Start the service
     */
    @Override
    public void start() {
        Service.runCommand("sudo start nzbdrone");
    }

    /** Stop the service
     */
    @Override
    public void stop() {
        Service.runCommand("sudo stop nzbdrone");
    }

    @Override
    public String getDescription() {
        return "Sonar";
    }
}
