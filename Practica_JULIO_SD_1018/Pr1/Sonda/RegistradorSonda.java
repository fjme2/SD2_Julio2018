import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RMISecurityManager;

public class RegistradorSonda {

	public static void main(String[] args) throws Exception {
		final String ip = args[0];
		final String nombre = args[1];
		
        final Registry registry = LocateRegistry.getRegistry(ip, Registry.REGISTRY_PORT);
        /*RegistradorSonda rs = new RegistradorSonda();
        System.setSecurityManager(new RMISecurityManager());
		final Sonda sonda = new Sonda(nombre);*/

		/*System.out.println("Try to register " + sonda.getNombre()
				+ " in remote Registry (only works if Registry was created in the same host).");*/
		try {
            RegistradorSonda rs = new RegistradorSonda();
            System.setSecurityManager(new RMISecurityManager());
            final Sonda sonda = new Sonda(nombre);
    
            System.out.println("Try to register " + nombre
                    + " in remote Registry (only works if Registry was created in the same host).");
            System.out.println("Register " + nombre + " through the Master.");
		    final InterfazRemoto remoto= (InterfazRemoto)registry.lookup("/ObjetoRemoto");
		    remoto.registrarSonda(sonda);

			//registry.rebind("/ObjetoRemoto", sonda);

		} catch (RemoteException e) {
			// No puedes hacer bind, rebind, or unbind de un objeto remoto
			// en un Registry que ha sido creado en otro host.
			e.printStackTrace();
		}

		/*System.out.println("Register " + sonda.getNombre() + " through the Master.");
		final InterfazRemoto remoto= (InterfazRemoto)registry.lookup("/ObjetoRemoto");
		remoto.registrarSonda(sonda);*/
	}
}
