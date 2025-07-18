package com.ms.ware.online.solution.school.model;

import lombok.Getter;
import lombok.Setter;


public class DatabaseName {

    @Getter
    @Setter
//    private static String database = "school_of_scholars";
//    -------------------------------------------
//   private static String database = "school_king_demo";
//   private static String database = "sidhhababa_plustwo_campus";
   private static String database = "bhanubhakta";
//   private static String database = "rrm_campus";
//   private static String database = "gaushala_mother_teresa";
//   private static String database = "janakpur_mother_teresa";
//   private static String database = "kamalamai_multiple_college";
//   private static String database = "bhimmavi";
//   private static String database = "gaurishankar_dolakha_plustwo";
//   private static String database = "shree_krishna_ratna_ganga";
//   private static String database = "chaitanya_ncollege";
//   private static String database = "chaitanya_nplustwo";
//   private static String database = "bardibas_siddhartha";
    @Getter
    @Setter
    private static String port = "3307";
    @Getter
    private static String documentUrl = "";
    @Getter
    private final static String username = "schoolking";
    @Getter
    private final static String password = "SchoolKing@123";

    public static void setDocumentUrl(String url) {
        documentUrl = "/" + url;
    }
}
