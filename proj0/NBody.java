public class NBody {
    public static double readRadius(String t){
        In in = new In(t);
        int firstItemInFile = in.readInt();
        double r = in.readDouble();
        return r;
    }
    public static Planet[] readPlanets(String t){
        In in = new In(t);
        int n = in.readInt();
        double r = in.readDouble();
        Planet[] planets = new Planet[n];

        for (int i=0; i<n; i++){
            double xP = in.readDouble();
            double yP = in.readDouble(); 
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            
            planets[i] = new Planet(xP, yP, xV, yV, m, img);
        }
        return planets;
    }
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        /* draw the background */
        String background = "images/starfield.jpg";
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, background);
        /* draw each planet */
        for (Planet p: planets){
            p.draw();
        }

        /* make planets animated */
        StdDraw.enableDoubleBuffering();

        double time = 0.0;
        while (time < T){
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];

            for (int i=0; i<planets.length; i++){
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            for (int i=0; i<planets.length; i++){
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, background);
            for (Planet p: planets){
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);

            time += dt;
        }

        /* print stuff */
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                        planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                        planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
}
    }
}
