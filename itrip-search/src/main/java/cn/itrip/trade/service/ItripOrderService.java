package cn.itrip.trade.service;

import cn.itrip.beans.pojo.ItripHotelOrder;

public interface ItripOrderService {
    public ItripHotelOrder getItripHotelOrderByOrderNo(String orderNo) throws Exception;
}
