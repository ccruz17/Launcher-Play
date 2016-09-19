package cruga.team.launcher_play;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.Cache;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.Products;
import org.solovyev.android.checkout.RobotmediaDatabase;
import org.solovyev.android.checkout.RobotmediaInventory;

import java.util.concurrent.Executor;

import static java.util.Arrays.asList;
import static org.solovyev.android.checkout.ProductTypes.IN_APP;
import static android.content.Intent.ACTION_VIEW;


/**
 * Created by christian on 15/08/16.
 */
public class CheckoutApplication extends Application {
    /**
     * For better performance billing class should be used as singleton
     */

    static final String MAIL = "mundo.iux17@gmail.com";

    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
        @Override
        public String getPublicKey() {
            final String s = MainActivity.API_KEY;
            return fromX(s, MAIL);
        }

        @Override
        public Cache getCache() {
            return null;
        }

        @Override
        public Inventory getFallbackInventory(Checkout checkout, Executor onLoadExecutor) {
            if (RobotmediaDatabase.exists(billing.getContext())) {
                return new RobotmediaInventory(checkout, onLoadExecutor);
            } else {
                return null;
            }
        }

    });

    /**
     * Method deciphers previously ciphered message
     * @param message ciphered message
     * @param salt salt which was used for ciphering
     * @return deciphered message
     */
    static String fromX(String message, String salt) {
        return x(new String(Base64.decode(message, 0)), salt);
    }

    /**
     * Method ciphers message. Later {@link #fromX} method might be used for deciphering
     * @param message message to be ciphered
     * @param salt salt to be used for ciphering
     * @return ciphered message
     */
    static String toX(String message, String salt) {
        return new String(Base64.encode(x(message, salt).getBytes(), 0));
    }

    /**
     * Symmetric algorithm used for ciphering/deciphering. Note that in your application you probably want to modify
     * algorithm used for ciphering/deciphering.
     * @param message message
     * @param salt salt
     * @return ciphered/deciphered message
     */
    static String x(String message, String salt) {
        final char[] m = message.toCharArray();
        final char[] s = salt.toCharArray();

        final int ml = m.length;
        final int sl = s.length;
        final char[] result = new char[ml];

        for (int i = 0; i < ml; i++) {
            result[i] = (char) (m[i] ^ s[i % sl]);
        }
        return new String(result);
    }

    /**
     * Application wide {@link org.solovyev.android.checkout.Checkout} instance (can be used anywhere in the app).
     * This instance contains all available products in the app.
     */
    private final Checkout checkout = Checkout.forApplication(
            billing,
            Products.create().add(IN_APP, asList(MainActivity.BILLING_BEER, MainActivity.BILLING_COFFEE, MainActivity.BILLING_EXCLUSIVE_FEATURES, MainActivity.BILLING_TICKET))
    );

    private static CheckoutApplication instance;

    public CheckoutApplication() {
        instance = this;
    }

    public static CheckoutApplication get() {
        return instance;
    }

    public Checkout getCheckout() {
        return checkout;
    }

    static boolean openUri(Activity activity, String uri) {
        try {
            activity.startActivity(new Intent(ACTION_VIEW, Uri.parse(uri)));
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e("Checkout", e.getMessage(), e);
        }
        return false;
    }

}