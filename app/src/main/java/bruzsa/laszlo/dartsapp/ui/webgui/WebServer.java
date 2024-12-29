package bruzsa.laszlo.dartsapp.ui.webgui;

import java.io.IOException;

import bruzsa.laszlo.dartsapp.ui.Nano;
import lombok.experimental.Delegate;

public class WebServer {

    public static final int PORT = 9000;
    private static WebServer webServer;

    private WebServer() {
        startWebserver();
    }

    public static WebServer getServer() {
        if (webServer == null) webServer = new WebServer();
        return webServer;
    }

    @Delegate
    private final Nano nano = new Nano(PORT);

    public void startWebserver() {
        try {
            if (!nano.wasStarted())
                nano.start();
        } catch (IOException e) {
            throw new IllegalStateException("Nano webserver can not start!", e);
        }
    }

    public void setWebServerContent(String html) {
        nano.setResponse(html);
    }

}
