package builds.elements;

import builds.utilities.TestNGXmlParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ElementInstance extends TestNGXmlParser {

    private static final ThreadLocal<HashMap<String, HashMap<String, String>>> elements = ThreadLocal.withInitial(HashMap::new);
    private static final ThreadLocal<Map<String, String>> keyToFileMap = ThreadLocal.withInitial(HashMap::new);

    public ElementInstance() {
        loadElementsForThread();
    }

    private void loadElementsForThread() {
        // Prevent multiple loads for the same thread
        if (elements.get().isEmpty()) {
            synchronized (ElementInstance.class) { // 🔒 Ensure no duplicate processing
                if (elements.get().isEmpty()) {
                    getAllElement();
                }
            }
        }
    }

    private void getAllElement() {
        String directoryPath = "src/test/java/workDirectory/pageObject";
        ObjectMapper objectMapper = new ObjectMapper();
        File folder = new File(directoryPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("❌ Invalid directory path: " + directoryPath);
            return;
        }

        searchJsonFiles(folder, objectMapper);
    }

    protected String getElementValue(String elementName, String platform) {
        if (elements.get().isEmpty()) {
            throw new RuntimeException("❌ Elements not loaded for this thread!");
        }

        HashMap<String, HashMap<String, String>> elementsMap = elements.get();

        if (elementsMap.containsKey(elementName)) {
            HashMap<String, String> platformData = elementsMap.get(elementName);
            String locator = platformData.get(platform);

            if (locator == null || locator.isEmpty()) {
                throw new RuntimeException("❌ Element found, but no locator for platform: " + platform);
            }
            return locator;
        }
        throw new RuntimeException("❌ Element name not found: " + elementName);
    }

    private void searchJsonFiles(File folder, ObjectMapper objectMapper) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                searchJsonFiles(file, objectMapper);
            } else if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    String fileContent = new String(Files.readAllBytes(file.toPath()));
                    Pattern pattern = Pattern.compile("\"(\\w+)\"\\s*:\\s*\\{");
                    Matcher matcher = pattern.matcher(fileContent);

                    HashSet<String> localKeys = new HashSet<>();
                    StringBuilder duplicateKeysInSameFile = new StringBuilder();

                    while (matcher.find()) {
                        String key = matcher.group(1);
                        if (!localKeys.add(key)) {
                            duplicateKeysInSameFile.append("\nDuplicate name: ").append(key);
                        }
                    }

                    if (!duplicateKeysInSameFile.isEmpty()) {
                        throw new RuntimeException("❌ Duplicate name found in file: " + file.getAbsolutePath() + duplicateKeysInSameFile);
                    }

                    HashMap<String, HashMap<String, String>> data = objectMapper.readValue(
                            file, new TypeReference<HashMap<String, HashMap<String, String>>>() {});

                    for (String key : data.keySet()) {
                        if (keyToFileMap.get().containsKey(key)) {
                            throw new RuntimeException("❌ Duplicate name found across multiple files: \"" + key +
                                    "\"\n  - " + keyToFileMap.get().get(key) +
                                    "\n  - " + file.getAbsolutePath());
                        }
                        keyToFileMap.get().put(key, file.getAbsolutePath());
                    }

                    elements.get().putAll(data);

                } catch (IOException e) {
                    System.err.println("❌ Error reading file: " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
    }
}