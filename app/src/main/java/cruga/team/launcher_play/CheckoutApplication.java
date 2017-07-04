package cruga.team.launcher_play;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.securepreferences.SecurePreferences;
import com.tozny.crypto.android.AesCbcWithIntegrity;

import org.solovyev.android.checkout.Billing;

import java.security.GeneralSecurityException;

/**
 * Created by christian on 15/08/16.
 */
public class CheckoutApplication extends Application {
    /**
     * For better performance billing class should be used as singleton
     */

    static final String MAIL = "mundo.iux17@gmail.com";
    private static final String TAG = "CRUGA_SECURE";

    private SecurePreferences mSecurePrefs;
    private SecurePreferences mUserPrefs;


    private final Billing billing = new Billing(this, new Billing.DefaultConfiguration() {
        @Override
        public String getPublicKey() {
            final String s = MainActivity.API_KEY;
            return fromX(s, MAIL);
        }
        /*

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
        */

    });

    private static CheckoutApplication sInstance;


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

    public CheckoutApplication() {
        super();
        sInstance = this;
    }

    public static CheckoutApplication get() {
        return sInstance;
    }

    public Billing getBilling() {
        return billing;
    }

    /**
     * Single point for the app to get the secure prefs object
     * @return
     */
    public SharedPreferences getSharedPreferences() {
        if(mSecurePrefs==null){
            mSecurePrefs = new SecurePreferences(this, "", "my_prefs.xml");
            SecurePreferences.setLoggingEnabled(true);
        }
        return mSecurePrefs;
    }


    /**
     * This is just an example of how you might want to create your own key with less iterations 1,000 rather than default 10,000. This makes it quicker but less secure.
     * @return
     */
    public SharedPreferences getSharedPreferences1000() {
        try {
            AesCbcWithIntegrity.SecretKeys myKey = AesCbcWithIntegrity.generateKeyFromPassword(Build.SERIAL,AesCbcWithIntegrity.generateSalt(),1000);
            return new SecurePreferences(this, myKey, "my_prefs_1000.xml");
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "Failed to create custom key for SecurePreferences", e);
        }
        return null;
    }

    public SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }


    public SecurePreferences getUserPinBasedSharedPreferences(String password){
        if(mUserPrefs==null) {
            mUserPrefs = new SecurePreferences(this, password, "user_prefs.xml");
        }
        return mUserPrefs;
    }

    public boolean changeUserPrefPassword(String newPassword){
        if(mUserPrefs!=null){
            try {
                mUserPrefs.handlePasswordChange(newPassword, this);
                return true;
            } catch (GeneralSecurityException e) {
                Log.e(TAG, "Error during password change", e);
            }
        }
        return false;
    }

    /**
     * Application wide {@link org.solovyev.android.checkout.Checkout} instance (can be used anywhere in the app).
     * This instance contains all available products in the app.
     *
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
    */
}