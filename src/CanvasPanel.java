
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
/**
 *This class represents the Panel with cities in it.
 *
 */
public class CanvasPanel extends JPanel implements MouseListener{
    //finals
    static final  Dimension canvasSize = new Dimension(400,400); //size
    static final int NODE_RADIUS = 2;
    static final int DEFAULT_SIZE = 15;

    Color currentColor = new Color(142, 0, 204);//I like purple

    //arrays containg points and orders
    static int[] pointOrder = null;
    int [][] distancesArray = null;
    static Point [] cities = null;

    //constuctor-makes a panel
    public CanvasPanel(Point[] vertices1) {
        if (vertices1 ==null){
            cities = new Point[0];
        }
        else{
            cities= vertices1;
        }

        this.setMinimumSize(canvasSize);
        this.setPreferredSize(canvasSize);
        this.setMaximumSize(canvasSize);
        this.setBackground(Color.BLACK);
        addMouseListener(this);
    }

    //set the points
    public void setPoints(Point[] newPoints)
    {
        cities = newPoints;
        this.repaint();

    }
    //set the path
    public void setOrder(int[] order)
    {
        pointOrder = order;
        this.repaint();

    }

    //repainting
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(currentColor);
        if(cities !=null ) {
            // ListIterator iterator = vertices.listIterator(0);

            Point currentVertex = null;
            //plot the points
            for (int i=0; i < cities.length; ++i) {
                currentVertex = (Point) cities[i];
                g.fillOval(currentVertex.x - NODE_RADIUS,
                    currentVertex.y - NODE_RADIUS,2*NODE_RADIUS, 2*NODE_RADIUS);
            }
            if(pointOrder!= null) {//make the circuit
                g.setColor(new Color(255, 204, 0)); //I like yellow too
                for (int i=0; i < cities.length-1; ++i) {
                    int first = pointOrder[i];
                    int second = pointOrder[i+1];
                    g.drawLine(cities[first].x ,cities[first].y,cities[second].x,cities[second].y);
                } 
                //draw line from point back to origin
                int first = pointOrder[cities.length-1];
                int second = pointOrder[0];
                g.drawLine(cities[first].x ,cities[first].y,cities[second].x,cities[second].y);
            }
        }
    }

    //If a mouse is click
    public void mouseClicked(MouseEvent e) {
        Point click_point = e.getPoint();
        if(pointOrder!=null){  //delete the circuit
            pointOrder=null;
        }
        //reset old solutions
        TravelingSalesman.genTracker = 0;
        TravelingSalesman.lastPopulation= null;

        //System.out.print(click_point);
        Point[] newArray = new Point[cities.length +1]; //make new array

        //add the points
        for(int i =0;i<cities.length ; i++)
        {
            newArray[i] = cities[i];
        }
        newArray[cities.length] = click_point;

        //update variables
        TSPPopulation.chromosomeSize = newArray.length;
        cities= newArray;
        TravelingSalesman.cityList = newArray;
        TravelingSalesmanGui.numCitField.setText(String.valueOf(cities.length));
        //Picasso                
        this.repaint();
    }

    //Generates the specified number of random points in the 400x400 grid    
    public static Point[] generateRandomPoints(int num)
    {
        Point[] arr = new Point[num];
        int x= TSPChromosome.generator.nextInt(400);
        int y = TSPChromosome.generator.nextInt(400);

        for (int i=0;i<num;i++)
        {
            arr[i] = new Point(x,y);
            x= TSPChromosome.generator.nextInt(400);
            y= TSPChromosome.generator.nextInt(400);

        }
        return arr;
    }

    //Unimplememted methods
    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}
}