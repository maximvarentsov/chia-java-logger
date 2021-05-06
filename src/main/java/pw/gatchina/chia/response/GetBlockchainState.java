package pw.gatchina.chia.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBlockchainState {
    @SerializedName("blockchain_state")
    public BlockchainState blockchainState;

    public boolean success;

    public static class BlockchainState {

        public static class Peak {
            public static class ChallengeVdfOutput {
                public String data;
            }

            public static class RewardClaimsIncorporated {
                public long amount;
                @SerializedName("parent_coin_info")
                public String parentCoinInfo;
                @SerializedName("puzzle_hash")
                public String puzzleHash;
            }

            public static class SubEpochSummaryIncluded {
                @SerializedName("new_difficulty")
                public String newDifficulty;

                @SerializedName("new_sub_slot_iters")
                public String newSubSlotIters;

                @SerializedName("num_blocks_overflow")
                public int numBlocksOverflow;

                @SerializedName("prev_subepoch_summary_hash")
                public String prevSubepochSummaryHash;

                @SerializedName("reward_chain_hash")
                public String rewardChainHash;
            }

            @SerializedName("challenge_block_info_hash")
            public String challengeBlockInfoHash;

            @SerializedName("challenge_vdf_output")
            public ChallengeVdfOutput challengeVdfOutput;

            public int deficit;

            @SerializedName("farmer_puzzle_hash")
            public String farmerPuzzleHash;

            public long fees;

            @SerializedName("finished_challenge_slot_hashes")
            public List<String> finishedChallengeSlotHashes;

            @SerializedName("finished_infused_challenge_slot_hashes")
            public List<String> finishedInfusedChallengeSlotHashes;

            @SerializedName("finished_reward_slot_hashes")
            public List<String> finishedRewardSlotHashes;

            @SerializedName("header_hash")
            public String headerHash;

            public int height;

            @SerializedName("infused_challenge_vdf_output")
            public ChallengeVdfOutput infusedChallengeVdfOutput;

            public boolean overflow;

            @SerializedName("pool_puzzle_hash")
            public String poolPuzzleHash;

            @SerializedName("prev_hash")
            public String prevHash;

            @SerializedName("prev_transaction_block_hash")
            public String prevTransactionBlockHash;

            @SerializedName("prev_transaction_block_height")
            public long prevTransactionBlockHeight;

            @SerializedName("required_iters")
            public int requiredIters;

            @SerializedName("reward_claims_incorporated")
            public List<RewardClaimsIncorporated> rewardClaimsIncorporated;

            @SerializedName("reward_infusion_new_challenge")
            public String rewardInfusionNewChallenge;

            @SerializedName("signage_point_index")
            public int signagePointIndex;

            @SerializedName("sub_epoch_summary_included")
            public SubEpochSummaryIncluded subEpochSummaryIncluded;

            @SerializedName("sub_slot_iters")
            public long subSlotIters;

            public long timestamp;

            @SerializedName("total_iters")
            public long totalIters;

            public long weight;
        }

        public static class Sync {
            @SerializedName("sync_mode")
            public boolean syncMode;

            @SerializedName("sync_progress_height")
            public int syncProgressHeight;

            @SerializedName("sync_tip_height")
            public int syncTipHeight;

            public boolean synced;
        }

        public int difficulty;

        @SerializedName("genesis_challenge_initialized")
        public boolean genesisChallengeInitialized;

        @SerializedName("mempool_size")
        public int mempoolSize;

        public Peak peak;

        public long space;

        @SerializedName("sub_slot_iters")
        public long subSlotIters;

        public Sync sync;
    }
}
