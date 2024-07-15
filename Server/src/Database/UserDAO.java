package Database;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAO {
    Connection c;

    public UserDAO()
    {
        try {
            c=DatabaseCon.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login()
    {
        return true;
    }
}
