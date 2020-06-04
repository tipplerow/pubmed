
package pubmed.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import jam.app.JamLogger;
import jam.sql.SQLColumn;
import jam.util.ListUtil;

import pubmed.article.ArticleType;
import pubmed.article.DOI;
import pubmed.article.PMID;
import pubmed.article.PubmedArticle;
import pubmed.article.PubmedDate;
import pubmed.medline.MedlineJournal;
import pubmed.medline.MedlineTA;

/**
 * Maintains the {@code articles} table.
 */
public final class ArticleTable extends ArticleAttrTable<ArticleRecord> {
    private static ArticleTable instance = null;

    private ArticleTable() {
    }

    /**
     * The name of the {@code articles} table.
     */
    public static final String TABLE_NAME = "articles";

    /**
     * The name of the {@code doi} column.
     */
    public static final String DOI_NAME = "doi";

    /**
     * The name of the {@code type} column.
     */
    public static final String TYPE_NAME = "type";

    /**
     * The name of the {@code medline_ta} column.
     */
    public static final String MED_TA_NAME = "medline_ta";

    /**
     * The name of the {@code pub_date} column.
     */
    public static final String PUB_DATE_NAME = "pub_date";

    /**
     * The name of the {@code load_date} column.
     */
    public static final String LOAD_DATE_NAME = "load_date";

    /**
     * The name of the {@code has_title} column.
     */
    public static final String HAS_TITLE_NAME = "has_title";

    /**
     * The name of the {@code has_abstract} column.
     */
    public static final String HAS_ABSTRACT_NAME = "has_abstract";

    /**
     * The name of the {@code has_chemical_list} column.
     */
    public static final String HAS_CHEMICAL_LIST_NAME = "has_chemical_list";

    /**
     * The name of the {@code has_heading_list} column.
     */
    public static final String HAS_HEADING_LIST_NAME = "has_heading_list";

    /**
     * The name of the {@code has_keyword_list} column.
     */
    public static final String HAS_KEYWORD_LIST_NAME = "has_keyword_list";

    /**
     * Meta-data for the {@code type} column.
     */
    public static final SQLColumn TYPE_COLUMN =
        SQLColumn.create(TYPE_NAME, "text")
        .notNull()
        .withIndex();

    /**
     * Meta-data for the {@code doi} column.
     */
    public static final SQLColumn DOI_COLUMN =
        SQLColumn.create(DOI_NAME, "text")
        .withIndex();

    /**
     * Meta-data for the {@code medline_ta} column.
     */
    public static final SQLColumn MED_TA_COLUMN =
        SQLColumn.create(MED_TA_NAME, "text")
        .withIndex();

    /**
     * Meta-data for the {@code pub_date} column.
     */
    public static final SQLColumn PUB_DATE_COLUMN =
        SQLColumn.create(PUB_DATE_NAME, "date")
	.withIndex();

    /**
     * Meta-data for the {@code load_date} column.
     */
    public static final SQLColumn LOAD_DATE_COLUMN =
        SQLColumn.create(LOAD_DATE_NAME, "date")
        .notNull()
        .withIndex();

    /**
     * Meta-data for the {@code has_title} column.
     */
    public static final SQLColumn HAS_TITLE_COLUMN =
	SQLColumn.create(HAS_TITLE_NAME, "boolean")
	.notNull();

    /**
     * Meta-data for the {@code has_abstract} column.
     */
    public static final SQLColumn HAS_ABSTRACT_COLUMN =
	SQLColumn.create(HAS_ABSTRACT_NAME, "boolean")
	.notNull();

    /**
     * Meta-data for the {@code has_chemical_list} column.
     */
    public static final SQLColumn HAS_CHEMICAL_LIST_COLUMN =
	SQLColumn.create(HAS_CHEMICAL_LIST_NAME, "boolean")
	.notNull();

    /**
     * Meta-data for the {@code has_heading_list} column.
     */
    public static final SQLColumn HAS_HEADING_LIST_COLUMN =
	SQLColumn.create(HAS_HEADING_LIST_NAME, "boolean")
	.notNull();

    /**
     * Meta-data for the {@code has_keyword_list} column.
     */
    public static final SQLColumn HAS_KEYWORD_LIST_COLUMN =
	SQLColumn.create(HAS_KEYWORD_LIST_NAME, "boolean")
	.notNull();

    /**
     * Meta-data for the table columns.
     */
    public static final List<SQLColumn> COLUMN_LIST =
        List.of(PMID_PRIMARY_KEY_COLUMN,
		TYPE_COLUMN,
		DOI_COLUMN,
		MED_TA_COLUMN,
		PUB_DATE_COLUMN,
                LOAD_DATE_COLUMN,
		HAS_TITLE_COLUMN,
		HAS_ABSTRACT_COLUMN,
		HAS_CHEMICAL_LIST_COLUMN,
		HAS_HEADING_LIST_COLUMN,
		HAS_KEYWORD_LIST_COLUMN);

    /**
     * Returns the single instance.
     *
     * @return the single instance.
     */
    public static synchronized ArticleTable instance() {
        if (instance == null)
            instance = new ArticleTable();

        instance.require();
        return instance;
    }

    /**
     * Drops the physical database table.
     */
    public static synchronized void dropTable() {
        DbManager.instance().dropTable(TABLE_NAME);
    }

    @Override public ArticleRecord getBulkRecord(PubmedArticle article) {
        return ArticleRecord.create(article);
    }

    @Override public List<SQLColumn> getColumns() {
        return COLUMN_LIST;
    }

    @Override public ArticleRecord getRow(ResultSet resultSet) throws SQLException {
        PMID        pmid      = getPMID(resultSet, PMID_NAME);
        ArticleType type      = getEnum(resultSet, ArticleType.class, TYPE_NAME);
        DOI         doi       = getDOI(resultSet, DOI_NAME);
        MedlineTA   medlineTA = getMedlineTA(resultSet, MED_TA_NAME);
        PubmedDate  pubDate   = getPubmedDate(resultSet, PUB_DATE_NAME);
        LocalDate   loadDate  = getDate(resultSet, LOAD_DATE_NAME);

	boolean hasTitle        = resultSet.getBoolean(HAS_TITLE_NAME);
	boolean hasAbstract     = resultSet.getBoolean(HAS_ABSTRACT_NAME);
	boolean hasChemicalList = resultSet.getBoolean(HAS_CHEMICAL_LIST_NAME);
	boolean hasHeadingList  = resultSet.getBoolean(HAS_HEADING_LIST_NAME);
	boolean hasKeywordList  = resultSet.getBoolean(HAS_KEYWORD_LIST_NAME);

        return ArticleRecord.create(pmid,
				    type,
				    doi,
				    medlineTA,
				    pubDate,
                                    loadDate,
				    hasTitle,
				    hasAbstract,
				    hasChemicalList,
				    hasHeadingList,
				    hasKeywordList);
    }

    @Override public String getTableName() {
        return TABLE_NAME;
    }

    @Override public boolean insertArticles(Collection<PubmedArticle> articles) {
        if (articles.isEmpty())
            return true;

        List<MedlineJournal> journals =
            ListUtil.apply(articles, article -> article.getMedlineJournal());

        JournalCache.instance().addAll(journals);

        return super.insertArticles(articles);
    }

    @Override public boolean hasBulkRecord(PubmedArticle article) {
        return true;
    }

    @Override public void prepareColumn(PreparedStatement statement, int index,
                                        ArticleRecord record, String columnName) throws SQLException {
        switch (columnName) {
        case PMID_NAME:
            setPMID(statement, index, record.getPMID());
            break;

        case TYPE_NAME:
            setEnum(statement, index, record.getType());
            break;

        case DOI_NAME:
            setKeyedObject(statement, index, record.getDOI());
            break;

        case MED_TA_NAME:
            setKeyedObject(statement, index, record.getMedlineTA());
            break;

        case PUB_DATE_NAME:
            setPubmedDate(statement, index, record.getPubDate());
            break;

        case LOAD_DATE_NAME:
            setDate(statement, index, record.getLoadDate());
            break;

	case HAS_TITLE_NAME:
	    statement.setBoolean(index, record.hasTitle());
	    break;

	case HAS_ABSTRACT_NAME:
	    statement.setBoolean(index, record.hasAbstract());
	    break;

	case HAS_CHEMICAL_LIST_NAME:
	    statement.setBoolean(index, record.hasChemicalList());
	    break;

	case HAS_HEADING_LIST_NAME:
	    statement.setBoolean(index, record.hasHeadingList());
	    break;

	case HAS_KEYWORD_LIST_NAME:
	    statement.setBoolean(index, record.hasKeywordList());
	    break;

        default:
            throw invalidColumn(columnName);
        }
    }
    /*
    @Override public void require() {
        db.createEnum("article_type_enum", ArticleType.class);
        super.require();
    }
    */
}
