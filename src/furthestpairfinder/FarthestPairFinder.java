package farthestpairfinder;

import javax.swing.JFrame;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class FarthestPairFinder extends JFrame {

     int pointSize = 6;
     int numPoints = 100;
     
     public ArrayList<Integer> polygonPoints = new ArrayList<Integer>();
     
     Point2D[] S = new Point2D[ numPoints ]; //the set S
     Point2D[] farthestPair = new Point2D[ 2 ]; //the two points of the farthest pair
     
     ArrayList<Point2D> convexHull = new ArrayList(); //the vertices of the convex hull of S
     
     Color convexHullColour = Color.white;
     Color genericColour = Color.yellow;
     Color largestDistanceColour = Color.red;

    public FarthestPairFinder() {
        this.list = new ArrayList<>();
    }
    
    //fills S with random points
    public void makeRandomPoints() {
        Random rand = new Random();
 
        for (int i = 0; i < numPoints; i++) {
            int x = 50 + rand.nextInt(500);
            int y = 50 + rand.nextInt(500);
            S[i] = new Point2D( x, y );            
        }        
    }

    
    public void paint(Graphics g) {  
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);
        for(int i =0; i<S.length; i++){
            g.setColor(Color.yellow);
            g.fillOval((int)S[i].x-pointSize/2, (int)S[i].y-pointSize/2, pointSize, pointSize);
            g.setColor(Color.orange);
            g.drawOval((int)S[i].x-pointSize/2, (int)S[i].y-pointSize/2, pointSize-1, pointSize-1);
        }
        
        //draw the points in the convex hull
        
        //draw a red line connecting the farthest pair
        g.setColor(largestDistanceColour);
        g.drawLine((int) farthestPair[0].x, (int) farthestPair[0].y, (int) farthestPair[1].x, (int) farthestPair[1].y);
    }
    
    //distance between array
    public static double distanceBetweenPoints(double x1, double y1, double x2, double y2){
        double distance = Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2);
        distance = Math.sqrt(distance);
        return distance;
    }
    
    
    
    public void findConvexHull() {
        Vector v = new Vector(1,0);
        for(int i = 0; i<numPoints; i++){
            double smallestAngle=360;
            int indexOfNextPoint=i+1;
            for(int k=i+1; k<numPoints;k++){
                Vector temp = new Vector(S[i].x-S[k].x, S[i].y-S[k].y);
                if(v.getAngle(temp)<smallestAngle){
                    indexOfNextPoint=k;
                    smallestAngle=v.getAngle(temp);
                }
            }
            convexHull.add(S[indexOfNextPoint]);
        }
    }
    
    public void findFarthestPair_EfficientWay() {
        //code this
        //must make use of the convex hull, which will have been calculated by the time this method is called
    }
    
    public void draw_Line(double x1, double y1, double x2, double y2, Graphics g){
        g.setColor(Color.red);
        g.drawLine((int) x1,(int) y1,(int) x2,(int) y2);
    }
    
    public void findFarthestPair_BruteForceWay() {
        long time3 =0;
        long time1 = System.nanoTime();
        int n = S.length;
        double largestDistance = 0;
        int pointOne=0;
        int pointTwo=1;
        long time2 = System.nanoTime();
        for(int i=0; i<n-1; i++){ 
            for (int j=i+1; j<n; j++){
                time3 = System.nanoTime();
                double temperaryDistance = distanceBetweenPoints(S[i].x,S[i].y,S[j].x,S[j].y);
                if(temperaryDistance>largestDistance){
                    largestDistance=temperaryDistance;
                    pointOne=i;
                    pointTwo=j;
                }
                long time4 = System.nanoTime();
            }
        }
        farthestPair[0] = S[pointOne];
        farthestPair[1] = S[pointTwo];
        long time4 = System.nanoTime();
        System.out.println(time1);
        System.out.println(time2-time1);
        System.out.println(time3-time2);
        System.out.println(time4-time3);
    }
    
   
    public static void main(String[] args) {

        //no changes are needed in main().  Just code the blank methods above.
        
        FarthestPairFinder fpf = new FarthestPairFinder();
        
        fpf.setBackground(Color.BLACK);
        fpf.setSize(800, 800);
        fpf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fpf.makeRandomPoints();
        
        fpf.findConvexHull();
        
        fpf.findFarthestPair_EfficientWay();
        
        fpf.findFarthestPair_BruteForceWay();
        
        fpf.setVisible(true); 
    }
}
