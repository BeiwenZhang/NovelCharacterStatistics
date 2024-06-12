package NovelCharacterStatistics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NovelCharacterStatistics {
//extends Applicatio

    private Map<String, Integer> characterCount;          // 用于存储每个角色的出现次数<原名对应别名的映射，和整数
    private Map<String, List<Integer>> characterPositions; // 用于存储每个角色在文本中的位置
    private Map<String, List<String>> characterAliases;          // 用于存储角色的别名映射
    private double[][] percentage = new double[10][2];
    private int[][] relation = new int[10][10];


    public NovelCharacterStatistics(Map<String, Integer> characterCount, Map<String, List<Integer>> characterPositions, Map<String, List<String>> characterAliases, double[][] percentage, int[][] relation) {
        this.characterCount = characterCount;
        this.characterPositions = characterPositions;
        this.characterAliases = characterAliases;
        this.percentage = percentage;
        this.relation = relation;
    }

    public void setCharacterCount(Map<String, Integer> characterCount) {
        this.characterCount = characterCount;
    }

    public void setCharacterPositions(Map<String, List<Integer>> characterPositions) {
        this.characterPositions = characterPositions;
    }

    public Map<String, List<String>> getCharacterAliases() {
        return characterAliases;
    }

    public void setCharacterAliases(Map<String, List<String>> characterAliases) {
        this.characterAliases = characterAliases;
    }

    public double[][] getPercentage() {
        return percentage;
    }

    public void setPercentage(double[][] percentage) {
        this.percentage = percentage;
    }

    public int[][] getRelation() {
        return relation;
    }

    public void setRelation(int[][] relation) {
        this.relation = relation;
    }

    //不要那么复杂，分开存
    public NovelCharacterStatistics(List<String> characters) {//构造方法
        this.characterCount = new HashMap<>();
        this.characterPositions = new HashMap<>();
        this.characterAliases = initializeCharacterAliases();
        //首先 数据结构不变，关键是chractercount 如果不变可以吗？如果不变的话，就还是保持原样（计算），但是把映射到的结果也去查，然后把次数加入。
        for (String character : characters) {
            characterCount.put(character, 0);
            characterPositions.put(character, new ArrayList<>());
        }

    }

    public void processNovel(String novelText) {
        for (String character : characterCount.keySet()) {//对于每一个角色
            //String mainName = getMainName(character); // 获取主要名称
            int count = countCharacterOccurrences(novelText, character);//拿到对应数目
            characterCount.put(character, count);//把map放进去
            List<Integer> positions = findCharacterPositions(novelText, character);
            characterPositions.put(character, positions);//对应位置的map放进来

        }
    }

    // 计算角色在文本中的出现次数，基础方法
    //@param 现有的count，目标角色名，总text
    //return 新的count
    public static int culculate(String text, String character, int count) {
        int index = text.indexOf(character);//找到对应的位置
        while (index != -1) {//找到一次就++
            count++;
            index = text.indexOf(character, index + 1);//直接找下一个位置
        }
        return count;
    }

    //总体计算次数
    //@param 角色本名
    //对映射到的每个别名再处理，返回最后结果
    public int countCharacterOccurrences(String text, String character) {
        int count = 0;
        count = culculate(text, character, count);//先对本名做
        //下一步 把character有映射的就取出来list（不空才做），且遍历，把对应的count加进去，并对position进行同样的处理
        List<String> values = characterAliases.get(character);
        if (values != null) {//如果不为空（一个list），就遍历
            for (int i = 0; i < values.size(); i++) {
                String element = values.get(i);
                count = culculate(text, element, count);//注意 count是在现在的基础上实现++的
            }
        }
        return count;
    }


    //@param 输入角色的本名
    //找本名+所有别名的位置
    public List<Integer> findCharacterPositions(String text, String character) {
        List<Integer> positions = new ArrayList<>();
        positions = findPositions(text, character, positions);
        List<String> values = characterAliases.get(character);
        if (values != null) {//可能初始就为空
            for (int i = 0; i < values.size(); i++) {
                String element = values.get(i);
                positions = findPositions(text, element, positions);
            }
        }
        Collections.sort(positions);//因为有别名，造成混乱，进行sort
        return positions;
    }

    // 查找一个角色名在文本中的位置（基础方法）
    public static List<Integer> findPositions(String text, String character, List<Integer> positions) {
        //List<Integer> positions = new ArrayList<>();
        int index = text.indexOf(character);//找到位置
        while (index != -1) {//最后一次-1位终止
            positions.add(index);//找到了位置就放进position
            index = text.indexOf(character, index + 1);//从下一个位置开始找
        }
        return positions;
    }


    public Map<String, Integer> getCharacterCount() {
        return characterCount;
    }

    public Map<String, List<Integer>> getCharacterPositions() {
        return characterPositions;
    }

    // 获取按出现次数排序的角色列表
    public List<String> getSortedCharactersByCount() {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(characterCount.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        List<String> sortedCharacters = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            sortedCharacters.add(entry.getKey());
        }
        return sortedCharacters;
    }

    // 获取按在文本中位置范围排序的角色列表
    public List<String> getSortedCharactersBySpan() {
        List<Map.Entry<String, List<Integer>>> sortedEntries = new ArrayList<>(characterPositions.entrySet());
        sortedEntries.sort(Comparator.comparingInt(entry -> entry.getValue().get(entry.getValue().size() - 1) - entry.getValue().get(0)));

        List<String> sortedCharacters = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : sortedEntries) {
            sortedCharacters.add(entry.getKey());
        }
        return sortedCharacters;
    }


    // 读取文本文件内容
    public static String readTxtFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {//直接用reader来读
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");//一行一行读（即一段一段）
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    // 计算两个角色之间的接触次数 之前取别名有映射
    //最简单的思路 二重循环（如果两个都存在的话），以这个方法为基础方法
    public static int countContact(String text, String character1, String character2, int count) {
        int index1 = text.indexOf(character1);
        int index2 = text.indexOf(character2);
        while (index1 != -1 && index2 != -1) {
            if (Math.abs(index1 - index2) <= 200) {
                count++;
            }
            if (index1 < index2) {
                index1 = text.indexOf(character1, index1 + 1);
            } else {
                index2 = text.indexOf(character2, index2 + 1);
            }
        }
        return count;
    }


    //加入了这一个方法之后，两两之间的contact大大增加
    public int countContactOccurrences(String text, String character1, String character2) {
//对于character，拿到对应的映射结果，调用countContact
        int count = 0;
        count = countContact(text, character1, character2, count);//初始的Contact
        List<String> values1 = characterAliases.get(character1);
        List<String> values2 = characterAliases.get(character2);
//之后逐一和别名比较，取到别名，如果不为null，则取出来处理
        if (values1 != null) {
            for (int i = 0; i < values1.size(); i++) {
                String element = values1.get(i);
                count = countContact(text, element, character2, count);
            }
        }
        if (values2 != null) {
            for (int i = 0; i < values2.size(); i++) {
                String element = values2.get(i);
                count = countContact(text, character1, element, count);
            }
        }
        //如果两个都不为null 再专门对别名处理一遍
        if (values2 != null && values1 != null) {
            for (int i = 0; i < values2.size(); i++) {
                String element2 = values2.get(i);
                for (int j = 0; j < values1.size(); j++) {
                    String element1 = values1.get(j);
                    count = countContact(text, element1, element2, count);
                }
            }
        }
        //最终return的是对应两个character的count
        return count;
    }


    // 初始化角色别名映射，直接返回原本名，对应全部别名
    private Map<String, List<String>> initializeCharacterAliases() {
        Map<String, List<String>> characterAliases = new HashMap<>();

        characterAliases.computeIfAbsent("莉拉", k -> new ArrayList<>()).add("莉娜");
        characterAliases.computeIfAbsent("莉拉", k -> new ArrayList<>()).add("拉法埃拉");
        characterAliases.computeIfAbsent("马尔切洛", k -> new ArrayList<>()).add("索拉拉");
        characterAliases.computeIfAbsent("布鲁诺", k -> new ArrayList<>());
//        //"莉拉", "埃莱娜", "安东尼奥", "尼诺", "斯特凡诺", "农奇亚", "里诺", "马尔切洛", "多纳托", "皮诺奇娅"
        characterAliases.computeIfAbsent("尼诺", k -> new ArrayList<>());
        characterAliases.computeIfAbsent("斯特凡诺", k -> new ArrayList<>());
        characterAliases.computeIfAbsent("农奇亚", k -> new ArrayList<>());
        characterAliases.computeIfAbsent("里诺", k -> new ArrayList<>());
        characterAliases.computeIfAbsent("多纳托", k -> new ArrayList<>());
        characterAliases.computeIfAbsent("皮诺奇娅", k -> new ArrayList<>());
        characterAliases.computeIfAbsent("安东尼奥", k -> new ArrayList<>());
        // 添加更多别名映射...

        return characterAliases;
    }

    //多人同时在一个windowSize里面出现3次以上，视为小团体
    public HashSet<Set<String>> findPossibleTeams(String text, int windowSize) {
        HashSet<Set<String>> possibleTeams = new HashSet<>();


        for (int i = 0; i < text.length() - windowSize; i++) {//一个一个窗口里面看
            String window = text.substring(i, i + windowSize);
            // 统计当前窗口内每个人物出现的次数
            Map<String, Integer> windowCharacterCount = new HashMap<>();
            for (String character : characterCount.keySet()) {
                int count = countCharacterOccurrences(window, character);
                if (count > 3) {
                    //System.out.println("first"+character);
                    windowCharacterCount.put(character, count);
                }
            }
            // 找出频繁出现的人物作为小团队的一部分 &&entry.getKey()!="莉拉"
            Set<String> team = new HashSet<>();
            for (Map.Entry<String, Integer> entry : windowCharacterCount.entrySet()) {
                if (entry.getValue() >= 3) {
                    //System.out.println("!!!"+entry.getKey());
                    team.add(entry.getKey());
                }
            }
            if (team.size() > 1) {
                possibleTeams.add(team);
            }
        }
        return possibleTeams;
    }
}
