package creatures;

import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {

    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    private static final double moveEnergyLost = 0.03;
    private static final double stayEnergyLost = 0.01;

    /** creates plip with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    /** creates a plip with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    /** Should return a color with red = 34, blue = 231, and green = 0 */
    public Color color() {
        return color(r, g, b);
    }

    /** If a Clorus attacks another creature, it should gain that creature’s energy. */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /** Clorus should lose 0.03 units of energy when moving. If you want to
     *  to avoid the magic number warning, you'll need to make a
     *  private static final variable. This is not required for this lab.
     */
    public void move() {
        energy -= moveEnergyLost;
    }


    /** Clorus should lose 0.01 units of energy when staying. */
    public void stay() {
        energy -= stayEnergyLost;
    }

    /** Clorus and their offspring each get 50% of the energy, with none
     *  lost to the process. Now that's efficiency! Returns a baby
     *  Clorus.
     */
    public Clorus replicate() {
        energy = energy / 2;
        return new Clorus(energy);
    }

    /** Plips take exactly the following actions based on NEIGHBORS:
     *  1. If no empty adjacent spaces, STAY.
     *  2. Otherwise, if any Plips, Attack random one.
     *  3. Otherwise, if energy greater than or equal to one, REPLICATE to a random empty square.
     *  4. Otherwise, if Move to a random empty space.
     *
     *  Returns an object of type Action. See Action.java for the
     *  scoop on how Actions work. See SampleCreature.chooseAction()
     *  for an example to follow.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");

        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (plips.size() > 0) {
            Direction attackDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, attackDir);
        } else if (energy >= 1) {
            Direction replicateDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, replicateDir);
        }
        Direction moveDir = HugLifeUtils.randomEntry(empties);
        return new Action(Action.ActionType.MOVE, moveDir);
    }
}
