import fitness.FitnessFunction;
import pso.ParticleSwarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PSOTest {
    public static void main(String[] args) {
        //问题域
        /*求解单峰函数极值问题*/
        FitnessFunction fitnessFunction = params -> {
            BigDecimal result = BigDecimal.ZERO;
            for (BigDecimal param : params) {
                result = result.add(param.pow(2));
            }
            return BigDecimal.ZERO.subtract(result);
        };

        //初始化粒子群算法参数
        int nPop = 50;
        ParticleSwarm particleSwarm = getParticleSwarm(nPop, fitnessFunction);
        particleSwarm.initParticles();
        List<BigDecimal> data =particleSwarm.iterativeParticles();

    }

    private static ParticleSwarm getParticleSwarm(int nPop, FitnessFunction fitnessFunction) {
        int dim = 7;
        double wMax = 0.9;
        double wMin = 0.6;
        int c1 = 2;
        int c2 = 2;
        int maxIt = 200;
        List<BigDecimal> positionUp = new ArrayList<>();
        List<BigDecimal> positionDown = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            positionUp.add(new BigDecimal(500));
        }
        for (int i = 0; i < dim; i++) {
            positionDown.add(new BigDecimal(-500));
        }
        return new ParticleSwarm(nPop, dim, positionUp, positionDown,
                new BigDecimal(wMax), new BigDecimal(wMin), new BigDecimal(c1), new BigDecimal(c2),
                maxIt, fitnessFunction);
    }
}
