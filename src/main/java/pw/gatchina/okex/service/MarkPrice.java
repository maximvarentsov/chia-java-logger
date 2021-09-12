package pw.gatchina.okex.service;

import java.util.List;

public class MarkPrice {
    public int code;
    public String msg;
    public List<Data> data;

    public MarkPrice() {}

    @Override
    public String toString() {
        return "MarkPrice{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        public String instId;
        public Instruments instType;
        public double markPx;
        public long ts;

        public Data() {}

        @Override
        public String toString() {
            return "Data{" +
                    "instId='" + instId + '\'' +
                    ", instType=" + instType +
                    ", markPx=" + markPx +
                    ", ts=" + ts +
                    '}';
        }
    }
}
