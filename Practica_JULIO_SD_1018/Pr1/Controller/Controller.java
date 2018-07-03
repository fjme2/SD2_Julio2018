import java.net.*;

public class Controller {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		* Descriptores de socket servidor y de socket con el cliente
        */
        int puerto;
    	String ipRMI = "";
    	String portRMI = "";
        

		try
		{
			
			if (args.length != 3) {
				System.out.println("Debe indicar el puerto de escucha del controller, la ip del RMI y su puerto");
				System.out.println("$./Controller port_controller ip_RMI port_RMI");
				System.exit (1);
			}
            puerto = Integer.parseInt(args[0]);
            ipRMI = args[1];
            portRMI = args[2];
            
			ServerSocket skServidor = new ServerSocket(puerto);
		    System.out.println("Escucho el puerto " + puerto);
	
			/*
			* Mantenemos la comunicacion con el cliente
            */
            	
			for(;;)
			{
                    /*
				    * Se espera un cliente que quiera conectarse
				    */
					Socket skCliente = skServidor.accept(); // Crea objeto
		            Thread t = new HiloController(skCliente, ipRMI, portRMI);
					t.start();
				
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
			
		}
	}

}
