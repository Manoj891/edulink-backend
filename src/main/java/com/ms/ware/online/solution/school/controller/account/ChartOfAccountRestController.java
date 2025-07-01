package com.ms.ware.online.solution.school.controller.account;

import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;
import com.ms.ware.online.solution.school.service.account.ChartOfAccountService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/Account/ChartOfAccount")
public class ChartOfAccountRestController {

    @Autowired
    ChartOfAccountService service;

    @GetMapping
    public Object index() {
        return service.getAll();
    }

    @GetMapping("/Byname")
    public Object indexByname(@RequestParam String name) {
        return service.getByname(name);
    }

    @GetMapping("/InventoryByname")
    public Object indexInventoryByname(@RequestParam String name) {
        return service.getInventoryByname(name);
    }

    @GetMapping("/InventoryFixedAssetByname")
    public Object indexInventoryFixedAssetByname(@RequestParam String name) {
        return service.getInventoryFixedAssetByname(name);
    }

    @GetMapping("/InventoryFixedAssetIssueByname")
    public Object indexInventoryFixedAssetIssueByname(@RequestParam String name) {
        return service.getInventoryFixedAssetIssueByname(name);
    }

    @GetMapping("/group-account")
    public ResponseEntity<List<Map<String, Object>>> groupAccount() {
        return ResponseEntity.status(HttpStatus.OK).body(service.groupAccount());
    }

    @GetMapping("/{mgrCode}")
    public Object index(@PathVariable String mgrCode) {
        return service.getAll(mgrCode);
    }

    @PostMapping
    public Object doSave(@RequestBody ChartOfAccount obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody ChartOfAccount obj) throws IOException {
        return service.update(obj);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
