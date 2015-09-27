package co.bitwatch.libbitcoinconsensus

import co.bitwatch.libbitcoinconsensus.exceptions.DeserializeException
import co.bitwatch.libbitcoinconsensus.exceptions.TxIndexOutOfBoundsException
import spock.lang.Specification

/**
 * Tests for Java wrapper of libbitcoinconsensus
 */
class BitcoinConsensusSpec extends Specification {

    private static BitcoinConsensus lib

    def testBitcoinConsensusVerifyScript_OK() {
        given:
        def scriptPubKey = "76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac".decodeHex()
        def txTo = "01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000".decodeHex()
        def nIn = 0

        when:
        def result = lib.VerifyScript(scriptPubKey, txTo, nIn)

        then:
        result
    }

    def testBitcoinConsensusVerifyScript_InvalidScript() {
        given:
        def scriptPubKey = "76a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac".decodeHex()
        def txTo = "01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000".decodeHex()
        def nIn = 0

        when:
        def result = lib.VerifyScript(scriptPubKey, txTo, nIn)

        then:
        !result
    }

    def testBitcoinConsensusVerifyScript_InvalidTxIndex() {
        given:
        def scriptPubKey = "76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac".decodeHex()
        def txTo = "01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000".decodeHex()
        def nIn = 1 // out of bounds

        when:
        lib.VerifyScript(scriptPubKey, txTo, nIn)

        then:
        thrown(TxIndexOutOfBoundsException)
    }

    def testBitcoinConsensusVerifyScript_InvalidSerialization() {
        given:
        def scriptPubKey = "76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac".decodeHex()
        def txTo = "abcd".decodeHex()  // invalid
        def nIn = 0

        when:
        lib.VerifyScript(scriptPubKey, txTo, nIn)

        then:
        thrown(DeserializeException)
    }

    def setupSpec() {
        lib = new BitcoinConsensus()
    }
}
