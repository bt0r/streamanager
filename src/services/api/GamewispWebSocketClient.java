package services.api;


import animations.LoadingAnimation;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;
import services.ConfigService;
import services.EventService;
import services.GamewispAPIService;
import services.LanguageService;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class GamewispWebSocketClient {
    private Logger             logger        = Logger.getLogger("streaManager");
    private ConfigService      config        = ConfigService.getInstance();
    private LanguageService    trans         = LanguageService.getInstance();
    private EventService       event         = EventService.getInstance();
    private GamewispAPIService gamewisp      = GamewispAPIService.getInstance();
    private Socket             currentSocket = null;


    public void connect() {
        try {
            IO.Options opts = new IO.Options();
            opts.reconnection = false;
            currentSocket = IO.socket("https://singularity.gamewisp.com", opts);
            currentSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject auth = new JSONObject();

                    try {
                        auth.put("key", gamewisp.CLIENT_ID);
                        auth.put("secret", gamewisp.CLIENT_SECRET);
                        currentSocket.emit("authentication", auth);
                        logger.info("Connected to gamewisp web socket server");
                        config.startTask("gamewisp_connect", trans.getProp("gamewisp.connecting"), LoadingAnimation.LEVEL_INFO);

                    } catch (JSONException e) {
                        logger.severe("Error when trying to authenticate the app on gamewisp web socket , error: " + e.getMessage());
                    }

                }

            }).on("authenticated", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    logger.info("Authenticated to gamewisp web socket server");
                    try {
                        JSONObject channelJson = (JSONObject) args[0];
                        if (config.getProp("gamewisp.token") != null && !config.getProp("gamewisp.token").equals("")) {
                            channelJson.put("access_token", config.getProp("gamewisp.token"));
                            currentSocket.emit("channel-connect", channelJson);
                            logger.info("Connected to gamewisp channel");
                            config.endTask("gamewisp_connect", trans.getProp("gamewisp.connected"), LoadingAnimation.LEVEL_SUCCESS);
                        }
                    } catch (JSONException e) {
                        config.endTask("gamewisp_connect", trans.getProp("gamewisp.disconnected"), LoadingAnimation.LEVEL_ERROR);
                        logger.severe("Error when trying to authenticate the web socket client on gamewisp, please check credentials");
                    }


                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    config.startTask("gamewisp_connect", trans.getProp("gamewisp.disconnected"), LoadingAnimation.LEVEL_ERROR);
                    logger.severe("Disconnect from GameWisp web socket server");
                }

            })
            ;
            currentSocket.on("subscriber-new", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        String           jsonString    = (String) args[0];
                        JSONObject       jsonResponse  = new JSONObject(jsonString);
                        JSONObject       data          = jsonResponse.getJSONObject("data");
                        JSONObject       usernames     = data.getJSONObject("usernames");
                        String           subDateString = data.getString("subscribed_at");
                        SimpleDateFormat format        = new SimpleDateFormat("Y-M-d H:m:s");
                        long             subDate       = format.parse(subDateString).getTime();
                        if (!usernames.isNull("twitch")) {
                            String twitchUsername = usernames.getString("twitch");
                            String message        = "SUB: " + twitchUsername + " - " + data.getString("amount") + "$";
                            event.addGamewispSub(twitchUsername, message);
                            gamewisp.addSubscriber(usernames.getString("twitch"), subDate);
                        } else {
                            String gamewispUsername = usernames.getString("gamewisp");
                            String message          = "SUB: " + gamewispUsername + " - " + data.getString("amount") + "$";
                            event.addGamewispSub(null, message);
                        }
                    } catch (JSONException e) {
                        logger.severe("Error with subscriber-new gamewisp event");
                    } catch (ParseException e) {
                        logger.warning("Can't cast gamewisp sub date, error:" + e.getMessage());
                    }
                }

            });
            currentSocket.on("subscriber-anniversary", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        String jsonString = (String) args[0];
                        System.out.println(jsonString);
                        JSONObject       jsonResponse  = new JSONObject(jsonString);
                        JSONObject       data          = jsonResponse.getJSONObject("data");
                        JSONObject       subscriber    = data.getJSONObject("subscriber");
                        JSONObject       usernames     = subscriber.getJSONObject("usernames");
                        String           subDateString = data.getString("subscribed_at");
                        int              month         = data.getInt("month_count");
                        SimpleDateFormat format        = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        long             subDate       = format.parse(subDateString).getTime();
                        if (!usernames.isNull("twitch")) {
                            String twitchUsername = usernames.getString("twitch");
                            String message        = "RESUB: " + twitchUsername + " " + month + " " + trans.getProp("gamewisp.month");
                            event.addGamewispSub(twitchUsername, message);
                            gamewisp.addSubscriber(twitchUsername, subDate);
                        } else {
                            String gamewispUsername = usernames.getString("gamewisp");
                            String message          = "RESUB: " + gamewispUsername + " " + month + " " + trans.getProp("gamewisp.month");
                            event.addGamewispSub(null, message);
                        }
                    } catch (JSONException e) {
                        logger.severe("Error with subscriber-new gamewisp event");
                        e.printStackTrace();
                    } catch (ParseException e) {
                        logger.warning("Can't cast gamewisp sub date, error:" + e.getMessage());
                    }
                }

            });
            currentSocket.on("subscriber-renewed", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        String           jsonString    = (String) args[0];
                        JSONObject       jsonResponse  = new JSONObject(jsonString);
                        JSONObject       data          = jsonResponse.getJSONObject("data");
                        JSONObject       usernames     = data.getJSONObject("usernames");
                        String           subDateString = data.getString("subscribed_at");
                        SimpleDateFormat format        = new SimpleDateFormat("Y-M-d H:m:s");
                        long             subDate       = format.parse(subDateString).getTime();
                        if (!usernames.isNull("twitch")) {
                            String twitchUsername = usernames.getString("twitch");
                            String message        = "RESUB: " + twitchUsername + " - " + data.getString("amount") + "$";
                            event.addGamewispSub(twitchUsername, message);
                            gamewisp.addSubscriber(twitchUsername, subDate);
                        } else {
                            String gamewispUsername = usernames.getString("gamewisp");
                            String message          = "RESUB: " + gamewispUsername + " - " + data.getString("amount") + "$";
                            event.addGamewispSub(null, message);
                        }
                    } catch (JSONException e) {
                        logger.severe("Error with subscriber-new gamewisp event");
                    } catch (ParseException e) {
                        logger.warning("Can't cast gamewisp sub date, error:" + e.getMessage());
                    }
                }

            });

            // Connection to gamewisp web socket
            currentSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            currentSocket.disconnect();
        } catch (Exception e) {
            logger.severe("Can't disconnect gamewisp websocket client");
        }

    }


}
