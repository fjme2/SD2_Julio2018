using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Collections;

namespace SDPr2
{
    public partial class Peticion : Form
    {
        private String key = "92AE31A79FEEB2A3";
        private Sonda.Sonda sonda;
        private String usu = "";
        private List<relacion> rel = new List<relacion>();
        public Peticion(String usu)
        {
            this.usu = usu;
            InitializeComponent();
            comboBox2.Items.Add("Temperatura");
            comboBox2.Items.Add("Humedad");

            comboBox3.Items.Add("aumentarTemperatura");
            comboBox3.Items.Add("aumentarHumedad");
            comboBox3.Items.Add("disminuirTemperatura");
            comboBox3.Items.Add("disminuirHumedad");

        }

        private void groupBox1_Enter(object sender, EventArgs e)
        {

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void AñaB_Click(object sender, EventArgs e)
        {
            sonda = new Sonda.Sonda();
            sonda.Url = "http://" + urlTB.Text + "/SWSonda/services/Sonda?wsdl";
            try
            {
                MessageBox.Show(Decrypt(sonda.getNombre(), key));
                comboBox1.Items.Add(Decrypt(sonda.getNombre(), key));
                comboBox4.Items.Add(Decrypt(sonda.getNombre(), key));
            }
            catch(Exception ex)
            {
                MessageBox.Show(ex.ToString());
            }

            rel.Add(new relacion(sonTB.Text, urlTB.Text));
        }

        public RijndaelManaged GetRijndaelManaged(String secretKey)
        {
            var keyBytes = new byte[16];
            var secretKeyBytes = Encoding.UTF8.GetBytes(secretKey);
            Array.Copy(secretKeyBytes, keyBytes, Math.Min(keyBytes.Length, secretKeyBytes.Length));
            return new RijndaelManaged
            {
                Mode = CipherMode.CBC,
                Padding = PaddingMode.PKCS7,
                KeySize = 128,
                BlockSize = 128,
                Key = keyBytes,
                IV = keyBytes
            };
        }

        public byte[] Encrypt(byte[] plainBytes, RijndaelManaged rijndaelManaged)
        {
            return rijndaelManaged.CreateEncryptor()
                .TransformFinalBlock(plainBytes, 0, plainBytes.Length);
        }

        public byte[] Decrypt(byte[] encryptedData, RijndaelManaged rijndaelManaged)
        {
            return rijndaelManaged.CreateDecryptor()
                .TransformFinalBlock(encryptedData, 0, encryptedData.Length);
        }

        public String Encrypt(String plainText, String key)
        {
            var plainBytes = Encoding.UTF8.GetBytes(plainText);
            return Convert.ToBase64String(Encrypt(plainBytes, GetRijndaelManaged(key)));
        }

        public String Decrypt(String encryptedText, String key)
        {
            var encryptedBytes = Convert.FromBase64String(encryptedText);
            return Encoding.UTF8.GetString(Decrypt(encryptedBytes, GetRijndaelManaged(key)));
        }

        private void button1_Click(object sender, EventArgs e)
        {

            //MessageBox.Show(comboBox1.SelectedItem.ToString());
            string ip="";
            for(int i = 0; i < rel.Count; i++)
            {
                if(rel.ElementAt<relacion>(i).Nombre == comboBox1.SelectedItem.ToString())
                {
                    ip = rel.ElementAt<relacion>(i).Ip;
                }
            }
            sonda.Url = "http://" + ip + "/SWSonda/services/Sonda?wsdl";
            MessageBox.Show(comboBox2.SelectedItem.ToString());
            if (comboBox2.SelectedItem.ToString() == "Temperatura")
            {
                textBox2.Text = "Temperatura = " + Decrypt(sonda.valorTemp(), key) + "º";
                sonda.EscribirLog(Encrypt("Temperatura pedida", key), Encrypt(usu, key));
            }
            else
            {
                textBox2.Text = "Humedad = " + Decrypt(sonda.valorHum(), key) + "%";
                sonda.EscribirLog(Encrypt("Humedad pedida", key), Encrypt(usu, key));
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string ip = "";
            for (int i = 0; i < rel.Count; i++)
            {
                if (rel.ElementAt<relacion>(i).Nombre == comboBox4.SelectedItem.ToString())
                {
                    ip = rel.ElementAt<relacion>(i).Ip;
                }
            }
            sonda.Url = "http://" + ip + "/SWSonda/services/Sonda?wsdl";
            string valor = Decrypt(sonda.activarActuador(Encrypt(comboBox3.SelectedItem.ToString(), key)),key);
            sonda.EscribirLog(Encrypt("Actuador de " + comboBox3.SelectedItem.ToString() + " pedido", key), Encrypt(usu, key));
            Sonda.Sonda aux = new Sonda.Sonda();
            aux.Url = "http://" + ip + "/SWSonda/services/Sonda?wsdl";
            if (comboBox3.SelectedItem.ToString().Contains("Temperatura"))
            {              
                textBox3.Text = "Temperatura = " + Decrypt(aux.valorTemp(), key) + "º";
            }else if (comboBox3.SelectedItem.ToString().Contains("Humedad"))
            {
                textBox3.Text = "Humedad = " + Decrypt(aux.valorHum(), key) + "%";
            }

        }
    }
}
