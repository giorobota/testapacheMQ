package pulsar;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MessagePublisher {

    public static void main(String[] args) throws Exception {
        String topicName = "test_topic";
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();
        ;
        Producer<Event> producer = client
                .newProducer(Schema.JSON(Event.class))
                .topic(topicName)
                .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS)
                .sendTimeout(10, TimeUnit.SECONDS)
                .blockIfQueueFull(true)
                .create();

        for (long eventCount = 0; eventCount < 1000; eventCount++) {
            String message = String.format("message-%0,4d", eventCount);
            String user = getRandomUserName();
            producer.send(new Event(user, eventCount, message));
            System.out.format("Published '%s' from '%s'", message, user);
            Thread.sleep(20);
        }
    }

    private static String getRandomUserName() {
        Random r = new Random();
        return "testUser" + r.nextInt(10);
    }
}
