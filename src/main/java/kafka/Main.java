package kafka;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.pulsar.shade.org.apache.commons.lang3.RandomUtils;

import javax.jms.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 25; i++) {
            int randomBoolean = RandomUtils.nextInt(0, 1);
            if (randomBoolean == 0) {
                startThread(new HelloWorldProducer(), false);
            } else {
                startThread(new HelloWorldConsumer(), false);
            }
            if (i % 4 == 0) Thread.sleep(1000);
        }
    }

    public static void startThread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {
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

    public static class HelloWorldConsumer implements Runnable, ExceptionListener {
        public void run() {
            try {
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                Connection connection = connectionFactory.createConnection();
                connection.start();

                connection.setExceptionListener(this);

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination destination = session.createQueue("TEST.QUEUE");

                MessageConsumer consumer = session.createConsumer(destination);

                Message message = consumer.receive(1000);
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + message);
                }
                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void onException(JMSException ex) {
            System.out.println(ex.getMessage());
        }
    }
}