import fitness.FitnessFunction;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pso.ParticleSwarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws Exception {
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
        List<BigDecimal> data = particleSwarm.iterativeParticles();
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        int i = 0;
        for (BigDecimal value : data) {
            series.getData().add(new XYChart.Data<>((double) i, -value.doubleValue()));
            i++;
        }
        LineChart lc = new LineChart(
                new NumberAxis("迭代次数", 0.0, data.size(), 1),
                new NumberAxis("目标函数值", -data.getLast().doubleValue(), -data.getFirst().doubleValue(), (data.getLast().doubleValue() - data.getFirst().doubleValue()) / data.size())
        );

        lc.getData().add(series);
        lc.setTitle("f(x1,x2,x3,x4,x5,x6,x7)=x1^2+x2^2+x3^2+x4^2+x5^2+x6^2+x7^2，x1...x7在[-500,500] 求f(x1,x2,x3,x4,x5,x6,x7)的最小值");
        lc.setStyle("-fx-background-color: lightgray");
        lc.setCreateSymbols(false);
        lc.setLegendVisible(false);

        VBox vbox = new VBox(lc);

        Scene scene = new Scene(vbox);

        stage.setScene(scene);
        stage.setTitle("标准PSO算法求解单峰函数极值问题");
        stage.show();
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

    public static void main(String[] args) {
        launch(args);
    }
}
