package activeMQ;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TestMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = null;
            try {
                text = textMessage.getText();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Received: " + text + "by " + this.getClass().getSimpleName());
        } else {
            System.out.println("Received: " + message);
        }
    }
}
