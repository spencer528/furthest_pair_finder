package furthestpairfinder;

public class Vector {
    
    double xComponent, yComponent;
    
    public Vector(double x, double y) {
        this.xComponent = x;
        this.yComponent = y;
    }
    
    //this helps make it easier to make a vector from two points
    public Vector(Point2D p1, Point2D p2){
        this.xComponent = p1.x - p2.x;
        this.yComponent = p1.y - p2.y;
    }
    
    //needed as part of the convex hull algorithm and for finding the farthest pair within the vertices of the convex hull
    public Vector subtract( Vector other ) {
        return new Vector( this.xComponent - other.xComponent, this.yComponent - other.yComponent);
    }
    
    //needed as part of the convex hull algorithm and for finding the farthest pair within the vertices of the convex hull
    public double getAngle( Vector other ) {  
        double vDotw = this.dotProduct( other );
        double magV = this.magnitude();
        double magW = other.magnitude();
        
        return Math.acos( vDotw / (magV*magW) );
    }
    
    public boolean clockwizeCheck(Vector other){
        double magV = this.magnitude();
        double magW = other.magnitude();
        Vector o = new Vector(this.xComponent/magV+other.xComponent/(magW), this.yComponent/magV+other.yComponent/(2*magW));
        if(o.getAngle(other)>this.getAngle(other)){return true;}
        else{return false;}
    }
    
    //only used inside getAngle()
    private double dotProduct( Vector other ) {
        return this.xComponent*other.xComponent + this.yComponent*other.yComponent;
    }
    
    //only used inside getAngle()
    private double magnitude() {
        return Math.sqrt( this.xComponent*this.xComponent + this.yComponent*this.yComponent);
    }
}
