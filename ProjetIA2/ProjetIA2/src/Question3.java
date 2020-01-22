import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class Question3 {
    static BufferedImage bi;
    public static void main(String[] args) throws IOException
    {
        String path = "./";
        String imageMMS = path + "people.png";

        // Lecture de l'image ici
        BufferedImage bui = ImageIO.read(new File(imageMMS));

        int width = bui.getWidth();
        int height = bui.getHeight();
        System.out.println("Hauteur=" + width);
        System.out.println("Largeur=" + height);

        //******************************************** Obtenir la couleur du pixel (0,0) sous 24 BITS
        int pixel = bui.getRGB(0, 0);
        //System.out.println("Pixel 0,0 = "+pixel);
        Color c = new Color(pixel);
        System.out.println("RGB = "+c.getRed()+" "+c.getGreen()+" "+c.getBlue());
        //Calcul des trois composant de couleurs normalisé à 1
        double[] pix = new double[3];
        pix[0] = (double) c.getRed()/255.0;
        pix[1] = (double) c.getGreen()/255.0;
        pix[2] = (double) c.getBlue()/255.0;
        System.out.println("RGB normalisé= "+pix[0]+" "+pix[1]+" "+ pix[2]);

        //******************************************** obtenir tous les pixel de 0,0 jusqua width,height
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
        // ****** MIX GAUSS ******//
        //*****************************//

        int D = 3; // R G B
        int M = nbrPix; // Nbr Pixels
        int K = 4;      // Nbr couleurs de M&M's

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

        /**remplissage des deux tableaux positions & données !**/
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

        /**Initialisation des clusters**/

        clusters[0][0] = 255/255.; clusters[0][1] = 0/255.; clusters[0][2] = 80/255.;  // ROSE
        clusters[1][0] = 180/255.; clusters[1][1] = 67/255.; clusters[1][2] = 183/255.; // VIOLET
        clusters[2][0] = 225/255.; clusters[2][1] = 76/255.; clusters[2][2] = 7/255.; // orange
        clusters[3][0] = 0/255.; clusters[3][1] = 194/255.; clusters[3][2] = 193/255.;   // BLEU


        /** assignement**/
        assi = MixGauss.assigner(donnees,clusters,rho,sigmas);

        /** declaration des images **/
        BufferedImage bui_outRose = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage bui_outViolet = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage bui_outOrange2 = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage bui_outBleu2 = new BufferedImage(bui.getWidth(),bui.getHeight(),BufferedImage.TYPE_3BYTE_BGR);


        /** dessiner sur les images **/
        for (int i = 0; i < donnees.length; i++) {
            bui_outRose.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            bui_outViolet.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            bui_outOrange2.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            bui_outBleu2.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color(255,255,255).getRGB());
            if(MixGauss.argmax(assi[i])==0) {
                bui_outRose.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }
            if(MixGauss.argmax(assi[i])==1) {
                bui_outViolet.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }
            if(MixGauss.argmax(assi[i])==2) {
                bui_outOrange2.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }

            if(MixGauss.argmax(assi[i])==3) {
                bui_outBleu2.setRGB(positionsDonnees[i][1], positionsDonnees[i][0], new Color((int)(donnees[i][0]*255),(int)(donnees[i][1]*255),(int)(donnees[i][2]*255)).getRGB());
            }

        }

        /** sauvegarde des images **/
        ImageIO.write(bui_outRose, "PNG", new File(path+"PeopleRose.png"));
        ImageIO.write(bui_outViolet, "PNG", new File(path+"PeopleViolet.png"));
        ImageIO.write(bui_outOrange2, "PNG", new File(path+"PeopleOrange.png"));
        ImageIO.write(bui_outBleu2, "PNG", new File(path+"PeopleBleu.png"));





    }
}