package tnt.z_pocs;


import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;


public class ActiveMQMainConsumer {
    static String username = "artemis";
    static String password = "artemis";
    static String queueName = "exampleQueue";
    public static void main(String[] args) {
        // ActiveMQ Artemis Verbindung herstellen
        try (ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
             Connection connection = factory.createConnection(username, password)) {

            // Verbindung starten
            connection.start();

            // Eine Session ohne Transaktionen und mit auto-acknowledge
            Session session1 = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Session session2 = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Eine Queue erstellen (oder verwenden, falls sie schon existiert)
            Queue queue = session1.createQueue(queueName);

            // Consumer 1: QueueBrowser erstellen
            QueueBrowser browser1 = session1.createBrowser(queue);

            // Consumer 2: Ein weiterer QueueBrowser
            QueueBrowser browser2 = session2.createBrowser(queue);

            // Consumer 1: Polling Loop
            new Thread(() -> {
                try {
                    while (true) {
                        Enumeration<?> messages1 = browser1.getEnumeration();
                        if (messages1.hasMoreElements()) {
                            while (messages1.hasMoreElements()) {
                                Message message = (Message) messages1.nextElement();
                                if (message instanceof TextMessage) {
                                    System.out.println("Browser 1 durchsucht: " + ((TextMessage) message).getText());
                                }
                            }
                        } else {
                            System.out.println("Browser 1: Keine neuen Nachrichten.");
                        }
                        Thread.sleep(5000);  // 5 Sekunden warten
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Consumer 2: Polling Loop
            new Thread(() -> {
                try {
                    while (true) {
                        Enumeration<?> messages2 = browser2.getEnumeration();
                        if (messages2.hasMoreElements()) {
                            while (messages2.hasMoreElements()) {
                                Message message = (Message) messages2.nextElement();
                                if (message instanceof TextMessage) {
                                    System.out.println("Browser 2 durchsucht: " + ((TextMessage) message).getText());
                                }
                            }
                        } else {
                            System.out.println("Browser 2: Keine neuen Nachrichten.");
                        }
                        Thread.sleep(5000);  // 5 Sekunden warten
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}