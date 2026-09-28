package com.iosProject.iosProject.service.childCase;

import com.iosProject.iosProject.entity.CaseEntity;
import com.iosProject.iosProject.repository.AreaRepository;
import com.iosProject.iosProject.repository.CaseRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CaseServiceImpl implements CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseServiceImpl.class);

    private final CaseRepository caseRepository;

    public CaseServiceImpl(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @Override
    public List<CaseEntity> getAllCases() {
        return caseRepository.findAll();
    }

    @Override
    public CaseEntity addCase(CaseEntity childCase) {
        log.info("Starting addCase");
        if (childCase.getName() == null || childCase.getName().isEmpty()) {
            throw new IllegalArgumentException("Area name cannot be empty");
        }
        log.info("Calling caseRepository save");
        log.info("Completed addCase");
        return caseRepository.save(childCase);
    }
}
