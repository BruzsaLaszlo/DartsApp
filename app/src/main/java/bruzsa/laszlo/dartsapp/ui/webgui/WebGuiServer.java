package bruzsa.laszlo.dartsapp.ui.webgui;

import javax.inject.Inject;

public class WebGuiServer {

    private final WebServer webServer;
    public static final String DEFAULT_HTML = """
            <!DOCTYPE html>
            <html lang="hu">
            <head>
                <title>Darts</title>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                <meta http-equiv="refresh" content="1"/>
            </head>
            <body>
                Nothing to show now...
            </body>
            </html>""";

    @Inject
    public WebGuiServer(WebServer webServer) {
        this.webServer = webServer;
        webServer.setResponse(DEFAULT_HTML);
    }

    public void setContent(String html) {
        webServer.setResponse(html);
    }
    
}
