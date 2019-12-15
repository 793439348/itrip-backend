package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripAreaDic;
import cn.itrip.beans.pojo.ItripImage;
import cn.itrip.beans.vo.ItripAreaDicVO;
import cn.itrip.beans.vo.ItripImageVO;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.service.areadic.ItripAreaDicService;
import cn.itrip.service.hotel.ItripHotelService;
import cn.itrip.service.image.ItripImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/hotel")
public class HotelController {
    @Resource
    private ItripAreaDicService itripAreaDicService;
    @Resource
    private ItripImageService itripImageService;
    @Resource
    private ItripHotelService itripHotelService;

    /*
    * 查询 国内/ 国外热门城市
    * */
    @RequestMapping(value = "/queryhotcity/{type}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<ItripAreaDicVO> queryHotCity(@PathVariable Integer type) throws Exception {
        if (type == null || type == 0)
            return DtoUtil.returnFail("type不能为空", "10201");
        try {
            Map<String, Object> param  = new HashMap<String, Object>();
            param.put("isHot", 1);
            param.put("isChina", type);
            List<ItripAreaDic> result = itripAreaDicService.getItripAreaDicListByMap(param);
            List<ItripAreaDicVO> output = new ArrayList<ItripAreaDicVO>();
            if (EmptyUtils.isNotEmpty(result)) {
                ItripAreaDicVO itripAreaDicVO = null;
                for (ItripAreaDic itripAreaDic : result) {
                    itripAreaDicVO = new ItripAreaDicVO();
                    BeanUtils.copyProperties(itripAreaDic, itripAreaDicVO);
                    output.add(itripAreaDicVO);
                }
            }
            return DtoUtil.returnDataSuccess(output);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10202");
        }
    }

    /*
    * 查询某个城市的商圈
    * */
    @RequestMapping(value = "/querytradearea/{cityId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<ItripAreaDicVO> queryTradeArea(@PathVariable Integer cityId) throws Exception {
        if (cityId == null || cityId == 0)
            return DtoUtil.returnFail("cityId不能为空", "10203");
        try {
            Map<String, Object> param  = new HashMap<String, Object>();
            param.put("isTradingArea", 1);
            param.put("parent", cityId);
            List<ItripAreaDic> result = itripAreaDicService.getItripAreaDicListByMap(param);
            List<ItripAreaDicVO> output = new ArrayList<ItripAreaDicVO>();
            if (EmptyUtils.isNotEmpty(result)) {
                ItripAreaDicVO itripAreaDicVO = null;
                for (ItripAreaDic itripAreaDic : result) {
                    itripAreaDicVO = new ItripAreaDicVO();
                    BeanUtils.copyProperties(itripAreaDic, itripAreaDicVO);
                    output.add(itripAreaDicVO);
                }
            }
            return DtoUtil.returnDataSuccess(output);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10204");
        }
    }

    /*
    * 查询某个酒店的图片
    * */
    @RequestMapping(value = "/getimg/{targetId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<ItripImageVO> getImgForHotel(@PathVariable String targetId) throws Exception {
        if (EmptyUtils.isEmpty(targetId))
            return DtoUtil.returnFail("酒店id不能为空", "100213");
        try {
            Map<String, Object> param  = new HashMap<String, Object>();
            param.put("type", "0");
            param.put("targetId", Long.valueOf(targetId));
            List<ItripImage> result = itripImageService.getItripImageListByMap(param);
            List<ItripImageVO> output = new ArrayList<>();
            if (EmptyUtils.isNotEmpty(result)) {
                ItripImageVO vo = null;
                for (ItripImage img : result) {
                    vo = new ItripImageVO();
                    BeanUtils.copyProperties(img, vo);
                    output.add(vo);
                }
            }
            return DtoUtil.returnDataSuccess(output);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "100212");
        }
    }

    /*
    * 查询某个酒店的视频描述详情
    * */
    @RequestMapping(value = "/getvideodesc/{hotelId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Dto<HotelVideoDescVO> getVideoDesc(@PathVariable String hotelId) throws Exception {
        if (EmptyUtils.isEmpty(hotelId))
            return DtoUtil.returnFail("酒店id不能为空", "100215");
        try {
            HotelVideoDescVO result = itripHotelService.getVideoDescByHotel(Long.valueOf(hotelId));
            return DtoUtil.returnDataSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取酒店视频文字描述失败", "100214");
        }
    }
}
