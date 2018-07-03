import java.rmi.Remote;

public interface InterfazSonda extends Remote {
    public String getNombre() throws java.rmi.RemoteException;
    public String getTipo() throws java.rmi.RemoteException;
    //public int getValor() throws java.rmi.RemoteException;
    public String getFecha() throws java.rmi.RemoteException;
    public void setValor(int valor) throws java.rmi.RemoteException;
    public int modificarHumedad(int humedad) throws java.rmi.RemoteException;
    public int modificarTemperatura(int temperatura) throws java.rmi.RemoteException;
    public void activarActuador(String nombre) throws java.rmi.RemoteException;
    public int valorTemp() throws java.rmi.RemoteException;
	public int valorHum()  throws java.rmi.RemoteException;
}
