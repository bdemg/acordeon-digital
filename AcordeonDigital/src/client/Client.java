/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import server.SeverInterface;

/**
 *
 * @author Jorge A. Cano
 */
public class Client {

    public static void main(String[] args) {

        try {
            int RMIPort;
            String hostName;
            InputStreamReader is
                    = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(is);

            System.out.println(
                    "Enter the RMIRegistry host name:");
            hostName = br.readLine();
            System.out.println(
                    "Enter the RMIregistry port number:");
            String portNum = br.readLine();
            RMIPort = Integer.parseInt(portNum);

            String registryURL
                    = "rmi://localhost:" + portNum + "/callback";
            // find the remote object and cast it to an 
            //   interface object
            SeverInterface server
                    = (SeverInterface) Naming.lookup(registryURL);
            System.out.println("Lookup completed ");

            AcordeonController callbackObj
                    = new AcordeonController(server);
            // register for callback
            //***IMPORTANTE ---> *** server.registerForCallback(callbackObj);
            System.out.println("Registered for callback.");
        } // end try 
        catch (Exception e) {
            System.out.println(
                    "Exception in CallbackClient: " + e);
        } // end catch
    }
}
