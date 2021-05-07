package pw.gatchina.chia;

import java.util.ArrayList;
import java.util.List;

public class JsonConfig {
    public Mongo mongo;
    public Hosts hosts;
    public Cron cron;
    public Wallet wallet;

    public static class Mongo {
        public String connection;
    }

    public static class Hosts {
        public String fullNode;
        public String wallet;
        public List<String> harvesters = new ArrayList<>();
    }

    public static class Cron {
        public String blockchain;
        public String walletBalance;
        public String plots;
        public String statistics;
    }

    public static class Wallet {
        public int id;
    }
}
