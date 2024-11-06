package a2ews.takx.plugin.cot;

import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class CoTMessageHandler {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Constructor to initialize the socket connection
    public CoTMessageHandler(String serverAddress, int port) throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // Method to create a CoT message as an XML String
    public String createCoTMessage(String uid, String type, String time, String how) {
        return "<event version=\"2.0\" uid=\"" + uid + "\" type=\"" + type + "\" time=\"" + time + "\" how=\"" + how + "\"></event>";
    }

    // Method to send a CoT message
    public void sendCoTMessage(String cotMessage) {
        out.println(cotMessage); // Sending the message through the output stream
    }

    // Method to receive a CoT message
    public String receiveCoTMessage() throws IOException {
        return in.readLine(); // Reading the message from the input stream
    }

    // Method to parse a CoT message from XML String to Document
    public Document parseCoTMessage(String cotMessage) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(cotMessage.getBytes());
        return builder.parse(is);
    }

    // Method to close the connection
    public void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
