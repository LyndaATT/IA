import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


import javax.imageio.ImageIO;

public class Question2 {
    static BufferedImage bi;

    public static void main(String[] args) throws IOException{
        String path = "./";
        String imageMMS = path + "mms.png";

        /** Lecture de l'image ici **/
        BufferedImage bui = ImageIO.read(new File(imageMMS));

        int width = bui.getWidth();
        int height = bui.getHeight();
        System.out.println("Hauteur=" + width);
        System.out.println("Largeur=" + height);

        /*** Obtenir la couleur du pixel (0,0) sous 24 BITS**/
        int pixel = bui.getRGB(0, 0);
        Color c = new Color(pixel);
        System.out.println("RGB = "+c.getRed()+" "+c.getGreen()+" "+c.getBlue());
        /**Calcul des trois composant de couleurs normalisé à 1**/
        double[] pix = new double[3];
        pix[0] = (double) c.getRed()/255.0;
        pix[1] = (double) c.getGreen()/255.0;
        pix[2] = (double) c.getBlue()/255.0;
        System.out.println("RGB normalisé= "+pix[0]+" "+pix[1]+" "+ pix[2]);

        /**obtenir tous les pixel de 0,0 jusqua width,height**/
        int[] im_pixels = bui.getRGB(0, 0, width, height, null, 0, width);

        /** Creation du tableau **/
        Color[] tabColor= new Color[im_pixels.length]; // COLOR CONTIENT LA VALEUR DE CHAQUE PIXEL SOUS 24 BITS
        for(int i=0 ; i<im_pixels.length ; i++)
            tabColor[i]=new Color(im_pixels[i]);

        int nbrPix = 0;
        /** sauvegarde de l'image **/
        BufferedImage bui_out = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        for(int i=0 ; i<height ; i++)
        {
            for(int j=0 ; j<width ; j++) {
                bui_out.setRGB(j,i,tabColor[i*width+j].getRGB());
                nbrPix++;}

        }
        /***************************************************************************************************************/
        Random rd = new Random();
        int D = 3;
        int M = nbrPix;
        double eps = 0.1;
        double[][] data = new double[M][D];
        int randooom = rd.nextInt(M);

        // Remplissage du tableau data
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                data[i * width + j][0] = (double) (tabColor[i * width + j].getRed()) / 255.;
                data[i * width + j][1] = (double) (tabColor[i * width + j].getGreen()) / 255.;
                data[i * width + j][2] = (double) (tabColor[i * width + j].getBlue()) / 255.;
            }
        }
        FileWriter f = new FileWriter("ScoreQuestion2.d");
        for(int K = 2; K<=10; K++){
            double[][] centres = new double[K][D];
            double[][] sigmas = new double[K][D];
            double[] rho = new double[K];
            double score = 0; // la variable qui stocke le meilleur score d'une partition
            // initialisation des sigmas
            for (int itter = 0; itter <K ; itter++) {
                for (int j = 0; j <D ; j++) {
                    sigmas[itter][j] = 0.5;
                }
                rho[itter] = 1./K;
            }
            for (int i = 0; i <10 ; i++) {

                // declaration tab d'assignement
                double[][] ass= new double[M][K] ;
                double sc = 0; // la variable qui stocke le score à chaque itération
                centres = MixGauss.ClustersInit(data,rd.nextInt(M),K);
                double[][] centres2 = new double[K][D];
                do{
                    centres2 = centres.clone();
                    ass= MixGauss.assigner(data,centres,rho,sigmas);
                    rho = MixGauss.UpdateRo(ass);
                    centres = MixGauss.Updateclusters(ass,data);
                    sigmas = MixGauss.sigmasUpdate(data,centres,ass);
                }while(!MixGauss.sameAs(centres,centres2,eps));
                /** Calcul du score d'une partition**/
                sc = MixGauss.scoreTotal(data,centres,rho,sigmas);

                /** Calcul du meilleur score d'une partition **/
                if(sc>score){
                    score = sc;
                }

            }
            System.out.println("pour K = "+K+" le score est "+score);
            f.write("" + K + "   " + score + "\n");
        }
        f.close();

    }

}
