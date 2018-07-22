namespace SDPr2
{
    partial class Inicio
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.usuTB = new System.Windows.Forms.TextBox();
            this.ConTB = new System.Windows.Forms.TextBox();
            this.button1 = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // usuTB
            // 
            this.usuTB.Location = new System.Drawing.Point(55, 27);
            this.usuTB.Name = "usuTB";
            this.usuTB.Size = new System.Drawing.Size(100, 20);
            this.usuTB.TabIndex = 0;
            this.usuTB.Text = "Usuario";
            this.usuTB.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            // 
            // ConTB
            // 
            this.ConTB.Location = new System.Drawing.Point(55, 54);
            this.ConTB.Name = "ConTB";
            this.ConTB.Size = new System.Drawing.Size(100, 20);
            this.ConTB.TabIndex = 1;
            this.ConTB.Text = "Contraseña";
            this.ConTB.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(66, 80);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 2;
            this.button1.Text = "Iniciar Sesión";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // Inicio
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(209, 121);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.ConTB);
            this.Controls.Add(this.usuTB);
            this.Name = "Inicio";
            this.Text = "Inicio";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox usuTB;
        private System.Windows.Forms.TextBox ConTB;
        private System.Windows.Forms.Button button1;
    }
}