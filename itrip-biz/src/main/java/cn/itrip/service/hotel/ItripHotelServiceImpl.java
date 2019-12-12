package cn.itrip.service.hotel;

import cn.itrip.beans.pojo.ItripAreaDic;
import cn.itrip.beans.pojo.ItripHotel;
import cn.itrip.beans.pojo.ItripLabelDic;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.areadic.ItripAreaDicMapper;
import cn.itrip.dao.hotel.ItripHotelMapper;
import cn.itrip.dao.labeldic.ItripLabelDicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripHotelServiceImpl implements ItripHotelService {

    @Resource
    private ItripHotelMapper itripHotelMapper;
    @Resource
    private ItripAreaDicMapper itripAreaDicMapper;
    @Resource
    private ItripLabelDicMapper itripLabelDicMapper;

    public ItripHotel getItripHotelById(Long id)throws Exception{
        return itripHotelMapper.getItripHotelById(id);
    }

    public List<ItripHotel>	getItripHotelListByMap(Map<String,Object> param)throws Exception{
        return itripHotelMapper.getItripHotelListByMap(param);
    }

    public Integer getItripHotelCountByMap(Map<String,Object> param)throws Exception{
        return itripHotelMapper.getItripHotelCountByMap(param);
    }

    public Integer itriptxAddItripHotel(ItripHotel itripHotel)throws Exception{
            itripHotel.setCreationDate(new Date());
            return itripHotelMapper.insertItripHotel(itripHotel);
    }

    public Integer itriptxModifyItripHotel(ItripHotel itripHotel)throws Exception{
        itripHotel.setModifyDate(new Date());
        return itripHotelMapper.updateItripHotel(itripHotel);
    }

    public Integer itriptxDeleteItripHotelById(Long id)throws Exception{
        return itripHotelMapper.deleteItripHotelById(id);
    }

    public Page<ItripHotel> queryItripHotelPageByMap(Map<String,Object> param, Integer pageNo, Integer pageSize)throws Exception{
        Integer total = itripHotelMapper.getItripHotelCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripHotel> itripHotelList = itripHotelMapper.getItripHotelListByMap(param);
        page.setRows(itripHotelList);
        return page;
    }

    @Override
    public HotelVideoDescVO getVideoDescByHotel(Long hotelId) throws Exception {
        HotelVideoDescVO hotelVideoDescVO = new HotelVideoDescVO();
        // 查询酒店名
        String name = itripHotelMapper.getItripHotelById(hotelId).getHotelName();
        hotelVideoDescVO.setHotelName(name);
        // 查询酒店商圈（多个）
        List<ItripAreaDic> result = itripAreaDicMapper.getItripAreaDicByHotelId(hotelId);
        hotelVideoDescVO.setTradingAreaNameList(new ArrayList<String>());
        if (EmptyUtils.isNotEmpty(result)) {
            for (ItripAreaDic dic : result)
                hotelVideoDescVO.getTradingAreaNameList().add(dic.getName());
        }
        // 查询酒店功能描述（多个）
        List<ItripLabelDic> labels = itripLabelDicMapper.getLabelDicByHotelId(hotelId);
        hotelVideoDescVO.setHotelFeatureList(new ArrayList<String>());
        if (EmptyUtils.isNotEmpty(labels)) {
            for (ItripLabelDic label : labels)
                hotelVideoDescVO.getHotelFeatureList().add(label.getName());
        }

        return hotelVideoDescVO;
    }

}
