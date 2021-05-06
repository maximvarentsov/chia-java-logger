package pw.gatchina.chia.response;

import com.google.gson.annotations.SerializedName;

public class GetPlots {
    @SerializedName("failed_to_open_filenames")
    public String[] failedToOpenFilenames;
    @SerializedName("not_found_filenames")
    public String[] notFoundFilenames;

    public Plot[] plots;
    public boolean success;

    public static class Plot {
        @SerializedName("file_size")
        public long fileSize;

        public String filename;

        @SerializedName("plot-seed")
        public String plotSeed;

        @SerializedName("plot_public_key")
        public String plotPublicKey;

        @SerializedName("pool_contract_puzzle_hash")
        public String poolContractPuzzleHash;

        @SerializedName("pool_public_key")
        public String poolPublicKey;

        public int size;

        @SerializedName("time_modified")
        public String timeModified;
    }
}
