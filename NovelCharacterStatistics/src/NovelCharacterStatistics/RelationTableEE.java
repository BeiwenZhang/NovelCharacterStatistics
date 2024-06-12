package NovelCharacterStatistics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

public class RelationTableEE extends JFrame {

    public RelationTableEE(String title, int[][] data) {
        super(title);
        setSize(800, 600);

        // 创建一个容器来放置表格、文本框、图片和按钮
        JPanel container = new JPanel(new BorderLayout());

        // 添加表格
        DefaultTableModel model = createTableModel(data);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane, BorderLayout.CENTER);
        JPanel imagePanel = new JPanel();

        // 添加图片
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\86158\\Desktop\\java\\课程test\\src\\image\\莉拉 出现频次.jpg"); // 请替换成实际图片路径
        JLabel imageLabel = new JLabel(imageIcon);

        imagePanel.add(imageLabel);
        ImageIcon imageIcon2 = new ImageIcon("C:\\Users\\86158\\Desktop\\java\\课程test\\src\\image\\双人 用于出现.jpg"); // 请替换成实际图片路径
        JLabel imageLabel2 = new JLabel(imageIcon2);
        imagePanel.add(imageLabel2);

        container.add(imagePanel, BorderLayout.NORTH);

        // 创建文本框
        JTextField textField = new JTextField("请输入想要查询的角色名");
        //textField.setToolTipText("请输入想要查询的角色名");

        // 创建确定按钮
        JButton searchButton = new JButton("确定");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String,Integer>relation= new HashMap();
                String searchTerm = textField.getText();
                switch (searchTerm) {
                    case "莉拉":
                         relation= change(0,data);
                        BarChartEE bce0=new BarChartEE("莉拉人物关系紧密排序",relation);
                        bce0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce0.setVisible(true);
                        break;
                    case "布鲁诺":
                        relation = change(1,data);
                        BarChartEE bce1=new BarChartEE("布鲁诺人物关系紧密排序",relation);
                        bce1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce1.setVisible(true);
                        break;

                    case "安东尼奥":
                        relation = change(2,data);
                        BarChartEE bce2=new BarChartEE("安东尼奥人物关系紧密排序",relation);
                        bce2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce2.setVisible(true);
                        break;
                    // 添加更多的 case 语句，根据需要
                    case "尼诺":
                        relation = change(3,data);
                        BarChartEE bce3=new BarChartEE("尼诺人物关系紧密排序",relation);
                        bce3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce3.setVisible(true);
                        break;
                    case "斯特凡诺":
                        relation = change(4,data);
                        BarChartEE bce4=new BarChartEE("斯特凡诺人物关系紧密排序",relation);
                        bce4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce4.setVisible(true);
                        break;
                    case "农奇亚":
                        relation = change(5,data);
                        BarChartEE bce5=new BarChartEE("农奇亚人物关系紧密排序",relation);
                        bce5.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce5.setVisible(true);
                        break;
                    case "里诺":
                        relation = change(6,data);
                        BarChartEE bce6=new BarChartEE("里诺人物关系紧密排序",relation);
                        bce6.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce6.setVisible(true);
                        break;
                    case "马尔切洛":
                        relation = change(7,data);
                        BarChartEE bce7=new BarChartEE("马尔切洛人物关系紧密排序",relation);
                        bce7.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce7.setVisible(true);
                        break;
                    case "多纳托":
                        relation = change(8,data);
                        BarChartEE bce8=new BarChartEE("多纳托人物关系紧密排序",relation);
                        bce8.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce8.setVisible(true);
                        break;
                    case "皮诺奇娅":
                        relation = change(9,data);
                        BarChartEE bce9=new BarChartEE("皮诺奇娅人物关系紧密排序",relation);
                        bce9.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        bce9.setVisible(true);
                        break;
                    // 添加更多的 case 语句，根据需要


                    default:
                        JOptionPane.showMessageDialog(null, "错误：输入不合法。", "输入错误", JOptionPane.ERROR_MESSAGE);
                }

//获取map
                System.out.println("查询角色名：" + searchTerm);
            }
        });

        // 创建一个面板来放置文本框和按钮
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(textField);
        inputPanel.add(searchButton);

        // 将输入面板添加到容器的南方
        container.add(inputPanel, BorderLayout.SOUTH);

        // 将容器添加到窗口
        setLayout(new BorderLayout());
        add(container, BorderLayout.CENTER);
    }
    private Map<String, Integer> change(int x, int[][] data) {
        Map<String, Integer> relation = new HashMap<>(); // 初始化为一个新的 HashMap
        String[] columnNames = {
                "莉拉", "布鲁诺", "安东尼奥", "尼诺", "斯特凡诺", "农齐亚", "里诺", "马尔切洛", "多纳托", "皮诺奇娅"
        };

        for (int j = 0; j < 10; j++) {
            if (j == x) {
                continue;
            }
            relation.put(columnNames[j], (int) data[x][j]);
        }
        Map<String, Integer> sortedMap = relation.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // 如果存在重复键，保留第一个
                        LinkedHashMap::new
                ));

        // 输出有序映射
        sortedMap.forEach((key, value) -> System.out.println(key + ": " + value));

        return sortedMap;
    }


    private DefaultTableModel createTableModel(int[][] data) {
        // 创建一个表格模型，包括标题行和标题列
        DefaultTableModel model = new DefaultTableModel();

        // 添加列名，这里使用指定的列名数组
        String[] columnNames = {
                " ", "莉拉", "布鲁诺", "安东尼奥", "尼诺", "斯特凡诺", "农齐亚", "里诺", "马尔切洛", "多纳托", "皮诺奇娅"
        };
        model.setColumnIdentifiers(columnNames);

        // 添加数据行
        for (int i = 0; i < data.length; i++) {
            Object[] rowData = new Object[data[i].length + 1]; // 加一列为行标题
            rowData[0] = columnNames[i + 1];
            for (int j = 0; j < data[i].length; j++) {
                rowData[j + 1] = data[i][j];
            }
            model.addRow(rowData);
        }

        return model;
    }


}
