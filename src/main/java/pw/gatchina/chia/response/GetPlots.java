package pw.gatchina.chia.response;

import com.google.gson.annotations.SerializedName;

/**
 * https://docs.chia.net/docs/10protocol/harvester_protocol#respond_plots
 */
public class GetPlots {
    /**
     * Filenames for files which cannot be opened
     */
    @SerializedName("failed_to_open_filenames")
    public String[] failedToOpenFilenames;
    /**
     * Filenames for files which cannot be farmed by this farmer
     */
    @SerializedName("no_key_filenames")
    public String[] noKeyFilenames;

    public Plot[] plots;
    public boolean success;

    public static class Plot {
        public String filename;

        public int size;

        @SerializedName("plot_id")
        public String plotId;

        @SerializedName("pool_public_key")
        public String poolPublicKey;

        @SerializedName("pool_contract_puzzle_hash")
        public String poolContractPuzzleHash;

        @SerializedName("plot_public_key")
        public String plotPublicKey;

        @SerializedName("file_size")
        public long fileSize;

        @SerializedName("plot-seed")
        public String plotSeed;

        @SerializedName("time_modified")
        public String timeModified;

        @Override
        public String toString() {
            return "Plot{" +
                    "filename='" + filename + '\'' +
                    ", size=" + size +
                    ", plotId='" + plotId + '\'' +
                    ", poolPublicKey='" + poolPublicKey + '\'' +
                    ", poolContractPuzzleHash='" + poolContractPuzzleHash + '\'' +
                    ", plotPublicKey='" + plotPublicKey + '\'' +
                    ", fileSize=" + fileSize +
                    ", plotSeed='" + plotSeed + '\'' +
                    ", timeModified='" + timeModified + '\'' +
                    '}';
        }
    }
}
