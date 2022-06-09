package com.fqy.crm.service;

import com.fqy.crm.dao.OldManDao;
import com.fqy.crm.dao.building.BedDao;
import com.fqy.crm.dao.building.BuildingDao;
import com.fqy.crm.dao.building.RoomDao;
import com.fqy.crm.entity.OldMan;
import com.fqy.crm.entity.building.Bed;
import com.fqy.crm.entity.building.Building;
import com.fqy.crm.entity.building.Room;
import com.fqy.crm.form.BuildingAddForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BuildingService {

    @Resource
    private BuildingDao buildingDao;

    @Resource
    private RoomDao roomDao;

    @Resource
    private BedDao bedDao;

    @Resource
    private OldManDao oldManDao;

//    @Resource
//    private PatientDao patientDao;

    /**
     * 职工查询所有的 building 信息（包含病人）
     *  @return 返回一个 List 队列
     */
    public List<Building> getAll() {
        List<Building> buildings = buildingDao.selectAll();
        for (Building building : buildings) {
            setBuildingRooms(building);
        }
        return buildings;
    }

    private void setBuildingRooms(Building building) {
        List<Room> rooms = roomDao.selectByBuildingId(building.getId());
        for (Room room : rooms) {
            setRoomBeds(room);
        }
        building.setRoomList(rooms);
    }

    private void setRoomBeds(Room room) {
        List<Bed> beds = bedDao.selectByRoomId(room.getId());
        for (Bed bed : beds) {
            OldMan oldMan = oldManDao.getOldManByBedId(bed.getId());
            bed.setOldMan(oldMan);
        }
        room.setBedList(beds);
    }

    /**
     * 根据楼栋的 id 查询该楼栋的信息
     *
     * @param id 楼栋的 id 号
     * @return 返回一个 Buiding 对象
     */
    public Building getBuildingById(Integer id) {
        Building building = buildingDao.selectById(id);
        setBuildingRooms(building);
        return building;
    }


    //名字


//    /**
//     * 增加楼栋操作
//     * @param building 将楼栋信息封装至 Building 对象中
//     * @return 返回一个 int 类型的的值：1为插入成功，0为插入失败
//     */
//    public int register(Building building){
//        Building flag = buildingDao.selectByUserName(building.getName());
//        if(flag != null)
//            //楼栋已经存在
//            return 0;
//        return buildingDao.insertBuilding(building);
//    }

    /**
     * 增加楼栋操作
     *
     * @param building 将楼栋信息封装至 Building 对象中
     * @return 返回一个 int 类型的的值：1为插入成功，0为插入失败
     */
    public int register(BuildingAddForm building) {
        Building flag = buildingDao.selectByName(building.getName());
        if (flag != null)
            //楼栋已经存在
            return 0;
        return buildingDao.insertBuilding(building);
    }

    /**
     * 删除楼栋操作
     *
     * @param id 楼栋的 id号
     * @return 返回一个 int 类型，1为删除成功，0为删除失败
     */
    public int delete(Integer id) {
        return buildingDao.deleteById(id);
    }


    /**
     * 更新楼栋的操作
     *
     * @param building 将楼栋信息封装至 Building 对象中
     * @return 返回一个 int 类型的的值：1为更新成功，0为更新失败
     */
    public int update(Building building) {
        System.out.println(building);
        return buildingDao.update(building);
    }
}

