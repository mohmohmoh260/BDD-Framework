package builds.utilities;

import java.util.HashMap;
import java.util.Map;

public class Context {

    Map<String, String> map;
    public Context(){
        map = new HashMap<>();
    }

    public void setContext(String key, String value){
        map.put(key, value);
    }

    public String getContext(String key){
        return map.get(key);
    }
}
