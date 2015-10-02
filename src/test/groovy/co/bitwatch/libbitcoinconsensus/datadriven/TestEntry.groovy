package co.bitwatch.libbitcoinconsensus.datadriven

import co.bitwatch.libbitcoinconsensus.VerifyFlag
import groovy.transform.CompileStatic
import org.bitcoinj.script.Script

/**
 * A test entry for data driven script tests
 */
@CompileStatic
public class TestEntry implements Iterable<Object> {

    /**
     * The script used for the input of the spending transaction
     */
    final Script scriptSig
    /**
     * The script used for the output of the crediting transaction
     */
    final Script scriptPubKey
    /**
     * The flags used for the script verification
     */
    final Set<VerifyFlag> flags
    /**
     * Optional comments or notes
     */
    final String comments

    /**
     * Constructs a new test entry.
     *
     * @param scriptSig    The scriptSig for the spending transaction
     * @param scriptPubKey The scriptPubKey for the crediting transaction
     * @param flags        The script verification flags
     * @param comments     Optional comments or notes
     */
    public TestEntry(Script scriptSig, Script scriptPubKey, Set<VerifyFlag> flags, String comments = "") {
        this.scriptSig = scriptSig
        this.scriptPubKey = scriptPubKey
        this.flags = flags
        this.comments = comments
    }

    /**
     * Creates an iterator for the test entry.
     *
     * @return An iterator, which may be used for data driven tests
     */
    @Override
    public Iterator<Object> iterator() {
        return [scriptSig, scriptPubKey, flags, comments].iterator()
    }

}
