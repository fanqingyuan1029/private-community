package com.fqy.dao;

import com.fqy.entity.OldMan;
import com.fqy.utils.SimpleTools;
import com.fqy.entity.building.Bed;
import com.fqy.entity.building.Building;
import com.fqy.entity.building.Room;
import com.fqy.entity.entityutils.ResponseMessage;
import com.fqy.cookie.Cache;
import javafx.scene.control.Alert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class BuildingDao {

    /**
     * 获取所有的楼栋信息
     *
     * @return 返回所有的楼栋信息
     */
    public static List<Building> getBuildings() {
        //指定要访问的接口地址
        String url = SimpleTools.SERVER_URL + "/building/all";
        //向后台发送请求,并通过ParameterizedTypeReference指定结果类型
        ResponseEntity<ResponseMessage<List<Building>>>
                responseEntity = SimpleTools.restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<ResponseMessage<List<Building>>>
                        () {
                });
        //从请求结果中获取返回对象
        ResponseMessage<List<Building>> response = responseEntity.getBody();
        //判断请求的相应状况
        if (response != null && response.getCode() == 200) {
            List<Building> buildings = response.getData();
            //把获取到的数据保存到缓存中去
            Cache.Cookie().put("BuildingList", buildings);
            return buildings;
        }
        return new ArrayList<>();
    }


    //根据楼栋列表获取楼栋的名字
    public static List<String> getNames(List<Building> buildings) {
        List<String> nameList = new ArrayList<>(buildings.size());
        for (Building building : buildings) {
            nameList.add(building.getName());
        }
        return nameList;
    }

    //从单个楼栋中获取房间列表
    public static List<?> getRoomListFromBuilding(String buildingName) {

        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");

        //查看列表中是否有楼栋，如果有，则进行下一步，没有，则弹出警告
        if (result.size() > 0) {

            //选择了一个楼栋名，显示房间列表
            if (buildingName != null) {
                for (Object buildingObj : result) {
                    Building building = (Building) buildingObj;

                    //返回房间列表
                    if (building.getName().equals(buildingName)) {
                        return building.getRoomList();
                    }
                }
            }
//            //没有点击楼栋名，弹出警告语句
        }
        return new ArrayList<>();
    }

    //从单个房间中获取床位列表
    public static List<?> getBedListFromRoom(String buildingName, String roomName) {
        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");

        //选择楼栋
        if (buildingName != null) {
            for (Object buildingObj : result) {
                Building building = (Building) buildingObj;

                //获取该楼栋里的房间列表
                if (building.getName().equals(buildingName)) {
                    List<Room> rooms = building.getRoomList();

                    //如果房间列表不为空，返回房间列表，否则返回空列表
                    if (rooms.size() > 0) {
                        if (roomName != null) {
                            for (Room room : rooms) {
                                if (room.getName().equals(roomName)) {
                                    return room.getBedList();
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public static Bed getBedByBuildingRoomBedName(String buildingName, String roomName, String bedName) {
        List<?> bedListFromRoom = getBedListFromRoom(buildingName, roomName);
//        List<Bed> bedList = new ArrayList<>(bedListFromRoom.size());
        for (Object obj : bedListFromRoom) {
            Bed bed = (Bed) obj;
//            bedList.add(bed);
            if (bed.getName().equals(bedName)) {
                return bed;
            }
        }
        return null;
    }

    public static OldMan getOldManFromBed(String buildingName, String roomName, String bedName) {
        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");

        if (buildingName != null) {
            for (Object buildingObj : result) {
                Building building = (Building) buildingObj;

                if (building.getName().equals(buildingName)) {
                    List<Room> rooms = building.getRoomList();
                    if (roomName != null) {
                        for (Room room : rooms) {
                            if (room.getName().equals(roomName)) {
                                List<Bed> beds = room.getBedList();

                                if (beds.size() > 0) {
                                    if (bedName != null) {
                                        for (Bed bed : beds)
                                            if (bed.getName().equals(bedName))
                                                return bed.getOldMan();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean delete(String buildingName) {
        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/building/";
        if (buildingName != null) {
            for (Object buildingObj : result) {
                Building building = (Building) buildingObj;
                if (building.getName().equals(buildingName)) {
                    //G
                    ResponseEntity<ResponseMessage<String>> responseEntity = restTemplate.exchange(url + building.getId(), HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseMessage<String>>() {
                    });
                    if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                        ResponseMessage<String> body = responseEntity.getBody();
                        if (body != null) {
                            if (body.getCode().equals(200)) {
                                SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "操作结果", "成功", body.getMessage());
                                return true;
                            }else if(body.getCode().equals(500)){
                                SimpleTools.informationDialog(Alert.AlertType.ERROR, "操作结果", "失败", body.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean delete(String buildingName, String roomName) {
        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/building";
        if (buildingName != null) {
            for (Object buildingObj : result) {
                Building building = (Building) buildingObj;
                if (building.getName().equals(buildingName)) {
                    if (roomName != null) {
                        List<Room> rooms = building.getRoomList();
//                        System.out.println(buildingName + "的" + rooms);
                        for (Room room : rooms) {
                            //G
                            if (room.getName().equals(roomName)) {
                                ResponseEntity<ResponseMessage<String>> responseEntity = restTemplate.exchange(url + "/room/" + room.getId(), HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseMessage<String>>() {
                                });
                                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                                    ResponseMessage<String> body = responseEntity.getBody();
                                    if (body != null) {
                                        if (body.getCode().equals(200)) {
                                            SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "操作结果", "成功", body.getMessage());
                                            return true;
                                        }else if(body.getCode().equals(500)){
                                            SimpleTools.informationDialog(Alert.AlertType.ERROR, "操作结果", "失败", body.getMessage());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean delete(String buildingName, String roomName, String bedName) {
        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");
        RestTemplate restTemplate = new RestTemplate();
        String url = SimpleTools.SERVER_URL + "/building";
        if (buildingName != null) {
            for (Object buildingObj : result) {
                Building building = (Building) buildingObj;
                if (building.getName().equals(buildingName)) {
                    if (roomName != null) {
                        List<Room> rooms = building.getRoomList();
                        for (Room room : rooms) {
                            if (room.getName().equals(roomName)) {
                                if (bedName != null) {
                                    List<Bed> beds = room.getBedList();
                                    for (Bed bed : beds) {
                                        if (bed.getName().equals(bedName)) {
                                            ResponseEntity<ResponseMessage<String>> responseEntity = restTemplate.exchange(url + "/bed/" + bed.getId(), HttpMethod.DELETE, null, new ParameterizedTypeReference<ResponseMessage<String>>() {
                                            });
                                            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                                                ResponseMessage<String> body = responseEntity.getBody();
                                                if (body != null) {
                                                    if (body.getCode().equals(200)) {
                                                        SimpleTools.informationDialog(Alert.AlertType.INFORMATION, "操作结果", "成功", body.getMessage());
                                                        return true;
                                                    }else if(body.getCode().equals(500)){
                                                        SimpleTools.informationDialog(Alert.AlertType.ERROR, "操作结果", "失败", body.getMessage());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean registerBed(String buildingName, String roomName, Bed bed) {
        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");
        String url = SimpleTools.SERVER_URL + "/building";

        if (buildingName != null) {
            for (Object buildingObj : result) {
                Building building = (Building) buildingObj;
                if (building.getName().equals(buildingName)) {
                    if (roomName != null) {
                        List<Room> rooms = building.getRoomList();
                        for (Room room : rooms) {
                            if (room.getName().equals(roomName)) {
                                bed.setRoomId(room.getId());
                                url = url + "/bed/register";
                                return SimpleTools.register(url, bed);
                            }
                        }
                    }

                }
            }

        }
        return false;
    }

    public static boolean registerRoom(String buildingName, Room room) {
        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");
        String url = SimpleTools.SERVER_URL + "/building";

        if (!SimpleTools.isEmpty(buildingName)) {
            for (Object buildingObj : result) {
                Building building = (Building) buildingObj;

                if (building.getName().equals(buildingName)) {

                    room.setBuildingId(building.getId());
                    url = url + "/room/register";
                    return SimpleTools.register(url, room);
                }
            }

        }
        return false;
    }

    public static boolean registerBuilding(Building building) {
//        List<?> result = (List<?>) Cache.Cookie().get("BuildingList");
        String url = SimpleTools.SERVER_URL + "/building" + "/register";

        return SimpleTools.register(url, building);

    }


    //从Session中获取楼栋、房间、床位的id


}
