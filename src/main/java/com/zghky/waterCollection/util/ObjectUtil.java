package com.zghky.waterCollection.util;

import com.zghky.waterCollection.config.jwt.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ObjectUtil {

    public static Integer getUserId(HttpServletRequest request, JwtTokenUtil jwtTokenUtil) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        return jwtTokenUtil.getUserIdFromToken(jwtToken);
    }

    public static String getYearMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }
}
