package co.bitwatch.libbitcoinconsensus;

import co.bitwatch.libbitcoinconsensus.exceptions.DeserializeException;
import co.bitwatch.libbitcoinconsensus.exceptions.TxIndexOutOfBoundsException;
import co.bitwatch.libbitcoinconsensus.exceptions.TxSizeMismatchException;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

import static co.bitwatch.libbitcoinconsensus.LibBitcoinConsensus.bitcoinconsensus_ERR_DESERIALIZE;
import static co.bitwatch.libbitcoinconsensus.LibBitcoinConsensus.bitcoinconsensus_ERR_TX_INDEX;
import static co.bitwatch.libbitcoinconsensus.LibBitcoinConsensus.bitcoinconsensus_ERR_TX_SIZE_MISMATCH;
import static co.bitwatch.libbitcoinconsensus.LibBitcoinConsensus.bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE;

/**
 * Java wrapper for libbitcoinconsensus
 */
public class BitcoinConsensus {

    /** Native libbitcoinconsensus library */
    private LibBitcoinConsensus nativeLib;

    /**
     * Creates a new instance of the library.
     */
    public BitcoinConsensus() {
        this("bitcoinconsensus");
    }

    /**
     * Creates a new instance of the library.
     *
     * @param libraryPath The path or name of the library
     */
    public BitcoinConsensus(String libraryPath) {
        this.nativeLib = (LibBitcoinConsensus) Native.loadLibrary(libraryPath, LibBitcoinConsensus.class);
    }

    /**
     * Evaluates a Bitcoin script.
     *
     * @param scriptPubKey The previous output script to be evaluated
     * @param txTo         The transaction with the input that is spending the previous output
     * @param nIn          The index of the input in txTo that spends the scriptPubKey
     * @return True, if the script evaluated successfully, and false otherwise
     * @throws DeserializeException        Serialization failure
     * @throws TxSizeMismatchException     Mismatch of txToLen and the size of txTo
     * @throws TxIndexOutOfBoundsException Invalid transaction input index for txTo
     */
    public Boolean VerifyScript(byte[] scriptPubKey, byte[] txTo, int nIn)
            throws DeserializeException, TxSizeMismatchException, TxIndexOutOfBoundsException {
        return VerifyScript(scriptPubKey, txTo, nIn, bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE);
    }

    /**
     * Evaluates a Bitcoin script.
     *
     * @param scriptPubKey The previous output script to be evaluated
     * @param txTo         The transaction with the input that is spending the previous output
     * @param nIn          The index of the input in txTo that spends the scriptPubKey
     * @param flags        The script validation flags
     * @return True, if the script evaluated successfully, and false otherwise
     * @throws DeserializeException        Serialization failure
     * @throws TxSizeMismatchException     Mismatch of txToLen and the size of txTo
     * @throws TxIndexOutOfBoundsException Invalid transaction input index for txTo
     */
    public Boolean VerifyScript(byte[] scriptPubKey, byte[] txTo, int nIn, int flags)
            throws DeserializeException, TxSizeMismatchException, TxIndexOutOfBoundsException {
        IntByReference err = new IntByReference();
        int result = nativeLib.bitcoinconsensus_verify_script(
                scriptPubKey, scriptPubKey.length, txTo, txTo.length, nIn, flags, err);

        switch (err.getValue()) {
            case bitcoinconsensus_ERR_TX_INDEX:
                throw new TxIndexOutOfBoundsException("invalid index for txTo");
            case bitcoinconsensus_ERR_TX_SIZE_MISMATCH:
                throw new TxSizeMismatchException("txToLen did not match with the size of txTo");
            case bitcoinconsensus_ERR_DESERIALIZE:
                throw new DeserializeException("failed to deserialize txTo");
        }

        return (result == 1);
    }

}
