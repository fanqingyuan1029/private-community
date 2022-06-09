package com.fqy.crm.dao.building;

import com.fqy.crm.entity.building.Building;
import com.fqy.crm.form.BuildingAddForm;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BuildingDao {

    // select()
    /**
     * 查询所有的 building 信息
     * @return  返回一个 List 队列
     */
    @Select("select * from tb_building")
    List<Building> selectAll();

    /**
     * 根据楼栋的 id 查询该楼栋的信息
     * @param id 楼栋的 id 号
     * @return  返回一个 Buiding 对象
     */
    @Select("select * from tb_building where id =#{id}")
    Building selectById(@Param("id") Integer id);

    /**
     * 根据楼栋的名字，查询楼栋的信息
     * @param name 楼栋的名字
     * @return 返回一个 Building 对象
     */
    @Select("select * from tb_building where name = #{name}")
    Building selectByName(@Param("name") String name);

    // insert()
//    /**
//     * 增加楼栋操作
//     * @param building  将楼栋信息封装至 Building 对象中
//     * @return  返回一个 int 类型的的值：1为插入成功，0为插入失败
//     */
//    @Insert("insert into tb_building (name,detail) values (#{building.name},#{building.detail})")
//    int insertBuilding(@Param("building") Building building);

    /**
     * 增加楼栋操作
     * @param building  将楼栋信息封装至 Building 对象中
     * @return  返回一个 int 类型的值：1为插入成功，0为插入失败
     */
    @Insert("insert into tb_building (name) values (#{building.name})")
    int insertBuilding(@Param("building") BuildingAddForm building);

    // delete()
    /**
     *  删除楼栋操作
     * @param id 楼栋的 id号
     * @return 返回一个 int 类型，1为删除成功，0为删除失败
     */
    @Delete("delete from tb_building where id = #{id}")
    int deleteById(@Param("id") Integer id);

    //update()

    /**
     * 更新楼栋的信息
     * @param building 将楼栋的新信息封装至 Building 对象中
     * @return 返回一个 int 类型的值，1为更新成功，0为更新失败
     */
    int update(@Param("building") Building building);
}
