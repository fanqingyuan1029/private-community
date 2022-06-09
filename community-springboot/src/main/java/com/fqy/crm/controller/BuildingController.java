package com.fqy.crm.controller;

import com.fqy.crm.entity.User;
import com.fqy.crm.entity.building.Bed;
import com.fqy.crm.entity.building.Building;
import com.fqy.crm.entity.building.Room;
import com.fqy.crm.entity.entityutils.ResponseMessage;
import com.fqy.crm.form.BuildingAddForm;
import com.fqy.crm.service.BedService;
import com.fqy.crm.service.BuildingService;
import com.fqy.crm.service.RoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/building")
public class BuildingController {

    @Resource
    private BuildingService buildingService;

    @Resource
    private RoomService roomService;

    @Resource
    private BedService bedService;
//    @GetMapping("/all")
//    public List<Building>  getAll(){
//        return buildingService.getAll();
//    }

    /**——————————————————————————————————————————楼栋管理——————————————————————————————*/
    @GetMapping("/all")
    public ResponseMessage<List<Building>> getAll(){
        List<Building> buildings = buildingService.getAll();
        if(buildings != null)
            return new ResponseMessage<> (200,"查询成功",buildings);
        return new ResponseMessage<>(500,"查询失败",null);
    }



//    @GetMapping("/{id}")
//    public Building getBuildingById(@PathVariable("id") Integer id){
//        return buildingService.getBuildingById(id);
//    }

    @GetMapping("/{id}")
    public ResponseMessage<Building> getBuildingById(@PathVariable("id") Integer id){
        Building building = buildingService.getBuildingById(id);
        if(building != null)
            return new ResponseMessage<> (200,"查询成功",building);
        return new ResponseMessage<> (500,"查询失败",null);
    }

//    @PostMapping("register")
//    public String register(@RequestBody @Valid Building building){
//        return buildingService.register(building)==1?"创建成功":"创建失败";
//    }

//    @PostMapping("register")
//    public String register(@RequestBody @Valid BuildingAddForm building){
//        return buildingService.register(building)==1?"创建成功":"创建失败";
//    }

    @PostMapping("/register")
    public ResponseMessage<?> register(@RequestBody @Valid BuildingAddForm building){
        if(buildingService.register(building) == 1)
            return new ResponseMessage<> (200,"注册成功");
        return new ResponseMessage<> (500,"注册失败","当前注册的楼栋名可能已存在");
    }


//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") Integer id){
//        return buildingService.delete(id)==1?"删除成功":"删除失败";
//    }

    @DeleteMapping("/{id}")
    public ResponseMessage<?> delete(@PathVariable("id") Integer id){
        if(buildingService.delete(id) == 1)
            return new ResponseMessage<>(200,"删除成功");
        return new ResponseMessage<>(500,"删除失败");
    }

//    @PutMapping("/{id}")
//    public String update(@PathVariable("id") Integer id,@RequestBody Building building){
//        building.setId(id);
//        return buildingService.update(building)==1 ? "更新成功":"更新失败";
//    }

    @PutMapping("/{id}")
    public ResponseMessage<?> update(@PathVariable("id") Integer id,@RequestBody Building building){
        building.setId(id);
        if(buildingService.update(building) == 1)
            return new ResponseMessage<>(200,"更新成功");
        return new ResponseMessage<>(500,"更新失败");
    }


    /**——————————————————————————————————————————房间管理——————————————————————————————*/

    @PostMapping("/room/register")
    public ResponseMessage<?> registerRoom(@RequestBody Room room){
        if(roomService.register(room) == 1)
            return new ResponseMessage<> (200,"注册成功");
        return new ResponseMessage<> (500,"注册失败","当前楼栋中可能已有同名房间");
    }

    @DeleteMapping("/room/{id}")
    public ResponseMessage<?> deleteRoom(@PathVariable("id") Integer id){
        if(roomService.delete(id) == 1)
            return new ResponseMessage<>(200,"删除成功");
        return new ResponseMessage<>(500,"删除失败","这个房间可能已经删除");
    }

    @PutMapping("/room/{id}")
    public ResponseMessage<?> updateRoom(@PathVariable("id") Integer id,@RequestBody Room room){
        room.setId(id);
        if(roomService.update(room) == 1)
            return new ResponseMessage<>(200,"更新成功");
        return new ResponseMessage<>(500,"更新失败");
    }

    /**——————————————————————————————————————————房间管理——————————————————————————————*/

    @PostMapping("/bed/register")
    public ResponseMessage<?> registerBed(@RequestBody Bed bed){
        if(bedService.register(bed) == 1)
            return new ResponseMessage<> (200,"注册成功");
        return new ResponseMessage<> (500,"注册失败","当前房间中可能已存在同名床位");
    }

    @DeleteMapping("/bed/{id}")
    public ResponseMessage<?> deleteBed(@PathVariable("id") Integer id){
        if(bedService.delete(id) == 1)
            return new ResponseMessage<>(200,"删除成功");
        return new ResponseMessage<>(500,"删除失败","这个床位可能已经删除");
    }

    @PutMapping("/bed/{id}")
    public ResponseMessage<?> updateRoom(@PathVariable("id") Integer id,@RequestBody Bed bed){
        bed.setId(id);
        if(bedService.update(bed) == 1)
            return new ResponseMessage<>(200,"更新成功");
        return new ResponseMessage<>(500,"更新失败","床位不存在或是未改变信息");
    }
}
