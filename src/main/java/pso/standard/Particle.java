package pso.standard;

import fitness.FitnessFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Particle {
    private final List<BigDecimal> position;
    private final List<BigDecimal> velocity;
    private final List<BigDecimal> best;
    private BigDecimal bestFit;

    public Particle() {
        this.position = new ArrayList<>();
        this.velocity = new ArrayList<>();
        this.best = new ArrayList<>();
    }

    public void positionInit(Integer dim, List<BigDecimal> positionUp, List<BigDecimal> positionDown, FitnessFunction fitnessFunction) {
        for (int i = 0; i < dim; i++) {
            BigDecimal data = positionUp.get(i).subtract(positionDown.get(i))
                    .multiply(BigDecimal.valueOf(Math.random()))
                    .add(positionDown.get(i));
            position.add(data);
            best.add(data);
        }
        bestFit = fitnessFunction.fitnessFunction(best);
    }

    public void velocityInit(Integer dim) {
        for (int i = 0; i < dim; i++) {
            velocity.add(BigDecimal.ZERO);
        }
    }

    public void velocityUpdate(BigDecimal w, BigDecimal c1, BigDecimal c2, List<BigDecimal> gBest) {
        for (int i = 0; i < velocity.size(); i++) {
            BigDecimal v = velocity.get(i);
            BigDecimal p = position.get(i);
            v = w.multiply(v)
                    .add(c1.multiply(BigDecimal.valueOf(Math.random())).multiply(best.get(i).subtract(p)))
                    .add(c2.multiply(BigDecimal.valueOf(Math.random())).multiply(gBest.get(i).subtract(p)));
            velocity.set(i, v);
        }
    }

    public void positionUpdate(List<BigDecimal> positionUp, List<BigDecimal> positionDown, FitnessFunction fitnessFunction) {
        for (int i = 0; i < position.size(); i++) {
            BigDecimal data = position.get(i);
            data = data.add(velocity.get(i));
            if (data.compareTo(positionUp.get(i)) >= 0) {
                data = positionUp.get(i);
            } else if (data.compareTo(positionDown.get(i)) <= 0) {
                data = positionDown.get(i);
            }
            position.set(i, data);
        }
        BigDecimal positionFit = fitnessFunction.fitnessFunction(position);
        if (positionFit.compareTo(bestFit) >= 0) {
            for (int i = 0; i < best.size(); i++) {
                BigDecimal data = position.get(i);
                best.set(i, data);
            }
            bestFit = positionFit;
        }
    }

    public List<BigDecimal> getBest() {
        return best;
    }

    public BigDecimal getBestFit() {
        return bestFit;
    }


    public List<BigDecimal> getVelocity() {
        return velocity;
    }

    public List<BigDecimal> getPosition() {
        return position;
    }


}
