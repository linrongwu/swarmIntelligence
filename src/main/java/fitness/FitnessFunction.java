package fitness;

import java.math.BigDecimal;
import java.util.List;

public interface FitnessFunction {

    BigDecimal fitnessFunction(List<BigDecimal> params);
}
