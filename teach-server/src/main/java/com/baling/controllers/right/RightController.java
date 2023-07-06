package com.baling.controllers.right;

import com.baling.payload.request.DataRequest;
import com.baling.payload.response.DataResponse;
import com.baling.service.right.RightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/right")
public class RightController {
    @Autowired
    RightService rightService;

    @GetMapping("/getRightList")
    public DataResponse getRightList(@RequestParam(value = "types",required = false) List<Integer> types, @RequestParam("name")String name, @RequestParam("page")int page){
        return rightService.getRightList(types,name,page);
    }

    @DeleteMapping("/deleteRight")
    public ResponseEntity<?> deleteRight(@RequestParam("id")Integer id){
        return rightService.deleteRight(id);
    }

    @PutMapping("/addRight")
    public ResponseEntity<?> addRight(@Valid @RequestBody DataRequest dataRequest){
        return rightService.addRight(dataRequest);
    }

    @PutMapping("/updateRight")
    public ResponseEntity<?> updateRight(@Valid @RequestBody DataRequest dataRequest){
        return rightService.updateRight(dataRequest);
    }



}
