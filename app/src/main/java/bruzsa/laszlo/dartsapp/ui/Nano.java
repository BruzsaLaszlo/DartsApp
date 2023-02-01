package bruzsa.laszlo.dartsapp.ui;

import fi.iki.elonen.NanoHTTPD;

public class Nano extends NanoHTTPD {

    public Nano(int port) {
        super(port);
    }

    public Nano(String hostname, int port) {
        super("192.168.0.124", port);
    }

    private String response = "default response";

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(response);
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
