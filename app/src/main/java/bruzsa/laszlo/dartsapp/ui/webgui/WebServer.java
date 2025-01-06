package bruzsa.laszlo.dartsapp.ui.webgui;

import fi.iki.elonen.NanoHTTPD;
import lombok.Getter;
import lombok.Setter;

public class WebServer extends NanoHTTPD {

    public static final int WEBSERVER_DEFAULT_PORT = 9000;
    @Getter
    private final int port;

    @Getter
    @Setter
    private String response;

    public WebServer(int port) {
        super(port);
        this.port = port;
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(response);
    }

}
