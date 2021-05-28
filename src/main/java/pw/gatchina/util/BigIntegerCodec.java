package pw.gatchina.util;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author maximvarentsov
 * @since 22.05.2021
 */

public final class BigIntegerCodec implements Codec<BigInteger> {
    public BigIntegerCodec() {
    }

    public void encode(BsonWriter writer, BigInteger value, EncoderContext encoderContext) {
        writer.writeDecimal128(new Decimal128(new BigDecimal(value)));
    }

    public BigInteger decode(BsonReader reader, DecoderContext decoderContext) {
        return reader.readDecimal128().bigDecimalValue().toBigInteger();
    }

    public Class<BigInteger> getEncoderClass() {
        return BigInteger.class;
    }
}
