package br.winstate.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IdleCheckerUtil {

    public static int activeTime;
    public static int idleTime;
    public static int threadCheckTime;


    public IdleCheckerUtil(){
        activeTime = Integer.parseInt(config().getProperty("activeTime","20000"));
        idleTime = Integer.parseInt(config().getProperty("idleTime","5000"));
        threadCheckTime = Integer.parseInt(config().getProperty("threadCheckTime", "2000"));
    }

    public Properties config(){

        String resourceName = "idlechecker.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }

}
