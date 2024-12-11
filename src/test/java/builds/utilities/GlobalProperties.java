package builds.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GlobalProperties {
    private static Properties globalVariable = new Properties();
    private static Properties extent = new Properties();

    private static String OS;
    public static void loadGlobalVariablesProperties() throws IOException {
        OS = System.getProperty("os.name");

        if(!OS.contains("Mac OS")){
            String filePath = System.getProperty("user.dir")+"\\Global Variable.properties";
            FileInputStream fileInputStream = new FileInputStream(filePath);
            globalVariable.load(fileInputStream);
        }else{
            String filePath = System.getProperty("user.dir")+"/Global Variable.properties";
            FileInputStream fileInputStream = new FileInputStream(filePath);
            globalVariable.load(fileInputStream);
        }
    }

    public static Properties getGlobalVariablesProperties(){
        return globalVariable;
    }

    public static void setGlobalVariableProperties(String key, String value){
        globalVariable.setProperty(key, value);
    }
}
