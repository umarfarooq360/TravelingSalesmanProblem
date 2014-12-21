import java.util.*;

/**
 * A TSPPopulation represents one generation. This is basically a collection of TSPChromosomes.
 * It also contains a total fitness variable which helps in the roulette wheel selection.
 * 
 * @author (Omar Farooq) 
 * @version (a version number or a date)
 */
public class TSPPopulation
{
    //These are variables read from file and  set by another class using mutator methods
    static int chromosomeSize ;  
    static int populationSize;
    static double percentRetained;
    static double mutationProbability;
    static double crossOverProbability;

    //these are data fields for a TSP chromosome
    TSPChromosome[] population;
    double totalFitness = 0;

    /**
     * Constructor for objects of class TSPPopulation. Makes a random population
     * of default size
     */
    public TSPPopulation()
    {
        if (populationSize==0) {
            System.out.println("Error: no set populationSize");
            System.exit(1);
        }
        population = new TSPChromosome[populationSize];
        
        for (int i=0;i < populationSize ;i++ ) { //adding to array
            population[i] = new TSPChromosome(chromosomeSize);
            totalFitness+= population[i].fitness;
        }
    }

    /**
     * Constructor for objects of class TSPPopulation with a defined pop size
     */
    public TSPPopulation(int popSize)
    {
        population = new TSPChromosome[popSize];
        populationSize = popSize;
        for (int i=0;i< popSize ;i++ ) {  //adding to array
            population[i] = new TSPChromosome(chromosomeSize);
            totalFitness+= population[i].fitness;
        }

    }

    /**
     * Constructor for objects of class TSPPopulation with defined population and chromosome size
     */
    public TSPPopulation(int popSize, int chromSize)
    {
        chromosomeSize = chromSize;
        population = new TSPChromosome[popSize];
        populationSize = popSize;
        for (int i=0;i< popSize ; i++ ) {  //adding to array
            population[i] = new TSPChromosome(chromSize);
            totalFitness+= population[i].fitness;
        }

    }

    /**
     * Constructor for objects of class TSPPopulation with defined population.
     * This means that all the chromosomes are given and are just added to array
     */
    public TSPPopulation( TSPChromosome[] genes){

        population = genes;
        populationSize = genes.length;
        for (int i=0;i< genes.length ;i++ ) {  //adding to array
            totalFitness+= genes[i].fitness;
        }

    }

       /**
     * This method takes in a population returns the next generation.
     * It will keep the best percent retained of the old population 
     * but for the rest it will cross all the population depending on
     * their fitness. It will then perform a muttaion too.
     */
     public static TSPPopulation orgy ( TSPPopulation old){
        //take the reatined old population
        if(old ==null){
            System.out.println("Error: null population");
            System.exit(1);
        }

        old.sortPopulation();//sort them
        double total = old.totalFitness;
        
        //keep the best whatever percent of population
        int numberRetained = (int)(old.populationSize*(double)percentRetained);
        TSPChromosome[] retainedPop = new TSPChromosome[numberRetained];
        
        for (int i=0; i< numberRetained; i++) {
            retainedPop[i] = old.population[i];
        }
        
        //this essentailly helps us perform the roulette wheel selection
       double[] intervals = new double[old.populationSize];
       intervals[0]= old.population[0].fitness;
       //making the array for selection using fitness not distance
       for (int i=1 ;i < intervals.length   ;i++ ) {
             intervals[i] = intervals[i-1]+ old.population[i].fitness;
            }
        
        //creating some variables
            //the new population
        TSPChromosome[] newPop =  new TSPChromosome[old.populationSize-numberRetained];
        int crossOverPoint;
        //the parents
       TSPChromosome firstParent;
        TSPChromosome secondParent;
        //counters
        int i = 0; //position in the roulette wheel
        int j=0; //position in the roulette wheel
        int count=0; //children made

        while(count < old.populationSize - (numberRetained)){  //till the number of children required are made
            double num1 = TSPChromosome.generator.nextDouble()*total;
            double num2 = TSPChromosome.generator.nextDouble()*total;

            i=0;
            j=0;

            while(true){  //going through the roulette wheel
                
                if(intervals[i]> num1){break;}
                    i++;
            }  
            while(true){ //going through the roulette wheel
                
                if(intervals[j]> num2){break;}
                    j++;
            }  

            firstParent = old.population[i];
            secondParent =old.population[j];
           

            if( TSPChromosome.generator.nextDouble()< crossOverProbability){//if crossover happens
                crossOverPoint = 1+TSPChromosome.generator.nextInt(chromosomeSize-2); 
                //ternery statement-wooohhhooo
                newPop[count]= (TSPChromosome.generator.nextDouble() < 0.5)? new TSPChromosome(TSPChromosome.generateFirstChild(firstParent,secondParent,
                    //the adult stuff-Viewer discretion advised(PG-18)
                    crossOverPoint)) :new TSPChromosome(TSPChromosome.generateSecondChild(firstParent,secondParent,crossOverPoint));

                    newPop[count].performMutation(mutationProbability);//gamma radiation
                if(TravelingSalesman.DEBUG){
                    System.out.println("child number " +count + " : " +newPop[count].toString() );
                }
                count++;
            }
        }

        //merging the new and the old EFFICIENTLY
        TSPChromosome [] finalPop = new TSPChromosome[retainedPop.length+newPop.length];
        System.arraycopy( retainedPop, 0, finalPop, 0, retainedPop.length);
        System.arraycopy( newPop, 0, finalPop, retainedPop.length, newPop.length );

        return new TSPPopulation(finalPop);
    }
    //sort population
    public void sortPopulation(){

        Arrays.sort(this.population);
    }

    //MUTATOR METHODS
    public static void setPercentRetained(double val)
    {
        percentRetained=val;

    }

    public static void setMutationProbability(double val)
    {
        mutationProbability =val;

    }

    public static void setChromosomeSize(int size){
        chromosomeSize = size;

    }

    public static void setCrossoverProbability(double val){
        crossOverProbability = val;

    }

}
