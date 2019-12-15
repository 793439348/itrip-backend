package cn.itrip.dao;

import cn.itrip.common.Constants;
import cn.itrip.common.Page;
import cn.itrip.common.PropertiesUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component("baseQuery")
public class BaseQuery<T> {
    private String url = PropertiesUtils.get("database.properties", "baseUrl");
    private HttpSolrClient httpSolrClient;

    public BaseQuery() {
        httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setConnectionTimeout(1000);
        httpSolrClient.setParser(new XMLResponseParser());
    }

    public Page<T> getHotelByPage(SolrQuery query, Integer pageNo, Integer pageSize, Class<T> clazz) throws IOException, SolrServerException {
        if (pageNo == null) pageNo = Constants.DEFAULT_PAGE_NO;
        if (pageSize == null) pageSize = Constants.DEFAULT_PAGE_SIZE;
        // 设置分页
        query.setStart((pageNo - 1) * pageSize);
        query.setRows(pageSize);
        // 执行查询
        QueryResponse response = null;
        response = httpSolrClient.query(query);
        // 获得总页数，构建page对象
        SolrDocumentList result = response.getResults();
        Page page = new Page(pageNo, pageSize, (int) result.getNumFound());
        // 封装查询结果
        page.setRows(response.getBeans(clazz));
        return page;
    }

    public List<T> getTop(SolrQuery query, Integer pageSize, Class<T> clazz) throws IOException, SolrServerException {
        // page == null ? null : page.getRows()
        return this.getHotelByPage(query, 1, pageSize, clazz).getRows();
    }

}
