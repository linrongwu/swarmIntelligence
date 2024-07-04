package pso.gaussian;

import pso.standard.Particle;

import java.math.BigDecimal;
import java.util.List;

public class GaussianParticle extends Particle {
    public void velocityUpdate(BigDecimal w, BigDecimal c1, BigDecimal c2, BigDecimal c3, List<BigDecimal> gBest, List<BigDecimal> gaussian) {
        for (int i = 0; i < this.getVelocity().size(); i++) {
            BigDecimal v = this.getVelocity().get(i);
            BigDecimal p = this.getPosition().get(i);
            v = w.multiply(v)
                    .add(c1.multiply(BigDecimal.valueOf(Math.random())).multiply(this.getBest().get(i).subtract(p)))
                    .add(c2.multiply(BigDecimal.valueOf(Math.random())).multiply(gBest.get(i).subtract(p)))
                    .add(c3.multiply(BigDecimal.valueOf(Math.random())).multiply(gaussian.get(i).subtract(p)));
            this.getVelocity().set(i, v);
        }
    }

    public BigDecimal getGaussianPosition(int index) {
        return this.getPosition().get(index);
    }
}
