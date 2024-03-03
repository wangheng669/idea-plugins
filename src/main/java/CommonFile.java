import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommonFile {

    public static void Save(String fileName,String value)  {
        String filePath = fileName+".txt"; // 替换为您要写入的文件路径
        try {
            replaceOrAppendStringInFile(filePath,value.replaceAll(":.*", "")+":\\d+",value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] readFileToArray(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        return lines.toArray(new String[0]);
    }

    private static void replaceOrAppendStringInFile(String filePath, String searchString, String replacement) throws IOException {
        // 创建文件
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("File created: " + filePath);
        }
        // 读取文件内容并替换字符串
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder content = new StringBuilder();
        boolean found = false;
        while ((line = reader.readLine()) != null) {
            if (line.contains(searchString.replaceAll(":.*", ""))) {
                found = true;
            }
            // 使用正则表达式匹配字符串
            String modifiedLine = line.replaceAll(searchString, replacement);
            content.append(modifiedLine).append("\n");
        }
        reader.close();
        // 如果未找到匹配的字符串，则追加到文件末尾
        if (!found) {
            content.append(replacement).append("\n");
        }
        // 写入替换后的内容到文件中
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content.toString());
        writer.close();
    }

}
