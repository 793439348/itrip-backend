package cn.itrip.dao.hotelorder;

import cn.itrip.beans.pojo.ItripHotelOrder;
import cn.itrip.beans.pojo.ItripHotelTempStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItripHotelOrderMapper {

	public ItripHotelOrder getItripHotelOrderById(@Param(value = "id") Long id)throws Exception;

	public List<ItripHotelOrder>	getItripHotelOrderListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripHotelOrderCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception;

	public Integer updateItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception;

	public Integer deleteItripHotelOrderById(@Param(value = "id") Long id)throws Exception;

	public void flushStore(Map<String, Object> param) throws Exception;

	public List<ItripHotelTempStore> queryRoomStore(Map<String, Object> param) throws Exception;

	public Integer getPayTypeByHotelId(@Param(value = "hotelId") Long hotelId) throws Exception;

	public Integer flushTempStore(Map<String, Object> param) throws Exception;

	public Integer cancelOrderTimeout() throws Exception;

	public Integer changeOrderToComment() throws Exception;
}
