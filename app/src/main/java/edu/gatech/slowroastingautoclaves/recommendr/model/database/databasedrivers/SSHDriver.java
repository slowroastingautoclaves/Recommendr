/**
 *
 * Created by Joshua Jibilian on 2/8/2016.
 *
 * Facilitates connection to server through an ssh tunnel
 */
package edu.gatech.slowroastingautoclaves.recommendr.model.database.databasedrivers;
import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.concurrent.Executor;

public class SSHDriver implements Runnable{
    private static String hostIp = "128.61.105.200";
    private static int lport;
    private static String rhost;
    private static int rport;
    private static Session session;

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
            //Log.d("SSHDriver", "Establishing server Connection...");
            session.connect();
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            //Log.i("SSHDriver", "localhost:" + assinged_port + " -> " + rhost + ":" + rport + session.isConnected());
        } catch (Exception e) {
            //Log.e("SSHDriver", "Could not open tunnel: " + e.getMessage());
        }
    }

    /**
     * Closes SSH connection to server
     */
        public static void closeSSHConnection() {
            if (session != null && session.isConnected()) {
               // Log.i("SSHDriver","Closing SSH Connection");
                session.disconnect();
            }
        }

    /**
     * checks if tunnel is connected to server
     * @return returns true if connected else false.
     */
    public boolean isConnected(){
        return session.isConnected();
    }


    public void run(){
        connectViaSSH();
    }
}


