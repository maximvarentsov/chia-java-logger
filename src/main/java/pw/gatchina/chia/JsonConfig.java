package pw.gatchina.chia;

import java.util.ArrayList;
import java.util.List;

public class JsonConfig {
    public Mongo mongo;
    public Hosts hosts;
    public Cron cron;
    public Wallet wallet;
    public Okex okex;

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
        public String okex;
    }

    public static class Wallet {
        public int id;
        public String address;
    }

    public static class Okex {
        public List<String> instruments = new ArrayList<>();
        public String endpoint;
        public String apikey;
        public String secretkey;
        public String passphrase;
    }
}
