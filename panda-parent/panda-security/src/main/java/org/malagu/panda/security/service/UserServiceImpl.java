package org.malagu.panda.security.service;

import org.malagu.linq.JpaUtil;
import org.malagu.panda.security.orm.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2017年6月6日
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    @Transactional(readOnly = true)
    public boolean isAdministrator(String username) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            if (((User) principal).getUsername().equals(username)) {
                return ((User) principal).isAdministrator();
            }
            if (username == null) {
                return ((User) principal).isAdministrator();
            }
        }
        User user = JpaUtil.linq(User.class).idEqual(username).findOne();
        return user.isAdministrator();
    }

    @Override
    public boolean isAdministrator() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).isAdministrator();
        }
        return false;
    }

}
