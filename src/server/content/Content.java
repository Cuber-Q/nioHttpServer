package server.content;

import java.nio.ByteBuffer;

import server.Sendable;

public interface Content extends Sendable{
	public ByteBuffer prepared();
}
