package com.so.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper {

	<T> List<T> selectTags(Map<String, Object> param);
	
	int deletePostsTagsByPostId(Map<String,Object> param);
}
