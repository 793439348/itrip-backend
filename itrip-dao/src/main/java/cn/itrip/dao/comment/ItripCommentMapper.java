package cn.itrip.dao.comment;

import cn.itrip.beans.pojo.ItripComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItripCommentMapper {
	/**
	 * 根据主键获取
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ItripComment getItripCommentById(@Param(value = "id") Long id)throws Exception;

	/**
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<ItripComment>	getItripCommentListByMap(Map<String, Object> param)throws Exception;

	/**
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer getItripCommentCountByMap(Map<String, Object> param)throws Exception;

	/**
	 *
	 * @param itripComment
	 * @return
	 * @throws Exception
	 */
	public Integer insertItripComment(ItripComment itripComment)throws Exception;

	/**
	 *
	 * @param itripComment
	 * @return
	 * @throws Exception
	 */
	public Integer updateItripComment(ItripComment itripComment)throws Exception;

	/**
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer deleteItripCommentById(@Param(value = "id") Long id)throws Exception;

}
