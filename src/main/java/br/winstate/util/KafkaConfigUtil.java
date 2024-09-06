package br.winstate.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaConfigUtil {

    public static String topic;

    public static Properties config(){

        String resourceName = "kafka.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
            topic =  MachineUtil.getComputerName()+props.getProperty("topic.suffix");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return props;
    }

}
