package NovelCharacterStatistics;
import com.sun.javafx.charts.Legend;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import javax.swing.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class percentChart extends Application {
    double[][] data=new double[10][2];



    //现在关键是接口问题。
    @Override
    public void start(Stage primaryStage) {
        List<String> characters = Arrays.asList("莉拉", "布鲁诺", "安东尼奥", "尼诺", "斯特凡诺", "农齐亚", "里诺", "马尔切洛", "多纳托", "皮诺奇娅");
        NovelCharacterStatistics statistics = new NovelCharacterStatistics(characters);

        String filePath = "C:\\Users\\86158\\Desktop\\java\\课程test\\src\\NovelCharacterStatistics\\那不勒斯四部曲：新名字的故事.txt";
        String novelText = statistics.readTxtFile(filePath);

        statistics.processNovel(novelText);

        Map<String, Integer> characterCount = statistics.getCharacterCount();
        //Map<String, Integer> characterCount映射
        List<String> sortedCharactersByCount = statistics.getSortedCharactersByCount();
        System.out.println("Character count:");
        for (String character : sortedCharactersByCount) {
            System.out.println(character + ": " + characterCount.get(character));
        }


        Map<String, List<Integer>> characterPositions = statistics.getCharacterPositions();
        List<String> sortedCharactersBySpan = statistics.getSortedCharactersBySpan();
        System.out.println("\nCharacter span:");



        int i0=0;
        for (String character : sortedCharactersBySpan) {
            List<Integer> positions = characterPositions.get(character);
            int span = positions.get(positions.size() - 1) - positions.get(0);
            double startPercentage = (double) positions.get(0) / novelText.length() * 100;
            double endPercentage = (double) positions.get(positions.size() - 1) / novelText.length() * 100;
            data[i0][0]=startPercentage;
            data[i0][1]=endPercentage;
            i0++;
            System.out.println(character + ": " + span + " (Start: " + startPercentage + "%, End: " + endPercentage + "%)");
        }
//percentChart PC=new percentChart(statistics.percentage);
//double 百分比
        //下面是关系密切程度，每次直接计算出两个角色的，现在需要存起来

        int i1=0,j1=0;
//所有ij相关的？？现在有这个relation[][]数组
        System.out.println("\nCharacter relationships:");
        for (String character1 : characters) {

            j1=0;
            for (String character2 : characters) {

                if (!character1.equals(character2)) {
                    int contactCount = statistics.countContactOccurrences(novelText, character1, character2);
                    statistics.getRelation()[i1][j1]=contactCount;
                    System.out.println(character1 + " and " + character2 + ": " + contactCount + " contacts");
                }j1++;
            }i1++;
        }

        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                System.out.println(statistics.getRelation()[i][j]);}
            System.out.println("\n");
        }


        ScatterPlot visualizer = new ScatterPlot("人物出现位置散点图", characterPositions);
        visualizer.setSize(800, 600);
        visualizer.setLocationRelativeTo(null);
        visualizer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        visualizer.setVisible(true);
        //小团队
        HashSet<Set<String>> teams=statistics.findPossibleTeams(novelText,300);
        //小团队有HashSet<Set<String>>
        for (Set<String> set : teams) {
            System.out.println("Set:");
            for (String element : set) {
                System.out.println(element);
            }
        }
        Map<String, Integer> sortedMap = characterCount.entrySet()
                .stream()
                //entrySet()返回map中包含所有键对的set，转化成stream
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                //Map.Entry取值，并且以降序排列。comparingByValue()方法返回一个比较器，用于按值排序，reversed()方法将排序顺序反转。
                .collect(Collectors.toMap(
                        Map.Entry::getKey,//直接引用Map.Entry的参数
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // 如果存在重复键，保留第一个
                        LinkedHashMap::new
                ));

        // 输出有序映射
        sortedMap.forEach((key, value) -> System.out.println(key + ": " + value));//lamda表达式

        SwingUtilities.invokeLater(() -> {
            BarChartEE EE = new BarChartEE("人物出场次数统计", sortedMap);
            EE.pack();
            EE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            EE.setVisible(true);
        });
//做表
        RelationTableEE EE = new RelationTableEE("关系紧密程度表", statistics.getRelation());
        EE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EE.setVisible(true);

        //下面是线条图
        String[] colors = {"#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b", "#e377c2", "#7f7f7f", "#bcbd22", "#17becf"};
//设置不同颜色

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();//LineChart的参数
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        for (int i = data.length - 1; i >= 0; i--) {
            double start = data[i][0] / 100;//取出之前的data
            double end = data[i][1] / 100;

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            //查文档XYChart是chart的子类，Series是一个内部类
            series.setName(sortedCharactersBySpan.get(9-i));

            series.getData().add(new XYChart.Data<>(start, i * 3));
            series.getData().add(new XYChart.Data<>(end, i * 3));

            String lineColor = colors[i];
            lineChart.getData().add(series);

            // Set line and symbol colors
            Node line = series.getNode().lookup(".chart-series-line");
            line.setStyle("-fx-stroke: " + lineColor + ";");

            for (XYChart.Data<Number, Number> point : series.getData()) {
                Node lineSymbol = point.getNode().lookup(".chart-line-symbol");
                lineSymbol.setStyle("-fx-background-color: " + lineColor + ", white;");
            }
        }

        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setTitle("人物篇幅跨度示意图");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set legend item colors
        for (int i = 0; i < lineChart.getData().size(); i++) {
            Node legendSymbol = lineChart.lookup(".chart-legend-item-symbol.series" + i);
            String lineColor = colors[i];
            legendSymbol.setStyle("-fx-background-color: " + lineColor + ", white;");
        }
    }

    public static void main(String[] args) {
        launch(args);
//    }
}

}
