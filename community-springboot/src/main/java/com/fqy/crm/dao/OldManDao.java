package com.fqy.crm.dao;


import com.fqy.crm.entity.OldMan;
import com.fqy.crm.form.OldManForm;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OldManDao {

    //@Select()

    /**
     * 查找所有病人：
     * 方便在使用了删除操作或者更新操作后的从前端获取病人的信息
     *
     * @return List 队列
     */
    @Select("select * from tb_oldman where deleted = 0")
    List<OldMan> selectAll();

    @Select("select * from tb_oldman where ISNULL(bed_id) = 1 and deleted = 0")
    List<OldMan> selectAllNotHaveBed();

    /**
     * 根据病人的id查找病人的信息
     *
     * @param id 病人的 Id 号
     * @return 返回一个 OldMan 对象
     */
    @Select("select * from tb_oldman where id = #{id} and deleted = 0")
    OldMan selectById(@Param("id") Integer id);

    @Select("select * from tb_oldman where name = #{name} and deleted = 0")
    OldMan selectByName(@Param("name") String name);

    /**
     * 根据床位 Id 获取 病人的信息
     *
     * @param id 床位的 id 号
     * @return 返回一个 OldMan 对象
     */
    @Select("select * from tb_oldman where bed_id = #{bedId} and deleted = 0")
    OldMan getOldManByBedId(@Param("bedId") Integer id);

    // @Insert()

    /**
     * 注册操作
     *
     * @param oldMan 将注册的病人信息封装至 OldMan 对象中
     * @return 返回一个 int 值，1代表插入成功，0代表插入失败
     */
    @Insert("insert into tb_oldman(name,sex,age,phone,contact_name,contact_phone) " +
            "values (#{oldMan.name},#{oldMan.sex},#{oldMan.age},#{oldMan.phone}," +
            "#{oldMan.contactName},#{oldMan.contactPhone})")
    int register(@Param("oldMan") OldManForm oldMan);

    //@Delete()

    /**
     * 根据病人的 id 做删除操作
     *
     * @param id 病人的 id
     * @ Delete("delete from tb_oldMan where id = #{id}")
     * @return 返回一个 int 类型的值，1代表删除成功，0代表删除失败
     */

    @Update("update tb_oldman set deleted = 1,bed_id=null,last_update_time=now() where id = #{id}")
    int deleteById(@Param("id") Integer id);

    //@Update()

    /**
     * 更新操作
     *
     * @param oldMan 将更新的病人信息封装至 OldMan 对象中
     * @return 返回一个 int 类型的值，1代表更新成功，0代表更新失败
     */
    int update(@Param("oldMan") OldMan oldMan);

    //UpdateBedId()
    @Update("update tb_oldman set bed_id = null where id=#{id}")
    int updateBedId(@Param("id") Integer id);
}
