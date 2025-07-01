package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.entity.utility.OrganizationTeam;
import com.ms.ware.online.solution.school.service.utility.OrganizationTeamService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
public class OrganizationTeamRestController {

    @Autowired
    OrganizationTeamService service;

    @GetMapping("/OrganizationTeam")
    public ResponseEntity indexPublic() {

        return service.getAll();
    }

    @GetMapping("api/Utility/OrganizationTeam")
    public ResponseEntity index() {

        return service.getAll();
    }

    @PostMapping("api/Utility/OrganizationTeam")
    public ResponseEntity doSave(HttpServletRequest request, @RequestParam(required = false) MultipartFile memberPhoto, @ModelAttribute OrganizationTeam obj) throws IOException {
        return service.save(request, memberPhoto, obj);
    }

    @PutMapping("api/Utility/OrganizationTeam/{id}")
    public ResponseEntity doUpdate(HttpServletRequest request, @RequestParam(required = false) MultipartFile memberPhoto, @ModelAttribute OrganizationTeam obj, @PathVariable long id) throws IOException {
        return service.update(request, memberPhoto, obj, id);
    }

    @DeleteMapping("api/Utility/OrganizationTeam/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
