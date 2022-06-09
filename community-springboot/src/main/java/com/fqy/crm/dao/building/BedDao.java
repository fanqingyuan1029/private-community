package com.fqy.crm.dao.building;

import com.fqy.crm.entity.building.Bed;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BedDao {

//    @Insert("insert")
//    int insert()

    /**
     * 根据床位类型统计数量
     * @return 返回一个数量
     */
    @Select("Select count(id) from tb_bed")
    int count(@Param("bedType") Integer type);

    /**
     * 根据床位的 id 查询该床位的信息
     * @param id 床位的 id 号
     * @return 返回一个 Bed 对象
     */
    @Select("select * from tb_bed where id = #{id}")
    Bed selectById(@Param("id") Integer id);

    /**
     * 根据床位的名字，查询床位的信息
     * @param name 床位的名字
     * @return 返回一个 Bed 对象
     */
    @Select("select * from tb_bed where name = #{name}")
    Bed selectByName(@Param("name") String name);

    /**
     * 根据房间的 id 查询该房间中的所有的床位
     * @param id 房间 id
     * @return 返回床位 List
     */
    @Select("select * from tb_bed where room_id = #{RoomId}")
    List<Bed> selectByRoomId(@Param("RoomId") Integer id);

    /**
     * 增加床位操作
     * @param bed 将床位信息封装至 Bed 对象中
     * @return 返回一个 int 类型的值：1为插入成功，0为插入失败
     */
    @Insert("insert into tb_bed (room_id,name) values (#{bed.roomId},#{bed.name})")
    int insertBed(@Param("bed") Bed bed);

    /**
     * 删除床位操作
     * @param id 床位的 id 号
     * @return 返回一个 int 类型：1为删除成功，0为删除失败
     */
    @Delete("delete from tb_bed where id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * 更新房间的操作
     * @param bed 将房间的新信息封装至 Room 对象中
     * @return 返回一个 int 类型的值，1为更新成功，0为更新失败
     */
    int update(@Param("bed") Bed bed);
}
