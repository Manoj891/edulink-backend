/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.billing;

import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.billing.StuBillingMasterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Billing")
public class WavReportRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private StuBillingMasterDao dao;

    @GetMapping("/Wav")
    public Object getRecord(@RequestParam String year, @RequestParam(required = false) Long program) {

        List<Map<String, Object>> list = new ArrayList<>();
        int total, male, female, other, janjati, dalit, madheshi;

        String gender, cast;
        String sql = "SELECT C.`NAME` 'className',P.`NAME` 'program',D.CLASS_ID classId,D.PROGRAM programId,SUM(D.DR) amount FROM stu_billing_master M,stu_billing_detail D,class_master C,program_master P WHERE M.`BILL_NO`=D.`BILL_NO` AND D.`CLASS_ID`=C.`ID` AND D.`PROGRAM`=P.`ID` AND D.ACADEMIC_YEAR=" + year + " AND P.`ID` =IFNULL(" + program + ",P.`ID` ) AND M.`BILL_TYPE`='WAV' GROUP BY D.ACADEMIC_YEAR,D.CLASS_ID,D.PROGRAM";
        for (Map<String, Object> m : dao.getRecord(sql)) {
            sql = "SELECT C.`NAME` 'cast',IFNULL(`GENDER`,'O') gender FROM student_info S,stu_billing_master M,stu_billing_detail D,cast_ethnicity_master C WHERE M.BILL_NO=D.BILL_NO AND S.`ID`=D.`REG_NO` AND S.`CAST_ETHNICITY`=C.`ID` AND D.`ACADEMIC_YEAR`=" + year + " AND D.`CLASS_ID`=" + m.get("classId") + " AND D.`PROGRAM`=" + m.get("programId") + " AND M.`BILL_TYPE`='WAV' GROUP BY S.ID";
            total = 0;
            other = 0;
            male = 0;
            female = 0;
            janjati = 0;
            madheshi = 0;
            dalit = 0;
            for (Map<String, Object> map : dao.getRecord(sql)) {
                total++;
                gender = map.get("gender").toString();
                cast = map.get("cast").toString();
                if (cast.contains("Aadibasi") || cast.contains("Janajati")) {
                    janjati++;
                } else if (cast.equalsIgnoreCase("Madhesi")) {
                    madheshi++;
                } else if (cast.equalsIgnoreCase("dalit")) {
                    dalit++;
                } else {
                    other++;
                }

                if (gender.equalsIgnoreCase("M")) {
                    male++;
                } else if (gender.equalsIgnoreCase("F")) {
                    female++;
                }
            }
            m.put("male", male);
            m.put("female", female);
            m.put("other", other);
            m.put("total", total);
            m.put("madheshi", madheshi);
            m.put("janjati", janjati);
            m.put("dalit", dalit);
            m.put("year", year);
            list.add(m);
        }

        return list;
    }



}
