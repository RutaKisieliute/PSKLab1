package med.tracker;


import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class PageUsecase {

    @Inject
    private Application appTracker;

    @Inject
    private Session sessionTracker;

    private int currentAppCount;
    private int currentSessionCount;

    @PostConstruct
    public void init() {
        currentAppCount = appTracker.incrementAppCount();
        currentSessionCount = sessionTracker.incrementSessionCount();
    }

    public int getCurrentAppCount() {
        return currentAppCount;
    }

    public int getCurrentSessionCount() {
        return currentSessionCount;
    }
}
