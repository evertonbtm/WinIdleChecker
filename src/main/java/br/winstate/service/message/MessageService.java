package br.winstate.service.message;

import br.winstate.util.KafkaConfigUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MessageService {

    public void publish(String state) throws ExecutionException, InterruptedException {
        var producer=new KafkaProducer<String, String>(KafkaConfigUtil.config());
        var key="state";
        var record= new ProducerRecord<>(KafkaConfigUtil.topic, key, state);
        Callback callback=(data, ex)->{
            if(ex!=null){
                ex.printStackTrace();
                return;
            }
            System.out.println("Message send to: "+data.topic()+" | partition "+data.partition()+"| offset "+data.offset()+"| time "+data.timestamp());
        };
        producer.send(record,callback).get();
    }

    public void retrieve(String topic) throws ExecutionException, InterruptedException {
        var consumer = new KafkaConsumer<String, String>(KafkaConfigUtil.config());
        consumer.subscribe(Collections.singletonList(topic));

        var records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            System.out.println("------------------------------------------");
            System.out.println("Message received");
            System.out.println(record.key());
            System.out.println(record.value());

            System.out.println("Message processed");
        }
    }

}
