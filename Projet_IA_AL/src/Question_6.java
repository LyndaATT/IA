import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import mnisttools.MnistReader;
import javax.imageio.ImageIO;

public class Question_6 {
	public static String path="/home/lynda/Documents/ml/";
    public static String labelDB=path+"emnist-byclass-train-labels-idx1-ubyte";
    public static String imageDB=path+"emnist-byclass-train-images-idx3-ubyte";
    public static String imageTestDB=path+"emnist-byclass-test-images-idx3-ubyte";
    public static String labelTestDB=path+"emnist-byclass-test-labels-idx3-ubyte";
    /* Parametres */
    // Na exemples pour l'ensemble d'apprentissage
    public static final int Na = 5000;
    // Nv exemples pour l'ensemble d'évaluation
    public static final int Nv =1000;
    // Nv exemples pour l'ensemble de test
    public static final int Nt =1000;
    // MnistReader db = new MnistReader(labelDB, imageDB) :
    public static MnistReader db = new MnistReader(labelDB, imageDB);
    public static int longueur = db.getImage(1)[1].length;
    public static int largeur = db.getImage(1).length;
    
    /*BinariserImage :
     *      image: une image int à deux dimensions (extraite de MNIST)
     *      seuil: parametre pour la binarisation
     *  on binarise l'image à l'aide du seuil indiqué
     */
    public static int[][] BinariserImage(int[][] image, int seuil) {
        int[][] imageBinarise = new int[image.length][image[0].length];
        for(int j = 0; j < image.length; j++) {
            for(int i = 0; i < image[j].length; i++) {
                if (image[j][i] >= seuil) {
                    imageBinarise[j][i] = 1;
                } else {
                    imageBinarise[j][i] = 0;
                }
            }
        }
        return imageBinarise;
    }
    
    /*
     *  ConvertImage :
     *      image: une image int binarisée à deux dimensions
     *
     *  1. on convertit l'image en deux dimension dx X dy, en un tableau unidimensionnel de tail dx.dy
     *  2. on rajoute un élément en première position du tableau qui sera à 1
     *  La taille finale renvoyée sera dx.dy + 1
     *
     */
    public static float[] ConvertImage(int[][] image) {
        float[] imageConvertie = new float[image.length*image[0].length+1]; imageConvertie[0] = 1;
        int z=1;
        for (int k = 0; k < image.length; k++) {
            for (int l = 0; l <image[0].length; l++) {
                imageConvertie[z] = image[k][l];
                z++;
            }
        }
        return imageConvertie;
    }
    
    /** labelToLetter
     * @param i  : l'index d'une image dans notre jeu de donnée
     * @return   : la lettre qu'elle représente
     */
    public static char labelToLetter(int i){
        char[] t = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L'};
        return t[i-10];
    }
    
    /** sauvegarderImage
     * @param idx : index de l'image à sauvegarder dans le jeu de donnée
     * @param p   : Portion du nom de notre fichier de sortie (.png)
     */
    public static void sauvegarderImage(int idx, String p) throws IOException {
        // On récupère une image
        int[][] image = db.getImage(idx);

        // On la sauvegarde
        int numberOfColumns = 28;//image.length;
        int numberOfRows = 28; //image[0].length;
        BufferedImage bimage = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_BYTE_GRAY);

        for (int ta = 0; ta < 28; ta++) {
            for (int j = 0; j < 28; j++) {
                int c = image[ta][j]; // ici 0 pour noir, 255 pour blanc
                int rgb = new Color(c, c, c).getRGB();
                bimage.setRGB(j, ta, rgb);
            }
        }

        // enregistrement

        File outputfile = new File(path + p + labelToLetter(db.getLabel(idx))+".png");
        ImageIO.write(bimage, "png", outputfile);
    }
    
    public static void main(String[] args) throws IOException  {
    	int ilabel = 0;
        int i = 0;
        int i2 = 0;
        int it = 1;
        float[][] trainData = new float[Na][largeur*longueur+1];
        float[][] validData = new float[Nv][largeur*longueur+1];
        float[][] testData = new float[Nt][largeur*longueur+1];

        int [] label = new int[Na];
        int [] validLabel = new int [Nv];
        int[] sonIndiceDansDB = new int[Nv];
        int [] testLabel = new int[Nt];
    //  Apprentissage
	    while (it + i < Na + 1 + i) {
	        i++;
	        i2++;
	        ilabel = db.getLabel(i);
	        if (ilabel >= 10 && ilabel <= 21) {
	            label[it - 1] = ilabel - 10;
	            trainData[it - 1] = ConvertImage(BinariserImage(db.getImage(i), 180));
	            it++;
	        }
	    }
	    it = 1;
     // Ainsi si on veut aceder a l'image m de validData il suffira de faire db.getImage(i2+m+1)
        while (it + i< Nv + 1 + i) {
            i++;
            ilabel = db.getLabel(i);
            if (ilabel >= 10 && ilabel <= 21) {
                validLabel[it - 1] = ilabel - 10;
                validData[it - 1] = ConvertImage(BinariserImage(db.getImage(i), 180));
                sonIndiceDansDB[it-1]= i;
                it++;
            }
        }
        	// On se fixe un etat
        float eta = (float)0.01;
	    	// On initialise un poid au hasard
	    float[][] poid = PerceptronMulti.InitialiseW(longueur * largeur + 1);
	    	// ne se fixe une itterationMAx
        int itterationMax = 200;
        	// Phase d'apprentissage
        for (int itteration = 0; itteration < itterationMax; itteration++) {
            int nbErreur = PerceptronMulti.epoqueApprentissage(trainData, poid, label, eta);
        }
        int nbErreurValidation = PerceptronMulti.epoqueValidation(validData, poid, validLabel, eta);
        System.out.println("Nombre d'erreur dans la phase de validation  : " + nbErreurValidation);
        
        // Question 6 : 5 images de label différent, mal classées et ayant les pires scores
        DessinerImage imageL10 = new DessinerImage();
        DessinerImage imageL12 = new DessinerImage();
        DessinerImage imageL14 = new DessinerImage();
        DessinerImage imageL16 = new DessinerImage();
        DessinerImage imageL18 = new DessinerImage();
        for (int m = 0; m < Nv; m++) {
            if(validLabel[m]==0){
                float[] probaDonnee = PerceptronMulti.InfPerception(validData[m], poid);
                int ind = PerceptronMulti.argmax(probaDonnee);
                if(ind != validLabel[m]){ // c'est à dire qu'il est mal classé !!!!
                    imageL10.add(m,probaDonnee[ind]);
                }
            }
            if(validLabel[m]==2){
                float[] probaDonnee = PerceptronMulti.InfPerception(validData[m], poid);
                int ind = PerceptronMulti.argmax(probaDonnee);
                if(ind != validLabel[m]){ // c'est à dire qu'il est mal classé !!!!
                    imageL12.add(m,probaDonnee[ind]);
                }
            }
            if(validLabel[m]==4){
                float[] probaDonnee = PerceptronMulti.InfPerception(validData[m], poid);
                int ind = PerceptronMulti.argmax(probaDonnee);
                if(ind != validLabel[m]){ // c'est à dire qu'il est mal classé !!!!
                    imageL14.add(m,probaDonnee[ind]);
                }
            }
            if(validLabel[m]==6){
                float[] probaDonnee = PerceptronMulti.InfPerception(validData[m], poid);
                int ind = PerceptronMulti.argmax(probaDonnee);
                if(ind != validLabel[m]){ // c'est à dire qu'il est mal classé !!!!
                    imageL16.add(m,probaDonnee[ind]);
                }
            }
            if(validLabel[m]==8) {
                float[] probaDonnee = PerceptronMulti.InfPerception(validData[m], poid);
                int ind = PerceptronMulti.argmax(probaDonnee);
                if (ind != validLabel[m]) { // c'est à dire qu'il est mal classé !!!!
                    imageL18.add(m, probaDonnee[ind]);
                }
            }
        }   // A la fin de notre boucle nous aurons les 5 éléments mal classé de chaque classe avec le pire score d'inférence.
            // Nous allons donc prendre l'indice du minimum d'entre eux et l'afficher !
        sauvegarderImage(sonIndiceDansDB[imageL10.getIndiceMin()+1], "Mal classées-Pire score");
        sauvegarderImage(sonIndiceDansDB[imageL12.getIndiceMin()+1], "Mal classées-Pire score");
        sauvegarderImage(sonIndiceDansDB[imageL14.getIndiceMin()+1], "Mal classées-Pire score");
        sauvegarderImage(sonIndiceDansDB[imageL16.getIndiceMin()+1], "Mal classées-Pire Score");
        sauvegarderImage(sonIndiceDansDB[imageL18.getIndiceMin()+1], "Mal classées-Pire Score");
	}
}
