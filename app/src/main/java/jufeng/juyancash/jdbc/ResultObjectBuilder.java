package jufeng.juyancash.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultObjectBuilder<T> {
	T build(ResultSet rs) throws SQLException;
}
