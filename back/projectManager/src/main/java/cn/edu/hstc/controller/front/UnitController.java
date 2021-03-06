package cn.edu.hstc.controller.front;

import cn.edu.hstc.common.JSONResponse;
import cn.edu.hstc.pojo.Unit;
import cn.edu.hstc.pojo.User;
import cn.edu.hstc.service.UnitService;
import cn.edu.hstc.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author yifang 1307720869@qq.com
 * @date Created in 9:43 2018/4/1
 * @description:学术团体
 * @modified by:
 */

@Controller
@RequestMapping("/unit")
public class UnitController {
    @Autowired
    UnitService unitService;

    @Autowired
    FileService fileService;

    /**
     * @Description:添加学术团体
     * @param: [session, unit]
     * @return: cn.edu.hstc.common.JSONResponse<cn.edu.hstc.pojo.Unit>
     * @author: yifang
     * @Date: 2018/4/1 9:48
     */
    @RequestMapping("/add.do")
    @ResponseBody
    public JSONResponse addUnit(HttpSession session, Unit unit) {
        User user = (User) session.getAttribute("currentUser");
        unit.setUserId(user.getUserId());
        return unitService.addUnit(unit);
    }


    /**
     * @Description:删除学术团体
     * @param: [session, unit_id]
     * @return: cn.edu.hstc.common.JSONResponse<cn.edu.hstc.pojo.Unit>
     * @author: yifang
     * @Date: 2018/4/1 9:52
     */
    @RequestMapping("/delete.do")
    @ResponseBody
    public JSONResponse<Integer> deleteUnit(Integer unitId) {
        return unitService.deleteUnit(unitId);
    }

    /**
     * @Description:修改学术团体
     * @param: [session, unit]
     * @return: cn.edu.hstc.common.JSONResponse<cn.edu.hstc.pojo.Unit>
     * @author: yifang
     * @Date: 2018/4/1 9:58
     */
    @RequestMapping("/update.do")
    @ResponseBody
    public JSONResponse updateUnit(HttpSession session, Unit unit) {
        User user = (User) session.getAttribute("currentUser");
        unit.setUserId(user.getUserId());
        return unitService.updateUnit(unit);
    }

    /**
     * @Description:按ID查询
     * @param: [session, unit_id]
     * @return: cn.edu.hstc.common.JSONResponse<cn.edu.hstc.pojo.Unit>
     * @author: yifang
     * @Date: 2018/4/1 10:00
     */
    @RequestMapping("/selectById.do")
    @ResponseBody
    public JSONResponse<Unit> selectUnitById(Integer unitId) {
        return unitService.selectUnitById(unitId);
    }

    /**
     * @Description:返回用户拥有的学术团体列表
     * @param: [session]
     * @return:
     * @author: yifang
     * @Date: 2018/4/1 10:02
     */
    @RequestMapping("/selectByUserId.do")
    @ResponseBody
    public JSONResponse selectUnitListByUserId(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        User user = (User) session.getAttribute("currentUser");
        return unitService.selectUnitListByUserId(user.getUserId(), pageNum, pageSize);
    }


    /**
     * @Description:查询学术团体数目
     * @param: [session]
     * @return: cn.edu.hstc.common.JSONResponse<java.lang.Integer>
     * @author: yifang
     * @Date: 2018/4/1 10:28
     */
    @RequestMapping("/getCount.do")
    @ResponseBody
    public JSONResponse<Integer> getUnitCountByUserId(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return JSONResponse.createBySuccess(0);
        return unitService.getUnitCountByUserId(user.getUserId());
    }

    /**
     * @author Veng Su
     * @date 2018/4/2 0:01
     * 方法作用：跳转到list
     **/
    @RequestMapping("/list")
    public String showUnitList() {
        return "/unit/list";
    }

    /**
     * @author Veng Su
     * @date 2018/4/2 20:30
     * 方法作用：跳转到add页面
     **/
    @RequestMapping("/add")
    public String showUnitAdd() {
        return "/unit/add";
    }

    @RequestMapping("/edit")
    public String showUnitEdit() {
        return "/unit/edit";
    }


    /**
    * @Description:上传附件
    * @param: [session, file, request]
    * @return: cn.edu.hstc.common.JSONResponse
    * @author: yifang
    * @Date: 2018/4/3 10:07
    */
    @RequestMapping(value = "/uploadAttachment.do")
    @ResponseBody
    public JSONResponse uploadAttachment(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute("currentUser");
        String username = user.getUsername();
        Integer userId = user.getUserId();
        String path = request.getSession().getServletContext().getRealPath("uploads")  + "\\"+username  + "\\unit\\";
//        path=path.substring(0,path.indexOf("target"))+"src\\main\\webapp\\"+path.substring(path.indexOf("uploads"));
        JSONResponse<String> upd = fileService.upload(file, path);
        if (!upd.isSuccess())//若上传出错
            return upd;

        String url ="/uploads"+"/"+username+"/unit"+"/"+ upd.getData();

        //上传成功，保存url到filepath表，并返回fileId
       return fileService.updateFilepathInfo(url, userId);



    }


}