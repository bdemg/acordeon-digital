/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Jorge A. Cano
 */
public class RunServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       InputStreamReader is
                = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL;
        
        try {
            System.out.println(
                    "Enter the RMIregistry port number:");
            portNum = (br.readLine()).trim();
            int RMIPortNum = Integer.parseInt(portNum);
            startRegistry(RMIPortNum);
            
            Server exportedObj
                    = new Server();
            registryURL
                    = "rmi://localhost:" + portNum + "/callback";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Server ready.");
        }// end try
        catch (Exception re) {
            re.printStackTrace();
        } // end catch
    }

    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry
                    = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();
        // This call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            Registry registry
                    = LocateRegistry.createRegistry(RMIPortNum);
        }
    } // end startRegistry
}
