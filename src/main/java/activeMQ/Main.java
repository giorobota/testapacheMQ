package activeMQ;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException, JMSException {
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            boolean shouldCreateProducer = random.nextBoolean();
            if (shouldCreateProducer) {
                startThread(new TestMessageProducer(), false);
            } else {
                startThread(new TestMessageConsumer(), false);
            }
            if (i % 4 == 0) Thread.sleep(1000);
        }

//        TestMessageProducerWithListener producer = new TestMessageProducerWithListener();
//        for (int i = 0; i < 25; i++) {
//            startThread(new TestMessageSender(producer), false);
//
//            if (i % 4 == 0) Thread.sleep(1000);
//        }
    }

    public static void startThread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class TestMessageSender implements Runnable{
        private TestMessageProducerWithListener producer;
        public TestMessageSender(TestMessageProducerWithListener producer){
            this.producer = producer;
        }
        @Override
        public void run() {
            String text = "Hello world From Thread: " + Thread.currentThread().getName() + " : " + this.hashCode();

            try {
                producer.sendMessage(text);

            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }
}