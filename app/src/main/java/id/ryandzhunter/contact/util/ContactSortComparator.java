package id.ryandzhunter.contact.util;

import java.util.Comparator;

import id.ryandzhunter.contact.model.ContactRealm;

/**
 * Created by ryandzhunter on 2/14/18.
 */

public class ContactSortComparator implements Comparator<ContactRealm> {

    @Override
    public int compare(ContactRealm c1, ContactRealm c2) {
        int b1 = c1.getFavorite() ? 1 : 0;
        int b2 = c2.getFavorite() ? 1 : 0;

        int i = b2 - b1;

        if (i != 0) return i;

        return c1.getFirstName().toUpperCase().compareTo(c2.getFirstName().toUpperCase());
    }
}
