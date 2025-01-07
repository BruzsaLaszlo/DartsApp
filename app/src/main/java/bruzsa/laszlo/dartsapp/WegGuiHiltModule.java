package bruzsa.laszlo.dartsapp;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import javax.inject.Singleton;

import bruzsa.laszlo.dartsapp.ui.webgui.WebGuiServer;
import bruzsa.laszlo.dartsapp.ui.webgui.WebServer;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class WegGuiHiltModule {

    @Provides
    @Singleton
    public HtmlTemplateReader getHtmlTemplateReader(@ApplicationContext Context context) {
        return new HtmlTemplateReader(context);
    }

    @Provides
    @Singleton
    public WebGuiServer getWebGuiServer() {
        try {
            long t = System.currentTimeMillis();
            WebServer webServer = new WebServer(WebServer.WEBSERVER_DEFAULT_PORT);
            webServer.start();
            Log.i("WebServer", "startWebserver: starting time: " + (System.currentTimeMillis() - t) + "ms");
            return new WebGuiServer(webServer);
        } catch (IOException e) {
            Log.e("WebServer", "startWebserver: WebServer can not start!", e);
            throw new IllegalStateException("WebServer can not start!");
        }
    }


}
