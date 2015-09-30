package co.bitwatch.libbitcoinconsensus

import groovy.util.logging.Slf4j
import org.bitcoinj.core.Coin
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Transaction
import org.bitcoinj.params.UnitTestParams
import org.bitcoinj.script.Script
import org.bitcoinj.script.ScriptBuilder
import spock.lang.Specification
import spock.lang.Unroll

import static co.bitwatch.libbitcoinconsensus.datadriven.TestLoader.LoadTestScripts

/**
 * Data driven tests for script verification with libbitcoinconsensus
 */
@Slf4j
class DataDrivenVerificationSpec extends Specification {

    /**
     * The parameters used to construct a Transaction
     */
    protected static final NetworkParameters params = UnitTestParams.get()
    /**
     * The instance of the libbitcoinconsensus library
     */
    protected static final lib = new BitcoinConsensus()

    /**
     * Creates the transaction used to fund the spending transaction.
     *
     * @param scriptPubKey The scriptPubKey to test
     * @return The crediting transaction
     */
    protected static Transaction BuildCreditingTransaction(Script scriptPubKey) {
        Transaction txCredit = new Transaction(params)

        Script script = new ScriptBuilder()
                .smallNum(0)
                .smallNum(0)
                .build()

        txCredit.addInput(Sha256Hash.wrap('0000000000000000000000000000000000000000000000000000000000000000'), -1, script)
        txCredit.addOutput(Coin.ZERO, scriptPubKey)

        return txCredit
    }

    /**
     * Creates the transaction with the output being spent.
     *
     * @param scriptSig The scriptSig to test
     * @param txCredit  The crediting transaction
     * @return The spending transaction
     */
    protected static Transaction BuildSpendingTransaction(Script scriptSig, Transaction txCredit) {
        Transaction txSpend = new Transaction(params)

        txSpend.addInput(txCredit.getHash(), 0, scriptSig)
        txSpend.addOutput(Coin.ZERO, new Script())

        return txSpend
    }

    /**
     * Verifies that this script correctly spends the given scriptPubKey.
     *
     * libbitcoinconsensus is used for the verification.
     *
     * @return True, if the script was executed successfully, and false otherwise
     */
    protected Boolean VerifyScript(Script scriptSig, Script scriptPubKey, Set<VerifyFlag> flags) {
        Transaction tx = BuildSpendingTransaction(scriptSig, BuildCreditingTransaction(scriptPubKey))

        byte[] scriptPubKeyBA = scriptPubKey.getProgram()
        byte[] txBA = tx.bitcoinSerialize()

        return lib.VerifyScript(scriptPubKeyBA, txBA, 0, flags)
    }

    /**
     * Tests the execution of valid scripts.
     *
     * To pass, the script execution must evaluate to true.
     */
    @Unroll
    def "Valid script [#iterationCount] #comments"(def scriptSig, def scriptPubKey, def flags, def comments) {
        log.debug 'scriptSig: ' + scriptSig.toString()
        log.debug 'scriptPubKey: ' + scriptPubKey.toString()
        log.debug 'flags: ' + flags.toString()
        log.debug 'comments: ' + comments

        expect:
        VerifyScript(scriptSig, scriptPubKey, flags)

        where:
        [scriptSig, scriptPubKey, flags, comments] << LoadTestScripts('src/test/resources/script_valid.json')
    }

    /**
     * Tests the execution of invalid scripts.
     *
     * To pass, the script execution must evaluate to false.
     */
    @Unroll
    def "Invalid script [#iterationCount] #comments"(def scriptSig, def scriptPubKey, def flags, def comments) {
        log.debug 'scriptSig: ' + scriptSig.toString()
        log.debug 'scriptPubKey: ' + scriptPubKey.toString()
        log.debug 'flags: ' + flags.toString()
        log.debug 'comments: ' + comments

        expect:
        !VerifyScript(scriptSig, scriptPubKey, flags)

        where:
        [scriptSig, scriptPubKey, flags, comments] << LoadTestScripts('src/test/resources/script_invalid.json')
    }

}
