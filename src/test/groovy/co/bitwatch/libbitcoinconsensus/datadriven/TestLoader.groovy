package co.bitwatch.libbitcoinconsensus.datadriven

import co.bitwatch.libbitcoinconsensus.VerifyFlag
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.bitcoinj.script.Script

import static ScriptParser.ParseScript
import static ScriptParser.ParseScriptFlags

/**
 * The test loader can be used to parse JSON encoded script verification test cases.
 *
 * The expected format is as follows:
 *
 * [
 *   [scriptSig, scriptPubKey, flags, comments],
 *   [scriptSig, scriptPubKey, flags, comments],
 *   ...
 * ]
 */
@Slf4j
public class TestLoader {

    /**
     * Creates a test entry.
     *
     * @param item The test entry extracted from the JSON object
     * @return A test entry
     */
     private static TestEntry itemToEntry(List<String> item) {
         Script scriptSig = ParseScript(item.get(0))
         Script scriptPubKey = ParseScript(item.get(1))
         Set<VerifyFlag> flags = ParseScriptFlags(item.get(2))
         String comments = item.size() > 3 ? item.get(3) : ""

         return new TestEntry(scriptSig, scriptPubKey, flags, comments)
    }

    /**
     * Loads test entries for data driven script tests.
     *
     * Invalid test entries are ignored.
     *
     * @param path The path to the JSON file with test entries
     * @return A list of test entries
     */
    public static List<TestEntry> LoadTestScripts(String path) {
        String jsonText = new File(path).getText()
        Object jsonBlob = new JsonSlurper().parseText(jsonText)

        List<List<String>> json = (jsonBlob as ArrayList<ArrayList<String>>)
        List<TestEntry> entries = []

        for (List<String> item in json) {
            if (item.size() < 3) {
                log.debug 'Skipping test entry with too few items: ' + item.toString()
                continue
            }

            try {
                TestEntry entry = itemToEntry(item)
                entries.add(entry)
            } catch (Exception e) {
                log.error 'Failed to parse test entry: ' + item.toString() + '\n' + e.toString()
            }
        }

        return entries
    }

}
