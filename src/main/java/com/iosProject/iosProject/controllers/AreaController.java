package com.iosProject.iosProject.controllers;

import com.iosProject.iosProject.entity.AreaEntity;
import com.iosProject.iosProject.service.area.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/area")
public class AreaController {

    private static final Logger log = LoggerFactory.getLogger(AreaController.class);

    private final AreaService areaService;

    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/get_areas")
    public ResponseEntity<List<AreaEntity>> getAllAreas() {
        List<AreaEntity> areas = areaService.getAllAreas();
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/admin/add_area")
    public ResponseEntity<AreaEntity> addArea(@RequestBody AreaEntity area) {
        log.info("Starting addArea");
        try {
            log.info("Calling areaService addArea");
            AreaEntity addedArea = areaService.addArea(area);
            return new ResponseEntity<>(addedArea, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Failed addArea", e);
            log.info("Completed addArea");
            return ResponseEntity.badRequest().build();
        }
    }
}
