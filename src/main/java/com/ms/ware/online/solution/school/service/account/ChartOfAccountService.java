package com.ms.ware.online.solution.school.service.account;

import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;

import java.util.List;
import java.util.Map;

public interface ChartOfAccountService {

    Object getAll();

    Object getAll(String mgrCode);

    Object getByname(String name);

    Object save(ChartOfAccount obj);

    Object update(ChartOfAccount obj);

    Object delete(String id);

    Object getInventoryByname(String name);

    Object getInventoryFixedAssetByname(String name);

    Object getInventoryFixedAssetIssueByname(String name);

    List<Map<String, Object>> groupAccount();
}
