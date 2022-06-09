package com.fqy.crm.dao.building;


import com.fqy.crm.entity.building.Room;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomDao {


    /**
     * 根据房间类型 统计数量
     * @return 返回一个 数量
     */
    @Select("Select count(id) from tb_room")
    int count(@Param("roomType") Integer type);

    /**
     * 根据房间的 id 查询该房间的信息
     * @param id 房间的 id 号
     * @return 返回一个 Room 对象
     */
    @Select("select * from tb_room where id = #{id}")
    Room selectById(@Param("id") Integer id);

    /**
     * 根据房间的名字，查询房间的信息
     * @param name 房间的名字
     * @return 返回一个 Room 对象
     */
    @Select("select * from tb_room where name = #{name}")
    Room selectByName(@Param("name") String name);

    /**
     * 根据楼栋的id查询该楼栋中的所有房间
     * @param id 楼栋id
     * @return 返回房间 List
     */
    @Select("select * from tb_room where building_id = #{buildingId}")
    List<Room> selectByBuildingId(@Param("buildingId") Integer id);

    /**
     * 增加房间操作
     * @param room 将房间信息封装至 Room 对象中
     * @return 返回一个 int 类型的值：1为插入成功, 0为插入失败
     */
    @Insert("insert into tb_room (building_id,name) values (#{room.buildingId},#{room.name})")
    int insertRoom(@Param("room") Room room);


    /**
     * 删除房间操作
     * @param id 房间的 id 号
     * @return 返回一个 int 类型，1为删除成功，0为删除失败
     */
    @Delete("delete from tb_room where id = #{id}")
    int deleteById(@Param("id") Integer id);


    /**
     * 更新房间的操作
     * @param room 将房间的新信息封装至 Room 对象中
     * @return 返回一个 int 类型的值，1为更新成功，0为更新失败
     */
    int update(@Param("room") Room room);
}
