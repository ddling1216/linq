package com.bestvike.linq.exception;

import com.bestvike.TestCase;
import com.bestvike.linq.resources.SR;
import com.bestvike.linq.util.Assertion;
import com.bestvike.linq.util.Environment;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

/**
 * Created by 许崇雷 on 2017-08-04.
 */
class ExceptionTest extends TestCase {
    @Test
    void testArgument() {
        try {
            throw new ArgumentException();
        } catch (ArgumentException e) {
            assertEquals(SR.Arg_ArgumentException, e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error");
        } catch (ArgumentException e) {
            assertEquals("arg error", e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", new NullPointerException());
        } catch (ArgumentException e) {
            assertEquals("arg error", e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", "index", new NullPointerException());
        } catch (ArgumentException e) {
            assertEquals("arg error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
        }

        try {
            throw new ArgumentException("arg error", "index");
        } catch (ArgumentException e) {
            assertEquals("arg error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
        }

        try {
            ThrowHelper.throwImplementComparableException();
        } catch (ArgumentException e) {
            assertEquals(SR.Argument_ImplementComparable, e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            ThrowHelper.throwTupleIncorrectTypeException(String.class, ExceptionArgument.index);
        } catch (ArgumentException e) {
            assertEquals(String.format(SR.ArgumentException_TupleIncorrectType, String.class) + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
        }

        try {
            ThrowHelper.throwTupleLastArgumentNotATupleException();
        } catch (ArgumentException e) {
            assertEquals(SR.ArgumentException_TupleLastArgumentNotATuple, e.getMessage());
            assertEquals(null, e.getParamName());
        }
    }

    @Test
    void testArgumentNullException() {
        try {
            throw new ArgumentNullException();
        } catch (ArgumentNullException e) {
            assertEquals(SR.ArgumentNull_Generic, e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentNullException("index");
        } catch (ArgumentNullException e) {
            assertEquals(SR.ArgumentNull_Generic + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
        }

        try {
            throw new ArgumentNullException("null error", new NullPointerException());
        } catch (ArgumentNullException e) {
            assertEquals("null error", e.getMessage());
            assertEquals(null, e.getParamName());
        }

        try {
            throw new ArgumentNullException("index", "null error");
        } catch (ArgumentNullException e) {
            assertEquals("null error" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
        }

        try {
            ThrowHelper.throwArgumentNullException(ExceptionArgument.index);
        } catch (ArgumentNullException e) {
            assertEquals(SR.ArgumentNull_Generic + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
        }
    }

    @Test
    void testArgumentOutOfRangeException() {
        try {
            throw new ArgumentOutOfRangeException();
        } catch (ArgumentOutOfRangeException e) {
            assertEquals(SR.Arg_ArgumentOutOfRangeException, e.getMessage());
            assertEquals(null, e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index");
        } catch (ArgumentOutOfRangeException e) {
            assertEquals(SR.Arg_ArgumentOutOfRangeException + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index", "out of range");
        } catch (ArgumentOutOfRangeException e) {
            assertEquals("out of range" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("out of range", new NullPointerException());
        } catch (ArgumentOutOfRangeException e) {
            assertEquals("out of range", e.getMessage());
            assertEquals(null, e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        try {
            throw new ArgumentOutOfRangeException("index", -1, "out of range");
        } catch (ArgumentOutOfRangeException e) {
            assertEquals("out of range" + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index") + Environment.NewLine + String.format(SR.ArgumentOutOfRange_ActualValue, "-1"), e.getMessage());
            assertEquals("index", e.getParamName());
            assertEquals(-1, e.getActualValue());
        }

        try {
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.index);
        } catch (ArgumentOutOfRangeException e) {
            assertEquals(SR.Arg_ArgumentOutOfRangeException + Environment.NewLine + String.format(SR.Arg_ParamName_Name, "index"), e.getMessage());
            assertEquals("index", e.getParamName());
            assertEquals(null, e.getActualValue());
        }

        assertEquals(SR.Arg_ArgumentException + Environment.NewLine + String.format(SR.ArgumentOutOfRange_ActualValue, 2), new ArgumentOutOfRangeException(null, 2, null).getMessage());
    }

    @Test
    void testInvalidOperationException() {
        try {
            throw new InvalidOperationException();
        } catch (InvalidOperationException e) {
            assertEquals(SR.Arg_InvalidOperationException, e.getMessage());
        }

        try {
            throw new InvalidOperationException("invalid operation");
        } catch (InvalidOperationException e) {
            assertEquals("invalid operation", e.getMessage());
        }

        try {
            throw new InvalidOperationException("invalid operation", new NullPointerException());
        } catch (InvalidOperationException e) {
            assertEquals("invalid operation", e.getMessage());
            assertIsType(NullPointerException.class, e.getCause());
        }

        try {
            ThrowHelper.throwMoreThanOneElementException();
        } catch (InvalidOperationException e) {
            assertEquals(SR.MoreThanOneElement, e.getMessage());
        }

        try {
            ThrowHelper.throwMoreThanOneMatchException();
        } catch (InvalidOperationException e) {
            assertEquals(SR.MoreThanOneMatch, e.getMessage());
        }

        try {
            ThrowHelper.throwNoElementsException();
        } catch (InvalidOperationException e) {
            assertEquals(SR.NoElements, e.getMessage());
        }
        try {
            ThrowHelper.throwNoMatchException();
        } catch (InvalidOperationException e) {
            assertEquals(SR.NoMatch, e.getMessage());
        }
    }

    @Test
    void testNotSupportedException() {
        try {
            throw new NotSupportedException();
        } catch (NotSupportedException e) {
            assertEquals(SR.Arg_NotSupportedException, e.getMessage());
        }

        try {
            throw new NotSupportedException("not supported");
        } catch (NotSupportedException e) {
            assertEquals("not supported", e.getMessage());
        }

        try {
            throw new NotSupportedException("not supported", new NullPointerException());
        } catch (NotSupportedException e) {
            assertEquals("not supported", e.getMessage());
            assertIsType(NullPointerException.class, e.getCause());
        }

        try {
            ThrowHelper.throwNotSupportedException();
        } catch (NotSupportedException e) {
            assertEquals(SR.Arg_NotSupportedException, e.getMessage());
        }
    }

    @Test
    void testRepeatInvokeException() {
        try {
            throw new RepeatInvokeException();
        } catch (RepeatInvokeException e) {
            assertEquals(SR.Arg_RepeatInvokeException, e.getMessage());
        }

        try {
            throw new RepeatInvokeException("repeat invoked");
        } catch (RepeatInvokeException e) {
            assertEquals("repeat invoked", e.getMessage());
        }

        try {
            throw new RepeatInvokeException("repeat invoked", new NullPointerException());
        } catch (RepeatInvokeException e) {
            assertEquals("repeat invoked", e.getMessage());
            assertIsType(NullPointerException.class, e.getCause());
        }

        try {
            ThrowHelper.throwRepeatInvokeException();
        } catch (RepeatInvokeException e) {
            assertEquals(SR.Arg_RepeatInvokeException, e.getMessage());
        }
    }

    @Test
    void testRuntimeException() {
        try {
            ThrowHelper.throwRuntimeException(null);
            fail("should not be here");
        } catch (RuntimeException e) {
            assertNull(e.getCause());
        }

        try {
            ThrowHelper.throwRuntimeException(new IllegalAccessException());
            fail("should not be here");
        } catch (RuntimeException e) {
            assertIsType(IllegalAccessException.class, e.getCause());
        }
    }

    @Test
    void testNullPointerException() {
        try {
            ThrowHelper.throwNullPointerException();
            fail("should not be here");
        } catch (RuntimeException e) {
            assertIsType(NullPointerException.class, e);
        }
    }

    @Test
    void testOther() {
        try {
            ThrowHelper.throwNoSuchElementException();
        } catch (NoSuchElementException e) {
            assertEquals(SR.NoSuchElement, e.getMessage());
        }

        try {
            ThrowHelper.throwIndexOutOfRangeException();
        } catch (IndexOutOfBoundsException e) {
            assertEquals(SR.Arg_IndexOutOfRangeException, e.getMessage());
        }

        if (Assertion.isEnabled()) {
            assertThrows(AssertionError.class, () -> ThrowHelper.throwArgumentNullException(null));
        } else {
            try {
                ThrowHelper.throwArgumentNullException(null);
                fail("should cause ArgumentException");
            } catch (Throwable e) {
                assertIsType(ArgumentNullException.class, e);
                assertEquals(Empty, ((ArgumentNullException) e).getParamName());
            }
        }
    }
}
