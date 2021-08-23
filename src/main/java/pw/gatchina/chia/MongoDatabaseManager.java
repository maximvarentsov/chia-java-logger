package pw.gatchina.chia;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jetbrains.annotations.NotNull;
import pw.gatchina.util.BigIntegerCodec;

import java.io.Closeable;

import static org.bson.codecs.configuration.CodecRegistries.*;

public class MongoDatabaseManager implements Closeable {
    private final static CodecRegistry pojoCodecRegistry = fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        fromCodecs(new BigIntegerCodec()),
        fromProviders(PojoCodecProvider.builder().automatic(true).build())
    );
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public MongoDatabaseManager(final @NotNull String connection) {
        final var connectionString = new ConnectionString(connection);

        final var settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connectionString)
                .applicationName("chia-java-logger")
                .build();

        mongoClient = MongoClients.create(settings);

        final var databaseName = connectionString.getDatabase();
        if (databaseName == null) {
            throw new IllegalArgumentException("MongoDB database name not set.");
        }

        mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    public MongoClient getClient() {
        return mongoClient;
    }

    public MongoDatabase getDatabase() {
        return mongoDatabase;
    }

    public boolean collectionExists(final @NotNull String name) {
        for (final var collection : getDatabase().listCollectionNames()) {
            if (name.equalsIgnoreCase(collection)) {
                return true;
            }
        }
        return false;
    }

    public boolean collectionNotExists(final @NotNull String name) {
        return ! collectionExists(name);
    }

    public void dropCollection(final @NotNull String name) {
        mongoDatabase.getCollection(name).drop();
    }

    public <TDocument> MongoCollection<TDocument> getCollection(final @NotNull String collection, final @NotNull Class<TDocument> type) {
        return mongoDatabase.getCollection(collection, type);
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
