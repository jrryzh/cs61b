public class Planet {
    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;
    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }
    public Planet(Planet p){
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }
    public double calcDistance(Planet p){
        return Math.sqrt((this.xxPos - p.xxPos) *  (this.xxPos - p.xxPos) + (this.yyPos - p.yyPos) * (this.yyPos - p.yyPos));
    }
    public double calcForceExertedBy(Planet p) {
        return G * this.mass * p.mass / (this.calcDistance(p) * this.calcDistance(p));
    }
    public double calcForceExertedByX(Planet p){
        return this.calcForceExertedBy(p) * (p.xxPos - this.xxPos) / this.calcDistance(p);
    }
    public double calcForceExertedByY(Planet p){
        return this.calcForceExertedBy(p) * (p.yyPos - this.yyPos) / this.calcDistance(p);
    }
    public double calcNetForceExertedByX(Planet[] pp){
        double netForceX = 0.0;
        for (Planet p: pp){
            if (p.equals(this)) {continue;}
            netForceX += this.calcForceExertedByX(p);
        }
        return netForceX;
    }
    public double calcNetForceExertedByY(Planet[] pp){
        double netForceY = 0.0;
        for (Planet p: pp){
            if (p.equals(this)) {continue;}
            netForceY += this.calcForceExertedByY(p);
        }
        return netForceY;
    }
    public void update(double dt, double fX, double fY){
        double aX = fX / this.mass;
        double aY = fY / this.mass;
        this.xxVel += aX * dt;
        this.yyVel += aY * dt;
        this.xxPos += this.xxVel * dt;
        this.yyPos += this.yyVel * dt;
    }
    public void draw(){
        StdDraw.picture(this.xxPos, this.yyPos, "images/"+this.imgFileName);
    }
}
