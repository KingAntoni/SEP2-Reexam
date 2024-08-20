package util;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;

public interface Subject {

    void addListener(String eventName, PropertyChangeListener listener)
            throws IOException, SQLException;

    void removeListener(String eventName, PropertyChangeListener listener);
}