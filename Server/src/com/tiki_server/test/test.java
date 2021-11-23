package com.tiki_server.test;

import com.tiki_server.util.Get500ProductIds;
import com.tiki_server.util.GetProductDetails;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        // categoryId = 1883,
        String categoryId = "1883";
//        Get500ProductIds a = new Get500ProductIds();
//        a.getget500ProductIds(categoryId);
        GetProductDetails a = new GetProductDetails();
        ArrayList<Long> arrayList = new ArrayList();
        arrayList.add(36461827l);
        for(int i=0;i<arrayList.size();i++)
            a.getProductDetails(arrayList.get(i));
        //a.getProductDetails("53789448");
    }
}