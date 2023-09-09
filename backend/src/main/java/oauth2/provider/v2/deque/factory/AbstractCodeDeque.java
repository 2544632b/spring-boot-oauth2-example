package oauth2.provider.v2.deque.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class AbstractCodeDeque<T> {

    protected final List<T> deque = new ArrayList<>();

    protected int nextIndex = 0;

    protected final Lock lock = new ReentrantLock();

    protected int hashSeed = 769;

    /**
     * @param filter
     * check the code expired or not
     */
    protected void checkExpired(Predicate<? super T> filter) {
        try {
            if(lock.tryLock(5, TimeUnit.SECONDS)) {
                deque.removeIf(filter);
            }
        } catch(Exception e) {
            e.printStackTrace();
            lock.unlock();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param t
     * add method, try safety
     */
    protected void add(T t) {
        try {
            if(lock.tryLock(5, TimeUnit.SECONDS)) {
                if (nextIndex < deque.size()) {
                    deque.set(nextIndex, t);
                } else {
                    deque.add(t);
                }
                nextIndex++;
            }
        } catch(Exception e) {
            e.printStackTrace();
            lock.unlock();
        } finally {
            lock.unlock();
        }
    }

}