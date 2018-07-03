import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;
import java.lang.Exception;
import java.net.Socket;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@SuppressWarnings({"serial"})
public class Sonda extends UnicastRemoteObject
implements InterfazSonda, Serializable 
{
    private String nombre="";
    private String tipo="";
	private int valorTemp=0;
	private int valorHum=0;
	public Sonda (String nombre) throws RemoteException , IOException
	{
        super();
        this.nombre=nombre;
		leerArchivo(nombre);
		
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
	public String getNombre() throws java.rmi.RemoteException{return nombre;}
	public String getTipo() throws java.rmi.RemoteException{return tipo;}
	public int valorTemp() throws java.rmi.RemoteException{return valorTemp;}
	public int valorHum()  throws java.rmi.RemoteException{return valorHum;}
    //public int getValor() throws java.rmi.RemoteException{return valor;}
    public String getFecha() throws java.rmi.RemoteException{
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return hourdateFormat.format(date);
	}
	
	public void setValor(int valor) throws java.rmi.RemoteException{
		if(getTipo().equals("temperatura")){
			valor = modificarTemperatura(valor);
		}else if(getTipo().equals("humedad")){
			valor = modificarHumedad(valor);
		}
	}

	public int modificarHumedad(int humedad) throws java.rmi.RemoteException{
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

	public int modificarTemperatura(int temperatura) throws java.rmi.RemoteException{
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

	public void activarActuador(String nombre) throws java.rmi.RemoteException{
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
}
