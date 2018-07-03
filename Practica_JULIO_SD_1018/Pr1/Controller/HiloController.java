import java.lang.Exception;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;

import java.io.*;

public class HiloController extends Thread {

	private Socket skCliente;
	private String ip;
	private String puerto;
	
	public HiloController(Socket p_cliente, String ip, String puerto)
	{
		this.skCliente = p_cliente;
		this.ip = ip;
		this. puerto = puerto;
	}
	
	/*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	*/
	public String leeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			InputStream aux = p_sk.getInputStream();
			//DataInputStream flujo = new DataInputStream( aux );
			BufferedReader flujo = new BufferedReader(new InputStreamReader(aux));
			p_Datos = new String();
			p_Datos = flujo.readLine();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
      return p_Datos;
	}

	/*
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
	*/
	public void escribeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			OutputStream aux = p_sk.getOutputStream();
			PrintWriter flujo = new PrintWriter(new OutputStreamWriter(aux));//prueba
			//skcliente.getoutputstream().write(cadena.getbytes("UTF-8"));
			flujo.println(p_Datos);
			flujo.flush();     //limpieza de buffer  
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
		return;
	}
	
	public String peticionRMI(InterfazSonda sonda, String pet) throws IOException{
		String respuesta ="<HTML><HEAD><TITLE>";
		respuesta+=sonda.getNombre() + "</TITLE></HEAD><BODY background=\"http://www.digitalrevolutions.biz/wp-content/uploads/2009/07/blue_line_background.jpg\"><H1>" + sonda.getNombre() + "</H1><br>";
		if(pet.equals("obtenerTemperatura")){
			respuesta+="<b>Temperatura:</b> " + sonda.valorTemp() + "º<br>";
		}else if(pet.equals("obtenerHumedad")){
			respuesta+="<b>Humedad:</b> " + sonda.valorHum() + "%<br>";
		}else if(pet.equals("aumentarTemperatura")){
			sonda.activarActuador(pet);
			respuesta += "<b>Temperatura</b> de la sonda cambiada ha sido aumentada " + sonda.valorTemp() + "º<br>";
		}else if(pet.equals("decrementarTemperatura")){
			sonda.activarActuador(pet);
			respuesta += "<b>Temperatura</b> de la sonda cambiada ha sido disminuida a " + sonda.valorTemp() + "º<br>";
		}else if(pet.equals("aumentarHumedad")){
			sonda.activarActuador(pet);
			respuesta += "<b>Humedad</b> de la sonda cambiada ha sido aumentada " + sonda.valorHum() + "%<br>";
		}else if(pet.equals("decrementarHumedad")){
			sonda.activarActuador(pet);
			respuesta += "<b>Humedad</b> de la sonda cambiada ha sido disminuida a " + sonda.valorHum() + "%<br>";
		}else{
			respuesta+="<b>Variable no aceptada.</b><br> Pruebe con <b>obtenerTemperatura</b> u <b>obtenerHumedad</b>.";
		}
		
		
		respuesta+=sonda.getFecha() + "</BODY></HTML>";

		return respuesta;
	}

	public String peticionSondas(String[] sondas, Registry registry){
		
		
		String respuesta ="<HTML><HEAD><TITLE>";
		respuesta+="Sondas" + "</TITLE></HEAD><BODY background=\"http://www.digitalrevolutions.biz/wp-content/uploads/2009/07/blue_line_background.jpg\"><H1>Todas las Sondas</H1><br>";
		for(String prueba: sondas){
			try{
				InterfazSonda sonda = (InterfazSonda) registry.lookup(prueba);
				respuesta+="<u>"+ sonda.getNombre() + "</u><br>";
				respuesta+="<b>Temperatura:</b> " + sonda.valorTemp() + "º<br>";
				respuesta+="<b>Humedad:</b> " + sonda.valorHum() + "%<br><br>";
			}catch (Exception ex){

			}
			
		}
		
		
		respuesta+="</BODY></HTML>";

		return respuesta;
	}

	public String leerArchivo(String s) throws IOException {
		String leido = "";
		//ruta del fichero
		File fichero = new File(System.getProperty("user.dir") + "/Estatico" + s);
		//System.out.println("La ruta del fichero es: " + fichero.getAbsolutePath());
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea = "";
			while((linea = br.readLine()) != null) {
				leido += linea;
			}
			fr.close();
			br.close();
		} catch(Exception e) {
			leido = "";
			leido = "HTTP/1.1 404 Not Found\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n" + leerArchivo("/404.html");
			//leido = leerArchivo("/404.html");
			this.escribeSocket(skCliente, leido);
			skCliente.close();
		}
		return leido;
    }	
	
    public void run() {
		InterfazSonda sonda = null;
            
        String servidor = "rmi://"+ip+":"+puerto+"/sonda";
		String Cadena = "";
		String respuesta = "";
		Cadena = this.leeSocket (skCliente, Cadena);
            try
            {
				
				//final Registry registry = LocateRegistry.getRegistry(ip,Integer.parseInt(puerto));
				final Registry registry = LocateRegistry.getRegistry(ip, Registry.REGISTRY_PORT);
				System.out.println(Cadena);
				//final String[] remoteObjNames = registry.list();
				
                if(Cadena.contains("index")){
					//devolver todas las sondas
					System.setSecurityManager(new RMISecurityManager());  
					String[] sondas = registry.list();
					
					respuesta = peticionSondas(sondas, registry);
					System.out.println(respuesta);	
					this.escribeSocket (skCliente, respuesta);			
					System.out.println("Se cierra este cliente.");						
					skCliente.close();

                }else{
                    //Buscamos el numero de invernadero que pide la cadena
                    String[] partida = Cadena.split("/");
                    System.out.println(partida[2]);
                    String[] inte = partida[2].split("\\?");
                    System.out.println(inte[1]);
                    String[] amper = inte[1].split("\\&");
					String[] inver = amper[1].split("\\=");
					System.out.println(inver[0]+inver[1]);
                    System.setSecurityManager(new RMISecurityManager());            	
					sonda = (InterfazSonda) registry.lookup(inver[0]+inver[1]);
					respuesta = peticionRMI(sonda, inte[0]);
					System.out.println(respuesta);	
					this.escribeSocket (skCliente, respuesta);			
					System.out.println("Se cierra este cliente.");						
					skCliente.close();
				}


				System.out.println(respuesta);	
				this.escribeSocket (skCliente, respuesta);			
				System.out.println("Se cierra este cliente.");						
				skCliente.close();
            	
            }
            catch(Exception ex)
            {
				try{
					System.out.println("Error al instanciar el objeto remoto "+ex);
					respuesta = leerArchivo("/404.html");
					this.escribeSocket (skCliente, respuesta);			
					System.out.println("Se cierra este cliente.");	
					//skCliente.close();
				}catch(Exception ex2){
					System.exit(0);
				}
				
                //System.exit(0);
            }
        }
}
