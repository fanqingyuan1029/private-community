package com.fqy.crm.controller;

import com.fqy.crm.entity.OldMan;
import com.fqy.crm.entity.entityutils.ResponseMessage;
import com.fqy.crm.form.OldManForm;
import com.fqy.crm.service.OldManService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/oldMan")
public class OldManController {

    @Resource
    private OldManService oldManService;

    @GetMapping("/add/notHaveBed")
    public ResponseMessage<List<OldMan>> getAllOldManNotHaveBed(){
        List<OldMan> oldMen = oldManService.selectAllNotHaveBed();
        if(oldMen != null)
            return new ResponseMessage<>(200,"查询成功", oldMen);
        return new ResponseMessage<>(500,"查询失败",null);
    }

    @GetMapping("/all")
    public ResponseMessage<List<OldMan>> getAll(){
        List<OldMan> oldMen = oldManService.selectAll();
        if(oldMen != null)
            return new ResponseMessage<>(200,"查询成功", oldMen);
        return new ResponseMessage<>(500,"查询失败",null);
    }

    @GetMapping("/{id}")
    public ResponseMessage<OldMan> getOldManById(@PathVariable("id") Integer id){
        OldMan oldMan =  oldManService.getOldManById(id);
        if(oldMan != null){
            return new ResponseMessage<> (200,"查询成功", oldMan);
        }
        return new ResponseMessage<>(500,"查询失败",null);
    }

    @PostMapping("/register")
    public ResponseMessage<?> register(@RequestBody @Valid OldManForm oldMan){
        if(oldManService.register(oldMan) == 1)
            return new ResponseMessage<>(200,"注册成功");
        return new ResponseMessage<>(500,"注册失败");
    }

    @DeleteMapping("/{id}")
    public ResponseMessage<?> delete(@PathVariable("id") Integer id){
        if(oldManService.delete(id) == 1)
            return new ResponseMessage<>(200,"删除成功");
        return new ResponseMessage<>(500,"删除失败");
    }

    @PutMapping("")
    public ResponseMessage<?> update(@RequestBody OldMan oldMan){

        if(oldManService.update(oldMan) == 1)
            return new ResponseMessage<>(200,"更新成功");
        return new ResponseMessage<>(500,"更新失败");
    }

    @PutMapping("/removeBedId")
    public ResponseMessage<?> removeOldManBed(@RequestBody Integer id){
        if(oldManService.removeOldManBed(id)==1){
            return new ResponseMessage<>(200,"OK");
        }
        return new ResponseMessage<>(500,"Failed");
    }
}
