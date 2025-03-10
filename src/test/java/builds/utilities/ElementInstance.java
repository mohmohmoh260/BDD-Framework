package builds.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElementInstance {

    public static final ThreadLocal<HashMap<String, HashMap<String, String>>> elements =
            ThreadLocal.withInitial(HashMap::new);

    private static final Map<String, String> keyToFileMap = new HashMap<>(); // Stores key → file mapping

    public void getAllElement() {
        String directoryPath = "src/test/java/workDirectory/pageObject"; // Change this to your directory path
        ObjectMapper objectMapper = new ObjectMapper();

        File folder = new File(directoryPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid directory path!");
            return;
        }

        // Recursively search for JSON files in all subdirectories
        searchJsonFiles(folder, objectMapper);
    }

    protected String getElementValue(String elementName, String platform) {
        HashMap<String, HashMap<String, String>> elementsMap = ElementInstance.elements.get();

        if (elementsMap.containsKey(elementName)) {
            HashMap<String, String> platformData = elementsMap.get(elementName);
            return platformData.getOrDefault(platform, "❌ Platform key not found");
        }
        return "❌ Element key not found";
    }

    private static void searchJsonFiles(File folder, ObjectMapper objectMapper) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                // If it's a directory, recurse into it
                searchJsonFiles(file, objectMapper);
            } else if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    // Read JSON file and convert to HashMap<String, HashMap<String, String>>
                    HashMap<String, HashMap<String, String>> data = objectMapper.readValue(
                            file, new TypeReference<HashMap<String, HashMap<String, String>>>() {});

                    // Check for duplicate keys
                    for (String key : data.keySet()) {
                        if (keyToFileMap.containsKey(key)) {
                            throw new RuntimeException("❌ Duplicate key found: \"" + key +
                                    "\" in files: \n  - " + keyToFileMap.get(key) +
                                    "\n  - " + file.getAbsolutePath());
                        }
                        keyToFileMap.put(key, file.getAbsolutePath()); // Store first occurrence
                    }

                    // Store the data in ThreadLocal elements
                    elements.get().putAll(data);

                    // Print the JSON file name and its content
                    System.out.println("✅ File processed: " + file.getAbsolutePath());
                    System.out.println(data);
                    System.out.println("----------------------");

                } catch (IOException e) {
                    System.err.println("❌ Error reading file: " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
    }
}