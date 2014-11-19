package org.opennms.smoketest.expectations;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.thoughtworks.selenium.Selenium;

public class ExpectationTest {
    private Selenium m_selenium;

    @Before
    public void setUp() {
        m_selenium = Mockito.mock(Selenium.class);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(m_selenium);
    }

    @Test
    public void testTextPresent() throws Exception {
        final Expectation e = new ExpectationBuilder("link=Foo")
            .withText("blah")
            .build();
        when(m_selenium.isTextPresent("blah")).thenReturn(true);
        e.check(m_selenium);
        verify(m_selenium, times(1)).click(anyString());
        verify(m_selenium, times(1)).waitForPageToLoad(anyString());
        verify(m_selenium, times(1)).isTextPresent(anyString());
    }

    @Test(expected=ExpectationFailed.class)
    public void testTextNotPresent() throws Exception {
        final Expectation e = new ExpectationBuilder("link=Foo")
            .withText("blah")
            .build();
        Mockito.when(m_selenium.isTextPresent("blah")).thenReturn(false);
        try {
            e.check(m_selenium);
        } finally {
            verify(m_selenium, times(1)).click(anyString());
            verify(m_selenium, times(1)).waitForPageToLoad(anyString());
            verify(m_selenium, times(1)).isTextPresent(anyString());
        }
    }

    @Test
    public void testMultipleTextOnePresentOr() throws Exception {
        final Expectation e = new ExpectationBuilder("link=Foo")
            .withText("blah")
            .withText("yeargh")
            .build();
        when(m_selenium.isTextPresent("blah")).thenReturn(false);
        when(m_selenium.isTextPresent("yeargh")).thenReturn(true);
        e.check(m_selenium);
        verify(m_selenium, times(1)).click(anyString());
        verify(m_selenium, times(1)).waitForPageToLoad(anyString());
        verify(m_selenium, times(2)).isTextPresent(anyString());
    }

    @Test(expected=ExpectationFailed.class)
    public void testMultipleTextNonePresentOr() throws Exception {
        final Expectation e = new ExpectationBuilder("link=Foo")
            .withText("blah")
            .withText("yeargh")
            .build();
        when(m_selenium.isTextPresent("blah")).thenReturn(false);
        when(m_selenium.isTextPresent("yeargh")).thenReturn(false);
        try {
            e.check(m_selenium);
        } finally {
            verify(m_selenium, times(1)).click(anyString());
            verify(m_selenium, times(1)).waitForPageToLoad(anyString());
            verify(m_selenium, times(2)).isTextPresent(anyString());
        }
    }

    @Test
    public void testMultipleTextAllPresentOr() throws Exception {
        final Expectation e = new ExpectationBuilder("link=Foo")
            .withText("blah")
            .withText("yeargh")
            .build();
        when(m_selenium.isTextPresent("blah")).thenReturn(true);
        when(m_selenium.isTextPresent("yeargh")).thenReturn(true);
        e.check(m_selenium);
        verify(m_selenium, times(1)).click(anyString());
        verify(m_selenium, times(1)).waitForPageToLoad(anyString());
        verify(m_selenium, times(1)).isTextPresent(anyString());
    }

    @Test(expected=ExpectationFailed.class)
    public void testMultipleTextOnePresentAnd() throws Exception {
        final Expectation e = new ExpectationBuilder("link=Foo")
            .and()
            .withText("blah")
            .withText("yeargh")
            .build();
        when(m_selenium.isTextPresent("blah")).thenReturn(true);
        when(m_selenium.isTextPresent("yeargh")).thenReturn(false);
        try {
            e.check(m_selenium);
        } finally {
            verify(m_selenium, times(1)).click(anyString());
            verify(m_selenium, times(1)).waitForPageToLoad(anyString());
            verify(m_selenium, times(2)).isTextPresent(anyString());
        }
    }

    @Test(expected=ExpectationFailed.class)
    public void testMultipleTextNonePresentAnd() throws Exception {
        final Expectation e = new ExpectationBuilder("link=Foo")
            .and()
            .withText("blah")
            .withText("yeargh")
            .build();
        when(m_selenium.isTextPresent("blah")).thenReturn(false);
        when(m_selenium.isTextPresent("yeargh")).thenReturn(false);
        try {
            e.check(m_selenium);
        } finally {
            verify(m_selenium, times(1)).click(anyString());
            verify(m_selenium, times(1)).waitForPageToLoad(anyString());
            verify(m_selenium, times(1)).isTextPresent(anyString());
        }
    }

    @Test()
    public void testMultipleTextAllPresentAnd() throws Exception {
        final Expectation e = new ExpectationBuilder("link=Foo")
            .and()
            .withText("blah")
            .withText("yeargh")
            .build();
        when(m_selenium.isTextPresent("blah")).thenReturn(true);
        when(m_selenium.isTextPresent("yeargh")).thenReturn(true);
        e.check(m_selenium);
        verify(m_selenium, times(1)).click(anyString());
        verify(m_selenium, times(1)).waitForPageToLoad(anyString());
        verify(m_selenium, times(2)).isTextPresent(anyString());
    }
}
