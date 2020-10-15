import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnection {

    String serverIP = "ec2-3-236-58-36.compute-1.amazonaws.com";
    String keyspace = "KS1";

    Cluster cluster = Cluster.builder()
            .addContactPoints(serverIP)
            .build();

    Session session = cluster.connect(keyspace);
    public void insert1000Rows(){
        String cqlStatementC = "INSERT INTO KS1.test(username, password) " +
                "VALUES ('Serenity', 'fa3dfQefx')";
        for (int i = 0; i < 1000; i++){
            session.execute(cqlStatementC);
        }
    }
}
