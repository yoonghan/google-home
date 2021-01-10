import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TriggerHttpClient() {
  private final String url;
  private final String method;

  public TriggerHttpClient() {
    Properties props = new Properties();
    try (InputStream input = ClassLoader.getSystemResourceAsStream("settings.properties")) {
      props.load(ClassLoader.getSystemResourceAsStream("settings.properties"));
      this.url = props.getProperty("url");
      this.method = props.getProperty("method");
    }
    catch(Exception e) {
      url = null;
      method = null;
    }
  }

  public void send(String message) {
    if(url != null & method != null) {
      URL url = new URL(this.url);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod(this.method);
      con.setRequestProperty("Content-Type", "application/json; utf-8");
      con.setRequestProperty("Accept", "application/json") ;
      con.setDoOutput(true);

      String jsonInputString = "{\"message\":"+ message+"}";

      try(OutputStream os = con.getOutputStream()) {
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);			
      }
      catch(IOException ioe) {
        ioe.printStackTrace();
      }

      //No response wait, expect httpstatus 204.

      con.close();
    }
  }
}
