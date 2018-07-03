
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Define que pueden hacer los nodos Maseter.
 */
public interface InterfazRemoto extends Remote {

	public void registrarSonda(InterfazSonda sonda) throws RemoteException;
}
