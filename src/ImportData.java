import org.neo4j.graphdb.*;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImportData {

    public static final int NUMBER_OF_USERS = 100_000;
    public static final Label PERSON = DynamicLabel.label("Person");
    public static final int NUMBER_OF_FRIEND_RELATIONSHIPS = 500_000;
    public static final DynamicRelationshipType FRIEND = DynamicRelationshipType.withName("FRIEND");

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String path = "neo4j-community-2.0.0/data/graph.db";
        FileUtils.deleteRecursively(new File(path));

        GraphDatabaseService batchDb = BatchInserters.batchDatabase(path);

        for (int i = 1; i <= NUMBER_OF_USERS; i++) {
            Node userNode = batchDb.createNode(PERSON);
            userNode.setProperty("name", "user" + i);
            userNode.setProperty("id", i);
        }

        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_FRIEND_RELATIONSHIPS; i++) {
            Node node1 = batchDb.getNodeById(random.nextInt(NUMBER_OF_USERS));
            Node node2 = batchDb.getNodeById(random.nextInt(NUMBER_OF_USERS));
            node1.createRelationshipTo(node2, FRIEND);
        }

        batchDb.shutdown();
        System.out.println("Users created");

//        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(path);
//        try(Transaction tx = db.beginTx()) {
//            System.out.println("adding index");
//            db.schema().indexFor(PERSON);
//            db.schema().awaitIndexesOnline(30000, TimeUnit.SECONDS);
//            tx.success();
//        }
//
//        db.shutdown();

        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }
}
