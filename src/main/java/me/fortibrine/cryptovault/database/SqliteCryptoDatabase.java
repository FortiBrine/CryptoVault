package me.fortibrine.cryptovault.database;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.sql.SQLException;

public class SqliteCryptoDatabase extends CryptoDatabase {

    public SqliteCryptoDatabase(String path) throws SQLException {
        super(new JdbcPooledConnectionSource("jdbc:sqlite:" + path));
    }

}
