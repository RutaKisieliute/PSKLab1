package med.tracker;


import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("sessionTracker")
@SessionScoped
public class Session implements Serializable {
    private int sessionVisitCount = 0;

    public int incrementSessionCount() {
        return ++sessionVisitCount;
    }

    public int getSessionVisitCount() {
        return sessionVisitCount;
    }
}