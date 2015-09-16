package co.bitwatch.libbitcoinconsensus;

import com.sun.jna.Library;
import com.sun.jna.ptr.IntByReference;

/**
 * JNA binding for libbitcoinconsensus
 *
 * @link https://github.com/bitcoin/bitcoin/blob/master/doc/shared-libraries.md
 */
public interface LibBitcoinConsensus extends Library {

    /** No special handling */
    int bitcoinconsensus_SCRIPT_FLAGS_VERIFY_NONE = 0;
    /** Evaluate P2SH (BIP16) subscripts */
    int bitcoinconsensus_SCRIPT_FLAGS_VERIFY_P2SH = 1;
    /** Enforce strict DER (BIP66) compliance */
    int bitcoinconsensus_SCRIPT_FLAGS_VERIFY_DERSIG = 4;

    /** No errors with input parameters */
    int bitcoinconsensus_ERR_OK = 0;
    /** An invalid index for txTo */
    int bitcoinconsensus_ERR_TX_INDEX = 1;
    /** txToLen did not match with the size of txTo */
    int bitcoinconsensus_ERR_TX_SIZE_MISMATCH = 2;
    /** An error deserializing txTo */
    int bitcoinconsensus_ERR_DESERIALIZE = 3;

    /**
     * Evaluates a Bitcoin script and returns the status of the script verification.
     *
     * The status will be 1, if the input nIn of the serialized transaction pointed to by
     * txTo correctly spends the scriptPubKey pointed to by scriptPubKey under the additional
     * constraints specified by script validation flags.
     *
     * If not null, err will contain an error/success code for the operation.
     *
     * @param scriptPubKey    The previous output script to be evaluated
     * @param scriptPubKeyLen The number of bytes for the scriptPubKey
     * @param txTo            The transaction with the input that is spending the previous output
     * @param txToLen         The number of bytes for the txTo
     * @param nIn             The index of the input in txTo that spends the scriptPubKey
     * @param flags           The script validation flags
     * @param err             Contains error/success code for the operation
     * @return The status of the verification
     */
    int bitcoinconsensus_verify_script(byte[] scriptPubKey, int scriptPubKeyLen,
                                       byte[] txTo        , int txToLen,
                                       int nIn, int flags , IntByReference err);

    /**
     * Returns the version of the libbitcoinconsensus API.
     *
     * @return The API version.
     */
    int bitcoinconsensus_version();

}
