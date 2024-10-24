package tnt.z_pocs;


import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQMain {
    public static void main(String[] args) throws Exception {
        String brokerUrl = "tcp://localhost:61616"; // Broker URL anpassen
        String username = "artemis";
        String password = "artemis";
        String queueName = "exampleQueue";

        // JMS Verbindungsfactory erstellen
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

            Connection connection = connectionFactory.createConnection(username, password);
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue queue = session.createQueue(queueName);

            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            for (int i=1; i<=10; i++ ) {
                TextMessage message = session.createTextMessage("Message " + i);
                producer.send(message);
                System.out.println("Sent message " + i);
            }

            session.commit();
            System.out.println("Message committed");





            //JMSContext context = connectionFactory.createContext(username, password);

            /*
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
            */


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}