package window;

import fitness.FitnessFunction;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pso.gaussian.GaussianParticleSwarm;
import pso.standard.ParticleSwarm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    public FitnessFunction fitnessFunction() {
        /*sphere*/
        return params -> {
            BigDecimal result = BigDecimal.ZERO;
            for (BigDecimal param : params) {
                result = result.add(param.pow(2));
            }
            return BigDecimal.ZERO.subtract(result);
        };
    }

    @Override
    public void start(Stage stage) {
        FitnessFunction fitnessFunction = fitnessFunction();
        int dim = 50;
        List<BigDecimal> positionUp = new ArrayList<>();
        List<BigDecimal> positionDown = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            positionUp.add(new BigDecimal(100));
        }
        for (int i = 0; i < dim; i++) {
            positionDown.add(new BigDecimal(-100));
        }
        int nPop = 30;
        BigDecimal wMax = BigDecimal.valueOf(0.9);
        BigDecimal wMin = BigDecimal.valueOf(0.4);
        BigDecimal c1 = BigDecimal.valueOf(1.2);
        BigDecimal c2 = BigDecimal.valueOf(1.5);
        int maxIt = 200;
        ParticleSwarm particleSwarm = new ParticleSwarm(nPop, dim, positionUp, positionDown,
                wMax, wMin, c1, c2, maxIt, fitnessFunction);

        BigDecimal m = BigDecimal.valueOf(500);
        int x = 5;
        int groupNumb = 10;
        BigDecimal c3 = BigDecimal.valueOf(1.7);
        GaussianParticleSwarm gaussianParticleSwarm = new GaussianParticleSwarm(nPop, dim, positionUp, positionDown,
                wMax, wMin, c1, c2, maxIt, fitnessFunction,
                m, x, groupNumb, c3);

        particleSwarm.initParticles();
        List<BigDecimal> data1 = particleSwarm.iterativeParticles();
        gaussianParticleSwarm.initParticles();
        List<BigDecimal> data2 = gaussianParticleSwarm.iterativeParticles();
        XYChart.Series<Double, Double> series1 = new XYChart.Series<>();
        XYChart.Series<Double, Double> series2 = new XYChart.Series<>();
        buildSeries(series1, data1);
        buildSeries(series2, data2);
        double maxData = data1.getLast().compareTo(data2.getLast()) > 0 ? data1.getLast().doubleValue() : data2.getLast().doubleValue();
        double minData = data1.getFirst().compareTo(data2.getLast()) < 0 ? data1.getFirst().doubleValue() : data2.getFirst().doubleValue();

        LineChart lc = new LineChart(
                new NumberAxis("迭代次数", 0.0, data1.size(), 1),
                new NumberAxis("目标函数值", -maxData, -minData, (maxData - minData) / data1.size())
        );
        lc.getData().addAll(series1, series2);
        lc.setTitle("sphere函数");
        lc.setStyle("-fx-background-color: lightgray");
        lc.setCreateSymbols(false);
        lc.setLegendVisible(false);
        VBox vbox = new VBox(lc);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("粒子群算法");
        stage.show();
    }

    private void buildSeries(XYChart.Series<Double, Double> series, List<BigDecimal> data) {
        int i = 0;
        for (BigDecimal value : data) {
            series.getData().add(new XYChart.Data<>((double) i, -value.doubleValue()));
            i++;
        }
    }
}
