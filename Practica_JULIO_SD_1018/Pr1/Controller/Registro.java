import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Registro {

	public static void main(String[] args) throws Exception {
		final Registry registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
		ObjetoRemoto obj = new ObjetoRemoto(registry);
		registry.rebind("/ObjetoRemoto", obj);

		System.out.println("Esperando a registrar.");
		Registro registro = new Registro();
		synchronized (registro) {
			try {
				registro.wait();
			} catch (InterruptedException e) {
				// Me han despertado, tengo que terminar la ejecución.
			}
		}
	}
}
