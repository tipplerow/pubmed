
package pubmed.sql;

import jam.app.JamEnv;
import jam.app.JamLogger;
import jam.sql.PostgreSQLDb;
import jam.sql.SQLEndpoint;

/**
 * Defines individual and distinct environments for the {@code pubmed}
 * SQL database.
 */
public enum DbEnv {
    PROD("PUBMED_PROD_SQL_HOSTNAME", "localhost",
         "PUBMED_PROD_SQL_PORT",     "5432",
         "PUBMED_PROD_SQL_DATABASE", "pubmed_prod",
         "PUBMED_PROD_SQL_USERNAME", "pubmed_prod",
         "PUBMED_PROD_SQL_PASSWORD", "pubmed_prod"),

    TEST("PUBMED_TEST_SQL_HOSTNAME", "localhost",
         "PUBMED_TEST_SQL_PORT",     "5432",
         "PUBMED_TEST_SQL_DATABASE", "pubmed_test",
         "PUBMED_TEST_SQL_USERNAME", "pubmed_test",
         "PUBMED_TEST_SQL_PASSWORD", "pubmed_test");
    
    private final String hostnameEnv;
    private final String portNumEnv;
    private final String databaseEnv;
    private final String usernameEnv;
    private final String passwordEnv;

    private final String hostnameDefault;
    private final String portNumDefault;
    private final String databaseDefault;
    private final String usernameDefault;
    private final String passwordDefault;

    private PostgreSQLDb db = null;

    private static DbEnv active = null;

    private DbEnv(String hostnameEnv,
                  String hostnameDefault,
                  String portNumEnv,
                  String portNumDefault,
                  String databaseEnv,
                  String databaseDefault,
                  String usernameEnv,
                  String usernameDefault,
                  String passwordEnv,
                  String passwordDefault) {

        this.hostnameEnv = hostnameEnv;
        this.portNumEnv  = portNumEnv; 
        this.databaseEnv = databaseEnv;
        this.usernameEnv = usernameEnv;
        this.passwordEnv = passwordEnv;

        this.hostnameDefault = hostnameDefault;
        this.portNumDefault  = portNumDefault; 
        this.databaseDefault = databaseDefault;
        this.usernameDefault = usernameDefault;
        this.passwordDefault = passwordDefault;
    }

    /**
     * Returns the active database environment.
     *
     * <p>The active database environment (production or test) must be
     * selected before calling this method.
     *
     * @return the active database environment.
     */
    public static DbEnv active() {
        if (active != null)
            return active;
        else
            throw new IllegalStateException("The active database environment has not been specified.");
    }

    /**
     * Returns the active SQL database server.
     *
     * <p>The active database environment (production or test) must be
     * selected before calling this method.
     *
     * @return the active SQL database server.
     */
    public static PostgreSQLDb activeDb() {
        return active().db();
    }

    /**
     * Returns the database server for this environment.
     *
     * @return the database server for this environment.
     */
    public synchronized PostgreSQLDb db() {
        if (db == null)
            db = PostgreSQLDb.instance(endpoint());

        return db;
    }

    /**
     * Marks a named environment as the default or global environment.
     *
     * @param name the name to use as the global environment.
     *
     * @throws RuntimeException unless the name is a valid environment
     * name.
     */
    public static void use(String name) {
        valueOf(name).use();
    }

    /**
     * Returns the database endpoint for this environment.
     *
     * @return the database endpoint for this environment.
     */
    public SQLEndpoint endpoint() {
        int port = Integer.parseInt(JamEnv.getOptional(portNumEnv, portNumDefault));

        String hostname = JamEnv.getOptional(hostnameEnv, hostnameDefault);
        String database = JamEnv.getOptional(databaseEnv, databaseDefault);
        String username = JamEnv.getOptional(usernameEnv, usernameDefault);
        String password = JamEnv.getOptional(passwordEnv, passwordDefault);

        return SQLEndpoint.create(hostname, port, database, username, password);
    }

    /**
     * Marks this environment as the default or global environment.
     */
    public void use() {
        JamLogger.info("Using the [%s] database environment...", this);
        active = this;
    }
}
