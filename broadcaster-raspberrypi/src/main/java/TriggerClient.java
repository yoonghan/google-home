import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TriggerClient {

	private final int PORT = 9000;
	private final String HOST_NAME = "10.0.1.1";
	
	public TriggerClient() {
		try (
		    Socket clientSocket = new Socket(HOST_NAME, PORT);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		){
			clientSocket.setKeepAlive(false);
			clientSocket.setSoTimeout(30_000);
			
      out.write("CX:U:"+getMovement(-50,10,0,0));//open arms
      out.write("AB:L:"+getMovement(360,360,0,0));
      out.write("CX:U:"+getMovement(50,10,0,0));
      out.write("AB:L:"+getMovement(-420,420,0,0));
      out.write("AB:L:"+getMovement(290,290,0,900));//move forward
      out.write("CX:U:"+getMovement(-50,10,0,0));
      out.write("AB:L:"+getMovement(-180,-180,0,0));

/*
      out.write("AB:L:018001800000000");"
      out.write("CX:U:005000100000000");//close arm
      out.write("AB:L:0805-8050000000");
      out.write("AB:L:290029000000900");
      out.write("AB:L:0440-4400000000");
      out.write("AB:L:-180-1800000900");
      out.write("CX:U:-05000100000000");//open arm
      out.write("AB:L:-360-3600000000");
*/

		    out.flush();
		    
		    System.out.println("Completed");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}


        private String getMovement(int rotation1, int rotation2, int acceleration, int speed) {
          return String.format("%04d%04d%04d%03d",rotation1,rotation2,acceleration,speed);
        }

	public static void main(String args[]) {
		new TriggerClient();
	}
}
