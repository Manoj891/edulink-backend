package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.dao.utility.OrganizationUserInfoDao;
import com.ms.ware.online.solution.school.dao.utility.OrganizationUserInfoDaoImp;
import com.ms.ware.online.solution.school.entity.utility.MenuUserAccess;
import com.ms.ware.online.solution.school.entity.utility.MenuUserAccessPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author MS
 */
@RestController
@RequestMapping("api/Utility/MenuControl")
public class MenuControlRestController {
    @Autowired
    private DB db;
    @GetMapping
    public Object findMenu() {
       
        String sql = "SELECT `ID` id,`EMP_NAME` name FROM organization_user_info WHERE `USER_TYPE`!='ADM' AND `STATUS`='Y'";
        return db.getRecord(sql);
    }

    @GetMapping("/{userId}")
    public Object findUser(@PathVariable Long userId) {
       
        String sql = "SELECT id AS id,menu AS menu,'N' status," + userId + " userId,menu_item menuItem FROM menu_master WHERE id NOT IN (SELECT MENU FROM menu_user_access WHERE USER_ID=" + userId + ")\n"
                + " UNION \n"
                + " SELECT M.id id,M.menu menu,STATUS status," + userId + " userId,menu_item menuItem FROM menu_master M,menu_user_access U WHERE M.id=U.MENU AND USER_ID=" + userId;
        return db.getRecord(sql);
    }

    @PostMapping
    public Object doSaveUser(@RequestBody List<MenuUserAccess> objList) {
        MenuUserAccess obj;
        OrganizationUserInfoDao dao = new OrganizationUserInfoDaoImp();
        int count = 0;
        for (int i = 0; i < objList.size(); i++) {
            try {
                obj = objList.get(i);
                obj.setPk(new MenuUserAccessPK(obj.getMenu().getId(), obj.getUserId().getId()));
                count += dao.save(obj);
            } catch (Exception e) {

            }
        }

        return new Message().respondWithMessage(count+" Record Saved!!");
    }
}
