import java.lang.Exception;
import java.net.Socket;
import java.io.*;

public class HiloHTTPServer extends Thread {

	private Socket skCliente;
	private String ip;
	private String puerto;
	
	public HiloHTTPServer(Socket p_cliente, String ip, String puerto)
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
	
	public String peticionHTTP(String peticion) throws IOException{
		//Partimos la peticion teniendo en cuenta los espacios en blanco para sacar las partes
		String[] partida = peticion.split(" ");
		String respuesta = "";
		if(partida[0].equals("GET")){
			if(!partida[1].equals("/")){

				//Partimos la Uri para saber si son metodos estáticos o dinamicos
				String[] uri = partida[1].split("/");
				if(uri[1].contains("controladorSD")){
					//recurso dinámico que invoca a controller	
					try {
						Socket skControlador = new Socket(ip, Integer.parseInt(puerto));
						escribeSocket(skControlador, partida[1]);//Enviamos la encapsulación
						respuesta = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n" + leeSocket(skControlador, uri[2]);
						skControlador.close();
					} catch(Exception e) {
						
						respuesta = "";
						respuesta = "HTTP/1.1 409 Conflict\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n" + leerArchivo("/409.html");
						this.escribeSocket(skCliente, respuesta);
						skCliente.close();
					}
				}else{
					//recurso estático
					respuesta = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n" + leerArchivo(uri[1]);
	
				}
			}else{
				respuesta = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n" + leerArchivo("/index.html");
				return respuesta;
			}
			

		}else{
			respuesta = "HTTP/1.1 405 Method Not Allowed\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n" + leerArchivo("/405.html");;
			return respuesta;
		}

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
			//this.escribeSocket(skCliente, leido);
			//skCliente.close();
			return leido;
		}
		return leido;
	}	
	
    public void run() {
		String Cadena="";
		String respuesta="";
	
		
        try {
			MyHTTPServer.setConexiones(MyHTTPServer.getConexiones() + 1);
			while(MyHTTPServer.getConexiones() > MyHTTPServer.getmaxConexiones()){
				System.out.println("Superadas el máximo de conexiones, mantengase a la espera.");
				System.out.println("Conexiones: " + MyHTTPServer.getConexiones());
                Thread.sleep(5000);
			}
			
			System.out.println("Sirviendo cliente...");
			System.out.println("Hay " + MyHTTPServer.getConexiones() + " conexiones actualmente");
			Cadena = this.leeSocket (skCliente, Cadena);
			System.out.println("Comunicación: " + Cadena);
				/*
				* Se escribe en pantalla la informacion que se ha recibido del
				* cliente
				*/
			respuesta = this.peticionHTTP(Cadena);
			System.out.println(respuesta);	
			this.escribeSocket (skCliente, respuesta);			
			System.out.println("Se cierra este cliente.");						
			skCliente.close();
				
			MyHTTPServer.setConexiones(MyHTTPServer.getConexiones() - 1);	
        }
        catch (Exception e) {
		  System.out.println("Error: " + e.toString());
		  System.out.println("Se cierra este cliente con Error.");
		  MyHTTPServer.setConexiones(MyHTTPServer.getConexiones() - 1);	
        }
      }
}
