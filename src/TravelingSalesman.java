
import java.awt.*;
import java.util.*;
import java.util.Properties;
import java.io.*;
import java.awt.Point;
import javax.swing.JOptionPane;
/**
 * This class implements the main genetic algorithm using instances of 
 * TSPPopulation and TSPChromosome
 * 
 * @author (Omar Farooq) 
 * @version (4/17/2014)
 */
public class TravelingSalesman 
{
    //These variables are read from file
    static int numGenerations;
    static int populationSize;
    static int numCities;
    static double crossoverProbability;
    static double mutationProbability;
    static double percentRetained;
    static Point[] cityList ;

    //matrix containgin distance of every point form the other
    static int[][] scoreMatrix ;

    Config configurationFile ; //this is the stored configuration file information

    //for showing debug output
    static  boolean DEBUG = false;
    static  boolean DEBUG_SHORT = false;

    static TSPChromosome aSolution;  //a solution for the viewers

    //in case we press pause;
    static int genTracker =0;
    static TSPPopulation lastPopulation;
    static boolean wasEverPaused =false;

    public static void main(String[] args) {
        String exec = args[0]; //what type of execution
        checkVariables();//checking the variables
        populateScoreMatrix();

        //setting the static variables in other classes
        TSPChromosome.setDistanceMatrix(scoreMatrix);
        TSPPopulation.setPercentRetained(percentRetained);
        TSPPopulation.setMutationProbability(mutationProbability);
        TSPPopulation.setCrossoverProbability(crossoverProbability);

        //old and new populations
        TSPPopulation pop1;
        TSPPopulation pop2;
        if (exec == "go"){ 
            int i=0;
            if(genTracker == 0  && lastPopulation==null ) {pop1 = new TSPPopulation(populationSize, numCities);
                pop1.sortPopulation(); } //sorting the population
            else{   pop1 = lastPopulation; i = genTracker;}

            //do for all genertations
            while(i<numGenerations) {
                //genertaing new population
                pop2 = TSPPopulation.orgy(pop1);//make babies

                //debug stuff
                if(DEBUG){
                    System.out.println( "\n Before Sorting, " + "Generation Num : " + i + "\n\n");
                    for (int j=0;j< pop2.populationSize ;j++ ) {
                        System.out.println(pop2.population[j].toString());
                    }
                }

                pop2.sortPopulation();
                pop1 = pop2;

                //debug stuff
                if(DEBUG_SHORT){
                    System.out.println("At gen " + i+ " best -> " + pop1.population[0].toString());
                }

                if(DEBUG){
                    System.out.println( "\n After Sorting, " + "Generation Num : " +i + "\n\n");
                    for (int j=0;j< pop1.populationSize ;j++ ) {
                        System.out.println(pop1.population[j].toString());
                    }
                }
                i++;
            }    
            //set the old solutions to null
            genTracker=0;
            lastPopulation=null;

            //get the best solution!
            aSolution = pop1.population[0];

        }
        else{//if we wanna step

            if(genTracker == 0  && lastPopulation==null ) {pop1 = new TSPPopulation(populationSize, numCities);
                pop1.sortPopulation(); }
            else{   pop1 = lastPopulation; }

            //make children
            pop2 = TSPPopulation.orgy(pop1);
            pop2.sortPopulation(); //sort them

            genTracker++;//increment counter

            lastPopulation = pop2;  //store last population

            aSolution = lastPopulation.population[0];//store best solution and display
            //debug
            if(DEBUG_SHORT){
                System.out.println("At gen " + (genTracker-1) + " -> " + aSolution.toString());

            }
        }
    }

    //construncting an instance of travelling salsesman using textfields
    public TravelingSalesman(){
        //if fields are empty
        if(TravelingSalesmanGui.numGenField.getText().equals("") || TravelingSalesmanGui.popSizeField.getText().equals("") ||
        TravelingSalesmanGui.numCitField.getText().equals("") ||    TravelingSalesmanGui.crossProbField.getText().equals("") ||
        TravelingSalesmanGui.mutProbField.getText().equals("") ||  TravelingSalesmanGui.percRetField.getText().equals("") ){
            JOptionPane.showMessageDialog(null,  "Please, be kind enough to fill in ALL the" + 
                "\n textfields before running the program.","C''MON DUDE", JOptionPane.WARNING_MESSAGE);
            System.out.println("ERROR:FILL THE FIELDS");
            System.exit(1);
        }else{

            //using the values given in the data fields
            //setting the variables
            numGenerations = Integer.parseInt((TravelingSalesmanGui.numGenField.getText()));
            populationSize = Integer.parseInt((TravelingSalesmanGui.popSizeField.getText()));
            numCities = Integer.parseInt((TravelingSalesmanGui.numCitField.getText()));
            crossoverProbability = Double.parseDouble((TravelingSalesmanGui.crossProbField.getText()));
            mutationProbability =  Double.parseDouble((TravelingSalesmanGui.mutProbField.getText()));
            percentRetained = Double.parseDouble((TravelingSalesmanGui.percRetField.getText()));
            cityList = CanvasPanel.cities;

            populateScoreMatrix();      
        }
    }

    //using a configurtaion file
    public TravelingSalesman(File theFile){
        configure(theFile);
    }
    //checks if variables match the one in the textfields
    public static  void checkVariables()    {
        numGenerations = Integer.parseInt((TravelingSalesmanGui.numGenField.getText()));
        populationSize = Integer.parseInt((TravelingSalesmanGui.popSizeField.getText()));
        numCities = Integer.parseInt((TravelingSalesmanGui.numCitField.getText()));
        crossoverProbability = Double.parseDouble((TravelingSalesmanGui.crossProbField.getText()));
        mutationProbability =  Double.parseDouble((TravelingSalesmanGui.mutProbField.getText()));
        percentRetained = Double.parseDouble((TravelingSalesmanGui.percRetField.getText()));

    }

    /**
     * This methods reads the configuration file and sets up the necessary variables
     */
    public  void configure(File file){
        try{
            configurationFile = new Config(file);

            //reading the required stuff from the configFile!
            numGenerations = Integer.parseInt(configurationFile.getProperty("numGenerations").trim());
            populationSize = Integer.parseInt(configurationFile.getProperty("populationSize").trim());
            numCities = Integer.parseInt(configurationFile.getProperty("numCities").trim());
            crossoverProbability = Double.parseDouble(configurationFile.getProperty("crossoverProbability").trim());
            mutationProbability = Double.parseDouble(configurationFile.getProperty("mutationProbability").trim());
            percentRetained = Double.parseDouble(configurationFile.getProperty("percentRetained").trim());

            //reads things off the file
            String tmp = configurationFile.getProperty("cityList");
            cityList = readPoints(tmp); 
            populateScoreMatrix();
        }
        catch(Exception e){
            System.err.println("ERROR: in reading the configuration file");
            System.exit(1);
        }

    }

    /**
     * Overloaded method: This methods reads the configuration file and sets up the necessary variables
     */
    public void configure(String filename){
        File f = new File(filename);
        configure(f);
    }

    /**
     * This methods converts points given in a string format to an array of points
     */
    public Point[] readPoints(String input){
        try{
            char tmp = input.charAt(0);
            String currentPoint ="";

            Point[] array = new Point[numCities];
            int i = 0 ;
            int j=0;
            while( tmp != 'X'){ //end there
                if(tmp ==';'){
                    array[i] = createPoint(currentPoint);
                    currentPoint="";
                    i++;
                }
                else{
                    currentPoint+=tmp;
                }
                j++;
                tmp= input.charAt(j);

            }
            array[i]=createPoint(currentPoint);
            return array;}
        catch(ArrayIndexOutOfBoundsException e1){ //error
            System.out.println("ERROR: More cities than the numCities variable.");
            System.exit(1);
        }
        catch(Exception e){
            System.out.println("ERROR: Could not create points from the String that was input");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * This methods converts a point given in a string format to a Point
     */
    public Point createPoint(String input){
        try{
            int x = Integer.parseInt(input.substring( input.indexOf("(")+1,input.indexOf(",")).trim());
            int y = Integer.parseInt(input.substring( input.indexOf(",")+1,input.indexOf(")")).trim());
            return (new Point(x,y));    }
        catch(Exception e){
            System.out.println("ERROR: Could not create point from the String that was input");
            System.exit(1);
        }
        return null;
    }

    //fills scoring matrix by finding distance of each point from the other
    static  void populateScoreMatrix(){
        scoreMatrix = new int[numCities][numCities];

        for (int i=0; i < numCities ; i++ ) {
            for ( int j=0;j<numCities ;j++ ) {
                if (i == j) {
                    scoreMatrix[i][j]= 0;
                }
                else {
                    scoreMatrix[i][j]=findDistance(cityList[i], cityList[j]);
                }
            }
        }

    }
    //returns square of distance between points
    static int findDistance(Point a, Point b){
        return ((int)Math.pow(a.x -b.x,2)+(int)Math.pow(a.y -b.y,2));

    }
    //mutator method
    public void setCities(Point[] newCit){
        cityList = newCit;
        numCities = newCit.length;
    }

    //class is an object holding the configuration file.
    //Taken from project 2.
    private class Config
    {
        Properties configFile;
        //constructor
        public Config(String filename)
        {
            configFile = new java.util.Properties();
            try {
                //read file
                FileInputStream file = new FileInputStream(filename);
                configFile.load(file);

            }catch(Exception eta){
                System.err.println("ERROR: Couldn''t load configuration file");
                eta.printStackTrace();
            }
        }
        //constructor
        public Config(File input)
        {
            configFile = new java.util.Properties();
            try {
                configFile.load(new FileInputStream(input));
                //configFile.setProperty( "sequenceInputFile
            }catch(Exception eta){
                System.err.println("ERROR: Couldn''t load configuration file");
                eta.printStackTrace();
            }

        }
        //get the value of key
        public String getProperty(String key)
        {
            String value = this.configFile.getProperty(key);
            return value;
        }
    }
}