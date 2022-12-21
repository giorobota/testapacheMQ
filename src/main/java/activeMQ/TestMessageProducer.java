package activeMQ;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class TestMessageProducer implements Runnable {
    public void run() {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("TEST.QUEUE");

            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            String text = "Hello world From Thread: " + Thread.currentThread().getName() + " : " + this.hashCode();
            TextMessage message = session.createTextMessage(text);

            System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);

            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}