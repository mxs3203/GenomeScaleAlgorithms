package util;

import java.io.*;
import java.util.Map;

public class FileUtil {
    public static void saveObject(Object o, String directoryName, String filename) {
        String destination = directoryName + filename;
        File directory = new File(directoryName);
        if (!directory.exists()){
            directory.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(destination);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object loadObject(String destination) {
        Object tmp = null;
        try {
            FileInputStream fis = new FileInputStream(destination);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis));
            try {
                tmp = ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public static boolean exist(String directoryName) {
        File directory = new File(directoryName);
        return directory.exists();
    }
}
