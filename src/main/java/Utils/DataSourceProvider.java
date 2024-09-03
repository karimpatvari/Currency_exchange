package Utils;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

public enum DataSourceProvider {

    INSTANCE;

    private final DataSource dataSource;

    DataSourceProvider() {
        DataSource ds = null;
        SQLiteDataSource sqlite = new SQLiteDataSource();
        sqlite.setUrl("jdbc:sqlite:/Users/karimpatvari/IdeaProjects/CurrencyExchange/identifier.sqlite");
        this.dataSource = sqlite;
    }

    public static DataSource dataSource() {
        
        return  INSTANCE.dataSource;
    }

}
