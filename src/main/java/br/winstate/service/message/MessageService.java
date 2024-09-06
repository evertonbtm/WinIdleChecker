package br.winstate.service.message;

import br.winstate.util.KafkaConfigUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MessageService {

    public void publish(String state) throws ExecutionException, InterruptedException {
        var producer=new KafkaProducer<String, String>(KafkaConfigUtil.getKafkaConfiguration());
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
        producer.close();
    }

    public void retrieve(String topic) throws ExecutionException, InterruptedException {

        final KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(KafkaConfigUtil.getKafkaConfiguration());
        consumer.subscribe(Collections.singletonList(topic));
        ConsumerRecords<Integer, String> records = consumer.poll(10000);

        System.out.println("size of records polled is "+ records.count());
        for (ConsumerRecord<Integer, String> record : records) {
            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
        }

        consumer.commitSync();
        consumer.close();
    }

}
