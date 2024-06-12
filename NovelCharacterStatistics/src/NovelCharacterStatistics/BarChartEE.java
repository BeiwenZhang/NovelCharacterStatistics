package NovelCharacterStatistics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BarChartEE extends JFrame {

    public BarChartEE(String title, Map<String, Integer> characterCount) {
        super(title);

        // 创建数据集
        DefaultCategoryDataset dataset = createDataset(characterCount);

        // 创建立体条形统计图
        JFreeChart chart = ChartFactory.createBarChart3D(
                title,  // 图表标题
                "Category",             // x 轴标签
                "Value",                // y 轴标签
                dataset,                // 数据集
                PlotOrientation.VERTICAL,
                true,                   // 是否显示图例
                true,                   // 是否生成工具
                false                   // 是否生成 URL 链接
        );
        // 获取图表的绘图区域
        CategoryPlot plot = chart.getCategoryPlot();

        // 创建一个渐变色画笔
        Paint[] paintArray = new Paint[] {
                new GradientPaint(0, 0, new Color(0, 128, 255), 0, 0, new Color(0, 255, 255)), // 蓝色到青色
                new GradientPaint(0, 0, new Color(0, 255, 0), 0, 0, new Color(255, 255, 0)),   // 绿色到黄色
                new GradientPaint(0, 0, new Color(255, 165, 0), 0, 0, new Color(255, 0, 0))    // 橙色到红色
        };

        // 设置绘图区域的渐变色
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, paintArray[0]);
        renderer.setSeriesPaint(1, paintArray[1]);
        renderer.setSeriesPaint(2, paintArray[2]);

        // 创建一个自定义颜色绘制器
        DefaultDrawingSupplier drawingSupp = new DefaultDrawingSupplier(
                new Paint[] {new Color(0, 128, 255), new Color(0, 255, 0), new Color(255, 165, 0)},
                new Paint[] {new Color(0, 255, 255), new Color(255, 255, 0), new Color(255, 0, 0)},
                new Stroke[] {new BasicStroke(2.0f)},
                new Stroke[] {new BasicStroke(2.0f)},
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
        );

        // 应用自定义颜色绘制器
        plot.setDrawingSupplier(drawingSupp);

        // 将图表添加到 Swing 组件中
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset(Map<String, Integer> characterCount) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : characterCount.entrySet()) {
            dataset.addValue(entry.getValue(), "重要角色", entry.getKey());
        }

        return dataset;
    }
}

