package com.li.jwt.util;

import com.li.jwt.config.jwt.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

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
