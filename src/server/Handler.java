package server;

import java.nio.channels.SelectionKey;

public interface Handler {
	public void handle(SelectionKey key);
}
