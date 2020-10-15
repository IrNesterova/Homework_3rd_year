package ru.itis;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import static java.lang.System.out;
public class Main {

    public static void main(String[] args) {
        Cluster cluster = null;
        try {
            cluster = Cluster.builder()
                    .withClusterName("Test Cluster")
                    .withPort(9042)
                    .addContactPoint("ec2-3-236-64-185.compute-1.amazonaws.com")
                    .build();
            Session session = cluster.connect("KS1");                                           // (2)

            ResultSet rs = session.execute("select release_version from system.local");    // (3)
            Row row = rs.one();
            System.out.println(row.getString("release_version"));                          // (4)
        } finally {
            if (cluster != null) cluster.close();                                          // (5)
        }
    }


//    public static void insert1000000Rows(){
//        for (int i = 0; i < 1000000; i++){
//         session.execute("INSERT INTO KS1.test(id, name) " +
//                    "VALUES ("+ i + ", 'la')");
//        }
//    }

}
