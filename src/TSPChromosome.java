import java.util.*;
/**
 * One chromosome represents an array with a possible order in which the cities can be visited
 *as a solution to TSP
 * 
 * @author (Omar Farooq ) 
 * @version (4/20/2014)
 */
public class TSPChromosome implements Comparable<TSPChromosome>
{
    // instance variables - 
    int[] orderOfVisiting ;  //the order in which the cities will be visited
    int numCities;   //the number of total cities
    double score ;   //the distance of this particular circuit
    double fitness; // this is the inverse of the score scaled by a factor of 1000000

    static Random generator = new Random();  //an instance of the RNG
    static int[][] distanceMatrix ;  //matrix representing scores of each vertex with the other

    /**
     * Constructor for objects of class TSPChromosome
     * Constructs a random chromosome with the given size
     */
    public TSPChromosome(int size)
    {
        orderOfVisiting = new int[size];
        for (int i =0; i<size ; i++) {
            orderOfVisiting [i]=i;
        }
        orderOfVisiting = shuffleArray(orderOfVisiting);
        numCities = size;

        if (distanceMatrix!= null) {
            this.findScore(null);
        }

        if (TravelingSalesman.DEBUG) {
            System.out.println(this.toString() );
        }
    }

    /**
     * Constructor for objects of class TSPChromosome
     * Constructs a random given the order in which the cities are to be visited
     */
    public TSPChromosome(int[] order)
    {
        orderOfVisiting = order;
        numCities = order.length;
        if (distanceMatrix!= null) {
            this.findScore(null);
        }

        //         if (TravelingSalesman.DEBUG) {
        //             System.out.println(this.toString() );
        //         }

    }

    /**
     * Used by another class to pass in the distance/score matrix
     * 
     */
    public static void setDistanceMatrix(int [][] input){
        distanceMatrix = input;
    }

    /*
     * Performs a one point crossover, returns the first parent with modified first half
     */    
    static int[] generateFirstChild( TSPChromosome firstParent, TSPChromosome secondParent, int point)
    {
        int crossOverPoint;
        if( point <0){
            crossOverPoint =  1+ generator.nextInt(firstParent.numCities -2 );
        }
        else{
            crossOverPoint = point;
        }
        int[] one = firstParent.orderOfVisiting.clone();
        int[] two = secondParent.orderOfVisiting.clone();

        for (int i = 0 ; i<=crossOverPoint ; i++ ) {
            swap(one, i , findIndex(one, two[i]));
            one[i]= two[i];
        }
        return one;
    }

    /*
     * Performs a one point crossover, returns the second parent with modified first half modified
     */  
    static  int[] generateSecondChild(TSPChromosome firstParent, TSPChromosome secondParent, int point)
    {
        return (generateFirstChild(secondParent,firstParent,point));
    }

    /**
     * Performs a point mutation, with the probability passed in
     */
    void performMutation(double prob)
    {
        if(generator.nextDouble()<prob  && orderOfVisiting.length!=0){
            int a = generator.nextInt(orderOfVisiting.length);
            int b=  generator.nextInt(orderOfVisiting.length);

            int tmp = orderOfVisiting[a];
            orderOfVisiting[a]=orderOfVisiting[b];
            orderOfVisiting[b]= tmp;
        }

    }

    /*
     * If the scoreing matrix is given this methods finds the distance of the circuit represented by 'this'
     * chromosome. Also set the instance variable score. 
     */
    double findScore(int[][] scoreMatrix )
    {
        if (distanceMatrix ==null){
            return 0;
        }
        else{ scoreMatrix = distanceMatrix;}
        double distance = 0;
        for(int i=0 ; i< numCities -1; i++){
            distance += scoreMatrix[ orderOfVisiting[i] ][orderOfVisiting [i+1]];
        }
        distance+= scoreMatrix[0][numCities-1];

        score =distance;
        fitness = 1000000/distance;
        return distance;
    }

    /**
     * Overrides the method given in the Comparable interface
     * 
     */
    public int compareTo(TSPChromosome that)
    {   
        if (this.score == 0 || that.score ==0) {
            System.out.println("ERROR:Scores were not calculated for both Chromosomes");
            System.exit(1);
        }
        else if (this.score < 0 || that.score <0) {
            System.out.println("ERROR: Negative Scores, possible overflow");
            System.exit(1);
        }
        else if(this.score == that.score){
            return 0;
        }
        else if (this.score > that.score) {
            return 1;
        }    
        else if (this.score < that.score) {
            return -1;
        }
        return 0;

    }

     // Implementing Fisher–Yates shuffle
    private static int[] shuffleArray(int[] ar)
    {    
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = generator.nextInt(i + 1);
            // Simple swap
            int tmp = ar[index];
            ar[index] = ar[i];
            ar[i] = tmp;
        }
        return ar;
    }

    //Swaps two points in an array
    private static void swap ( int[] ar, int a , int b){
        int tmp = ar[a];
        ar[a] = ar[b];
        ar[b] = tmp;

    }

    //finds the index of the value specified in the array.
    private static int findIndex(int[] array, int value) {
        for(int i=0; i<array.length; i++) {
            if(array[i] == value){
                return i;}
        }
        return 0;
    }

    //converts Chromosome to string for printing
    public String toString(){
        String x = "";
        for (int i=0;i<numCities ;i++ ) {
            x+= orderOfVisiting[i] + " ";
        }
        x+= "  ----  " + this.score;
        return x;
    }
}
