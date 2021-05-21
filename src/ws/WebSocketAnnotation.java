package ws;

import meta1.MulticastLibrary;
import meta1.classes.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

import action.Action;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;
    private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet<>();
    private MulticastLibrary rmiServer;

    public WebSocketAnnotation() {
        username = "User" + sequence.getAndIncrement();
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        users.add(this);
    }

    @OnClose
    public void end() {
    	// clean up once the WebSocket connection is closed
        users.remove(this);
    }

    @OnMessage
    public void receiveMessage(String message) {
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
        sendMessage(message);
    }
    
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
    	try {
    	    for (Session sess : session.getOpenSessions()) {
    	        if (sess.isOpen()) {
    	            sess.getBasicRemote().sendText(text);
                }
            }
			//this.session.getBasicRemote().sendText(text);
		} catch (IOException e) {
			// clean up once the WebSocket connection is closed
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
}