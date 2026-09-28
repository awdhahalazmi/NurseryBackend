package com.iosProject.iosProject.service.child;

import com.iosProject.iosProject.bo.child.Child;
import com.iosProject.iosProject.bo.child.ChildWithCaseDetails;
import com.iosProject.iosProject.entity.CaseEntity;
import com.iosProject.iosProject.entity.ChildEntity;
import com.iosProject.iosProject.entity.UserEntity;
import com.iosProject.iosProject.repository.CaseRepository;
import com.iosProject.iosProject.repository.ChildRepository;
import com.iosProject.iosProject.repository.UserRepository;
import com.iosProject.iosProject.service.user.UserService;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChildServiceImpl implements ChildService {

    private static final Logger log = LoggerFactory.getLogger(ChildServiceImpl.class);

    private final ChildRepository childRepository;

    private final UserRepository userRepository;

    private final CaseRepository caseRepository;

    public ChildServiceImpl(ChildRepository childRepository, UserRepository userRepository, CaseRepository caseRepository) {
        this.childRepository = childRepository;
        this.userRepository = userRepository;
        this.caseRepository = caseRepository;
    }

    @Override
    public void registerChildForUser(Child child, Long userId) {
        log.info("Starting registerChildForUser");
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        CaseEntity caseEntity = caseRepository.findById(child.getCaseId()).orElseThrow(() -> new IllegalArgumentException("Case not found with ID: " + userId));
        ChildEntity childEntity = new ChildEntity();
        childEntity.setName(child.getName());
        childEntity.setAge(child.getAge());
        childEntity.setUserId(user);
        childEntity.setCaseEntity(caseEntity);
        log.info("Calling childRepository save");
        childRepository.save(childEntity);
    }

    @Override
    public ChildWithCaseDetails getChildById(Long childId) {
        log.info("Starting getChildById");
        ChildEntity childEntityFromDB = childRepository.findById(childId).orElse(null);
        ChildWithCaseDetails childWithCaseDetails = new ChildWithCaseDetails();
        if (childEntityFromDB != null) {
            childWithCaseDetails.setChildId(childEntityFromDB.getId());
            childWithCaseDetails.setName(childEntityFromDB.getName());
            childWithCaseDetails.setAge(childEntityFromDB.getAge());
            childWithCaseDetails.setCaseName(childEntityFromDB.getCaseEntity().getName());
            childWithCaseDetails.setCaseId(childEntityFromDB.getCaseEntity().getId());
            childEntityFromDB.setUserId(childEntityFromDB.getUserId());
        }
        log.info("Completed getChildById");
        return childWithCaseDetails;
    }
}
