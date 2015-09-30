package co.bitwatch.libbitcoinconsensus.datadriven

import co.bitwatch.libbitcoinconsensus.VerifyFlag
import groovy.util.logging.Slf4j
import org.bitcoinj.core.UnsafeByteArrayOutputStream
import org.bitcoinj.core.Utils
import org.bitcoinj.script.Script
import org.bitcoinj.script.ScriptOpCodes

import java.nio.charset.Charset

import static org.bitcoinj.core.Utils.HEX
import static org.bitcoinj.script.ScriptOpCodes.OP_INVALIDOPCODE

/**
 * A utility class to parse the script, and script verification flags, of test entries for data driven script tests
 */
@Slf4j
public class ScriptParser {

    /**
     * Parses a String of script verification flags to a Set of VerifyFlag.
     *
     * Examples:
     *
     *   "NONE"
     *   "P2SH,NULLDUMMY"
     *
     * Unknown flags are ignored.
     *
     * @param string The script verification flags as String
     * @return A Set of VerifyFlag
     */
    public static Set<VerifyFlag> ParseScriptFlags(String string) {
        Set<VerifyFlag> flags = EnumSet.noneOf(VerifyFlag.class)

        for (String flag : string.split(',')) {
            if (flag.empty) {
                flag = 'NONE'
            }

            try {
                flags.add(VerifyFlag.valueOf(flag))
            } catch (IllegalArgumentException ignored) {
                log.error 'Cannot handle verify flag: ' + flag
            }
        }

        return flags
    }

    /**
     * Parses a String of script ASM codes to Script.
     *
     * Note: this is a copy of org.bitcoinj.script.ScriptTest#parseScriptString(String), which is private.
     *
     * @param string The script ASM codes
     * @return A Script
     */
    public static Script ParseScript(String string) {
        String[] words = string.split('[ \\t\\n]')

        UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream()

        for (String w : words) {
            if (w.empty) {
                continue
            }
            if (w.matches('^-?[0-9]*$')) {
                // Number
                long val = Long.parseLong(w)
                if (val >= -1 && val <= 16) {
                    out.write(Script.encodeToOpN((int) val))
                } else {
                    Script.writeBytes(out, Utils.reverseBytes(Utils.encodeMPI(BigInteger.valueOf(val), false)))
                }
            } else if (w.matches('^0x[0-9a-fA-F]*$')) {
                // Raw hex data, inserted NOT pushed onto stack:
                out.write(HEX.decode(w.substring(2).toLowerCase()))
            } else if (w.length() >= 2 && w.startsWith('\'') && w.endsWith('\'')) {
                // Single-quoted string, pushed as data. NOTE: this is poor-man's
                // parsing, spaces/tabs/newlines in single-quoted strings won't work.
                Script.writeBytes(out, w.substring(1, w.length() - 1).getBytes(Charset.forName('UTF-8')))
            } else if (ScriptOpCodes.getOpCode(w) != OP_INVALIDOPCODE) {
                // opcode, e.g. OP_ADD or OP_1:
                out.write(ScriptOpCodes.getOpCode(w))
            } else if (w.startsWith('OP_') && ScriptOpCodes.getOpCode(w.substring(3)) != OP_INVALIDOPCODE) {
                // opcode, e.g. OP_ADD or OP_1:
                out.write(ScriptOpCodes.getOpCode(w.substring(3)))
            } else {
                throw new RuntimeException('Invalid Data')
            }
        }

        return new Script(out.toByteArray())
    }

}
