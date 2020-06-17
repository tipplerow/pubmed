
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jam.lang.JamException;
import jam.report.LineBuilder;
import jam.sql.SQLColumn;
import jam.sql.SQLKeyTable;
import jam.util.ListUtil;
import jam.util.RegexUtil;
import jam.util.StreamUtil;

import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.filter.ArticleFilter;
import pubmed.filter.ChemicalListFilter;
import pubmed.filter.KeywordAbstractFilter;
import pubmed.filter.KeywordListFilter;
import pubmed.filter.KeywordTitleFilter;
import pubmed.filter.MeshHeadingFilter;
import pubmed.filter.MeshTreeFilter;
import pubmed.subject.Subject;

/**
 * Provides {@code pubmed} database tables that score articles for
 * their relevance to a particular subject.
 */
public final class SubjectScoreTable
    extends SQLKeyTable<PMID, SubjectScoreRecord>
    implements BulkFileTarget<SubjectScoreRecord>, PubmedResource {

    private final Subject subject;
    private final String  tableName;

    private final ArticleFilter chemicalListFilter;
    private final ArticleFilter keywordAbstractFilter;
    private final ArticleFilter keywordListFilter;
    private final ArticleFilter keywordTitleFilter;
    private final ArticleFilter meshHeadingFilter;
    private final ArticleFilter meshTreeFilter;

    private static final Set<String> tableNames =
        new HashSet<String>();

    private static final Map<Subject, SubjectScoreTable> instances =
        new HashMap<Subject, SubjectScoreTable>();

    private SubjectScoreTable(Subject subject) {
        super(DbEnv.activeDb());

        this.subject = subject;
        this.tableName = getTableName(subject);

        this.chemicalListFilter    = ChemicalListFilter.create(subject);
        this.keywordAbstractFilter = KeywordAbstractFilter.create(subject);
        this.keywordListFilter     = KeywordListFilter.create(subject);
        this.keywordTitleFilter    = KeywordTitleFilter.create(subject);
        this.meshHeadingFilter     = MeshHeadingFilter.create(subject);
        this.meshTreeFilter        = MeshTreeFilter.create(subject);

        register();
    }

    private void register() {
        //
        // One table per subject...
        //
        if (instances.containsKey(subject))
            throw JamException.runtime("Duplicate subject table: [%s].", subject);
        else
            instances.put(subject, this);

        // Substance names are unique, but there is a VERY small
        // possibility that their table names might not be unique
        // after removing white space and non-word characters...
        if (tableNames.contains(tableName))
            throw JamException.runtime("Duplicate table name: [%s].", tableName);
        else
            tableNames.add(tableName);
    }

    /**
     * The name of the {@code title_score} column.
     */
    public static final String TITLE_SCORE_NAME = "title_score";

    /**
     * The name of the {@code abstract_score} column.
     */
    public static final String ABSTRACT_SCORE_NAME = "abstract_score";

    /**
     * The name of the {@code mesh_tree_score} column.
     */
    public static final String MESH_TREE_SCORE_NAME = "mesh_tree_score";

    /**
     * The name of the {@code heading_list_score} column.
     */
    public static final String HEADING_LIST_SCORE_NAME = "heading_list_score";

    /**
     * The name of the {@code keyword_list_score} column.
     */
    public static final String KEYWORD_LIST_SCORE_NAME = "keyword_list_score";

    /**
     * The name of the {@code chemical_list_score} column.
     */
    public static final String CHEMICAL_LIST_SCORE_NAME = "chemical_list_score";

    /**
     * Meta-data for the {@code title_score} column.
     */
    public static final SQLColumn TITLE_SCORE_COLUMN =
	SQLColumn.create(TITLE_SCORE_NAME, "integer")
	.notNull();

    /**
     * Meta-data for the {@code abstract_score} column.
     */
    public static final SQLColumn ABSTRACT_SCORE_COLUMN =
	SQLColumn.create(ABSTRACT_SCORE_NAME, "integer")
	.notNull();

    /**
     * Meta-data for the {@code mesh_tree_score} column.
     */
    public static final SQLColumn MESH_TREE_SCORE_COLUMN =
	SQLColumn.create(MESH_TREE_SCORE_NAME, "integer")
	.notNull();

    /**
     * Meta-data for the {@code heading_list_score} column.
     */
    public static final SQLColumn HEADING_LIST_SCORE_COLUMN =
	SQLColumn.create(HEADING_LIST_SCORE_NAME, "integer")
	.notNull();

    /**
     * Meta-data for the {@code keyword_list_score} column.
     */
    public static final SQLColumn KEYWORD_LIST_SCORE_COLUMN =
	SQLColumn.create(KEYWORD_LIST_SCORE_NAME, "integer")
	.notNull();

    /**
     * Meta-data for the {@code chemical_list_score} column.
     */
    public static final SQLColumn CHEMICAL_LIST_SCORE_COLUMN =
	SQLColumn.create(CHEMICAL_LIST_SCORE_NAME, "integer")
	.notNull();

    /**
     * Meta-data for the table columns.
     */
    public static final List<SQLColumn> COLUMN_LIST =
        List.of(PMID_PRIMARY_KEY_COLUMN,
		TITLE_SCORE_COLUMN,
		ABSTRACT_SCORE_COLUMN,
		MESH_TREE_SCORE_COLUMN,
		HEADING_LIST_SCORE_COLUMN,
		KEYWORD_LIST_SCORE_COLUMN,
		CHEMICAL_LIST_SCORE_COLUMN);

    /**
     * Returns the table storing scores for a particular subject.
     *
     * @param subject the subject of interest.
     *
     * @return the table storing scores for the specified subject.
     */
    public static synchronized SubjectScoreTable instance(Subject subject) {
        SubjectScoreTable instance = instances.get(subject);
        
        if (instance == null) {
            instance = new SubjectScoreTable(subject);
            instance.require();
        }

        return instance;
    }

    /**
     * Drops the physical database table for a particular subject.
     *
     * @param subject the subject of interest.
     */
    public static synchronized void dropTable(Subject subject) {
        DbEnv.activeDb().dropTable(getTableName(subject));
    }

    /**
     * Converts an object name into a form suitable to use as a
     * component of a table name.
     *
     * @param name the name to convert.
     *
     * @return a coded string suitable to use as a component of a
     * table name.
     */
    public static String getTableCode(String name) {
        // Start with the original name...
        String code = name;

        // Convert to lower case...
        code = code.toLowerCase();

        // Replace spaces with underscores...
        code = RegexUtil.replace(RegexUtil.MULTI_WHITE_SPACE, code, "_");

        // Remove any remaining non-word characters...
        code = RegexUtil.remove(RegexUtil.MULTI_NON_WORD, code);

        return code;
    }

    /**
     * Returns the name of the database table that stores scores for a
     * particular subject.
     *
     * @param subject the subject of interest.
     *
     * @return the name of the database table that stores scores for
     * the specified subject.
     */
    public static String getTableName(Subject subject) {
        String prefix = subject.getTablePrefix();
        String suffix = subject.getTableSuffix();

        LineBuilder builder = new LineBuilder("_");

        if (prefix != null && !prefix.isEmpty())
            builder.append(getTableCode(prefix));

        builder.append("score");

        if (suffix != null && !suffix.isEmpty())
            builder.append(getTableCode(suffix));

        return builder.toString();
    }

    /**
     * Determines whether to add a score record to this table.
     *
     * @param scoreRecord the score record for an article.
     *
     * @return {@code true} iff the score record should be added to
     * this table.
     */
    public boolean filterRecord(SubjectScoreRecord scoreRecord) {
        return scoreRecord.filter();
    }

    /**
     * Selects records to be added to this table.
     *
     * @param scoreRecords score records from a collection of articles
     * to be stored.
     *
     * @return the score records that pass the selection filter and
     * will be stored in this table.
     */
    public List<SubjectScoreRecord> filterRecords(Collection<SubjectScoreRecord> scoreRecords) {
        return ListUtil.filter(scoreRecords, record -> filterRecord(record));
    }

    /**
     * Generates the score record for an article.
     *
     * @param article the article to score.
     *
     * @return the score record for the given article.
     */
    public SubjectScoreRecord scoreArticle(PubmedArticle article) {
        int titleScore        = keywordTitleFilter.score(article);
        int abstractScore     = keywordAbstractFilter.score(article);
        int meshTreeScore     = meshTreeFilter.score(article);
        int headingListScore  = meshHeadingFilter.score(article);
        int keywordListScore  = keywordListFilter.score(article);
        int chemicalListScore = chemicalListFilter.score(article);

        return SubjectScoreRecord.create(article.getPMID(),
                                         titleScore,
                                         abstractScore,
                                         meshTreeScore,
                                         headingListScore,
                                         keywordListScore,
                                         chemicalListScore);
    }

    /**
     * Generates the score records for a collection of articles.
     *
     * <p>The default implementation here uses a parallel stream and
     * the {@code scoreArticle} method.
     *
     * @param articles the articles to score.
     *
     * @return the score records for the given articles.
     */
    public List<SubjectScoreRecord> scoreArticles(Collection<PubmedArticle> articles) {
        return StreamUtil.apply(articles.parallelStream(), article -> scoreArticle(article));
    }

    @Override public Collection<SubjectScoreRecord> bulkRecords(Collection<PubmedArticle> articles) {
        return filterRecords(scoreArticles(articles));
    }

    @Override public List<SQLColumn> getColumns() {
        return COLUMN_LIST;
    }

    @Override public PMID getKey(SubjectScoreRecord record) {
	return record.getPMID();
    }

    @Override public PMID getKey(ResultSet resultSet, String columnName) throws SQLException {
        return getPMID(resultSet, columnName);
    }

    @Override public SubjectScoreRecord getRow(ResultSet resultSet) throws SQLException {
        PMID pmid = getPMID(resultSet, PMID_NAME);

	int titleScore        = resultSet.getInt(TITLE_SCORE_NAME);
	int abstractScore     = resultSet.getInt(ABSTRACT_SCORE_NAME);
	int meshTreeScore     = resultSet.getInt(MESH_TREE_SCORE_NAME);
	int headingListScore  = resultSet.getInt(HEADING_LIST_SCORE_NAME);
	int keywordListScore  = resultSet.getInt(KEYWORD_LIST_SCORE_NAME);
	int chemicalListScore = resultSet.getInt(CHEMICAL_LIST_SCORE_NAME);

        return SubjectScoreRecord.create(pmid,
                                         titleScore,
                                         abstractScore,
                                         meshTreeScore,
                                         headingListScore,
                                         keywordListScore,
                                         chemicalListScore);
    }

    @Override public String getTableName() {
        return tableName;
    }

    @Override public void prepareColumn(PreparedStatement statement, int index,
                                        SubjectScoreRecord record, String columnName) throws SQLException {
        switch (columnName) {
        case PMID_NAME:
            setPMID(statement, index, record.getPMID());
            break;

	case TITLE_SCORE_NAME:
	    statement.setInt(index, record.getTitleScore());
	    break;

	case ABSTRACT_SCORE_NAME:
	    statement.setInt(index, record.getAbstractScore());
	    break;

	case MESH_TREE_SCORE_NAME:
	    statement.setInt(index, record.getMeshTreeScore());
	    break;

	case HEADING_LIST_SCORE_NAME:
	    statement.setInt(index, record.getHeadingListScore());
	    break;

	case KEYWORD_LIST_SCORE_NAME:
	    statement.setInt(index, record.getKeywordListScore());
	    break;

	case CHEMICAL_LIST_SCORE_NAME:
	    statement.setInt(index, record.getChemicalListScore());
	    break;

        default:
            throw invalidColumn(columnName);
        }
    }

    @Override public void prepareKey(PreparedStatement statement, int index, PMID key) throws SQLException {
        setPMID(statement, index, key);
    }

    @Override public boolean updateArticles(Collection<PubmedArticle> articles) {
        //
        // It is very unlikely that a revision to the article will
        // change its scores, so this is a no-op...
        //
        return true;
    }

    @Override public boolean updateRecords(Collection<SubjectScoreRecord> records) {
        //
        // It is very unlikely that a revision to the article will
        // change its scores, so this is a no-op...
        //
        return true;
    }
}
