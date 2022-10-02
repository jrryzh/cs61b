/**
 *  Tests calcForceExertedBy
 */
public class TestPlanet {
    public static void main(String[] args){
        checkCalcForce();
    }

    private static void checkEquals(double actual, double expected, String label, double eps) {
        if (Math.abs(expected - actual) <= eps * Math.max(expected, actual)) {
            System.out.println("PASS: " + label + ": Expected " + expected + " and you gave " + actual);
        } else {
            System.out.println("FAIL: " + label + ": Expected " + expected + " and you gave " + actual);
        }
    } 

    private static void checkCalcForce(){
        System.out.println("Checking calcForce...");

        Planet sun = new Planet(1e12, 2e11, 0.0, 0.0, 2e30, "Sun.jpg");
        Planet saturn = new Planet(2.3e12, 9.5e11, 0.0, 0.0, 6e26, "Saturn.jpg");

        checkEquals((sun).calcForceExertedBy(saturn), 3.6e22, "calcForceExertedBy()", 0.01);
    }
}
