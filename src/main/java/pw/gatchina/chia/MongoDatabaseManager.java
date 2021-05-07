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

import java.io.Closeable;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDatabaseManager implements Closeable {
    private final static CodecRegistry pojoCodecRegistry =
            fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    public MongoDatabaseManager(final @NotNull String connection) {
        var connectionString = new ConnectionString(connection);

        var settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connectionString)
                .applicationName("chia-java-logger")
                .build();

        mongoClient = MongoClients.create(settings);

        var databaseName = connectionString.getDatabase();
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

    public <TDocument> MongoCollection<TDocument> getCollection(final @NotNull String collection, @NotNull final Class<TDocument> type) {
        return mongoDatabase.getCollection(collection, type);
    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
