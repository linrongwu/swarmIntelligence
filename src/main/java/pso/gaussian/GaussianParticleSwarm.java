package pso.gaussian;

import fitness.FitnessFunction;
import math.MathUtil;
import pso.standard.Particle;
import pso.standard.ParticleSwarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GaussianParticleSwarm extends ParticleSwarm {
    private final BigDecimal m;//最优值的波动阈值
    private final Integer x;//波动阈值内连续命中的次数阈值
    private final Integer groupNumb;//分组比例
    private final BigDecimal c3;
    private Integer num;//波动阈值内连续命中的次数

    public GaussianParticleSwarm(Integer nPop, Integer dim, List<BigDecimal> positionUp, List<BigDecimal> positionDown, BigDecimal wMax, BigDecimal wMin, BigDecimal c1, BigDecimal c2, Integer maxIt, FitnessFunction fitnessFunction,
                                 BigDecimal m, Integer x, Integer groupNumb, BigDecimal c3) {
        super(nPop, dim, positionUp, positionDown, wMax, wMin, c1, c2, maxIt, fitnessFunction);
        this.m = m;
        this.x = x;
        this.groupNumb = groupNumb;
        this.c3 = c3;
        this.num = 0;
    }


    @Override
    public void initParticles() {
        for (int i = 0; i < this.getnPop(); i++) {
            GaussianParticle particle = new GaussianParticle();
            particle.velocityInit(this.getDim());
            particle.positionInit(this.getDim(), this.getPositionUp(), this.getPositionDown(), this.getFitnessFunction());
            this.getParticles().add(particle);
            if (Objects.isNull(this.getgBestParticle())) {
                this.setgBestParticle(particle);
            } else if (particle.getBestFit().compareTo(this.getgBestParticle().getBestFit()) >= 0) {
                this.setgBestParticle(particle);
            }
        }
    }

    @Override
    public List<BigDecimal> iterativeParticles() {
        List<BigDecimal> data = new ArrayList<>();
        for (int i = 0; i < this.getMaxIt(); i++) {
            double d = (double) i / this.getMaxIt();
            BigDecimal w = this.getwMax().subtract(this.getwMax().subtract(this.getwMin()).multiply(new BigDecimal(d)));
            if (num >= x) {
                List<Particle> particleList = this.getParticles().stream().sorted(Comparator.comparing(Particle::getBestFit).reversed()).toList();
                List<Particle> particleTop = particleList.subList(0, groupNumb);
                List<Particle> particleDown = particleList.subList(groupNumb, this.getnPop());
                iterativeDown(w, particleTop, particleDown);
                iterativeTop(w, particleTop);
                data.add(this.getgBestParticle().getBestFit());
                num = 0;
                continue;
            }
            BigDecimal oldBest = this.getgBestParticle().getBestFit();
            for (Particle p : this.getParticles()) {
                p.velocityUpdate(w, this.getC1(), this.getC2(), this.getgBestParticle().getBest());
                p.positionUpdate(this.getPositionUp(), this.getPositionDown(), this.getFitnessFunction());
                if (p.getBestFit().compareTo(this.getgBestParticle().getBestFit()) >= 0) {
                    this.setgBestParticle(p);
                }
            }
            data.add(this.getgBestParticle().getBestFit());
            if (this.getgBestParticle().getBestFit().subtract(oldBest).compareTo(m) <= 0) {
                num = num + 1;
            } else {
                num = 0;
            }
        }
        return data;
    }

    private void iterativeDown(BigDecimal w, List<Particle> particleTop, List<Particle> particleDown) {
        List<BigDecimal> meanList = new ArrayList<>();
        List<BigDecimal> varianceList = new ArrayList<>();
        for (int j = 0; j < this.getDim(); j++) {
            List<BigDecimal> jDim = getGaussianDimList(particleTop, j);
            BigDecimal meanJDim = MathUtil.getMean(jDim);
            BigDecimal varianceJDim = MathUtil.getVariance(meanJDim, jDim);
            meanList.add(meanJDim);
            varianceList.add(varianceJDim);
        }
        for (Particle p : particleDown) {
            GaussianParticle gp = (GaussianParticle) p;
            List<BigDecimal> gaussian = new ArrayList<>();
            for (int j = 0; j < this.getDim(); j++) {
                gaussian.add(MathUtil.getGaussian(meanList.get(j), varianceList.get(j)));
            }
            gp.velocityUpdate(w, this.getC1(), this.getC2(), c3, this.getgBestParticle().getBest(), gaussian);
            gp.positionUpdate(this.getPositionUp(), this.getPositionDown(), this.getFitnessFunction());
            if (gp.getBestFit().compareTo(this.getgBestParticle().getBestFit()) >= 0) {
                this.setgBestParticle(gp);
            }
        }

    }

    private List<BigDecimal> getGaussianDimList(List<Particle> particleTop, int j) {
        return particleTop.stream().map(p -> ((GaussianParticle) p).getGaussianPosition(j)).collect(Collectors.toList());
    }

    private void iterativeTop(BigDecimal w, List<Particle> particleTop) {
        for (Particle p : particleTop) {
            p.velocityUpdate(w, this.getC1(), this.getC2(), this.getgBestParticle().getBest());
            p.positionUpdate(this.getPositionUp(), this.getPositionDown(), this.getFitnessFunction());
            if (p.getBestFit().compareTo(this.getgBestParticle().getBestFit()) >= 0) {
                this.setgBestParticle(p);
            }
        }
    }
}
