package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotCityVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.service.HotelSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/hotellist")
public class HotelListController{
    @Resource
    private HotelSearchService hotelSearchService;
    private Logger logger = LoggerFactory.getLogger(HotelListController.class);

    @RequestMapping(value = "/searchItripHotelPage", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<Page<ItripHotelVO>> searchItripHotelPage(@RequestBody SearchHotelVO searchHotelVO) throws  Exception {
        if (EmptyUtils.isEmpty(searchHotelVO) || EmptyUtils.isEmpty(searchHotelVO.getDestination()))
            return DtoUtil.returnFail("目的地不能为空", "20002");
        // 业务拼solrj查询
        try {
            Page<ItripHotelVO> page = hotelSearchService.getHotelForPage(searchHotelVO);
            return DtoUtil.returnDataSuccess(page);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error("查询酒店分页异常", e);
            return DtoUtil.returnFail("系统异常,获取失败", "20001");
        }

    }
    @RequestMapping(value = "/searchItripHotelListByHotCity", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<List<ItripHotelVO>> searchItripHotelListByHotCity(@RequestBody SearchHotCityVO searchHotCityVO) throws Exception {
        if (EmptyUtils.isEmpty(searchHotCityVO) || EmptyUtils.isEmpty(searchHotCityVO.getCityId()))
            return DtoUtil.returnFail("城市id不能为空", "20004");
        try {
            List<ItripHotelVO> result = hotelSearchService.getTopHotelByCityId(searchHotCityVO);
            return DtoUtil.returnDataSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询热门城市酒店异常", e);
            return DtoUtil.returnFail("系统异常,获取失败", "20003");
        }

    }
}
