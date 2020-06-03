
package pubmed.sql;

import jam.app.JamEnv;
import jam.sql.PostgreSQLDb;
import jam.sql.SQLEndpoint;

/**
 * Provides access to the {@code pubmed} SQL databases.
 */
public final class DbManager {
    private static PostgreSQLDb prodDb = null;
    private static PostgreSQLDb testDb = null;
    private static PostgreSQLDb instance = null;

    /**
     * Name of the environment variable that specifies the hostname
     * running the production SQL server.
     */
    public static final String PROD_SQL_HOSTNAME_ENV = "PUBMED_PROD_SQL_HOSTNAME";

    /**
     * Name of the environment variable that specifies the port number
     * for the production SQL server.
     */
    public static final String PROD_SQL_PORT_ENV = "PUBMED_PROD_SQL_PORT";

    /**
     * Name of the environment variable that specifies the production
     * SQL database name.
     */
    public static final String PROD_SQL_DATABASE_ENV = "PUBMED_PROD_SQL_DATABASE";

    /**
     * Name of the environment variable that specifies the production
     * SQL username.
     */
    public static final String PROD_SQL_USERNAME_ENV = "PUBMED_PROD_SQL_USERNAME";

    /**
     * Name of the environment variable that specifies the login
     * password for the production SQL user.
     */
    public static final String PROD_SQL_PASSWORD_ENV = "PUBMED_PROD_SQL_PASSWORD";

    /**
     * Default value for the hostname running the production SQL
     * server.
     */
    public static final String PROD_SQL_HOSTNAME_DEFAULT = "localhost";

    /**
     * Default value for the port number for the production SQL
     * server.
     */
    public static final String PROD_SQL_PORT_DEFAULT = "5432";

    /**
     * Default value for the production SQL database name.
     */
    public static final String PROD_SQL_DATABASE_DEFAULT = "pubmed_prod";

    /**
     * Default value for the production SQL username.
     */
    public static final String PROD_SQL_USERNAME_DEFAULT = "pubmed_prod";

    /**
     * Default value for the login password for the production SQL
     * user.
     */
    public static final String PROD_SQL_PASSWORD_DEFAULT = "pubmed_prod";

    /**
     * Name of the environment variable that specifies the hostname
     * running the test SQL server.
     */
    public static final String TEST_SQL_HOSTNAME_ENV = "PUBMED_TEST_SQL_HOSTNAME";

    /**
     * Name of the environment variable that specifies the port number
     * for the test SQL server.
     */
    public static final String TEST_SQL_PORT_ENV = "PUBMED_TEST_SQL_PORT";

    /**
     * Name of the environment variable that specifies the test
     * SQL database name.
     */
    public static final String TEST_SQL_DATABASE_ENV = "PUBMED_TEST_SQL_DATABASE";

    /**
     * Name of the environment variable that specifies the test
     * SQL username.
     */
    public static final String TEST_SQL_USERNAME_ENV = "PUBMED_TEST_SQL_USERNAME";

    /**
     * Name of the environment variable that specifies the login
     * password for the test SQL user.
     */
    public static final String TEST_SQL_PASSWORD_ENV = "PUBMED_TEST_SQL_PASSWORD";

    /**
     * Default value for the hostname running the test SQL server.
     */
    public static final String TEST_SQL_HOSTNAME_DEFAULT = "localhost";

    /**
     * Default value for the port number for the test SQL server.
     */
    public static final String TEST_SQL_PORT_DEFAULT = "5432";

    /**
     * Default value for the test SQL database name.
     */
    public static final String TEST_SQL_DATABASE_DEFAULT = "pubmed_test";

    /**
     * Default value for the test SQL username.
     */
    public static final String TEST_SQL_USERNAME_DEFAULT = "pubmed_test";

    /**
     * Default value for the login password for the test SQL user.
     */
    public static final String TEST_SQL_PASSWORD_DEFAULT = "pubmed_test";

    /**
     * Returns the active SQL database.
     *
     * <p>The active database (production or test) must be assigned
     * before calling this method.
     *
     * @return the active SQL database.
     */
    public static PostgreSQLDb instance() {
        if (instance != null)
            return instance;
        else
            throw new IllegalStateException("The pubmed database has not been specified.");
    }

    /**
     * Returns the endpoint for the production SQL server.
     *
     * @return the endpoint for the production SQL server.
     */
    public static SQLEndpoint resolveProdEndpoint() {
        int port = Integer.parseInt(JamEnv.getOptional(PROD_SQL_PORT_ENV, PROD_SQL_PORT_DEFAULT));
        String hostname = JamEnv.getOptional(PROD_SQL_HOSTNAME_ENV, PROD_SQL_HOSTNAME_DEFAULT);
        String database = JamEnv.getOptional(PROD_SQL_DATABASE_ENV, PROD_SQL_DATABASE_DEFAULT);
        String username = JamEnv.getOptional(PROD_SQL_USERNAME_ENV, PROD_SQL_USERNAME_DEFAULT);
        String password = JamEnv.getOptional(PROD_SQL_PASSWORD_ENV, PROD_SQL_PASSWORD_DEFAULT);

        return SQLEndpoint.create(hostname, port, database, username, password);
    }

    /**
     * Returns the endpoint for the test SQL server.
     *
     * @return the endpoint for the test SQL server.
     */
    public static SQLEndpoint resolveTestEndpoint() {
        int port = Integer.parseInt(JamEnv.getOptional(TEST_SQL_PORT_ENV, TEST_SQL_PORT_DEFAULT));
        String hostname = JamEnv.getOptional(TEST_SQL_HOSTNAME_ENV, TEST_SQL_HOSTNAME_DEFAULT);
        String database = JamEnv.getOptional(TEST_SQL_DATABASE_ENV, TEST_SQL_DATABASE_DEFAULT);
        String username = JamEnv.getOptional(TEST_SQL_USERNAME_ENV, TEST_SQL_USERNAME_DEFAULT);
        String password = JamEnv.getOptional(TEST_SQL_PASSWORD_ENV, TEST_SQL_PASSWORD_DEFAULT);

        return SQLEndpoint.create(hostname, port, database, username, password);
    }

    /**
     * Returns the production SQL database.
     *
     * @return the production SQL database.
     */
    public static synchronized PostgreSQLDb prodDb() {
        if (prodDb == null)
            prodDb = PostgreSQLDb.instance(resolveProdEndpoint());

        return prodDb;
    }

    /**
     * Returns the test SQL database.
     *
     * @return the test SQL database.
     */
    public static synchronized PostgreSQLDb testDb() {
        if (testDb == null)
            testDb = PostgreSQLDb.instance(resolveTestEndpoint());

        return testDb;
    }

    /**
     * Assigns the production SQL database for use.
     */
    public static void useProd() {
        instance = prodDb();
    }

    /**
     * Assigns the test SQL database for use.
     */
    public static void useTest() {
        instance = testDb();
    }
}
