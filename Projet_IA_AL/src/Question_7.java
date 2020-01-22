import java.io.FileWriter;
import java.io.IOException;
import mnisttools.MnistReader;

public class Question_7 {
	  public static String path="/home/lynda/Documents/ml/";
	    public static String labelDB=path+"emnist-byclass-train-labels-idx1-ubyte";
	    public static String imageDB=path+"emnist-byclass-train-images-idx3-ubyte";
	   
	    /** Parametres */
	    // Na exemples pour l'ensemble d'apprentissage
	    public static final int Na = 1000;
	    // Nv exemples pour l'ensemble d'évaluation
	    public static final int Nv =1000;
	    // Nv exemples pour l'ensemble de test 
	    public static MnistReader db = new MnistReader(labelDB, imageDB);
	    public static int longueur = db.getImage(1)[1].length;
	    public static int largeur = db.getImage(1).length;



	    /***
	     *  BinariserImage :
	     *      image: une image int à deux dimensions (extraite de MNIST)
	     *      seuil: parametre pour la binarisation
	     *
	     *  on binarise l'image à l'aide du seuil indiqué
	     *
	     */
	    public static int[][] BinariserImage(int[][] image, int seuil) {
	        int[][] imageBinarise = new int[image.length][image[0].length];
	        for(int j = 0; j < image.length; j++) {
	            for(int i = 0;  i < image[j].length; i++) {
	                if (image[j][i] >= seuil) {
	                    imageBinarise[j][i] = 1;
	                } else {
	                    imageBinarise[j][i] = 0;
	                }
	            }
	        }
	        return imageBinarise;
	    }


	    /***
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

	    
	    
	    /** Main contenant la partie du code permettant de répondre à la question 7**/
	    
	    public static void main(String[] args) throws IOException {

	       FileWriter fw = new FileWriter("Nombre d'erreurs pour l'ensemble d'apprentissage.d");
	       FileWriter fw2 = new FileWriter("Nombre d'erreurs pour l'ensemble de validation .d");
	       int ilabel = 0;
         int i = 0;
         int it = 1;
         float[][] trainData = new float[Na][largeur*longueur+1];
         float[][] validData = new float[Nv][largeur*longueur+1];
         int [] label = new int[Na];
         int [] validLabel = new int [Nv];
         float eta = (float)0.001;
         int itterationMax = 200;
                   

	     /**Chargement de l'nsemble d'apprentissage **/
	            it=1;
	            i=0;
	            while (it   < Na + 1) {
	                i++;
	                ilabel = db.getLabel(i);
	                if (ilabel >= 10 && ilabel <= 36) {
	                    label[it - 1] = ilabel - 10;
	                    trainData[it - 1] = ConvertImage(BinariserImage(db.getImage(i), 180));
	                    it++;
	                }
	            }
	            
	            
	      /**Chargement de l'nsemble de validation**/
	            it=1;
	            while (it+i < Nv + 1+i) {
	                i++;
	                ilabel = db.getLabel(i);
	                if (ilabel >= 10 && ilabel <= 36) {
	                    validLabel[it - 1] = ilabel - 10;
	                    validData[it - 1] = ConvertImage(BinariserImage(db.getImage(i), 180));
	                    it++;
	                }
	            }
	            
	      /**Initialisation des poids du perceptron**/
	            float[][] poids = PerceptronMultiNewLabel.InitialiseW(longueur * largeur + 1);
	            
	            
	      /**Tracé de l'evolution du nombre d'erreur pour Na et Nv en fonction de l'itteration**/
	             
              for (int itteration = 0; itteration < itterationMax; itteration++) {
                  int nbErreurApprentissage = PerceptronMultiNewLabel.epoqueApprentissage(trainData, poids, label, eta);
                  fw.write("" + itteration + "    " + nbErreurApprentissage + "\n");
                  int nbErreurValid= PerceptronMultiNewLabel.epoqueValidation(validData, poids, validLabel, eta);
                  fw2.write("" + itteration + "    " + nbErreurValid+ "\n");                   
              }
              fw.close();
              fw2.close();
              
              
         /** Le score obtenu pour chaque ensemble **/
              System.out.println("Score sur l'ensemble d'apprentissage = "+ (float)(PerceptronMultiNewLabel.epoqueApprentissage(trainData, poids, label, eta))/100+"%");
              System.out.println("Score sur l'ensemble de validation = "+ (float)(PerceptronMultiNewLabel.epoqueValidation(validData, poids, validLabel, eta))/100+"%");
             
          
	            
	    }     
}
