package med.tracker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Named
@ApplicationScoped
public class Application implements Serializable {
    private final AtomicInteger appVisitCount = new AtomicInteger();

    public int incrementAppCount() {
        return appVisitCount.incrementAndGet();
    }

    public int getAppVisitCount() {
        return appVisitCount.get();
    }
}