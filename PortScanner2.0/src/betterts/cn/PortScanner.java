package betterts.cn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class PortScanner {
    public final ExecutorService es = Executors.newFixedThreadPool(100);
    public String ip_start;
    public String ip_end;
    public int port_start;
    public int port_end;
    public ArrayList<String> ips;
    public ArrayList<Integer> ports;

    public PortScanner(String ip_start, String ip_end, int port_start, int port_end) {
        this.ip_start = ip_start;
        this.ip_end = ip_end;
        this.port_start = port_start;
        this.port_end = port_end;
        this.ips = new ArrayList<>();
        this.ports = new ArrayList<>();

        //IP组
        String[] ipStarts = ip_start.split("\\.");
        String[] ipEnds = ip_end.split("\\.");
        StringBuilder sb = new StringBuilder(ipStarts[0] + '.' + ipStarts[1] + '.' + ipStarts[2] + '.');
        for (int i = Integer.parseInt(ipStarts[3]); i <= Integer.parseInt(ipEnds[3]) ; i++) {
            //需深拷贝
            StringBuilder temp = new StringBuilder(sb);
            temp.append( i );
            this.ips.add(temp.toString());
        }
        //port组
        for (int i = port_start; i <= port_end; i++) {
            ports.add( i );
        }
    }

    private Future<ScanResult> portIsOpen(final ExecutorService es , String ip , int port , int timeOut){
       return es.submit(new Callable<ScanResult>() {
           @Override
           public ScanResult call() throws Exception {
              try {
                  Socket socket = new Socket();
                  socket.connect(new InetSocketAddress(ip , port) , timeOut);
                  socket.close();
                  return new ScanResult(ip , port , true);
              } catch (IOException e) {
                  return new ScanResult(ip , port ,false);
              }
           }
       });
    }

    public List<Future<ScanResult>> Scanner() throws ExecutionException, InterruptedException {
        ArrayList<String> result = new ArrayList<>();
        List<Future<ScanResult>> futures = new ArrayList<>();
        for (String ip: ips) {
            for (int port : ports) {
                futures.add(portIsOpen(es , ip , port , 200));
            }
        }
        return futures;
    }

    public class ScanResult{
        private String ip;
        private int port;
        private boolean isOpen;
        private String result;

        public ScanResult(String ip , int port, boolean isOpen) {
            this.ip = ip;
            this.port = port;
            this.isOpen = isOpen;
            if (isOpen)
                result = "Opened";
            else
                result = "Closed";
        }


        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }

        @Override
        public String toString() {
            return ip + ":          " + port + "        is      " + result;
        }
    }


}
