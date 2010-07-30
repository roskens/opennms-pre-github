package org.opennms.acl.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.opennms.netmgt.model.OnmsAuthority;

public class AuthoritiesNodeHelperTest {

    @Test
    public void constructor() {
        OnmsAuthority authority = createAuthority();

        Set<Integer> items = new HashSet<Integer>();
        items.add(1);

        authority.setItems(items);

        Set<OnmsAuthority> authorities = new HashSet<OnmsAuthority>();
        authorities.add(authority);

        AuthoritiesCategoriesHelper helper = new AuthoritiesCategoriesHelper(authorities);
        assertNotNull(helper);
    }

    @Test
    public void getAuthoritiesItems() {

        OnmsAuthority authority = createAuthority();

        Set<Integer> items = new HashSet<Integer>();
        items.add(1);

        authority.setItems(items);

        Set<OnmsAuthority> authorities = new HashSet<OnmsAuthority>();
        authorities.add(authority);

        AuthoritiesCategoriesHelper helper = new AuthoritiesCategoriesHelper(authorities);
        assertNotNull(helper);
        Set<OnmsAuthority> auths = new HashSet<OnmsAuthority>();
        auths.add(authority);
        
        Set<Integer> itemsRetrieve = helper.getAuthoritiesItems(authorities);
        assertTrue(itemsRetrieve.size() == 1);

    }

    @Test
    public void getAuthoritiesItemsNoDuplicated() {

        OnmsAuthority authority = createAuthority();
        Set<Integer> items = new HashSet<Integer>();
        items.add(1);
        items.add(2);
        items.add(3);

        authority.setItems(items);

        Set<OnmsAuthority> authorities = new HashSet<OnmsAuthority>();
        authorities.add(authority);
        authorities.add(authority);

        AuthoritiesCategoriesHelper helper = new AuthoritiesCategoriesHelper(authorities);
        assertNotNull(helper);
        
        Set<OnmsAuthority> auths = new HashSet<OnmsAuthority>();
        auths.add(authority);

        Set<Integer> itemsRetrieve = helper.getAuthoritiesItems(authorities);
        assertTrue(itemsRetrieve.size() == 3);
    }

    @Test
    public void deleteItems() {

        OnmsAuthority authority = createAuthority();

        Set<Integer> items = new HashSet<Integer>();
        items.add(1);
        items.add(2);
        items.add(3);
        authority.setItems(items);
        
        OnmsAuthority admin = new OnmsAuthority();
        authority.setDescription("this is a description");
        authority.setId(13);
        authority.setName("ROLE_ADMIN");
        admin.setItems(items);

        Set<OnmsAuthority> authorities = new HashSet<OnmsAuthority>();
        authorities.add(authority);
        authorities.add(admin);

        AuthoritiesCategoriesHelper helper = new AuthoritiesCategoriesHelper(authorities);
        assertNotNull(helper);
        Set<OnmsAuthority> auths = new HashSet<OnmsAuthority>();
        auths.add(authority);

        
        Set<Integer> itemsRetrieve = helper.getAuthoritiesItems(authorities);
        
        assertTrue(itemsRetrieve.size() == 3);

        Set<OnmsAuthority> authoritiesView = new HashSet<OnmsAuthority>();
        authoritiesView.add(authority);

        assertTrue(helper.deleteAuthority(authority.getName()));
        assertTrue(helper.getAuthoritiesItems(authoritiesView).size() == 0);
        
        authoritiesView.add(admin);
        assertTrue(helper.getAuthoritiesItems(authoritiesView).size() == 3);

    }

    private OnmsAuthority createAuthority() {
        OnmsAuthority authority = new OnmsAuthority();
        authority.setDescription("this is a description");
        authority.setId(12);
        authority.setName("ROLE_USER");
        return authority;
    }

}
