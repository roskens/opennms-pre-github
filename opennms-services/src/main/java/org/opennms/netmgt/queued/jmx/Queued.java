package org.opennms.netmgt.queued.jmx;

import org.opennms.netmgt.daemon.AbstractSpringContextJmxServiceDaemon;
import org.opennms.netmgt.rrd.QueuingRrdStrategy;
import org.opennms.netmgt.rrd.RrdUtils;

public class Queued extends AbstractSpringContextJmxServiceDaemon implements
QueuedMBean {

	@Override
	protected String getLoggingPrefix() {
		return "OpenNMS.Queued";
	}

	@Override
	protected String getSpringContext() {
		return "queuedContext";
	}

	public boolean getStatsStatus() {
		if (RrdUtils.getStrategy() instanceof QueuingRrdStrategy) {
			return true;
		} else {
			return false;
		}
	}

	public long getCreatesCompleted() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getCreatesCompleted();
		} else {
			return 0;
		}
	}

	public long getTotalOperationsPending() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getTotalOperationsPending();
		} else {
			return 0;
		}
	}

	public long getSignificantOpsPending() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getSignificantOpsEnqueued() - strategy.getSignificantOpsCompleted();
		} else {
			return 0;
		}
	}

	public long getErrors() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getErrors();
		} else {
			return 0;
		}
	}

	public long getUpdatesCompleted() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getUpdatesCompleted();
		} else {
			return 0;
		}
	}

	public long getPromotionCount() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getPromotionCount();
		} else {
			return 0;
		}
	}
	
	public long getDequeuedItems() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getDequeuedItems();
		} else {
			return 0;
		}
	}

	public long getDequeuedOperations() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getDequeuedOperations();
		} else {
			return 0;
		}
	}

	public long getEnqueuedOperations() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getEnqueuedOperations();
		} else {
			return 0;
		}
	}

	public long getSignificantOpsDequeued() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getSignificantOpsDequeued();
		} else {
			return 0;
		}
	}

	public long getSignificantOpsEnqueued() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getSignificantOpsEnqueued();
		} else {
			return 0;
		}
	}

	public long getSignificantOpsCompleted() {
		if (getStatsStatus()) {
			QueuingRrdStrategy strategy = (QueuingRrdStrategy) RrdUtils.getStrategy();
			return strategy.getSignificantOpsCompleted();
		} else {
			return 0;
		}
	}
		
}
