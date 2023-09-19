package ruangong;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class CosineSimilarity {//用余弦相似度近似代替文章相似度，越接近1文章间越相似

    // 将文本转换为词向量
    private static RealVector getVector(String text, List<String> words) {
        int size = words.size();
        double[] vector = new double[size];
        //使用HanLP进行中文分词
        List<Term> termList = HanLP.segment(text);
        for (Term term : termList) {
            String word = term.word.toLowerCase();//将词语转换为小写
            if (words.contains(word)) {
                int index = words.indexOf(word);//获取词语在词表中的索引
                vector[index]++;//增加相应位置的词频
            }
        }
        return new ArrayRealVector(vector);//返回词向量
    }

    // 计算余弦相似度
    private static double getCosineSimilarity(RealVector vector1, RealVector vector2) {
        return vector1.dotProduct(vector2) / (vector1.getNorm() * vector2.getNorm());
    }

    // 读取文件内容为字符串
    private static String readFileToString(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            // 读取原文和抄袭版论文文件的内容
            String originalText = readFileToString("D:\\JAVA_IDEA\\CODE\\TEXT1\\src\\ruangong\\lyp_orig.txt");
            String plagiarizedText1 = readFileToString("D:\\JAVA_IDEA\\CODE\\TEXT1\\src\\ruangong\\lyp_orig_0.8_add.txt");
            String plagiarizedText2 = readFileToString("D:\\JAVA_IDEA\\CODE\\TEXT1\\src\\ruangong\\lyp_orig_0.8_del.txt");
            String plagiarizedText3 = readFileToString("D:\\JAVA_IDEA\\CODE\\TEXT1\\src\\ruangong\\lyp_orig_0.8_dis_1.txt");
            String plagiarizedText4 = readFileToString("D:\\JAVA_IDEA\\CODE\\TEXT1\\src\\ruangong\\lyp_orig_0.8_dis_10.txt");
            String plagiarizedText5 = readFileToString("D:\\JAVA_IDEA\\CODE\\TEXT1\\src\\ruangong\\lyp_orig_0.8_dis_15.txt");

            // 使用 HanLP 进行中文分词
            List<Term> termList = HanLP.segment(originalText);
            List<String> words = HanLP.extractKeyword(originalText, 1000000);

            // 转换为向量
            RealVector vector = getVector(originalText, words);
            RealVector vector1 = getVector(plagiarizedText1, words);
            RealVector vector2 = getVector(plagiarizedText2, words);
            RealVector vector3 = getVector(plagiarizedText3, words);
            RealVector vector4 = getVector(plagiarizedText4, words);
            RealVector vector5 = getVector(plagiarizedText5, words);

            // 计算余弦相似度
            double cosineSimilarity_1 = getCosineSimilarity(vector, vector1);
            double cosineSimilarity_2 = getCosineSimilarity(vector, vector2);
            double cosineSimilarity_3 = getCosineSimilarity(vector, vector3);
            double cosineSimilarity_4 = getCosineSimilarity(vector, vector4);
            double cosineSimilarity_5 = getCosineSimilarity(vector, vector5);

           //打印答案
            System.out.printf("lyp_orig_0.8_add.txt文本内容与lyp_orig.txt原文本内容相似度为%f\n" , cosineSimilarity_1);
            System.out.printf("lyp_orig_0.8_del.txt文本内容与lyp_orig.txt原文本内容相似度为%f\n" , cosineSimilarity_2);
            System.out.printf("lyp_orig_0.8_dis_1.txt文本内容与lyp_orig.txt原文本内容相似度为%f\n" , cosineSimilarity_3);
            System.out.printf("lyp_orig_0.8_dis_10.txt文本内容与lyp_orig.txt原文本内容相似度为%f\n" , cosineSimilarity_4);
            System.out.printf("lyp_orig_0.8_dis_15.txt文本内容与lyp_orig.txt原文本内容相似度为%f\n" , cosineSimilarity_5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
