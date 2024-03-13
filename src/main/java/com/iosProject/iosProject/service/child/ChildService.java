package com.iosProject.iosProject.service.child;

import com.iosProject.iosProject.bo.child.Child;
import com.iosProject.iosProject.bo.child.ChildWithCaseDetails;

public interface ChildService {

    void registerChildForUser(Child child, Long userId);
    ChildWithCaseDetails getChildById(Long childId);

}
