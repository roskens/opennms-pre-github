package org.opennms.smoketest.expectations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.opennms.smoketest.OpenNMSSeleniumTestCase;

import com.thoughtworks.selenium.Selenium;

public class Expectation {
    public static enum Type {
        OR,
        AND
    }

    private final String m_target;
    private Type m_matchType = Type.OR;
    private List<String> m_textPresent = new ArrayList<>();
    private Long m_waitTime;
    private TimeUnit m_waitUnits;

    public Expectation(final String target) {
        m_target = target;
    }

    public void setMatchType(final Expectation.Type type) {
        m_matchType = type;
    }

    public void setTextPresent(final List<String> textPresent) {
        if (textPresent != m_textPresent) {
            m_textPresent.clear();
            m_textPresent.addAll(textPresent);
        }
    }

    public void addTextPresent(final String text) {
        m_textPresent.add(text);
    }

    public void setWaitTime(final Long time, final TimeUnit units) {
        m_waitTime = time;
        m_waitUnits = units;
    }

    public void check(final Selenium selenium) throws Exception {
        selenium.click(m_target);
        selenium.waitForPageToLoad(String.valueOf(OpenNMSSeleniumTestCase.LOAD_TIMEOUT));

        if (m_waitTime != null) {
            Thread.sleep(m_waitUnits.toMillis(m_waitTime));
        }

        if (m_matchType == Type.OR) {
            for (final String tp : m_textPresent) {
                if (selenium.isTextPresent(tp)) {
                    // we've had a match, return
                    return;
                }
            }
            throw new ExpectationFailed(this);
        } else if (m_matchType == Type.AND) {
            for (final String tp : m_textPresent) {
                if (!selenium.isTextPresent(tp)) {
                    throw new ExpectationFailed(this, "isTextPresent=" + tp);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Expectation [target=" + m_target + ", matchType=" + m_matchType + ", textPresent=" + m_textPresent + "]";
    }
}