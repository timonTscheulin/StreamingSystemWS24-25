package tnt;


import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;

public class ActiveMQMain {
    public static void main(String[] args) throws Exception {
        String brokerUrl = "tcp://localhost:61616"; // Broker URL anpassen
        String username = "artemis";
        String password = "artemis";
        String queueName = "exampleQueue";

        // JMS Verbindungsfactory erstellen
        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
             JMSContext context = connectionFactory.createContext(username, password)) {

            // Queue erstellen
            Queue queue = context.createQueue(queueName);

            // Produzent erstellen und Nachricht senden
            JMSProducer producer = context.createProducer();
            String messageText = "Hello from Artemis!";
            producer.send(queue, messageText);
            System.out.println("Nachricht gesendet: " + messageText);

            // Konsument erstellen und Nachricht empfangen
            JMSConsumer consumer = context.createConsumer(queue);
            String receivedMessage = consumer.receiveBody(String.class, 5000);
            System.out.println("Nachricht empfangen: " + receivedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {
        public void run() {
            try {
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
                connectionFactory.setUser("artemis");
                connectionFactory.setPassword("artemis");

                Connection connection = connectionFactory.createConnection();
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination destination = session.createQueue("timon");

                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);

                String text = "Hello World! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);

                System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());

                session.close();
                connection.close();
            } catch( Exception e ) {
                System.out.println( e );
                e.printStackTrace();
            }
        }
    }

    public static class HelloWorldConsumer implements Runnable, ExceptionListener {
        public void run() {
            try{
                ActiveMQJMSConnectionFactory connectionFactory = new ActiveMQJMSConnectionFactory();
                connectionFactory.setUser("artemis");
                connectionFactory.setPassword("artemis");

                Connection connection = connectionFactory.createConnection();
                connection.start();

                connection.setExceptionListener(this);

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination destination = session.createQueue("test");

                MessageConsumer consumer = session.createConsumer(destination);

                Message message = consumer.receive(2000);

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received text: " + text + " : " + Thread.currentThread().getName());
                } else {
                    System.out.println("Received message : " + message + " : " + Thread.currentThread().getName());
                }
            } catch ( Exception e ) {
                System.out.println( e );
                e.printStackTrace();
            }
        }

        public synchronized void onException(JMSException ex) {
            System.out.println( "JMS Exception occurred. Shutting down client.");
        }
    }
}