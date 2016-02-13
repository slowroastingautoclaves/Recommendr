/**
 * Created by Blaze on 2/8/2016.
 */
package edu.gatech.slowroastingautoclaves.recommendr.databasedrivers;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
public class SSHDriver {
    static String hostIp = "128.61.105.200";
    static int lport;
    static String rhost;
    static int rport;
    static Session session;
    public static void go() {
        int port = 30;
        lport = 3306;
        rport = 3306;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession("slowroastingautoclaves", hostIp, port);
            rhost = "localhost";
            lport = 3306;
            rport = 3306;
            session.setPassword("slowroasting");
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public static void CloseSSHConnection() {
            if (session != null && session.isConnected()) {
                System.out.println("Closing SSH Connection");
                session.disconnect();
            }
        }
 }


