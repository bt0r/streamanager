package main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.ConfigService;
import services.TwitchAPIService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class DemoConstraints {

    TwitchAPIService twitch = TwitchAPIService.getInstance();
    ConfigService    config = ConfigService.getInstance();

    public DemoConstraints() throws JSONException{
        if (!config.getProp("streamer.login").isEmpty()) {
            Boolean    found      = false;
            JSONObject users      = twitch.getJsonObjectFromUrl("http://btor.fr/bot/users");
            JSONArray  usersArray = users.getJSONArray("users");
            for(int i = 0; i < usersArray.length();i++) {
                String user = usersArray.getString(i).toLowerCase();
                if (user.equals(config.getProp("streamer.login"))) {
                    found = true;

                    try {

                        BufferedImage u = ImageIO.read(getClass().getResourceAsStream("/img/" + user + ".png"));
                        if(u != null){
                            config.setProp("loading","/img/"+user+".png");
                        }
                    } catch (Exception e) {
                        config.setProp("loading","/img/loading.png");
                    }



                }
            }
            if (!found) {
                System.exit(0);
            }
        }

    }

    public void oldConstraints() {
        /*
         * DEMONSTRATION CODE
    	 */
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf   = new SimpleDateFormat("d");
        String           jour  = sdf.format(cal.getTime());
        SimpleDateFormat sdf2  = new SimpleDateFormat("MM");
        String           mois  = sdf2.format(cal.getTime());
        SimpleDateFormat sdf3  = new SimpleDateFormat("Y");
        String           annee = sdf3.format(cal.getTime());


        int jour2  = new Integer(jour);
        int mois2  = new Integer(mois);
        int annee2 = new Integer(annee);

        if ((mois2 < 06 && mois2 > 10) && annee2 == 2016) {
            System.exit(0);
        }


    	/*
    	 * END DEMONSTRATION
    	 *
    	 */
    }
}
