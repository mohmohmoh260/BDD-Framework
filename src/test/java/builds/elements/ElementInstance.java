package builds.elements;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            return platformData.getOrDefault(platform, "❌ Platform name not found");
        }
        return "❌ Element name not found";
    }

    private static void searchJsonFiles(File folder, ObjectMapper objectMapper) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                searchJsonFiles(file, objectMapper);
            } else if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    // Read JSON as a raw string to check for duplicate top-level keys
                    String fileContent = new String(Files.readAllBytes(file.toPath()));

                    // Extract top-level keys only (not nested ones)
                    Pattern pattern = Pattern.compile("\"(\\w+)\"\\s*:\\s*\\{");
                    Matcher matcher = pattern.matcher(fileContent);

                    HashSet<String> localKeys = new HashSet<>();
                    StringBuilder duplicateKeysInSameFile = new StringBuilder();

                    while (matcher.find()) {
                        String key = matcher.group(1); // Extract only top-level keys
                        if (!localKeys.add(key)) {
                            duplicateKeysInSameFile.append("\nDuplicate name: ").append(key);
                        }
                    }

                    // If duplicates are found in the same file, throw an error
                    if (duplicateKeysInSameFile.length() > 0) {
                        throw new RuntimeException("❌ Duplicate name found in the same file: " + file.getAbsolutePath() +
                                duplicateKeysInSameFile);
                    }

                    // Parse JSON and check for duplicates across multiple files
                    HashMap<String, HashMap<String, String>> data = objectMapper.readValue(
                            file, new TypeReference<HashMap<String, HashMap<String, String>>>() {});

                    for (String key : data.keySet()) {
                        if (keyToFileMap.containsKey(key)) {
                            throw new RuntimeException("❌ Duplicate name found across multiple files: \"" + key +
                                    "\"\n  - " + keyToFileMap.get(key) +
                                    "\n  - " + file.getAbsolutePath());
                        }
                        keyToFileMap.put(key, file.getAbsolutePath());
                    }

                    elements.get().putAll(data);

                    System.out.println("✅ File processed: " + file.getAbsolutePath());

                } catch (IOException e) {
                    System.err.println("❌ Error reading file: " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
    }
}