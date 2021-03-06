package cn.edu.hstc.controller.front;

import cn.edu.hstc.common.JSONResponse;
import cn.edu.hstc.pojo.Project;
import cn.edu.hstc.pojo.User;
import cn.edu.hstc.service.ProjectService;
import cn.edu.hstc.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author yifang 1307720869@qq.com
 * @date Created in 9:43 2018/4/1
 * @description:科研项目
 * @modified by:
 */

@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    FileService fileService;

    /**
     * @Description:添加科研项目
     * @param: [session, project]
     * @return: cn.edu.hstc.common.JSONResponse<cn.edu.hstc.pojo.Project>
     * @author: yifang
     * @Date: 2018/4/1 9:48
     */
    @RequestMapping("/add.do")
    @ResponseBody
    public JSONResponse addProject(HttpSession session, @Valid Project pro, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JSONResponse.createByErrorMessage(bindingResult.getFieldError().getDefaultMessage());
        }
        User user = (User) session.getAttribute("currentUser");
        pro.setUserId(user.getUserId());
        return projectService.addProject(pro);
    }


    /**
     * @Description:删除科研项目
     * @param: [session, pro_id]
     * @return: cn.edu.hstc.common.JSONResponse<cn.edu.hstc.pojo.Project>
     * @author: yifang
     * @Date: 2018/4/1 9:52
     */
    @RequestMapping("/delete.do")
    @ResponseBody
    public JSONResponse<Integer> deleteProject(Integer proId) {
        return projectService.deleteProject(proId);
    }

    /**
     * @Description:修改科研项目
     * @param: [session, project]
     * @return: cn.edu.hstc.common.JSONResponse<cn.edu.hstc.pojo.Project>
     * @author: yifang
     * @Date: 2018/4/1 9:58
     */
    @RequestMapping("/update.do")
    @ResponseBody
    public JSONResponse updateProject(HttpSession session, Project pro) {
        User user = (User) session.getAttribute("currentUser");
        pro.setUserId(user.getUserId());
        return projectService.updateProject(pro);
    }

    /**
     * @Description:按ID查询
     * @param: [session, pro_id]
     * @return: cn.edu.hstc.common.JSONResponse<cn.edu.hstc.pojo.Project>
     * @author: yifang
     * @Date: 2018/4/1 10:00
     */
    @RequestMapping("/selectById.do")
    @ResponseBody
    public JSONResponse<Project> selectProjectById(Integer proId) {
        return projectService.selectProjectById(proId);
    }

    /**
     * @Description:返回用户拥有的科研项目列表
     * @param: [session]
     * @return:
     * @author: yifang
     * @Date: 2018/4/1 10:02
     */
    @RequestMapping("/selectByUserId.do")
    @ResponseBody
    public JSONResponse selectProjectListByUserId(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        User user = (User) session.getAttribute("currentUser");
        return projectService.selectProjectListByUserId(user.getUserId(), pageNum, pageSize);
    }


    /**
     * @Description:查询科研项目数目
     * @param: [session]
     * @return: cn.edu.hstc.common.JSONResponse<java.lang.Integer>
     * @author: yifang
     * @Date: 2018/4/1 10:28
     */
    @RequestMapping("/getCount.do")
    @ResponseBody
    public JSONResponse<Integer> getProjectCountByUserId(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return JSONResponse.createBySuccess(0);
        return projectService.getProjectCountByUserId(user.getUserId());
    }

    /**
     * @author Veng Su
     * @date 2018/4/2 0:01
     * 方法作用：跳转到list
     **/
    @RequestMapping("/list")
    public String showProjectList() {
        return "/project/list";
    }

    /**
     * @author Veng Su
     * @date 2018/4/2 20:30
     * 方法作用：跳转到add页面
     **/
    @RequestMapping("/add")
    public String showProjectAdd() {
        return "/project/add";
    }

    @RequestMapping("/edit")
    public String showProjectEdit() {
        return "/project/edit";
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
        String path = request.getSession().getServletContext().getRealPath("uploads") + "\\" + username + "\\project\\";
//        path = path.substring(0, path.indexOf("target")) + "src\\main\\webapp\\" + path.substring(path.indexOf("uploads"));
        JSONResponse<String> upd = fileService.upload(file, path);
        if (!upd.isSuccess())//若上传出错
            return upd;

        String url ="/uploads"+"/"+username+"/project"+"/"+ upd.getData();

        //上传成功，保存url到filepath表，并返回fileId
        return fileService.updateFilepathInfo(url, userId);


    }


}