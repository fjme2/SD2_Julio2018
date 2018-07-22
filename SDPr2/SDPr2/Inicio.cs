using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Data.SqlClient;

namespace SDPr2
{
    public partial class Inicio : Form
    {
        public Inicio()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {

            if (usuTB.Text == "Fran" && ConTB.Text == "1234")
            {
                Peticion pet = new Peticion(usuTB.Text);
                pet.Show();
                this.Hide();
            }else{
                MessageBox.Show("Usuario o contraseña incorrectos.");
            }
        }
    }
}
