import java.net.*;

public class MyHTTPServer {

	
    private static int maxConexiones = 0;
    private static int conexiones = 0;

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
    	String ipController = "";
    	String portController = "";
        

		try
		{
			
			if (args.length != 4) {
				System.out.println("Debe indicar el puerto de escucha del servidor, Ip del Controller, puerto del Controller y el máximo de conexiones.");
				System.out.println("$./Servidor puerto_servidor ip_controller puerto_controller maxconexiones");
				System.exit (1);
			}
            puerto = Integer.parseInt(args[0]);
            ipController = args[1];
            portController = args[2];
            setmaxConexiones(Integer.parseInt(args[3]));
            
			ServerSocket skServidor = new ServerSocket(puerto);
		    System.out.println("Escucho el puerto " + puerto);
	
			/*
			* Mantenemos la comunicacion con el cliente
            */
            	
			for(;;)
			{
				
                //if(getConexiones() <= getmaxConexiones()){
                    /*
				    * Se espera un cliente que quiera conectarse
				    */
					Socket skCliente = skServidor.accept(); // Crea objeto
		            Thread t = new HiloHTTPServer(skCliente, ipController, portController);
					t.start();
                    //System.out.println("Hay " + getConexiones() + " conexiones actualmente");

				/*}else{
					System.out.println("Superadas el máximo de conexiones, mantengase a la espera.");
					System.out.println("Conexiones: " + getConexiones());
                    Thread.sleep(5000);
                }*/
				
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
			
		}
	}

	public static int getmaxConexiones(){return maxConexiones;}
	public static void setmaxConexiones(int variado){
		maxConexiones = variado;
	}
	public static int getConexiones(){return conexiones;}
	public static void setConexiones(int variado){
		conexiones = variado;
	}

}
