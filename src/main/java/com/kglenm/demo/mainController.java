package com.kglenm.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
public class mainController {

    public HashMap fromThreeColumns(String[] tableData){
        HashMap area_map = new HashMap();
        int count = 0;
        String tempTech = "", tempReason = "";
        for (int y = 1; y < tableData.length; y++) {
            if (count == 0) {
                tempTech = tableData[y].replaceAll("</td>", "");
                count++;
            } else if (count == 1) {
                tempReason = tableData[y].replaceAll("</td>", "");
                count++;
            } else {
                area_map.put(tempTech, tempReason);
                count = 0;
            }
        }
        return area_map;
    }

    public HashMap fromFourColumns(String[] tableData){
        HashMap area_map = new HashMap();
        int count = 0;
        String tempTech = "", tempReason = "";
        for (int y = 1; y < tableData.length; y++) {
            if (count == 0) {
                tempTech = tableData[y].replaceAll("</td>", "");
                System.out.println(y+" "+tableData[y].replaceAll("</td>", ""));
                count++;
            } else if (count == 1) {
                //System.out.println(y+" "+tableData[y].replaceAll("</td>", ""));
                count++;
            } else if (count == 2){
                tempReason = tableData[y].replaceAll("</td>", "");
                //System.out.println(y+" "+tableData[y].replaceAll("</td>", ""));
                count ++;
            } else {
                area_map.put(tempTech, tempReason);
                count = 0;
            }
        }
        return area_map;
    }

    public HashMap getTables(String content){
        String[] areas = {"Programming Stack", "Build Stack", "Infrastructure"};
        HashMap entire_map = new HashMap();
        String ns = content.replaceAll("[\r\n]+", " ").replaceAll("<tr>", "").replaceAll("</tr>", "").replaceAll("<div>", "").replaceAll("</div>", "");
        String[] table = ns.split("<table>");

        for(int x = 1; x<table.length-1; x++){
            String[] tableData = table[x].split("<td>");

            if (x != 2) {
                entire_map.put(areas[x-1], fromThreeColumns(tableData));
            }else{
                entire_map.put(areas[x-1], fromFourColumns(tableData));
            }

        }
        return entire_map;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HashMap getFromUrl(){
        try {
            URL url = new URL("https://github.com/egis/handbook/blob/master/Tech-Stack.md");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            //in.close();
            String content = in.lines().collect(Collectors.joining());
            in.close();
            return getTables(content);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
