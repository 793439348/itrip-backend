package cn.itrip.trade.service;

import cn.itrip.beans.pojo.ItripHotelOrder;
import cn.itrip.beans.pojo.ItripTradeEnds;
import cn.itrip.dao.hotelorder.ItripHotelOrderMapper;
import cn.itrip.dao.tradeends.ItripTradeEndsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("itripOrderService")
public class ItripOrderServiceImpl implements ItripOrderService {
    @Resource
    private ItripHotelOrderMapper itripHotelOrderMapper;
    @Override
    public ItripHotelOrder getItripHotelOrderByOrderNo(String orderNo) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        List<ItripHotelOrder> result = itripHotelOrderMapper.getItripHotelOrderListByMap(params);
        if (result != null && result.size() == 1)
            return result.get(0);
        return null;
    }
}
