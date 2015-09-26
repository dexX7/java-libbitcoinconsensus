package co.bitwatch.libbitcoinconsensus;

import java.util.EnumSet;
import java.util.Set;

/**
 * Script verification flags
 */
public enum VerifyFlag {

    /**
     * No special handling.
     */
    NONE(0),

    /**
     * Evaluate P2SH subscripts.
     *
     * (softfork safe, BIP16)
     */
    P2SH(1 << 0),

    /**
     * Require defined signature hash types and public keys.
     *
     * Passing a non-strict-DER signature or one with undefined hashtype to a checksig
     * operation causes script failure.
     *
     * Evaluating a pubkey that is not (0x04 + 64 bytes) or (0x02 or 0x03 + 32 bytes)
     * by checksig causes script failure.
     *
     * (softfork safe, but not used or intended as a consensus rule)
     */
    STRICTENC(1 << 1),

    /**
     * Enforce strict DER encoded ECDSA signatures.
     *
     * Passing a non-strict-DER signature to a checksig operation causes script
     * failure.
     *
     * (softfork safe, BIP62 rule 1)
     */
    DERSIG(1 << 2),

    /**
     * Require S values are at most the curve order divided by 2.
     *
     * Passing a non-strict-DER signature or one with S greater than order/2 to a
     * checksig operation causes script failure.
     *
     * (softfork safe, BIP62 rule 5)
     */
    LOW_S(1 << 3),

    /**
     * Verify dummy stack item consumed by CHECKMULTISIG is of zero-length.
     *
     * (softfork safe, BIP62 rule 7)
     */
    NULLDUMMY(1 << 4),

    /**
     * Using a non-push operator in the scriptSig causes script failure.
     *
     * (softfork safe, BIP62 rule 2)
     */
    SIGPUSHONLY(1 << 5),

    /**
     * Require minimal encodings for all push operations.
     *
     * Require minimal encodings for all push operations (OP_0... OP_16, OP_1NEGATE
     * where possible, direct pushes up to 75 bytes, OP_PUSHDATA up to 255 bytes,
     * OP_PUSHDATA2 for anything larger). Evaluating any other push causes the script
     * to fail (BIP62 rule 3).
     *
     * In addition, whenever a stack element is interpreted as a number, it must be of
     * minimal length (BIP62 rule 4).
     *
     * (softfork safe)
     */
    MINIMALDATA(1 << 6),

    /**
     * Discourage use of NOPs reserved for upgrades (NOP1-10).
     *
     * Provided so that nodes can avoid accepting or mining transactions containing
     * executed NOP's whose meaning may change after a soft-fork, thus rendering the
     * script invalid; with this flag set executing discouraged NOPs fails the script.
     * This verification flag will never be a mandatory flag applied to scripts in a
     * block. NOPs that are not executed, e.g. within an unexecuted IF ENDIF block,
     * are *not* rejected.
     */
    DISCOURAGE_UPGRADABLE_NOPS(1 << 7),

    /**
     * Require that only a single stack element remains after evaluation.
     *
     * This changes the success criterion from "At least one stack element must
     * remain,and when interpreted as a boolean, it must be true" to "Exactly one
     * stack element must remain, and when interpreted as a boolean, it must be
     * true".
     *
     * (softfork safe, BIP62 rule 6)
     *
     * Note: CLEANSTACK should never be used without P2SH.
     */
    CLEANSTACK(1 << 8),

    /**
     * Verify CHECKLOCKTIMEVERIFY.
     *
     * See BIP65 for details.
     */
    CHECKLOCKTIMEVERIFY(1 << 9);

    /**
     * Mandatory script verification flags
     *
     * Mandatory script verification flags that all new blocks must comply with for
     * them to be valid. (but old blocks may not comply with) Currently just P2SH,
     * but in the future other flags may be added, such as a soft-fork to enforce
     * strict DER encoding.
     */
    public static final EnumSet<VerifyFlag> MANDATORY_VERIFY_FLAGS = EnumSet.of(P2SH);

    /**
     * Standard script verification flags
     *
     * Standard script verification flags that standard transactions will comply
     * with. However scripts violating these flags may still be present in valid
     * blocks and we must accept those blocks.
     */
    public static final EnumSet<VerifyFlag> STANDARD_VERIFY_FLAGS = EnumSet.of(P2SH,
            DERSIG,
            STRICTENC,
            MINIMALDATA,
            NULLDUMMY,
            DISCOURAGE_UPGRADABLE_NOPS,
            CLEANSTACK,
            CHECKLOCKTIMEVERIFY);

    /** Script verification flag value */
    private int value;

    /**
     * Creates a new VerifyFlag.
     *
     * Note: only used internally.
     *
     * @param value The script verification flag value
     */
    private VerifyFlag(int value) {
        this.value = value;
    }

    /**
     * Returns the value of the script verification flag.
     *
     * @return The value of the script verification flag
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the value of the script verification flags.
     *
     * @param flags The script verification flags
     * @return The value of the script verification flags
     */
    public static int getValueOf(Set<VerifyFlag> flags) {
        int mergedValue = 0;
        for (VerifyFlag flag : flags) {
            mergedValue |= flag.getValue();
        }
        return mergedValue;
    }
}
