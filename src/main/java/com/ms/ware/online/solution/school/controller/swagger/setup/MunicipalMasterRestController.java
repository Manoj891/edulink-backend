package com.ms.ware.online.solution.school.controller.swagger.setup;

import com.ms.ware.online.solution.school.entity.setup.MunicipalMaster;
import com.ms.ware.online.solution.school.service.setup.MunicipalMasterService;
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
@RequestMapping("api/Setup/MunicipalMaster")
public class MunicipalMasterRestController {

    @Autowired
    MunicipalMasterService service;

    @GetMapping
    public Object index(@RequestParam String district) {

        return service.getAll(district);
    }

    @PostMapping
    public Object doSave(@RequestBody MunicipalMaster obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody MunicipalMaster obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
