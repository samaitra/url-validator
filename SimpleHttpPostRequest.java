/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package travelQa;

//import java.lang.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleHttpPostRequest {

    /**   * @param args   */
    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File("spamdata.txt");

        BufferedReader bufRdr = new BufferedReader(new FileReader(file));
        String csvline = null;
        //read each line of text file
        File count = null;

        int c = 0;
        List<String> urlList = new ArrayList<String>();
        while ((csvline = bufRdr.readLine()) != null) {
            if(c==1500) {
                startProcessing(urlList);
                urlList = new ArrayList<String>();
                c=0;
            }
            urlList.add(csvline);
            c++;
        }
        startProcessing(urlList);
    }

    static void startProcessing(final List<String> urls) throws InterruptedException {
        Thread t = new Thread() {
            @Override
            public void run() {
            	
                for (String next : urls) {
                    HttpURLConnection connection = null;
                    try {
                        String url="http://serverhost/write/add_post";
                        String urlParameters =
                            "version=" + URLEncoder.encode("1", "UTF-8") +
                            "&body=" + URLEncoder.encode(next, "UTF-8")+
                            "&topic_id=" + URLEncoder.encode("test_spam_1", "UTF-8")+
                            "&user_id=" + URLEncoder.encode("demouser", "UTF-8");
                            
                    	URL serverAddress = new URL(url);
                        
                        connection = (HttpURLConnection) serverAddress.openConnection();
                        connection.setRequestMethod("POST");
                       
                        connection.setRequestProperty("Content-Type", 
                        "application/x-www-form-urlencoded");
             			
                   connection.setRequestProperty("Content-Length", "" + 
                            Integer.toString(urlParameters.getBytes().length));
                   connection.setRequestProperty("Content-Language", "en-US");  
             			
                   connection.setUseCaches (false);
                   connection.setDoInput(true);
                   connection.setDoOutput(true);

                   //Send request
                   DataOutputStream wr = new DataOutputStream (
                               connection.getOutputStream ());
                   wr.writeBytes (urlParameters);
                   wr.flush ();
                   wr.close ();

                   //Get Response	
                   InputStream is = connection.getInputStream();
                   BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                   String line;
                   StringBuffer response = new StringBuffer(); 
                   while((line = rd.readLine()) != null) {
                     response.append(line);
                     response.append('\r');
                   }
                   rd.close();
                   System.out.println(response.toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        connection.disconnect();
                        connection = null;
                    }
                }
            }
        };
        t.start();
    }
}
