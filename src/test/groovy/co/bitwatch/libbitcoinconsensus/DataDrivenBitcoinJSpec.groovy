package co.bitwatch.libbitcoinconsensus

import groovy.util.logging.Slf4j
import org.bitcoinj.core.ScriptException
import org.bitcoinj.core.Transaction
import org.bitcoinj.script.Script
import spock.lang.Ignore

/**
 * Data driven tests for script verification with BitcoinJ
 */
@Ignore('BitcoinJ is currently not able to pass all tests, and ensuring it is not the primary goal of this project.')
@Slf4j
class DataDrivenBitcoinJSpec extends DataDrivenVerificationSpec {

    /**
     * Converts a Set of VerifyFlags to String.
     *
     * @param nativeFlags The script verification flags to convert
     * @return A String representing the flags
     */
    private static String flagsToString(Set<VerifyFlag> nativeFlags) {
        if (nativeFlags.empty) {
            return 'NONE'
        }

        String str = ''
        for (flag in nativeFlags) {
            str += flag.toString() + ','
        }

        return str
    }

    /**
     * Parses a String of script verification flags to a Set of BitcoinJ Script.VerifyFlag.
     *
     * Examples:
     *
     *   "NONE"
     *   "P2SH,NULLDUMMY"
     *
     * Unknown flags are ignored.
     *
     * Note: this is a copy of org.bitcoinj.script.ScriptTest#parseVerifyFlags(String), which is private.
     *
     * @param str The String with flags to parse
     * @return The BitcoinJ script verification flags
     */
    private static Set<Script.VerifyFlag> parseVerifyFlags(String str) {
        Set<Script.VerifyFlag> flags = EnumSet.noneOf(Script.VerifyFlag.class)

        if (!'NONE'.equals(str)) {
            for (String flag : str.split(',')) {
                try {
                    flags.add(Script.VerifyFlag.valueOf(flag))
                } catch (IllegalArgumentException ignored) {
                    log.error 'Cannot handle verify flag: ' + flag
                }
            }
        }

        return flags
    }

    /**
     * Converts a Set of VerifyFlag to a Set of BitcoinJ Script.VerifyFlag.
     *
     * @param nativeFlags The script verification flags to convert
     * @return The BitcoinJ script verification flags
     */
    private static Set<Script.VerifyFlag> convertFlags(Set<VerifyFlag> nativeFlags) {
        String str = flagsToString(nativeFlags)
        Set<Script.VerifyFlag> verifyFlags = parseVerifyFlags(str)

        return verifyFlags
    }

    /**
     * Verifies that this script correctly spends the given scriptPubKey.
     *
     * BitcoinJ is used for the verification.
     *
     * @return True , if the script was executed successfully, and false otherwise
     */
    @Override
    protected Boolean VerifyScript(Script scriptSig, Script scriptPubKey, Set<VerifyFlag> flags) {
        Set<Script.VerifyFlag> verifyFlags = convertFlags(flags)
        Transaction tx = BuildSpendingTransaction(scriptSig, BuildCreditingTransaction(scriptPubKey))
        Boolean isValidScript = true

        try {
            scriptSig.correctlySpends(tx, 0, scriptPubKey, verifyFlags)
        } catch (ScriptException e) {
            log.error e.toString()
            isValidScript = false
        }

        return isValidScript
    }

}
