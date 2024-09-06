package br.winstate.util;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaConfigUtil {

    public static String topic;

    public static Properties getKafkaConfiguration(){

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

    public static Properties getStreamsConfiguration() {
        return getStreamsConfiguration(getKafkaConfiguration().getProperty("bootstrap.servers"));
    }

    public static Properties getStreamsConfiguration(final String bootstrapServers) {
        final Properties streamsConfiguration = new Properties();
        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
        // against which the application is run.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "win-idle-checker");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "win-idle-checker-client");
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Specify default (de)serializers for record keys and for record values.
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        // Records should be flushed every 10 seconds. This is less than the default
        // in order to keep this example interactive.
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        // For illustrative purposes we disable record caches.
        streamsConfiguration.put(StreamsConfig.STATESTORE_CACHE_MAX_BYTES_CONFIG, 0);
        // Use a temporary directory for storing state, which will be automatically removed after the test.
        //streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath());
        return streamsConfiguration;
    }

}
