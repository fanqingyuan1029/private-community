package com.fqy.crm.service;

import com.fqy.crm.dao.building.BedDao;
import com.fqy.crm.dao.building.BuildingDao;
import com.fqy.crm.dao.building.RoomDao;
import com.fqy.crm.entity.building.Bed;
import com.fqy.crm.entity.building.Building;
import com.fqy.crm.entity.building.Room;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BedService {

    @Resource
    private BuildingDao buildingDao;

    @Resource
    private RoomDao roomDao;

    @Resource
    private BedDao bedDao;

//    /**
//     * 查询所有的 building 信息
//     * @return 返回一个 List 队列
//     */
//    public List<Building> getAll(){
//        List<Building> buildings = buildingDao.selectAll();
//        for(Building building:buildings){
//            setBuildingRooms(building);
//        }
//        return buildings;
//    }
//
//    private void setBuildingRooms(Building building) {
//        List<Room> rooms = roomDao.selectByBuildingId(building.getId());
//        for(Room room:rooms){
//            setRoomBeds(room);
//        }
//        building.setRoomList(rooms);
//    }
//
//    private void setRoomBeds(Room room) {
//        List<Bed> beds = bedDao.selectByRoomId(room.getId());
//        room.setBedList(beds);
//    }

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
     * 增加床位的操作
     *
     * @param bed 将房间的信息封装至 Bed 对象中
     * @return 返回一个 int 类型的的值：1为插入成功，0为插入失败
     */
    public int register(Bed bed) {
        List<Bed> beds = bedDao.selectByRoomId(bed.getRoomId());
        for(Bed b:beds){
            if(b.getName().equals(bed.getName())){
                return 0;
            }
        }
        return bedDao.insertBed(bed);
    }

    /**
     * 删除床位的操作
     *
     * @param id 床位的 id号
     * @return 返回一个 int 类型，1为删除成功，0为删除失败
     */
    public int delete(Integer id) {
        return bedDao.deleteById(id);
    }

    /**
     * 更新房间操作
     *
     * @param bed 将床位的新信息封装至 Bed 对象中
     * @return 返回一个 int 类型的值，1为更新成功，0为更新失败
     */
    public int update(Bed bed) {
//        System.out.println(room);
        return bedDao.update(bed);
    }
}
