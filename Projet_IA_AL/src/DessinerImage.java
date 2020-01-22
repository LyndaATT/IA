import java.util.*;

public class DessinerImage {
	/* Cette classe me servira à obtenir des images avec le pire score,
	 *La condition pour savoir si elles sont mals classées ou pas se fera avant l'appel de ses méthodes !
	 */
	
	// Mes attributs :
	private int indiceDuMinimum;
    private float valeurMinimum;
    private HashMap<Integer, Float> imagesMoinsClasse;

    /** 
     * Constructor de ma classe :  
     */
    public DessinerImage(){
        this.imagesMoinsClasse = new HashMap<Integer,Float>();
        this.valeurMinimum = (float)1;
    }
    
    /** maxHM : méthodes qui cherche la clé de mon HashMap qui a la plus grande Valeur
     * @param HM : Ma HashMap tq <Key = imageIdx, Value = scoreInference>
     * @return la clé (l'indice) de la plus grande valeur (score d'inférence)
     */
    public int maxHM(HashMap<Integer, Float> HM){
        int indicexMax = -1;
        float scoreMax = 0;
        for (int i : HM.keySet()) {
            float f =HM.get(i);
            if(f>scoreMax){
                indicexMax = i;
            }
        }
        return indicexMax;
    }
    
    /** add :  Méhode qui ajoute une image (idx et score d'inférence) à notre HashMap
     *  En s'assurant que la HashMap ne stock que les 5 images associées aux 5 pires scores
     * @param indice : l'indice de l'image dans validData
     * @param score  : son score d'inferance 
     */
    public void add(int indice, float score){
        if(this.imagesMoinsClasse.size()!=0){
            if(this.imagesMoinsClasse.size()<5) {
                this.imagesMoinsClasse.put(indice,score);
                if (score <= this.valeurMinimum) {
                    this.valeurMinimum = score;
                    this.indiceDuMinimum = indice;
                }
            } else { if(this.imagesMoinsClasse.size()>=5) { // Dans le cas où notre HashMap est de taille 5 on cherche 
                if (score <= this.valeurMinimum) {			// On cherche l'image qui est la mieux classée et on la remplace par la nouvelle image pour ainsi n'avoir que les pires scores 
                    this.imagesMoinsClasse.remove(maxHM(this.imagesMoinsClasse));
                    this.imagesMoinsClasse.put(indice, score);
                    this.valeurMinimum = score;
                    this.indiceDuMinimum = indice;
                } // Sinon on le rajoute pas;
              }
            }
        } else {
            this.imagesMoinsClasse.put(indice,score);
            this.valeurMinimum = score;
            this.indiceDuMinimum = indice;
        }
    }
    
    /** indiceMin 
     * @return le tableau contenant les indices des images obtenues dans l'ensemble "validData"
     */
    public int[] indiceMin(){
        int[] tab = new int[this.imagesMoinsClasse.size()];
        int j = 0;
        System.out.println(this.imagesMoinsClasse.size());
        for (int i: this.imagesMoinsClasse.keySet()) {
            tab[j] = i;
            j++;
        }
        return tab;
    }
    
    /** getIndiceMin : permet de récuperer un attribut de ma classe
     * @return : renvoie l'attribut 'indiceDuMinimum'
     */
    public int getIndiceMin(){
        return this.indiceDuMinimum;
    }
}
