package activeMQ;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TestMessageProducerWithListener {

    private MessageProducer producer;
    private final Connection connection;

    private final Session session;

    public TestMessageProducerWithListener() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

        connection = connectionFactory.createConnection();
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue("TEST.QUEUE");
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new TestMessageListener());
    }

    public void sendMessage(String text) throws JMSException {
        TextMessage message = session.createTextMessage(text);

        System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);
    }

    public void close() throws JMSException {
        connection.close();
    }
}
