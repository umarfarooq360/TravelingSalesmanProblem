
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Arrays;
import java.io.*;
/**
 * Write a description of class TravellingSalesmanGui here.
 * 
 * @author (Omar Farooq) 
 * @version (4/17/2014)
 */
public class TravelingSalesmanGui extends JFrame implements  ActionListener
{
    private JMenuBar menuBar; //the menubar
    private JPanel mainPanel;  //the panel with the visual representation for cities
    private JPanel buttonPanel;  // the panel with buttons 
    static CanvasPanel canvas;   //the panel where we draw
    private JPanel informationPanel;  //the panel showing output scores
    private JPanel parameterPanel1;  //the panel with all the text fields
    private JPanel parameterPanel2; //the panel with all the text fields part 2

    //size of the buttons
    private final Dimension buttonSize = new Dimension(75,50);
    private final Dimension textfieldSize = new Dimension(80,30);

    //the text fields
    static JTextField numGenField ;
    static JTextField popSizeField;
    static JTextField numCitField;
    static JTextField crossProbField;
    static JTextField mutProbField;
    static JTextField percRetField;
    JTextField scoreVal;
    JTextField cirLenVal;
    JRadioButton debugButton;

    double score= 0.0;
    double circuitLength= 0.0;
    ArrayList <Point> cities = new ArrayList <Point> ();

    JFileChooser fileChooser;

    final boolean DEBUG = false;
    static boolean wasStopPressed = false;
    static boolean wasFileRead = false;

    TravelingSalesman TSP;

    public static void main(String[] args){
        new TravelingSalesmanGui();
    }

    //Constructor 
    public TravelingSalesmanGui(){

        this.setTitle("Traveling Salesman(Genetic Algorithm)");
        this.setSize(700,600);

        //menu generate method
        generateMenu();
        this.setJMenuBar(menuBar);

        //pane with null layout
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(700,600));
        contentPane.setBackground(new Color(192,192,192));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        //create the main panel
        mainPanel = new JPanel(null);
        //mainPanel.setBorder(BorderFactory.createEtchedBorder(1));
        mainPanel.setPreferredSize(new Dimension(700,400));
        mainPanel.setMinimumSize(new Dimension(700,400));
        mainPanel.setMaximumSize(new Dimension(1000,400));
        mainPanel.setBackground(Color.lightGray);
        //mainPanel.setForeground(new Color(0,0,0));
        //mainPanel.setEnabled(true);
        mainPanel.setFont(new Font("sansserif",0,12));
        //mainPanel.setVisible(true);
        mainPanel.setLayout(new GridBagLayout());

        //creating the canvas for drawing
        canvas = new CanvasPanel(null);
        //canvas.addMouseListener(this);
        Dimension canvasSize = new Dimension(400,400);
        canvas.setMinimumSize(canvasSize);
        canvas.setPreferredSize(canvasSize);
        canvas.setMaximumSize(canvasSize);
        canvas.setBackground(Color.black);
        mainPanel.add(canvas, new GridBagConstraints());

        //creating infoformation Panel
        informationPanel = new JPanel();
        Dimension infoPanelSize = new Dimension(700,30);
        informationPanel.setPreferredSize(infoPanelSize);
        //informationPanel.setLayout(new BoxLayout(parameterPanel1, BoxLayout.X_AXIS));

        //creating labels for the information panel
        JLabel scoreLab = new JLabel("Score: ");
        JLabel cirLenLab = new JLabel("Circuit Length Squared: ");
        scoreVal = new JTextField();
        cirLenVal = new JTextField();
        scoreVal.setPreferredSize(new Dimension(120,30));
        scoreVal.setMaximumSize(new Dimension(120,30));
        cirLenVal.setMaximumSize(new Dimension(120,30));
        cirLenVal.setMinimumSize(new Dimension(120,30));
        cirLenVal.setPreferredSize(new Dimension(120,30));
        scoreVal.setEditable(false);
        cirLenVal.setEditable(false);

        //adding the labels to the panel
        informationPanel.add(scoreLab);
        informationPanel.add(scoreVal);
        informationPanel.add(cirLenLab);
        informationPanel.add(cirLenVal);

        //creating the textfield Panel
        parameterPanel1 = new JPanel();
        Dimension parPanelSize = new Dimension(700,20);
        parameterPanel1.setPreferredSize(parPanelSize);
        // parameterPanel1.setLayout(new BoxLayout(parameterPanel1, BoxLayout.X_AXIS));
        //see if default layout is flow?

        parameterPanel2 = new JPanel();

        parameterPanel2.setPreferredSize(parPanelSize);
        //parameterPanel2.setLayout(new BoxLayout(parameterPanel2, BoxLayout.X_AXIS));

        //adding textfields and labels
        numGenField  =  new JTextField();
        JLabel numGenLab = new JLabel("Number Of Generations: ");
        numGenField.setMaximumSize(textfieldSize);
        numGenField.setMinimumSize(textfieldSize);
        numGenField.setPreferredSize(textfieldSize);

        popSizeField  =  new JTextField();
        JLabel popSizeLab = new JLabel("Population Size: ");
        popSizeField.setMinimumSize(textfieldSize);
        popSizeField.setMaximumSize(textfieldSize);
        popSizeField.setPreferredSize(textfieldSize);

        numCitField  =  new JTextField();
        JLabel numCitLab = new JLabel("Num Cities: ");
        numCitField.setMinimumSize(textfieldSize);
        numCitField.setMaximumSize(textfieldSize);
        numCitField.setPreferredSize(textfieldSize);

        crossProbField  =  new JTextField();
        JLabel crossProbLab = new JLabel("Crossover Probability: ");
        crossProbField.setMinimumSize(textfieldSize);
        crossProbField.setMaximumSize(textfieldSize);
        crossProbField.setPreferredSize(textfieldSize);

        mutProbField  =  new JTextField();
        JLabel mutProbLab = new JLabel("Mutation Probablity: ");
        mutProbField.setMaximumSize(textfieldSize);
        mutProbField.setMinimumSize(textfieldSize);
        mutProbField.setPreferredSize(textfieldSize);

        percRetField  =  new JTextField();
        JLabel percRetLab = new JLabel("Percent Retained: ");
        percRetField.setMaximumSize(textfieldSize);
        percRetField.setMinimumSize(textfieldSize);
        percRetField.setPreferredSize(textfieldSize);

        //adding the labels and text fields to the panel
        parameterPanel1.add( numGenLab  );
        parameterPanel1.add(  numGenField );
        parameterPanel1.add( popSizeLab , BorderLayout.CENTER );
        parameterPanel1.add(  popSizeField , BorderLayout.CENTER);
        parameterPanel1.add( numCitLab  , BorderLayout.EAST);
        parameterPanel1.add(  numCitField , BorderLayout.EAST);
        parameterPanel2.add( crossProbLab  , BorderLayout.WEST);
        parameterPanel2.add(  crossProbField , BorderLayout.WEST);
        parameterPanel2.add(  mutProbLab , BorderLayout.WEST);
        parameterPanel2.add( mutProbField  , BorderLayout.WEST);
        parameterPanel2.add(  percRetLab , BorderLayout.WEST);
        parameterPanel2.add(  percRetField , BorderLayout.WEST);

        //creating the button panel
        buttonPanel = new JPanel();
        Dimension buttonPanelSize = new Dimension(700,75);
        buttonPanel.setMinimumSize(buttonPanelSize);
        buttonPanel.setPreferredSize(buttonPanelSize);
        //buttonPanel.setMaximumSize(buttonPanelSize);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        //defining all the buttons
        JButton goButton = new JButton("Go");
        goButton.setPreferredSize(buttonSize);
        goButton.addActionListener(this);
        goButton.setActionCommand("go");

        JButton stopButton = new JButton("Step");
        stopButton.setPreferredSize(buttonSize);
        stopButton.addActionListener(this);
        stopButton.setActionCommand("stop");

        JButton newPopButton = new JButton("New Population");
        //newPopButton.setPreferredSize(buttonSize);
        newPopButton.addActionListener(this);
        newPopButton.setActionCommand("newpop");    

        JButton openFileButton = new JButton("Open File");
        //openFileButton.setPreferredSize(buttonSize);
        openFileButton.addActionListener(this);
        openFileButton.setActionCommand("openfile"); 
        
        debugButton = new JRadioButton("Show Debug");
       debugButton.addActionListener(this);
        debugButton.setActionCommand("debug"); 

        //adding all the buttons
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(goButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(stopButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(newPopButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(openFileButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(debugButton);

        //adding components to contentPane panel
        contentPane.add(mainPanel);
        contentPane.add(informationPanel);
        contentPane.add(parameterPanel1);
        contentPane.add(parameterPanel2);
        contentPane.add(buttonPanel);

        //adding panel to JFrame and seting of window position and close operation
        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

    //method to generate menu
    public void generateMenu(){
        menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu tools = new JMenu("Tools");
        JMenu help = new JMenu("Help");

        JMenuItem preferences = new JMenuItem("Preferences   ");
        JMenuItem about = new JMenuItem("About   ");

        tools.add(preferences);
        help.add(about);

        menuBar.add(file);
        menuBar.add(tools);
        menuBar.add(help);
    }

    //defines what each button should do.
    public void actionPerformed(ActionEvent e){
        String buttonIdentifier = e.getActionCommand(); //the action command of the method

        if (buttonIdentifier.equals("openfile")) {
            canvas.pointOrder= null;
            fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile(); //get the file
                wasFileRead=true;
                if(DEBUG)System.out.println(selectedFile.getName());

                TSP = new TravelingSalesman(selectedFile);  //create an instance of TSP
                CanvasPanel.pointOrder =null;
                canvas.setPoints(TSP.cityList);  //put the points on the grid.
                //canvas.
                TSPPopulation.setChromosomeSize(TSP.numCities);

                //setting the text fields
                numCitField.setText((String.valueOf(TSP.numCities)));
                percRetField.setText((String.valueOf(TSP.percentRetained)));
                crossProbField.setText((String.valueOf(TSP.crossoverProbability)));
                mutProbField.setText((String.valueOf(TSP.mutationProbability)));
                popSizeField.setText((String.valueOf(TSP.populationSize)));
                numGenField.setText((String.valueOf(TSP.numGenerations)));

            } else if (result == JFileChooser.CANCEL_OPTION) {
                if(DEBUG)System.out.println("Cancel was selected");

            }

        }else if(buttonIdentifier.equals("stop")){  //I though this was stop rater than step

            //the case if a file was not read
            if(!wasFileRead){ //make random points!
                if(canvas.cities.length != Integer.parseInt(numCitField.getText())  )
                { int num = Integer.parseInt(numCitField.getText());
                    canvas.setPoints( canvas.generateRandomPoints(num) ); }
                TSP = new TravelingSalesman();
                TSPPopulation.setChromosomeSize(TSP.numCities);
            }
            String[] arg ={"step"}; 
            TSP.main(arg);  //run themain with only one step

            //set the circuit path found by Genetic Algo
            canvas.setOrder( TSP.aSolution.orderOfVisiting);

            //display the score and length
            this.circuitLength = TSP.aSolution.score;  
            this.score= 1000000/this.circuitLength;
            cirLenVal.setText(String.valueOf(circuitLength));
            scoreVal.setText(String.format("%.5g%n", this.score));

            //Picasso
            canvas.repaint();
            informationPanel.repaint();
            canvas.repaint();

        }
        else if(buttonIdentifier.equals("go")){ //if you wanna go all the way
            
            canvas.pointOrder= null;
            if(!wasFileRead){ 
                //setting up variables using text fields
                if(canvas.cities.length != Integer.parseInt(numCitField.getText())  )
                { int num = Integer.parseInt(numCitField.getText());
                    canvas.setPoints( canvas.generateRandomPoints(num) ); 
                    canvas.repaint();}  //if no file was read
                TSP = new TravelingSalesman();
                TSPPopulation.setChromosomeSize(TSP.numCities);

            }
            String[] arg ={"go"}; 
            TSP.main(arg);      
            
            //plot the bext solution
            canvas.setOrder( TSP.aSolution.orderOfVisiting);
            
            //show score
            this.circuitLength = TSP.aSolution.score;
            this.score= 1000000/this.circuitLength;
            cirLenVal.setText(String.valueOf(circuitLength));
            scoreVal.setText(String.format("%.5g%n", this.score));

            //Picasso
            canvas.repaint();
            informationPanel.repaint();

        }else if(buttonIdentifier.equals("newpop")){  //making new babies
            //reset tracking variables
            TSP.genTracker=0;
            wasFileRead=false;
            TSP.lastPopulation = null;
            canvas.pointOrder= null;

            JOptionPane.showMessageDialog(null, "It behooves me", "Go big or go home", JOptionPane.INFORMATION_MESSAGE);

            //if text field is null use 15 as num
            if (numCitField.getText().equals("") || numCitField.getText().equals(null)){
                canvas.setPoints( canvas.generateRandomPoints(CanvasPanel.DEFAULT_SIZE) ); 
                numCitField.setText(String.valueOf(CanvasPanel.DEFAULT_SIZE));
                canvas.repaint();}
            else{  //make the specified number of babies
                int num = Integer.parseInt(numCitField.getText());
                canvas.setPoints( canvas.generateRandomPoints(num) ); 

            }
        }else if(buttonIdentifier.equals("debug")){  //listeener for the radio button 
            if( debugButton.isSelected()) {
                TravelingSalesman.DEBUG_SHORT = true;
            }
            else{
                TravelingSalesman.DEBUG_SHORT = false;
            }
        }
    }

    //does stuff when you click something
    public void mouseClicked(MouseEvent e) {
        Point click_point = e.getPoint();
        System.out.print(click_point);
        cities.add(click_point);
        canvas.repaint();
    }

    //Unimplememted methods
    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}
}
