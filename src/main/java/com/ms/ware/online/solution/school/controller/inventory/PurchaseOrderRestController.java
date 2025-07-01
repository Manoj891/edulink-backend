package com.ms.ware.online.solution.school.controller.inventory;

import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrder;
import com.ms.ware.online.solution.school.service.inventory.PurchaseOrderService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Inventory/PurchaseOrder")
public class PurchaseOrderRestController {

    @Autowired
    PurchaseOrderService service;

    @GetMapping
    public Object index() {
        return service.getAll();
    }

    @GetMapping("/Report")
    public Object indexReport(@RequestParam(required = false) Long sundryCreditors, @RequestParam String dateFrom, @RequestParam String dateTo) {
        return service.getAllReport(sundryCreditors, dateFrom, dateTo);
    }

    @GetMapping("/{orderNo}")
    public Object index(@PathVariable String orderNo) {
        return service.getAll(orderNo);
    }

    @PostMapping
    public Object doSave(@RequestBody PurchaseOrder obj) throws IOException {
        return service.save(obj);
    }

    @PostMapping("/Opening")
    public Object doOpening(@RequestBody PurchaseOrder obj) throws IOException {
        return service.opening(obj);
    }

    @PutMapping
    public Object doUpdate(@RequestBody String jsonData) throws IOException {
        return service.update(jsonData);
    }

    @PatchMapping
    public Object doReject(@RequestBody String jsonData) throws IOException {
        return service.doReject(jsonData);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
