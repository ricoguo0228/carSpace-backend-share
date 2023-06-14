package com.example.carspacesdemo.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CarSpacesUtils {
    public static Map<Date, Date> getSlots(Date startTime, Date endTime,Map<Date,Date> reserveSlots){
        Map<Date, Date> slots = new HashMap<Date, Date>();
        for(Date left : reserveSlots.keySet()){
            if(left.after(startTime) && left.before(endTime)){
                slots.put(startTime, left);
            }
            if(reserveSlots.get(left).after(startTime) && reserveSlots.get(left).before(endTime)){
                slots.put(reserveSlots.get(left), endTime);
            }
        }
        return slots;
    }
}
