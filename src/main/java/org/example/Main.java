package org.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldConsumer(), false);
        Thread.sleep(1000);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldProducer(), false);
        Thread.sleep(1000);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldProducer(), false);
        Thread.sleep(1000);
        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldProducer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldConsumer(), false);
        startThread(new HelloWorldProducer(), false);
    }
    public static void startThread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {
        public void run() {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.QUEUE");

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                // Create a messages
                String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);

                // Tell the producer to send the message
                System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);

                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
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

                // Wait for a message
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