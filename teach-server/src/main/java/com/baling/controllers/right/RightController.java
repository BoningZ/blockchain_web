package com.baling.controllers.right;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.service.right.RightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/right")
public class RightController {
    @Autowired
    RightService rightService;

    @GetMapping("/getRightList")
    public DataResponse getRightList(@RequestParam("type")String type,@RequestParam("name")String name){
        return rightService.getRightList(type,name);
    }

    @DeleteMapping("/deleteRight")
    public ResponseEntity<?> deleteRight(@RequestParam("id")Integer id){
        return rightService.deleteRight(id);
    }

    @PutMapping("/addRight")
    public ResponseEntity<?> addRight(@Valid @RequestBody DataRequest dataRequest){
        return rightService.addRight(dataRequest);
    }

    @GetMapping("/getRightTypeList")
    public DataResponse getRightTypeList(){
        return rightService.getRightTypeList();
    }

}
