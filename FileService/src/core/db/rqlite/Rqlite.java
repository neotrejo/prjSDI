package core.db.rqlite;

import core.db.rqlite.dto.ExecuteResults;
import core.db.rqlite.dto.Pong;
import core.db.rqlite.dto.QueryResults;

public interface Rqlite {

    /**
     * ReadConsistencyLevel specifies the consistency level of a query.
     */
    public enum ReadConsistencyLevel {
        /** Node queries local SQLite database. */
        NONE("none"),

        /** Node performs leader check using master state before querying. */
        WEAK("weak"),

        /** Node performs leader check through the Raft system before querying */
        STRONG("strong");

        private final String value;

        private ReadConsistencyLevel(final String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    /** Query executes a single statement that returns rows. */
    public QueryResults Query(String q, ReadConsistencyLevel lvl);

    /** Query executes multiple statement that returns rows. */
    public QueryResults Query(String[] q, boolean tx, ReadConsistencyLevel lvl);

    /** Execute executes a single statement that does not return rows. */
    public ExecuteResults Execute(String q);

    /** Execute executes multiple statement that do not return rows. */
    public ExecuteResults Execute(String[] q, boolean tx);

    // Ping checks communication with the rqlite node. */
    public Pong Ping();
}