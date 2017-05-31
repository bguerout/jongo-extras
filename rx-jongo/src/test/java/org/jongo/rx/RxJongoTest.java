package org.jongo.rx;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.reactivex.Observable;
import io.reactivex.Observer;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.model.Friend;
import org.jongo.util.JongoTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RxJongoTest extends JongoTestBase {

    private MongoCollection collection;

    @Before
    public void setUp() throws Exception {
        collection = createEmptyCollection("friends");
    }

    @After
    public void tearDown() throws Exception {
        dropCollection("friends");
    }

    @Test
    public void testObservableFind() {
        /* given */
        Friend friend = new Friend(new ObjectId(), "John");
        Friend friend2 = new Friend(new ObjectId(), "Smith");
        collection.save(friend);
        collection.save(friend2);

        /* when */
        Observer<Friend> observer = mock(Observer.class);
        Observable<Friend> friendObservable = collection.find().as(Friend.class).to(RxJongo.<Friend>toObservable());
        friendObservable.subscribe(observer);

        /* then */
        verify(observer, never()).onError(any(Throwable.class));
        verify(observer, times(1)).onComplete();
        verify(observer, times(1)).onNext(friend);
        verify(observer, times(1)).onNext(friend2);
    }

}
