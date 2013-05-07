package mine.emf1002.security;

import mine.emf1002.rbac.model.Department;
import mine.emf1002.rbac.model.EndUser;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUserHolder {
	public static EndUser getCurrentUser(){
		Object o = SecurityContextHolder.getContext().getAuthentication();
        if(null != o) {
            EndUser user = (EndUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user;
        } else {
            EndUser u = new EndUser();
            u.setUsername("GUEST");
            return u;
        }

	}
	public static Department getCurrentDept(){
		Object o = SecurityContextHolder.getContext().getAuthentication();
        if(null != o) {
            EndUser user = (EndUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getDepartment();
        } else {
        	Department d=new Department();
            return d;
        }
	}
}
