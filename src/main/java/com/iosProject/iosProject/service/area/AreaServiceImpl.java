package com.iosProject.iosProject.service.area;

import com.iosProject.iosProject.entity.AreaEntity;
import com.iosProject.iosProject.repository.AreaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AreaServiceImpl implements AreaService {

    private static final Logger log = LoggerFactory.getLogger(AreaServiceImpl.class);

    private final AreaRepository areaRepository;

    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    public List<AreaEntity> getAllAreas() {
        return areaRepository.findAll();
    }

    @Override
    public AreaEntity addArea(AreaEntity area) {
        log.info("Starting addArea");
        if (area.getName() == null || area.getName().isEmpty()) {
            throw new IllegalArgumentException("Area name cannot be empty");
        }
        log.info("Calling areaRepository save");
        log.info("Completed addArea");
        return areaRepository.save(area);
    }
}
