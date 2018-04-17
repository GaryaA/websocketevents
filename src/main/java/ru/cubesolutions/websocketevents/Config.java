package ru.cubesolutions.websocketevents;

import org.apache.log4j.Logger;
import ru.cubesolutions.rabbitmq.RabbitConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Garya on 24.12.2017.
 */
public class Config {

    private final static Logger log = Logger.getLogger(WebSocketListener.class);
    public final static String BASE_WS_URL;
    public final static String AUTH_TOKEN;
    public final static String LIST_ID;
    public final static RabbitConfig RABBIT_CONFIG;
    public final static String EXCHANGE;
    public final static String ROUTING_KEY;
    public final static Properties PROPS = new Properties();

    static {
        try (InputStream input = Config.class.getResourceAsStream("/websocket.properties")) {
            PROPS.load(input);
            BASE_WS_URL = PROPS.getProperty("base-ws-url");
            AUTH_TOKEN = PROPS.getProperty("auth-token");
            LIST_ID = PROPS.getProperty("list-id");
        } catch (Throwable t) {
            log.error(t);
            throw new RuntimeException(t);
        }
        try (InputStream input = Config.class.getResourceAsStream("/rabbitmq.properties")) {
            Properties props = new Properties();
            props.load(input);
            String host = props.getProperty("host");
            int port = Integer.parseInt(props.getProperty("port"));
            String vHost = props.getProperty("v-host");
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            EXCHANGE = props.getProperty("exchange") == null ? "" : props.getProperty("exchange");
            ROUTING_KEY = props.getProperty("routing-key");
            RABBIT_CONFIG = new RabbitConfig(host, port, vHost, user, password);
        } catch (Throwable e) {
            log.error(e);
            throw new RuntimeException("can't connect to rabbitmq", e);
        }
    }
}
