package simpleStreaming;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RunFeeder {


    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("need to set hostname and port in pom.xml or at the command line");
            System.exit(-1);
        }

        startMasterServer(args);
    }

    private static void startMasterServer(String[] args) {
        InetAddress hostAddress = null;
        try {
            hostAddress = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int portNumber = Integer.parseInt(args[1]);
        boolean listening = true;

        System.out.println("Starting Server Socket");
        try (ServerSocket serverSocket = new ServerSocket(portNumber, 50, hostAddress)) {
            while (listening) {
                new MultiServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }

    public static class MultiServerThread extends Thread {
        private Socket socket = null;

        public MultiServerThread(Socket socket) {
            super("MultiServerThread");
            this.socket = socket;
        }

        public void run() {

            System.out.println("Starting Thread");
            try (
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));
            ) {
                while (!out.checkError()) {
                    String nxtMessage = StringUtils.join(nextEventList(), ' ');
                    System.out.println("sending nextEventList = " + nxtMessage);
                    out.println(nxtMessage);
                    Thread.sleep(50);
                }
                socket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Shutting Down Thread");
        }
    }

    public static List<String> EVENT_NAMES = Arrays.asList("thyrotome", "radioactivated", "toreutics", "metrological",
            "adelina", "architecturally", "unwontedly", "histolytic", "clank", "unplagiarised",
            "inconsecutive", "scammony", "pelargonium", "preaortic", "goalmouth", "adena",
            "murphy", "vaunty", "confetto", "smiter", "chiasmatype", "fifo", "lamont", "acnode",
            "mutating", "unconstrainable", "donatism", "discept", "expressions", "benevolent");

    private static Random rand = new Random();

    private static Integer MAX_STREAM_SIZE = 20;

    private static List<String> nextEventList() {
        List<String> nextStream = new ArrayList<>(MAX_STREAM_SIZE);

        for (int indx = 0; indx < MAX_STREAM_SIZE; indx++) {
            int randomNum = rand.nextInt(EVENT_NAMES.size());
            String event = EVENT_NAMES.get(randomNum);
            nextStream.add(event);
        }

        return nextStream;
    }


}
