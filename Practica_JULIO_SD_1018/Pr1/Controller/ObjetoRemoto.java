
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings({"serial"})
public class ObjetoRemoto extends UnicastRemoteObject implements InterfazRemoto {

    private final Registry registro;
    static final String RMI_NAME = ObjetoRemoto.class.getSimpleName();

	protected ObjetoRemoto(Registry registro) throws RemoteException {
		super();
		this.registro = registro;
	}

	public void registrarSonda(InterfazSonda sonda) throws RemoteException {
        String rmiName;
        System.out.println(sonda.getNombre());
		try {
            rmiName = sonda.getNombre();
            System.out.println(rmiName);
			registro.rebind(rmiName, sonda);
		} catch (RemoteException e) {
            System.out.println("Error en el registro.");
			e.printStackTrace();
			throw e;
		}
		System.out.println("Registrado: " + rmiName);
	}

	public void sayHelloWorld() throws RemoteException {
		System.out.println("I'm de Master, and I'm unique !!!");
	}

	public String prueba() throws RemoteException{
		return "Ha funcionado";
    }
}
