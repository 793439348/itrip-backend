package cn.itrip.service;

import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotCityVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.BaseQuery;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("hotelSearchService")
public class HotelSearchServiceImpl implements HotelSearchService {
    @Resource
    private BaseQuery<ItripHotelVO> baseQuery;

    @Override
    public Page<ItripHotelVO> getHotelForPage(SearchHotelVO searchHotelVO) throws Exception {
        SolrQuery query = new SolrQuery("*:*");
        // 构建全文检索 q条件
        StringBuilder builder = new StringBuilder("");
        builder.append(" destination:"+searchHotelVO.getDestination());
        if (EmptyUtils.isNotEmpty(searchHotelVO.getKeywords())) {
            // xxxccc -> xxx|ccc
            searchHotelVO.setKeywords(searchHotelVO.getKeywords().replace(' ', '|'));
            builder.append(" AND keyword:"+searchHotelVO.getKeywords());
        }
        // 构建过滤查询 fq条件
        // 商圈条件(a OR b OR c) AND
        if (EmptyUtils.isNotEmpty(searchHotelVO.getTradeAreaIds())) {
            // 123,334,234
            // ( tradingAreaIds:*,123,* OR tradingAreaIds:*,334,* OR tradingAreaIds:*,234,* )
            StringBuilder builder1 = new StringBuilder("(");
            String[] ids = searchHotelVO.getTradeAreaIds().split(",");
            boolean isFirst = true;
            for (String id : ids) {
                if (isFirst) {
                    builder1.append(" tradingAreaIds:*," + id + ",*");
                    isFirst = false;
                } else
                    builder1.append(" OR tradingAreaIds:*,"+id+",*");
            }
            builder1.append(")");
            query.addFilterQuery(builder1.toString());
        }
        // 我：1000《 2000
        // a: 3000 - 10000
        // b: 200 - 2000 - 3000
        // c: 400 - 1500 - 2000
        if (EmptyUtils.isNotEmpty(searchHotelVO.getMaxPrice())) {
            query.addFilterQuery(" minPrice:[* TO "+searchHotelVO.getMaxPrice()+"]");
        }if (EmptyUtils.isNotEmpty(searchHotelVO.getMinPrice())) {
            query.addFilterQuery(" minPrice:["+searchHotelVO.getMinPrice()+" TO *]");
        }
        // 星级
        if (EmptyUtils.isNotEmpty(searchHotelVO.getHotelLevel())) {
            query.addFilterQuery(" hotelLevel:" + searchHotelVO.getHotelLevel());
        }
        // 功能
        if (EmptyUtils.isNotEmpty(searchHotelVO.getFeatureIds())) {
            // 123,334,234
            // ( featureIds:*,123,* OR featureIds:*,334,* OR featureIds:*,234,* )
            StringBuilder builder1 = new StringBuilder("(");
            String[] ids = searchHotelVO.getFeatureIds().split(",");
            boolean isFirst = true;
            for (String id : ids) {
                if (isFirst) {
                    builder1.append(" featureIds:*," + id + ",*");
                    isFirst = false;
                } else
                    builder1.append(" OR featureIds:*,"+id+",*");
            }
            builder1.append(")");
            query.addFilterQuery(builder1.toString());
        }
        // 设置 q 查询条件
        query.setQuery(builder.toString());
        if (EmptyUtils.isNotEmpty(searchHotelVO.getAscSort())) {
            query.addSort(searchHotelVO.getAscSort(), SolrQuery.ORDER.asc);
        }
        if (EmptyUtils.isNotEmpty(searchHotelVO.getDescSort())) {
            query.addSort(searchHotelVO.getDescSort(), SolrQuery.ORDER.desc);
        }
        return baseQuery.getHotelByPage(query, searchHotelVO.getPageNo(),
                searchHotelVO.getPageSize(), ItripHotelVO.class);
    }

    @Override
    public List<ItripHotelVO> getTopHotelByCityId(SearchHotCityVO searchHotCityVO) throws Exception {
        SolrQuery query = new SolrQuery("*:*");
        query.addFilterQuery("cityId:" + searchHotCityVO.getCityId());
        return baseQuery.getTop(query, searchHotCityVO.getCount(), ItripHotelVO.class);
    }


}
