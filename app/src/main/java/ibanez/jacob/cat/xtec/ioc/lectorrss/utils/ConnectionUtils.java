package ibanez.jacob.cat.xtec.ioc.lectorrss.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Class with connection utils
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class ConnectionUtils {

    /**
     * @param activity
     */
    public static boolean hasConnection(Activity activity) {
        boolean hasConnection = false;

        //Get the connectivity manager
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get all networks
        Network[] networks = connMgr.getAllNetworks();

        for (Network network : networks) {
            //Get mobile network status
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);

            if (networkInfo != null) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_MOBILE:
                        hasConnection = networkInfo.isConnected();
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        hasConnection = networkInfo.isConnected();
                        break;
                }
            }
        }

        return hasConnection;
    }
}
