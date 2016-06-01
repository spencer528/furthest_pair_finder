package furthestpairfinder;

import javax.swing.JFrame;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class FarthestPairFinder extends JFrame {

     int pointSize = 6;
     int numPoints = 100;
     
     //x and y sizes of screen must be substancially greater than 100
     int xScreenSize=800;
     int yScreenSize=600;
     
     Point2D[] S = new Point2D[ numPoints ]; //the set S
     Point2D[] farthestPair = new Point2D[ 2 ]; //the two points of the farthest pair
     
     ArrayList<Point2D> convexHull = new ArrayList(); //the vertices of the convex hull of S
     
     Color convexHullColour = Color.white;
     Color genericColour = Color.yellow;
     Color largestDistanceColour = Color.red;
     
     int indexOfLowestPoint=0;
     
    
    //fills S with random points
    public void makeRandomPoints() {
        Random rand = new Random();
 
        for (int i = 0; i < numPoints; i++) {
            int x = 50 + rand.nextInt(xScreenSize-100);
            int y = 50 + rand.nextInt(yScreenSize-100);
            S[i] = new Point2D( x, y );            
        }        
    }

    
    public void paint(Graphics g) {  
        g.setColor(Color.black);
        g.fillRect(0, 0, xScreenSize, yScreenSize);
        for(int i =0; i<S.length; i++){
            g.setColor(genericColour);
            g.fillOval((int)S[i].x-pointSize/2, (int)S[i].y-pointSize/2, pointSize, pointSize);
            g.setColor(genericColour.darker());
            g.drawOval((int)S[i].x-pointSize/2, (int)S[i].y-pointSize/2, pointSize-1, pointSize-1);
        }
        
        g.setColor(Color.white);
            g.fillOval((int)S[indexOfLowestPoint].x-pointSize/2, (int)S[indexOfLowestPoint].y-pointSize/2, pointSize, pointSize);
            g.setColor(genericColour.darker());
            g.drawOval((int)S[indexOfLowestPoint].x-pointSize/2, (int)S[indexOfLowestPoint].y-pointSize/2, pointSize-1, pointSize-1);
        
        //draw the points in the convex hull
        g.setColor(convexHullColour);
        int temp;
        for(int i =0; i<convexHull.size(); i++){
            if(i==convexHull.size()-1){temp=0;}
            else{temp=i+1;}
            g.drawLine((int) convexHull.get(i).x, (int) convexHull.get(i).y, (int) convexHull.get(temp).x, (int) convexHull.get(temp).y);
        }
        
        //draw a red line connecting the farthest pair
        g.setColor(largestDistanceColour);
        g.drawLine((int) farthestPair[0].x, (int) farthestPair[0].y, (int) farthestPair[1].x, (int) farthestPair[1].y);
    }
    
    //returns distance between two points
    private static double distanceBetweenPoints(double x1, double y1, double x2, double y2){
        double distance = Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2);
        distance = Math.sqrt(distance);
        return distance;
    }
    
    private static double[][] mergeSort(double[][] array_one, double[][] array_two) {
        
        // for all the arrays: first number is the index, second is the value
        
        double[][] c = new double[array_one.length + array_two.length][2];
        int i = 0;//keeps track of index of array one
        int j = 0;//keeps track of index of array two
        int k = 0;//keeps track of index of new merged array
        
	int length_one = array_one.length;
        int length_two = array_two.length;

        while ( i < length_one && j < length_two ) {
            if ( array_one[i][1] <= array_two[j][1] ) {
                c[k][1] = array_one[i][1];
                c[k][0] = array_one[i][0];
                i++;
            } 
	    
	    else {
                c[k][1] = array_two[j][1];
                c[k][1] = array_two[j][0];
                j++;
            }
            k++;
        }

        if(i == length_one){
            for (int m = j; m < length_two; m++) {
                c[k][1] = array_two[m][1];
                c[k][0] = array_two[m][0];
                k++;
            }
        }
        
        else{
            for (int m = i; m < length_one; m++) {
                c[k][1] = array_one[m][1];
                c[k][0] = array_one[m][0];
                k++;
            }
        }
        
        return c;
    }
    
    public void findConvexHull() {
        ArrayList<Integer> points_inside = new ArrayList();//keeps track of the index of points inside of the convex hull
        int indexOfLowestPoint=0;
        
        //find the point closest to the bottom using one swip of selection sort and to add terms to S_list
        for(int i=0; i<S.length; i++){
            points_inside.add(i);
            if(S[i].y>S[indexOfLowestPoint].y){indexOfLowestPoint=i;}
        }
        
        convexHull.add(S[indexOfLowestPoint]);
        
        //initialize the 'temp' vector
        Vector temp;
        if(indexOfLowestPoint!=0){  temp = new Vector(S[indexOfLowestPoint].x-S[0].x, S[indexOfLowestPoint].y-S[0].y);    }
        else{   temp = new Vector(S[indexOfLowestPoint].x-S[1].x, S[indexOfLowestPoint].y-S[1].y);  }
        
        Vector v = new Vector(1,0);//vector 'v' is used as a temperary variable to keep track of the current convex hull point and the previous one;
        int chp=indexOfLowestPoint;//'chp' is an counter used to keep track of the current convex hall point
        double smallestAngle;//'int smallestAngle' is a temperary variable
        
        while(true){
            
            smallestAngle=Math.PI;
            int indexOfNextPoint=0;
            
            for(Integer k: points_inside){
                temp = new Vector(S[k].x-S[chp].x, S[k].y-S[chp].y);
                if(v.getAngle(temp)<smallestAngle){
                    indexOfNextPoint=k;
                    smallestAngle=v.getAngle(temp);
                }
            }
            if(indexOfNextPoint==indexOfLowestPoint){break;}
            
            convexHull.add(S[indexOfNextPoint]);//adds point to convex hull
            chp=indexOfNextPoint;//sets current point for next cycle as index of next point
            points_inside.remove(points_inside.indexOf(chp));//removes point from list of points in convex hull
            
            int m=convexHull.size()-1;//temperary value m too reference as the current size of the convex hull
            v = new Vector(convexHull.get(m).x-convexHull.get(m-1).x,  convexHull.get(m).y-convexHull.get(m-1).y);
        }
    }
    
    
    
    
    
    public void findFarthestPair_EfficientWay() {
        int n = convexHull.size();
        double largestDistance=0;
        int pointOne=0;
        int pointTwo=1;
        for(int i=0; i<n-1; i++){ 
            for (int j=i+1; j<n; j++){
                double temperaryDistance = distanceBetweenPoints(convexHull.get(i).x,convexHull.get(i).y,convexHull.get(j).x,convexHull.get(j).y);
                if(temperaryDistance>largestDistance){
                    largestDistance=temperaryDistance;
                    pointOne=i;
                    pointTwo=j;
                }
            }
        }
        System.out.println("Size is "+convexHull.size()+" and indexes are "+pointOne+" "+pointTwo);
        farthestPair[0] = convexHull.get(pointOne);
        farthestPair[1] = convexHull.get(pointTwo);
    }
    
    public void findFarthestPair_BruteForceWay() {
        int n = S.length;
        double largestDistance=0;
        int pointOne=0;
        int pointTwo=1;
        for(int i=0; i<n-1; i++){ 
            for (int j=i+1; j<n; j++){
                double temperaryDistance = distanceBetweenPoints(S[i].x,S[i].y,S[j].x,S[j].y);
                if(temperaryDistance>largestDistance){
                    largestDistance=temperaryDistance;
                    pointOne=i;
                    pointTwo=j;
                }
            }
        }
        farthestPair[0] = S[pointOne];
        farthestPair[1] = S[pointTwo];
    }
    
   
    public static void main(String[] args) {

        //no changes are needed in main().  Just code the blank methods above.
        
        FarthestPairFinder fpf = new FarthestPairFinder();
        
        fpf.setBackground(Color.BLACK);
        fpf.setSize(fpf.xScreenSize, fpf.yScreenSize);
        fpf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fpf.makeRandomPoints();
        
        long startTime2 = System.nanoTime();
        fpf.findFarthestPair_BruteForceWay();
        long endTime2 = System.nanoTime();
        System.out.println("Brute Force Method took: "+(endTime2-startTime2)+" nanoSeconds");
        
        long startTimeE = System.nanoTime();
        fpf.findConvexHull();
        fpf.findFarthestPair_EfficientWay();
        long endTimeE = System.nanoTime();
        System.out.println("Convex Hull Method took: "+(endTimeE-startTimeE)+" nanoSeconds");
        
        
        fpf.setVisible(true); 
    }
}
