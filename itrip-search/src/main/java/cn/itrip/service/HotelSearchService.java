package cn.itrip.service;

import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotCityVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.Page;

import java.util.List;

public interface HotelSearchService {
    Page<ItripHotelVO> getHotelForPage(SearchHotelVO searchHotelVO) throws Exception;
    List<ItripHotelVO> getTopHotelByCityId(SearchHotCityVO searchHotCityVO) throws Exception;
}
