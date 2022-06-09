package com.fqy.crm.service;

import com.fqy.crm.dao.building.RoomDao;
import com.fqy.crm.entity.building.Room;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoomService {

    @Resource
    private RoomDao roomDao;

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

//    /**
//     * 根据房间的 id 查询该房间的信息
//     * @param id 房间的 id 号
//     * @return 返回一个 Room 对象
//     */
    public Room getRoomById(Integer id){
        Room room = roomDao.selectById(id);
        return room;
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
     * 增加房间的操作
     * @param room 将房间的信息封装至 Room 对象中
     * @return 返回一个 int 类型的的值：1为插入成功，0为插入失败
     */
    public int register(Room room){
        List<Room> rooms = roomDao.selectByBuildingId(room.getBuildingId());
        for(Room r : rooms){
            if(r.getName().equals(room.getName()))
                return 0;
        }
        return roomDao.insertRoom(room);
    }

    /**
     * 删除房间操作
     * @param id 房间的 id号
     * @return 返回一个 int 类型，1为删除成功，0为删除失败
     */
    public int delete(Integer id){
        return roomDao.deleteById(id);
    }

    /**
     * 更新房间操作
     * @param room 将房间的新信息封装至 Room 对象中
     * @return 返回一个 int 类型的值，1为更新成功，0为更新失败
     */
    public int update(Room room){
//        System.out.println(room);
        return roomDao.update(room);
    }
}
