package com.fqy.crm.service;

import com.fqy.crm.dao.OldManDao;
import com.fqy.crm.dao.UserDao;
import com.fqy.crm.dao.building.BedDao;
import com.fqy.crm.dao.building.BuildingDao;
import com.fqy.crm.dao.building.RoomDao;
import com.fqy.crm.entity.OldMan;
import com.fqy.crm.entity.User;
import com.fqy.crm.entity.building.Bed;
import com.fqy.crm.entity.building.Building;
import com.fqy.crm.entity.building.Room;
import com.fqy.crm.form.OldManForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OldManService {

    @Resource
    private BuildingDao buildingDao;

    @Resource
    private RoomDao roomDao;

    @Resource
    private BedDao bedDao;

    @Resource
    private OldManDao oldManDao;

    @Resource
    private UserDao userDao;

    public List<OldMan> selectAllNotHaveBed() {
        return oldManDao.selectAllNotHaveBed();
    }

    /**
     * 职工查询所有的病人信息（包含楼栋）
     *
     * @return 返回一个 List 队列
     */
    public List<OldMan> selectAll() {
        List<OldMan> oldMen = oldManDao.selectAll();
        for (OldMan oldMan : oldMen) {
            if (oldMan.getBedId() != null) {
                Integer id = oldMan.getBedId();
                String s = getBedById(id);
                oldMan.setLoadingName(s);
            }
        }
        return oldMen;
    }

    /**
     * 根据床位的 id 查询该床所处的房间信息与楼栋信息
     *
     * @param id 房间的 id 号
     * @return 返回一个 Room 对象
     */
    public String getBedById(Integer id) {
        //1.先根据ID查询Bed信息
        Bed bed = bedDao.selectById(id);
        if (bed == null) {
            return null;
        }
        //2.查询出的bed信息包含roomId,根据它得到Room信息
        Room room = roomDao.selectById(bed.getRoomId());
        //3.根据roomId查出room所处的Building信息
        Building building = buildingDao.selectById(room.getBuildingId());
//        //4.将Building信息绑定到这个room对象中
//        room.setBuilding(building);
//        //5.将这个room对象绑定到bed对象中
//        bed.setRoom(room);
        //6.返回至
        String result = building.getName() + room.getName() + bed.getName();
        return result;
    }

    /**
     * 根据id号查找病人的信息
     *
     * @param id 病人的id
     * @return 返回一个Patient对象
     */
    public OldMan getOldManById(Integer id) {
        return oldManDao.selectById(id);
    }

    /**
     * 注册操作
     *
     * @param oldMan 将注册的病人信息封装至 User 对象中
     * @return 返回一个 int 值，1代表插入成功，0代表插入失败
     */
    public int register(OldManForm oldMan) {
//        OldMan flag = oldManDao.selectByName(oldMan.getName());
//        if(flag != null){
//            return 0;
//        }
        return oldManDao.register(oldMan);
    }

    /**
     * 根据病人的id做删除操作
     *
     * @param id 病人的 id
     * @return 返回一个 int 类型的值，1代表删除成功，0代表删除失败
     */
    public int delete(Integer id) {
        return oldManDao.deleteById(id);
    }

    /**
     * 更新操作
     *
     * @param oldMan 将更新的病人信息封装至 Patient 对象中
     * @return 返回一个 int 类型的值，1代表更新成功，0代表更新失败
     */
    public int update(OldMan oldMan) {
        System.out.println(oldMan);
        if (oldMan.getBedId() == null) {
            if (StringUtils.isBlank(oldMan.getName()) && oldMan.getSex() == null && oldMan.getAge() == null
                    && StringUtils.isBlank(oldMan.getPhone()) && StringUtils.isBlank(oldMan.getContactName())
                    && StringUtils.isBlank(oldMan.getContactPhone()))
                return 0;
        }
        return oldManDao.update(oldMan);
    }

    /**
     * 把病人从床上挪走
     *
     * @param id 病人id
     * @return 改变的行数
     */
    public int removeOldManBed(Integer id) {
        return oldManDao.updateBedId(id);
    }
}
