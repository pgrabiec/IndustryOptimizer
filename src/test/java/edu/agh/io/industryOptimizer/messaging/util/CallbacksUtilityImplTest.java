package edu.agh.io.industryOptimizer.messaging.util;

import edu.agh.io.industryOptimizer.messaging.Message;
import edu.agh.io.industryOptimizer.messaging.MessageCallback;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CallbacksUtilityImplTest {
    @Test
    public void testAddCallback() throws Exception {
        CallbacksUtility util = new CallbacksUtilityImpl();

        MessageCallback callback = Mockito.mock(MessageCallback.class);

        util.addCallback(new Object(), callback);

        Assert.assertTrue("Callback should have been added", util.contains(callback));
    }

    @Test
    public void testRemoveCallback() throws Exception {
        CallbacksUtility util = new CallbacksUtilityImpl();

        MessageCallback callback = Mockito.mock(MessageCallback.class);

        util.addCallback(new Object(), callback);

        Assert.assertTrue("Callback should have been added", util.contains(callback));

        util.removeCallback(callback);

        Assert.assertFalse("Callback should have been removed", util.contains(callback));
    }

    @Test
    public void testExecuteCallbacks() throws Exception {
        CallbacksUtility util = new CallbacksUtilityImpl();

        Object[] types = new Object[] {
                new Object(),
                new Object(),
                new Object()
        };

        MessageCallback callback = Mockito.mock(MessageCallback.class);

        util.addCallback(types[0], callback);
        util.addCallback(types[1], callback);

        Message message = Mockito.mock(Message.class);

        Mockito.verify(
                callback,
                Mockito.times(0)
        ).messageReceived(Mockito.anyObject());

        util.executeCallbacks(types[0], message);

        Mockito.verify(
                callback,
                Mockito.times(1)
        ).messageReceived(Mockito.anyObject());

        util.executeCallbacks(types[1], message);

        Mockito.verify(
                callback,
                Mockito.times(2)
        ).messageReceived(Mockito.anyObject());

        util.executeCallbacks(types[2], message);

        Mockito.verify(
                callback,
                Mockito.times(2)
        ).messageReceived(Mockito.anyObject());
    }

}