package math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

public class MathUtil {
    public static BigDecimal getMean(List<BigDecimal> list) {
        return list.stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(list.size()), 10, RoundingMode.DOWN);
    }

    public static BigDecimal getVariance(BigDecimal mean, List<BigDecimal> list) {
        BigDecimal variance = BigDecimal.ZERO;
        for (BigDecimal data : list) {
            variance = variance.add(data.subtract(mean).pow(2));
        }
        variance = variance.divide(BigDecimal.valueOf(list.size()), 10, RoundingMode.DOWN);
        return variance;
    }


    public static BigDecimal getGaussian(BigDecimal mean, BigDecimal variance) {
        Random random = new Random();
        return BigDecimal.valueOf(Math.sqrt(variance.doubleValue()) * random.nextGaussian() + mean.doubleValue());
    }
}
