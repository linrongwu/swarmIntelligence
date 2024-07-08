package window;

import swarmIntelligence.SwarmIntelligence;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

public class AppTask implements Callable<List<BigDecimal>> {

    private final SwarmIntelligence swarmIntelligence;

    public AppTask(SwarmIntelligence swarmIntelligence) {
        this.swarmIntelligence = swarmIntelligence;
    }

    @Override
    public List<BigDecimal> call() {
        return swarmIntelligence.iterative();
    }
}
