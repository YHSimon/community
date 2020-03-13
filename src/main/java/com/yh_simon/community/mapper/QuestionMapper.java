package com.yh_simon.community.mapper;

import com.yh_simon.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{index},#{limit}")
    List<Question> findAllQuestions(int index, int limit);

    @Select("select count(1) from question")
    int count();

    @Select("select count(1) from question where creator=#{id}")
    int countByUserId(Integer id);

    @Select("select * from question where creator=#{id} limit #{index},#{limit}")
    List<Question> findAllQuestionsByUserId(Integer id, int index, Integer limit);


    @Select("select * from question where id=#{id}")
    Question getById(@Param("id") Integer id);


    @Update("update question set title=#{title} ,description=#{description},tag=#{tag} where id=#{id}")
    int update(Question question);


    @Update("update question set view_count=view_count+1 where id=#{id}")
    void incView(@Param("id")Integer id);
}
