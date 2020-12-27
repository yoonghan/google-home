import com.walcron.lejos.motors.CommandExecutor;
import com.walcron.lejos.socket.LocalServer;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class Main {

	private final CommandExecutor commandExecutor;
	private final LocalServer localServer;
	private final int port = 9000;
	
	public Main() {
		
		System.out.println("-----------------");
		System.out.println("-----STARTED-----");
		System.out.println("-----------------");
		
		commandExecutor = new CommandExecutor();
		commandExecutor.start();
		
		localServer = new LocalServer(port);
		localServer.start();
		
		
		Button.ESCAPE.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(Key k) {
				
			}

			@Override
			public void keyReleased(Key k) {
				localServer.interrupt();
				commandExecutor.interrupt();
				System.out.println("Received Input");
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		new Main();
	}

}
