/**
 *
 * Created by Joshua Jibilian on 2/8/2016.
 *
 * Facilitates connection to server through an ssh tunnel
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

    /**
     * Conects to server via SSH
     */
    public static void connectViaSSH() {
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

    /**
     * Closes SSH connection to server
     */
        public static void closeSSHConnection() {
            if (session != null && session.isConnected()) {
                System.out.println("Closing SSH Connection");
                session.disconnect();
            }
        }
 }


