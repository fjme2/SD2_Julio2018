package master;
import java.lang.Exception;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.apache.axis2.AxisFault;

public class Sonda
{
    private String nombre="";
    private String tipo="";
	private int valorTemp=0;
	private int valorHum=0;
	private String key="SD";
	private String usu="";
	public Sonda () throws IOException
	{
    }
	public void setDatos(String name, String usuario) throws IOException
	{
		Crypto crypto= new Crypto();
    	try{
    		
    		nombre=crypto.decrypt(name, key) ;
    		usu = crypto.decrypt(usuario, key) ;
    		leerArchivo(nombre);
    		
    	}catch(Exception e){}
    }
    
    public String leerArchivo(String s) throws IOException {
		String leido = "";
		System.out.println(s);
		//ruta del fichero
		File fichero = new File(System.getProperty("user.dir") + "/" + s + ".txt");
		System.out.println("La ruta del fichero es: " + fichero.getAbsolutePath());
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea = "";
			while((linea = br.readLine()) != null) {
				leido += linea;
				String[] datos = linea.split("\\=");
				if(datos[0].contains("temperatura")){
					valorTemp=modificarTemperatura(Integer.parseInt(datos[1]));
				}else if(datos[0].contains("humedad")){
					valorHum = modificarHumedad(Integer.parseInt(datos[1]));
				}
			}
			System.out.println(leido);
			fr.close();
			br.close();
		} catch(Exception e) {
			FileWriter archivo = null;
        	PrintWriter pw = null;
        	try{
            	archivo = new FileWriter(s + ".txt");
            	pw = new PrintWriter(archivo);
				Random ran = new Random();
				pw.println("temperatura=" + (ran.nextInt(10)+25));
				pw.print("humedad=" + (ran.nextInt(25)+40));
				leido = leerArchivo(s);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (null != fichero){
						archivo.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
		}
		return leido;
    }	
	public String getNombre(){
		Crypto crypto= new Crypto();
		String encry ="";
		try{
			encry = crypto.encrypt(nombre, key);
			EscribirLog("Pedido nombre de la sonda por el usuario " + usu);
		}catch(Exception e){}
		return encry;
		}
	public String getTipo(){
		Crypto crypto= new Crypto();
		String encry ="";
		try{
			encry = crypto.encrypt(tipo, key);
			EscribirLog("Pedido tipo del valor");
		}catch(Exception e){}
		return encry;
	}
	public String valorTemp(){
		Crypto crypto= new Crypto();
		String encry ="";
		try{
			encry = crypto.encrypt(String.valueOf(valorTemp), key);
			EscribirLog("Pedido valor de temperatura por el usuario " + usu);
		}catch(Exception e){}
		return encry;
	}
	public String valorHum(){
		Crypto crypto= new Crypto();
		String encry ="";
		try{
			encry = crypto.encrypt(String.valueOf(valorHum), key);
			EscribirLog("Pedido valor de humedad por el usuario " + usu);
		}catch(Exception e){} 
		return encry;
	}
    //public int getValor() throws java.rmi.RemoteException{return valor;}
    public String getFecha(){
    	Crypto crypto= new Crypto();
		String encry ="";
		Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		try{
			encry = crypto.encrypt(hourdateFormat.format(date), key);
		}catch(Exception e){}
		return encry;
        
	}
	
	public void setValor(int valor){
		if(getTipo().equals("temperatura")){
			valor = modificarTemperatura(valor);
		}else if(getTipo().equals("humedad")){
			valor = modificarHumedad(valor);
		}
	}

	public int modificarHumedad(int humedad) {
		if(humedad<40){
			while(humedad < 40){
				System.out.println("Humedad muy baja. Goteo activado");
				humedad++;
			}			
		}else if(humedad > 75){
			while(humedad > 75){
				System.out.println("Humedad muy alta. Deshumidificador activado");
				humedad--;
			}	
		}

		return humedad;
	}

	public int modificarTemperatura(int temperatura){
		if(temperatura<25){
			while(temperatura < 25){
				System.out.println("Temperatura muy baja. Calefactor activado");
				temperatura++;
			}			
		}else if(temperatura > 35){
			while(temperatura > 35){
				System.out.println("Humedad muy alta. Ventilación activada");
				temperatura--;
			}	
		}

		return temperatura;
	}

	public void activarActuador(String nombre){
		String leido = "";
		System.out.println(nombre);
		//ruta del fichero
		File fichero = new File(System.getProperty("user.dir") + "/" + nombre + ".txt");
		System.out.println("La ruta del fichero es: " + fichero.getAbsolutePath());
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);
			String linea = "";
			while((linea = br.readLine()) != null) {
				leido += linea;
			}
			System.out.println(leido);
            String[] datos = leido.split("\\=");
            tipo = datos[0];
            setValor(Integer.parseInt(datos[1]));
			fr.close();
			br.close();
		} catch(Exception e) {
			FileWriter archivo = null;
        	PrintWriter pw = null;
        	try{
            	archivo = new FileWriter(nombre + ".txt");
            	pw = new PrintWriter(fichero);
				Random ran = new Random();
            	if(nombre.contains("temperatura")){
					pw.print("temperatura=" + (ran.nextInt(4)+1));
				}else if(nombre.contains("temperatura")){
					pw.print("humedad=" + (ran.nextInt(5)+10));
				}
				activarActuador(nombre);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (null != fichero){
						archivo.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
	public void EscribirLog(String datos){
        FileWriter fichero;
        Date fecha1 = new Date ();
		try {
			fichero = new FileWriter(System.getProperty("user.dir") + "/log.txt");
			PrintWriter entrada =  new PrintWriter(fichero);
            entrada.println(fecha1.toString()+" - "+ datos);  
            fichero.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
	
	public void setUsu(String usuario){
		Crypto crypto= new Crypto();
    	try{
    		
    		usu=crypto.decrypt(usuario, key) ;
    		EscribirLog(usu + " Registrado");
    		}catch(Exception e){}
    	
	}
}
