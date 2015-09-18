package co.bitwatch.libbitcoinconsensus

import com.sun.jna.Native
import com.sun.jna.ptr.IntByReference
import spock.lang.Specification

import static co.bitwatch.libbitcoinconsensus.LibBitcoinConsensus.*
import static co.bitwatch.libbitcoinconsensus.util.StringUtil.hexToBinary

/**
 * Tests for JNA binding of libbitcoinconsensus
 */
class LibBitcoinConsensusSpec extends Specification {

    private static LibBitcoinConsensus nativeLib

    def testLibBitcoinConsensusVerifyScript_OK() {
        given:
        def scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac")
        def txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000")
        def nIn = 0
        def flags = bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE

        when:
        def err = new IntByReference()
        def result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length, nIn, flags, err)

        then:
        result == 1
        err.getValue() == bitcoinconsensus_ERR_OK
    }

    def testLibBitcoinConsensusVerifyScript_InvalidScript() {
        given:
        def txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000")
        def scriptPubKey = hexToBinary("76a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac")
        def nIn = 0
        def flags = bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE

        when:
        def err = new IntByReference()
        def result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length, nIn, flags, err)

        then:
        result == 0
        err.getValue() == bitcoinconsensus_ERR_OK
    }

    def testLibBitcoinConsensusVerifyScript_InvalidTxIndex() {
        given:
        def scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac")
        def txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000")
        def nIn = 1 // out of bounds
        def flags = bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE

        when:
        def err = new IntByReference()
        def result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length, nIn, flags, err)

        then:
        result == 0
        err.getValue() == bitcoinconsensus_ERR_TX_INDEX
    }

    def testLibBitcoinConsensusVerifyScript_InvalidSerialization() {
        given:
        def scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac")
        def txTo = hexToBinary("abcd") // invalid
        def nIn = 0
        def flags = bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE

        when:
        def err = new IntByReference()
        def result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length, nIn, flags, err)

        then:
        result == 0
        err.getValue() == bitcoinconsensus_ERR_DESERIALIZE
    }

    def testLibBitcoinConsensusVerifyScript_InvalidLength() {
        given:
        def scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac")
        def txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000")
        def txToLen = txTo.length+ 5 // mismatch
        def nIn = 0
        def flags = bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE

        when:
        def err = new IntByReference()
        def result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txToLen, nIn, flags, err)

        then:
        result == 0
        err.getValue() == bitcoinconsensus_ERR_TX_SIZE_MISMATCH
    }

    def testLibBitcoinConsensusVersion() {
        when:
        def apiVersion = nativeLib.bitcoinconsensus_version()

        then:
        apiVersion == 0
    }

    def setupSpec() {
        nativeLib = (LibBitcoinConsensus) Native.loadLibrary("bitcoinconsensus", LibBitcoinConsensus.class)
    }
}
