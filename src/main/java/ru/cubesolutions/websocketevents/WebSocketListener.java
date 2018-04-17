package ru.cubesolutions.websocketevents;

import org.apache.log4j.Logger;
import ru.cubesolutions.rabbitmq.Producer;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Garya on 23.12.2017.
 */
@ClientEndpoint
public class WebSocketListener {

    private final static Logger log = Logger.getLogger(WebSocketListener.class);

    private static final Object waitLock = new Object();

    private Session session;
    private Producer rabbitMqSender;

    public WebSocketListener(String url) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(url));
            wait4TerminateSignal();
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException("Can't connect to ws by url: " + url, ex);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        this.rabbitMqSender = new Producer(Config.RABBIT_CONFIG);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Received message: " + message);
        System.out.println("Received message: " + message);
        try {
            rabbitMqSender.sendMessage(message, Config.EXCHANGE, Config.ROUTING_KEY);
        } catch (IOException e) {
            log.error(e);
        }
    }

    private static void wait4TerminateSignal() {
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }
}