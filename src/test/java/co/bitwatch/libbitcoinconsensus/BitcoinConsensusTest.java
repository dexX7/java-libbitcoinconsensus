package co.bitwatch.libbitcoinconsensus;

import co.bitwatch.libbitcoinconsensus.exceptions.DeserializeException;
import co.bitwatch.libbitcoinconsensus.exceptions.TxIndexOutOfBoundsException;
import org.junit.BeforeClass;
import org.junit.Test;

import static co.bitwatch.libbitcoinconsensus.util.StringUtil.hexToBinary;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for Java wrapper of libbitcoinconsensus
 */
public class BitcoinConsensusTest {

    private static BitcoinConsensus lib;

    @BeforeClass
    public static void setupLibrary() {
        lib = new BitcoinConsensus();
    }

    @Test
    public void testBitcoinConsensusVerifyScript_OK() throws Exception {
        byte[] txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000");
        byte[] scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac");

        Boolean result = lib.VerifyScript(scriptPubKey, txTo, 0);

        assertTrue(result);
    }

    @Test
    public void testBitcoinConsensusVerifyScript_InvalidScript() throws Exception {
        byte[] txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000");
        byte[] scriptPubKey = hexToBinary("76a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac");

        Boolean result = lib.VerifyScript(scriptPubKey, txTo, 0);

        assertFalse(result);
    }

    @Test(expected=TxIndexOutOfBoundsException.class)
    public void testBitcoinConsensusVerifyScript_InvalidTxIndex() throws Exception {
        byte[] txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000");
        byte[] scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac");

        lib.VerifyScript(scriptPubKey, txTo, 1); // throws
    }

    @Test(expected=DeserializeException.class)
    public void testBitcoinConsensusVerifyScript_InvalidSerialization() throws Exception {
        byte[] txTo = hexToBinary("abcd");
        byte[] scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac");

        lib.VerifyScript(scriptPubKey, txTo, 0); // throws
    }

}
