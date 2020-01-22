import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class LoadSaveImage {
    static BufferedImage bi;
    public static void main(String[] args) throws IOException
    {
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


        //*****************************//
        // ****** Application de l'algo MIX GAUSS ******//
        //*****************************//

        int D = 3; // R G B
        int M = nbrPix; // Nbr Pixels
        int K = 7;      // Nbr couleurs de M&M's

        double[][] donnees  = new double[M][D];
        int[][] positionsDonnees  = new int[M][D];
        double[][] clusters = new double[K][D];
        double[] rho = new double[K];
        double[][] sigmas = new double[K][D];
        double[][] assi = new double[M][K];


        /** Initialisation rho et sigmas**/
        for (int i = 0; i < K; i++) {
            rho[i] = 1./K;
            for (int j = 0; j < D ; j++) {
                sigmas[i][j] = 0.5;
            }
        }

        /** remplissage des deux tableaux positions & données ! **/
        int p = 0;
        for(int i=0 ; i<height ; i++) {
            for(int j=0 ; j<width ; j++){
                double r = (double)(tabColor[i * width + j].getRed())/255;
                double g = (double)(tabColor[i * width + j].getGreen())/255;
                double b = (double)(tabColor[i * width + j].getBlue())/255;
                positionsDonnees[p][0] = i;
                positionsDonnees[p][1] = j;
                donnees[p][0] = r;
                donnees[p][1] = g;
                donnees[p][2] = b;
                p++;
            }
        }

        /** Initialisation des clusters **/

        clusters[0][0] = 240/255.; clusters[0][1] = 240/255.; clusters[0][2] = 3/255.;  // Jaune
        clusters[1][0] = 35/255.; clusters[1][1] = 255/255.; clusters[1][2] = 35/255.; // Vert
        clusters[2][0] = 239/255.; clusters[2][1] = 30/255.; clusters[2][2] = 0/255.; // orange
        clusters[3][0] = 30/255.; clusters[3][1] = 10/255.; clusters[3][2] = 10/255.;   // Noir
        clusters[4][0] = 220/255.; clusters[4][1] = 10/255.; clusters[4][2] = 23/255.; // Rouge
        clusters[5][0] = 14/255.; clusters[5][1] = 110/255.; clusters[5][2] =170/255.;// bleu
        clusters[6][0] =  224/255.; clusters[6][1] = 210/255.; clusters[6][2] = 165/255.; // Beige




        /** assignement donnee/cluster **/
        assi = MixGauss.assigner(donnees,clusters,rho,sigmas);


        /** Declaration des nouvelles images **/
        BufferedImage bui_outJaune = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage bui_outVert = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage bui_outOrange = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage bui_outNoir = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage bui_outRouge = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage bui_outBleu = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);


        /** Dessiner les petites boules sur les images déclarees au dessus **/
        for (int i = 0; i < donnees.length; i++) {
            bui_outJaune.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            bui_outVert.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            bui_outOrange.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            bui_outNoir.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            bui_outRouge.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            bui_outBleu.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());

            if(MixGauss.argmax(assi[i])==0) {
                bui_outJaune.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }
            if(MixGauss.argmax(assi[i])==1) {
                bui_outVert.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }
            if(MixGauss.argmax(assi[i])==2) {
                bui_outOrange.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }
            if(MixGauss.argmax(assi[i])==3) {
                bui_outNoir.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }
            if(MixGauss.argmax(assi[i])==4) {
                bui_outRouge.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }
            if(MixGauss.argmax(assi[i])==5) {
                bui_outBleu.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }
        }

        /** On sauvegarde les images après le dessin **/
        ImageIO.write(bui_outJaune, "PNG", new File(path+"m&m'sJaunes.png"));
        ImageIO.write(bui_outVert, "PNG", new File(path+"m&m'sVertes.png"));
        ImageIO.write(bui_outOrange, "PNG", new File(path+"m&m'sOranges.png"));
        ImageIO.write(bui_outNoir, "PNG", new File(path+"m&m'sNoires.png"));
        ImageIO.write(bui_outRouge, "PNG", new File(path+"m&m'sRouges.png"));
        ImageIO.write(bui_outBleu, "PNG", new File(path+"m&m'sBleues.png"));





    }
}
