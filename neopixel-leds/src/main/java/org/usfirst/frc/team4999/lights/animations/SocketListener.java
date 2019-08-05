package org.usfirst.frc.team4999.lights.animations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.usfirst.frc.team4999.lights.Packet;

public class SocketListener implements Animation {
	
	private ArrayList<Packet> completedPackets;
	private ArrayList<Packet> buildingPackets;
	
	private int currentWait = 1000;
	
	private Thread listener;

	public SocketListener() {
		this(5800);
	}

	public SocketListener(int port) {
		buildingPackets = new ArrayList<Packet>();
		completedPackets = new ArrayList<Packet>();
		listener = new Thread() {
			ByteBuffer buff = ByteBuffer.allocate(16);
			
			ArrayList<Packet> tmp;
			
			@Override
			public void run() {
				try( 
					ServerSocket serverSocket = new ServerSocket(port);
				) {
					while(!Thread.interrupted() ) {
						try (
							Socket clientSocket = serverSocket.accept();
							OutputStream out = clientSocket.getOutputStream();
							InputStream in = clientSocket.getInputStream();
						) {
							while(!clientSocket.isClosed()) {
								byte inByte = (byte)in.read();
								if(inByte == 0x01) {
									buff.clear();
									in.read(buff.array());
									synchronized(buildingPackets) {
										buildingPackets.add(new Packet(buff.array()));
									}
								} else if(inByte == 0x02) {
									synchronized(completedPackets) {
										tmp = completedPackets;
										completedPackets = buildingPackets;
										buildingPackets = tmp;
										buildingPackets.clear();
									}
								} else if(inByte == 0x03) {
									buff.clear();
									in.read(buff.array(), 0, 4);
									currentWait = buff.getInt(0);
									System.out.println("Set waittime to " + currentWait);
								}
							}
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								break;
							}
						} catch (SocketException e) {
							System.out.println("Lost connection");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		listener.start();
	}

	@Override
	public Packet[] getNextFrame() {
		synchronized(completedPackets) {
			//System.out.println(completedPackets);
			Packet[] out = new Packet[completedPackets.size()];
			for(int i = 0; i < out.length; i++) {
				out[i] = completedPackets.get(i);
			}
			return out;
		}
	}

	@Override
	public int getFrameDelayMilliseconds() {
		return currentWait;
	}

}
