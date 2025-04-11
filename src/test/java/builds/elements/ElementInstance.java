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

/**
 * ElementInstance provides centralized access to JSON-defined UI elements.
 * It loads JSON files from the pageObject directory and stores them in thread-safe maps.
 *
 * Each JSON file is expected to define UI element locators per platform (e.g., Android, iOS, Web).
 * Example structure:
 * {
 *     "loginButton": {
 *         "Android": "id:loginBtn",
 *         "iOS": "accessibility:login"
 *     }
 * }
 */
public abstract class ElementInstance extends TestNGXmlParser {

    // Holds all element data, structured as: ElementName -> Platform -> Locator
    private static final ThreadLocal<HashMap<String, HashMap<String, String>>> elements =
            ThreadLocal.withInitial(HashMap::new);

    // Tracks where each key was loaded from, to detect duplicates across files
    private static final ThreadLocal<Map<String, String>> keyToFileMap =
            ThreadLocal.withInitial(HashMap::new);

    /**
     * Constructor automatically triggers loading of element data for the thread.
     */
    public ElementInstance() {
        loadElementsForThread();
    }

    /**
     * Ensures elements are loaded only once per thread.
     */
    private void loadElementsForThread() {
        if (elements.get().isEmpty()) {
            synchronized (ElementInstance.class) {
                if (elements.get().isEmpty()) {
                    getAllElement();
                }
            }
        }
    }

    /**
     * Entry point to scan and load JSON element files from the directory.
     */
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

    /**
     * Fetches the locator for a given element name and platform.
     *
     * @param elementName the element key name
     * @param platform the target platform (e.g., Android, iOS, Web)
     * @return locator string if found
     * @throws RuntimeException if element or platform locator is missing
     */
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

    /**
     * Recursively scans folders for JSON files, validates structure, and loads into memory.
     *
     * @param folder current directory to scan
     * @param objectMapper Jackson object mapper
     */
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

                    // Detect duplicate element keys in the same file
                    while (matcher.find()) {
                        String key = matcher.group(1);
                        if (!localKeys.add(key)) {
                            duplicateKeysInSameFile.append("\nDuplicate name: ").append(key);
                        }
                    }

                    if (!duplicateKeysInSameFile.isEmpty()) {
                        throw new RuntimeException("❌ Duplicate name found in file: " +
                                file.getAbsolutePath() + duplicateKeysInSameFile);
                    }

                    // Parse the JSON into the main element map
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