package bruzsa.laszlo.dartsapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.util.Log;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IpAddress {

    private final Context context;

    public Optional<String> getIPv4Address() {
        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager instanceof ConnectivityManager cm) {
            LinkProperties link = cm.getLinkProperties(cm.getActiveNetwork());
            Log.i("IPAddress List", link.getLinkAddresses().toString());

            // return only one IPv4Address
            return link.getLinkAddresses().stream()
                    .filter(linkAddress -> linkAddress.getAddress().getAddress().length == 4)
                    .findFirst()
                    .map(LinkAddress::toString)
                    .map(s -> s.substring(0, s.indexOf("/")));
        }
        return Optional.empty();
    }

}
