package pso.standard;

import fitness.FitnessFunction;
import swarmIntelligence.SwarmIntelligence;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParticleSwarm implements SwarmIntelligence {
    private Particle gBestParticle;
    private final List<Particle> particles;//粒子群
    private final Integer nPop;//粒子群数量
    private final Integer dim;//问题维度
    private final List<BigDecimal> positionUp;//上界
    private final List<BigDecimal> positionDown;//下界
    private final BigDecimal wMax;
    private final BigDecimal wMin;
    private final BigDecimal c1;
    private final BigDecimal c2;
    private final Integer maxIt;
    private final FitnessFunction fitnessFunction;

    public ParticleSwarm(
            Integer nPop,
            Integer dim,
            List<BigDecimal> positionUp,
            List<BigDecimal> positionDown,
            BigDecimal wMax,
            BigDecimal wMin,
            BigDecimal c1,
            BigDecimal c2,
            Integer maxIt,
            FitnessFunction fitnessFunction) {
        this.nPop = nPop;
        this.dim = dim;
        this.positionUp = positionUp;
        this.positionDown = positionDown;
        this.wMax = wMax;
        this.wMin = wMin;
        this.c1 = c1;
        this.c2 = c2;
        this.maxIt = maxIt;
        this.fitnessFunction = fitnessFunction;
        this.particles = new ArrayList<>();
    }

    public void initParticles() {
        for (int i = 0; i < nPop; i++) {
            Particle particle = new Particle();
            particle.velocityInit(dim);
            particle.positionInit(dim, positionUp, positionDown, fitnessFunction);
            particles.add(particle);
            if (Objects.isNull(gBestParticle)) {
                gBestParticle = particle;
            } else if (particle.getBestFit().compareTo(gBestParticle.getBestFit()) >= 0) {
                gBestParticle = particle;
            }
        }
    }

    public List<BigDecimal> iterativeParticles() {
        List<BigDecimal> data = new ArrayList<>();
        for (int i = 0; i < maxIt; i++) {
            double d = (double) i / maxIt;
            BigDecimal w = wMax.subtract(wMax.subtract(wMin).multiply(new BigDecimal(d)));
            for (Particle p : particles) {
                p.velocityUpdate(w, c1, c2, gBestParticle.getBest());
                p.positionUpdate(positionUp, positionDown, fitnessFunction);
                if (p.getBestFit().compareTo(gBestParticle.getBestFit()) >= 0) {
                    gBestParticle = p;
                }
            }
            data.add(gBestParticle.getBestFit());
        }
        return data;
    }

    public Integer getMaxIt() {
        return maxIt;
    }

    public BigDecimal getwMax() {
        return wMax;
    }

    public BigDecimal getwMin() {
        return wMin;
    }

    public BigDecimal getC1() {
        return c1;
    }

    public BigDecimal getC2() {
        return c2;
    }

    public Particle getgBestParticle() {
        return gBestParticle;
    }

    public void setgBestParticle(Particle gBestParticle) {
        this.gBestParticle = gBestParticle;
    }

    public List<BigDecimal> getPositionUp() {
        return positionUp;
    }

    public List<BigDecimal> getPositionDown() {
        return positionDown;
    }

    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }

    public Integer getnPop() {
        return nPop;
    }

    public Integer getDim() {
        return dim;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    @Override
    public List<BigDecimal> iterative() {
        this.initParticles();
        return this.iterativeParticles();
    }
}
