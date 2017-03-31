using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Imaging;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Image_Compressor
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            timer1.Start();
            
        }
        private void CompressImage(Image sourceImage, int imageQuality, string savePath)
        {

            try
            {

                ImageCodecInfo jpegCodec = null;

                EncoderParameter imageQualitysParameter = new EncoderParameter(
                            System.Drawing.Imaging.Encoder.Quality, imageQuality);

                ImageCodecInfo[] alleCodecs = ImageCodecInfo.GetImageEncoders();

                EncoderParameters codecParameter = new EncoderParameters(1);
                codecParameter.Param[0] = imageQualitysParameter;


                for (int i = 0; i < alleCodecs.Length; i++)
                {
                    if (alleCodecs[i].MimeType == "image/jpeg")
                    {
                        jpegCodec = alleCodecs[i];
                        break;
                    }
                }

                sourceImage.Save(savePath, jpegCodec, codecParameter);
            }
            catch (Exception e)
            {
                throw e;
            }
        }


        private void button1_Click(object sender, EventArgs e)
        {

            DialogResult res = openFileDialog1.ShowDialog();
            if (res ==  DialogResult.Cancel )
            {
                MessageBox.Show("Image selection cancelled by user", "Information");
            }
            else
            {
                string img = openFileDialog1.FileName;
                pictureBox1.Image = System.Drawing.Bitmap.FromFile(img);
                trackBar1.Value = 100;
                long length = new System.IO.FileInfo(openFileDialog1.FileName).Length;
                long x = length / 1024;
                label4.Text = x.ToString() + "KB";
            }


        }
        private void cim(Image sourceImage, int imageQuality, string savePath)
        {
            try
            {
                ImageCodecInfo jpegCodec = null;

                EncoderParameter imageQualitysParameter = new EncoderParameter(
                            System.Drawing.Imaging.Encoder.Quality, imageQuality);

                ImageCodecInfo[] alleCodecs = ImageCodecInfo.GetImageEncoders();

                EncoderParameters codecParameter = new EncoderParameters(1);
                codecParameter.Param[0] = imageQualitysParameter;

                for (int i = 0; i < alleCodecs.Length; i++)
                {
                    if (alleCodecs[i].MimeType == "image/jpeg")
                    {
                        jpegCodec = alleCodecs[i];
                        break;
                    }
                }

                sourceImage.Save(savePath, jpegCodec, codecParameter);
            }
            catch (Exception e)
            {
                throw e;
            }
        }


        private void button2_Click(object sender, EventArgs e)
        {
            folderBrowserDialog1.ShowDialog();
            string savepath = folderBrowserDialog1.SelectedPath;
            string img = openFileDialog1.FileName;
            cim(System.Drawing.Bitmap.FromFile(img), trackBar1.Value, savepath + "/compressedimage.jpg");

        }

        private void timer1_Tick(object sender, EventArgs e)
        {
           //nothing here now!
        }

        private void trackBar1_Scroll(object sender, EventArgs e)
        {
            long length = new System.IO.FileInfo(openFileDialog1.FileName).Length;
            long x = ((trackBar1.Value * length) / 100)/1024;
            label5.Text = x.ToString() + "KB";
            
        }
    }
}
