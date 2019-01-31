package server;


import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.scalecube.socketio.Session;
import io.scalecube.socketio.SocketIOListener;
import io.scalecube.socketio.SocketIOServer;

public class TestChatServer {


    public TestChatServer() {
        SocketIOServer logServer = SocketIOServer.newInstance(8080);

        logServer.setListener(new SocketIOListener() {
            public void onConnect(Session session) {
                System.out.println("Connected: " + session);
            }

            public void onMessage(Session session, ByteBuf message) {
                System.out.println("Received: " + message.toString(CharsetUtil.UTF_8));
                message.release();
            }

            public void onDisconnect(Session session) {
                System.out.println("Disconnected: " + session);
            }
        });
        logServer.start();
    }


}
