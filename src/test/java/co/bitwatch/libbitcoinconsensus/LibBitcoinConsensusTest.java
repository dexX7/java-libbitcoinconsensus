package co.bitwatch.libbitcoinconsensus;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import org.junit.BeforeClass;
import org.junit.Test;

import static co.bitwatch.libbitcoinconsensus.LibBitcoinConsensus.*;
import static co.bitwatch.libbitcoinconsensus.util.StringUtil.hexToBinary;
import static org.junit.Assert.assertEquals;

/**
 * Tests for JNA binding of libbitcoinconsensus
 */
public class LibBitcoinConsensusTest {

    private static LibBitcoinConsensus nativeLib;

    @BeforeClass
    public static void setupNativeLibrary() {
        nativeLib = (LibBitcoinConsensus) Native.loadLibrary("bitcoinconsensus", LibBitcoinConsensus.class);
    }

    @Test
    public void testLibBitcoinConsensusVerifyScript_OK() throws Exception {
        byte[] txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000");
        byte[] scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac");

        IntByReference err = new IntByReference();
        int result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length, 0, bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE, err);

        assertEquals(1, result);
        assertEquals(bitcoinconsensus_ERR_OK, err.getValue());
    }

    @Test
    public void testLibBitcoinConsensusVerifyScript_InvalidScript() throws Exception {
        byte[] scriptPubKey = hexToBinary("76a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac");
        byte[] txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000");

        IntByReference err = new IntByReference();
        int result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length, 0, bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE, err);

        assertEquals(0, result);
        assertEquals(bitcoinconsensus_ERR_OK, err.getValue());
    }

    @Test
    public void testLibBitcoinConsensusVerifyScript_InvalidTxIndex() throws Exception {
        byte[] txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000");
        byte[] scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac");

        IntByReference err = new IntByReference();
        int result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length, 1, bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE, err);

        assertEquals(0, result);
        assertEquals(bitcoinconsensus_ERR_TX_INDEX, err.getValue());
    }

    @Test
    public void testLibBitcoinConsensusVerifyScript_InvalidSerialization() throws Exception {
        byte[] txTo = hexToBinary("abcd");
        byte[] scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac");

        IntByReference err = new IntByReference();
        int result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length, 0, bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE, err);

        assertEquals(0, result);
        assertEquals(bitcoinconsensus_ERR_DESERIALIZE, err.getValue());
    }

    @Test
    public void testLibBitcoinConsensusVerifyScript_InvalidLength() throws Exception {
        byte[] txTo = hexToBinary("01000000015884e5db9de218238671572340b207ee85b628074e7e467096c267266baf77a4000000006a4730440220340f35055aceb14250e4954b23743332f671eb803263f363d1d7272f1d487209022037a0eaf7cb73897ba9069fc538e7275c5ae188e934ae47ca4a70453b64fc836401210234257444bd3aead2b851bda4288d60abe34095a2a8d49aff1d4d19773d22b32cffffffff01a0860100000000001976a9147821c0a3768aa9d1a37e16cf76002aef5373f1a888ac00000000");
        byte[] scriptPubKey = hexToBinary("76a9144621d47f08fcb1e6be0b91144202de7a186deade88ac");

        IntByReference err = new IntByReference();
        int result = nativeLib.bitcoinconsensus_verify_script(scriptPubKey, scriptPubKey.length, txTo, txTo.length+5, 0, bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE, err);

        assertEquals(0, result);
        assertEquals(bitcoinconsensus_ERR_TX_SIZE_MISMATCH, err.getValue());
    }

    @Test
    public void testLibBitcoinConsensusVersion() throws Exception {
        assertEquals(0, nativeLib.bitcoinconsensus_version());
    }

}
