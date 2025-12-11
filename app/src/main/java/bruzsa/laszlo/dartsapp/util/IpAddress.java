package bruzsa.laszlo.dartsapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IpAddress {

    private final Context context;

    public Optional<String> getIPv4Address() {
        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager instanceof ConnectivityManager cm) {
            Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork == null) return Optional.empty();
            LinkProperties link = cm.getLinkProperties(activeNetwork);
            if (link == null) return Optional.empty();
            List<LinkAddress> addresses = link.getLinkAddresses();
            Log.i("IPAddress List", addresses.toString());

            // return only one IPv4Address
            return addresses.stream()
                    .filter(linkAddress -> linkAddress.getAddress().getAddress().length == 4)
                    .findFirst()
                    .map(LinkAddress::toString)
                    .map(s -> s.substring(0, s.indexOf("/")));
        }
        return Optional.empty();
    }

    public Optional<String> getIPv4AddressAlternative() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return Optional.ofNullable(addr.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("IpAddress", "Error getting network interfaces", e);
        }
        return Optional.empty();
    }

}
