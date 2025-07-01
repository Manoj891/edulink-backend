package com.ms.ware.online.solution.school.excel;

import com.ms.ware.online.solution.school.config.DB;

import java.io.FileReader;
import java.io.IOException;

public class MarkRead {
    public static void main(String[] args) throws IOException {
        DB db = new DB();
        db.delete("delete from gipss_ghorahi.mark_insert");
        FileReader in = new FileReader("C:\\Users\\DELL VOSTRO 3400\\Desktop\\ghorahi.sql");
        String sql = "";
        char c;
        while (in.ready()) {
            c = (char) in.read();
            if (c == '\n') {
                db.save(sql);
                sql = "";
            } else {
                sql += c;
            }
        }
    }
}
