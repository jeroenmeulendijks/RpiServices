package nl.meul.rpiservices;

import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class ServicesManager {

    private static ServicesManager myInstance = null;

    protected ServicesManager() {
    }

    public static ServicesManager getInstance() {
        if(myInstance == null) {
            myInstance = new ServicesManager();
        }
        return myInstance;
   }

    public Set<Class<? extends Service>> getServices() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
               .setUrls(ClasspathHelper.forPackage("nl.meul.rpiservices"))
               .setScanners(new SubTypesScanner())
           );

        return reflections.getSubTypesOf(nl.meul.rpiservices.Service.class);
    }
}
