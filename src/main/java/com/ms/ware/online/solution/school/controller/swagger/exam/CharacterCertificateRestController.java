/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.exam;

import com.ms.ware.online.solution.school.dao.utility.OrganizationMasterDao;
import com.ms.ware.online.solution.school.entity.exam.CharacterIssue;
import com.ms.ware.online.solution.school.entity.exam.CharacterIssuePK;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/exam/character-certificate")
public class CharacterCertificateRestController {

    @Autowired
    private OrganizationMasterDao masterDao;


    @PostMapping
    public Map<String, Object> get(@RequestBody List<CharacterIssue> list) {
        List<Integer> printed = new ArrayList<>();
        List<Integer> error = new ArrayList<>();
        CharacterIssue obj;
       for(int i=0;i<list.size();i++){
           obj=list.get(i);
            obj.setPk(new CharacterIssuePK(obj.getRegNo(), obj.getExamId()));
            if (masterDao.save(obj) > 0) {
                printed.add(i);
            } else {
                error.add(i);
            }
        }
        Map<String, Object> m = new HashMap<>();
        m.put("printed", printed);
        m.put("error", error);
        return m;
    }
}
