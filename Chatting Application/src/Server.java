import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    public  Server(){
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection request.");
            socket = server.accept(); // Accepting Client connection request

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }catch(Exception exception){
            System.err.println(STR."Connection Failed\{exception.getMessage()}");
            }
    }
    public void startReading(){
        // Thread for reading
        Runnable runnableReading = () -> {
            try {
            while(true){
                String message;
                message = bufferedReader.readLine();        // What actually this function startReading do it read data from Client
                                                                // and prints it on Server console, or simply it prints the message from client.
                if(message.equals("exit")) {
                    System.out.println("Client Terminated Chat");
                    socket.close();
                    break;
                }
                System.out.println(STR."Client : \{message}");
                }
            }catch (IOException e) {
                //throw new RuntimeException(e);
                System.out.println("Connection Terminated");
            }
        };
        new Thread(runnableReading).start();
    }

    public void startWriting() {
         // Thread for Writing
        Runnable runnableWriting = () -> {
                                            // Note: This is for sending message to Client,
            try{                                // All I will write here will be sent to Client as content
            while (!socket.isClosed()) {
                    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = bufferedReader1.readLine();
                    printWriter.println(content);
                    printWriter.flush();
                    if(content.equals("exit")) {
                        System.out.println("Server Terminated Chat");
                        socket.close();
                        break;
                }
                }
            }
            catch (Exception exception) {
                //System.out.println(STR."Exception: \{exception.getMessage()}");
                System.out.println("Connection Terminated");
            }

        };
        new Thread(runnableWriting).start();
    }
    public static void main(String[] args) {
        System.out.println("Hello and welcome This is Server!");
        new Server();
    }
}
