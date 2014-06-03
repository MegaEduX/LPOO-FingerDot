package pt.up.fe.lpoo.fingerdot.logic.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by MegaEduX on 03/06/14.
 */

public class UserManager {
    private class LoginResponse {
        public boolean success;
        public HashMap<String, String> info;
    }

    private static final String kBaseApiURL = "https://edr.io/fingerdot/v1/users/";
    private static final String kLoginURL = kBaseApiURL + "login.php";

    private static UserManager _sharedInstance = null;

    private User _user = null;

    public static UserManager sharedManager() {
        if (_sharedInstance == null)
            _sharedInstance = new UserManager();

        return _sharedInstance;
    }

    public boolean login(User details) {
        try {
            URL loginURL = new URL(kLoginURL + "?username=" + details.getUsername() + "&pin=" + details.getPin());

            HttpsURLConnection connection = (HttpsURLConnection) loginURL.openConnection();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = rd.readLine()) != null)
                    sb.append(line);

                Gson gs = new Gson();

                Type responseType = new TypeToken<LoginResponse>(){}.getType();

                LoginResponse response = gs.fromJson(sb.toString(), responseType);

                if (response.success) {
                    _user = new User(response.info.get("username"), response.info.get("pin"));

                    return true;
                }

                return false;
            } catch (Exception e) {
                return false;
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            return false;
        }
    }
}
