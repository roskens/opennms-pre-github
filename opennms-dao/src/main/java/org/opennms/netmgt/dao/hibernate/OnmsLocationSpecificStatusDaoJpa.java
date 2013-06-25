package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.OnmsLocationSpecificStatusDao;
import org.opennms.netmgt.model.OnmsLocationSpecificStatus;

public class OnmsLocationSpecificStatusDaoJpa extends AbstractDaoHibernate<OnmsLocationSpecificStatus, Integer> implements OnmsLocationSpecificStatusDao {
    public OnmsLocationSpecificStatusDaoJpa() {
        super(OnmsLocationSpecificStatus.class);
    }
}
