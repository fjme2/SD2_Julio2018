using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SDPr2
{
    public class relacion
    {
        private string nombre = "";
        private string ip = "";

        public relacion()
        {

        }
        public relacion(string nombre, string ip)
        {
            this.Nombre = nombre;
            this.Ip = ip;
        }

        public string Nombre { get => nombre; set => nombre = value; }
        public string Ip { get => ip; set => ip = value; }
    }
}
