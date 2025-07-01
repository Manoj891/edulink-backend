package com.ms.ware.online.solution.school.controller.swagger.setup;

import com.ms.ware.online.solution.school.entity.setup.DistrictMaster;
import com.ms.ware.online.solution.school.service.setup.DistrictMasterService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Setup/DistrictMaster")
public class DistrictMasterRestController {

    @Autowired
    DistrictMasterService service;

    @GetMapping
    public Object index(@RequestParam String province) {
        return service.getAll(province);
    }

    @PostMapping
    public Object doSave(@RequestBody DistrictMaster obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody DistrictMaster obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
