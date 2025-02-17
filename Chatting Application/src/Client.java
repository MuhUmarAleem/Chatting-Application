import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame{
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter  printWriter;
    public  Client(){
        try{
            socket = new Socket("127.0.0.1",7777);

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());

            createGUI();

            startReading();
            startWriting();

        }catch (Exception exception){
            System.err.println(STR."Exception: \{exception.getMessage()}");

        }
    }

    private void createGUI(){
        this.setTitle("Client Messenger");
        this.setSize(500,500);
        this.setLocationRelativeTo(null); //For Locating Window at Centre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder((BorderFactory.createEmptyBorder(20,20,20,20)));


        this.setLayout(new BorderLayout()); //BoarderLayout will divide Window into four components North(heading)
                                            //Center, East (Right), West (Left) and south
        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


    }

    public void startReading(){
        Runnable runnableReading = ()->{
            try{
            while (true) {
                String message = null;
                message = bufferedReader.readLine();
                if (message.equals("exit")) {
                    System.out.println("Server Terminated Chat");
                    socket.close();
                    break;
                }

                System.out.println(STR."Server: \{message}");
            }
            }catch (Exception exception){
                //System.err.println(STR."Exception: \{exception.getMessage()}");
                System.out.println("Connection Terminated");
            }
        };
        new Thread(runnableReading).start();
    }

    public void startWriting(){
        Runnable runnableWriting = ()->{
                                                // Note: This is for sending message to Server,
                                                 // All I will write here will be sent to Server as content
            try {
                while (!socket.isClosed()) {
                        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
                        String content = bufferedReader1.readLine();
                        printWriter.println(content);
                        printWriter.flush();
                        if(content.equals("exit")) {
                            System.out.println("Client Terminated Chat");
                            socket.close();
                            break;
                    }
                    }

                }catch (Exception exception) {
                //System.err.println(STR."Exception: \{exception.getMessage()}");
                System.out.println("Connection Terminated");
            }
        };
        new Thread(runnableWriting).start();
    }

    public static void main(String[] args) {
        System.out.println("Hello! This is Client");
        new Client();
    }
}
