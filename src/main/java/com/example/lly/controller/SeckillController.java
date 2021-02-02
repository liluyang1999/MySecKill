package com.example.lly.controller;

import com.example.lly.dto.ExecutedResult;
import com.example.lly.dto.ResponseResult;
import com.example.lly.dto.StateExposer;
import com.example.lly.entity.SeckillInfo;
import com.example.lly.entity.rbac.User;
import com.example.lly.exception.BaseSeckillException;
import com.example.lly.exception.FailedSeckillException;
import com.example.lly.exception.MyException;
import com.example.lly.exception.RepeatSeckillException;
import com.example.lly.service.SeckillService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.enumeration.SeckillStateType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@Api(tags = "MySeckill")
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private static final int generateRate = 10;
    @Autowired
    private final SeckillService seckillService;
    private final UserSecurityService userSecurityService;
    private final RedisTemplate<String, Serializable> redisTemplate;

    private static int corePoolScale = Runtime.getRuntime().availableProcessors();   //JVM可用的核心数量
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolScale, corePoolScale+1, 101, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1500));


    @Autowired
    public SeckillController(SeckillService seckillService, UserSecurityService userSecurityService, RedisTemplate<String, Serializable> redisTemplate) {
        this.seckillService = seckillService;
        this.userSecurityService = userSecurityService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获得秒杀活动的列表
     * @param model  Spring模型，储存有活动商品的详细信息
     * @return  秒杀活动列表
     */
    @RequestMapping(method = RequestMethod.GET, value = {"/list", "/index", ""})
    public String listSeckillInfo(Model model) {
        List<SeckillInfo> seckillList = seckillService.getAllSeckillInfo();
        model.addAttribute(seckillList);
        return "SeckillInfoList";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{seckillInfoId}/detail")
    public String showSeckillInfoDetail(Model model, @PathVariable("seckillInfoId") Integer seckillInfoId) {
        if(seckillInfoId == null) {
            return "redirect:/seckill/list";  //重定向，发送二次Request请求
        }

        SeckillInfo seckillInfo = seckillService.getSeckillInfoById(seckillInfoId);
        if(seckillInfo == null) {
            return "forward:/seckill/list";   //转发，服务器直接调用资源相应，Request资源共享
        }

        model.addAttribute("seckillInfo", seckillInfo);
        return "detail";
    }


    /**
     * 当前时间数值一定从服务器端获取, 防止提前参与秒杀
     * @return  包含当前时间的封装类
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getCurrentTime")
    public ResponseResult<LocalDateTime> getCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        return new ResponseResult<>(true, currentTime);
    }


    /**
     * 用来返回秒杀状态
     * @param seckillInfoId   对应的秒杀活动
     * @return  包含秒杀状态实体的封装类
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{seckillInfoId}/stateExposer")
    public ResponseResult<StateExposer> showStateExpoer(@PathVariable("seckillInfoId") Integer seckillInfoId) {
        ResponseResult<StateExposer> result;
        try {
            StateExposer exposer = seckillService.getCorrespondingStateExposer(seckillInfoId);
            result = new ResponseResult<>(true, exposer);
        } catch (Exception e) {
            logger.error("**********发生异常！**********");
            e.printStackTrace();
            result = new ResponseResult<>(false, e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "Lock with AOP")
    @RequestMapping(method = RequestMethod.GET, value = "/{seckillInfoId}/{encodedUrl}/executeSeckillWithAopLock")
    public ResponseResult<ExecutedResult> executeSeckillWithAopLock(@PathVariable("seckillInfoId") Integer seckillInfoId, @PathVariable("encodedUrl") String encodedUrl) {
        //先从缓存中查询用户, 空值则先让用户登录
        User user = (User) redisTemplate.opsForValue().get("user");
        if(user == null) {
            logger.error("**********用户未登录, 请先登录！**********");
            throw new MyException("请先登录！");
        }

        try {
            Callable<ExecutedResult> callable = () -> seckillService.executeSeckillTask(user.getId(), seckillInfoId, encodedUrl);
            ExecutedResult executedResult = executor.submit(callable).get();
            return new ResponseResult<>(true, executedResult);
        } catch(FailedSeckillException e) {
            ExecutedResult executedResult =  new ExecutedResult(seckillInfoId, SeckillStateType.FINISH);
            return new ResponseResult<>(false, executedResult);
        } catch(RepeatSeckillException e) {
            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.DUPLICATE);
            return new ResponseResult<>(false, executedResult);
        } catch(BaseSeckillException | InterruptedException | ExecutionException e) {
            //unknown Exception
            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.SYSTEM_ERROR);
            return new ResponseResult<>(false, executedResult);
        }
    }


    /**
     * 用户点击按钮后发送到此接口，开启秒杀执行过程
     * @param seckillInfoId  对应的活动Id
     * @param encodedUrl   需要检验的md5加密Url值
     * @return   包含执行结果的封装类
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{seckillInfoId}/{encodedValue}/startExecution")
    public ResponseResult<ExecutedResult> startExecution(@PathVariable("seckillInfoId") Integer seckillInfoId,
                                                         @PathVariable("encodedUrl") String encodedUrl) {
        User user = (User) redisTemplate.opsForValue().get("user");
        if(user == null) {
            return new ResponseResult<>(false, "用户没有登录");
        }

        try {
            ExecutedResult executedResult = seckillService.executeSeckillTask(user.getId(), seckillInfoId, encodedUrl);
            return new ResponseResult<>(true, executedResult);
        } catch(FailedSeckillException e) {
            ExecutedResult executedResult =  new ExecutedResult(seckillInfoId, SeckillStateType.FINISH);
            return new ResponseResult<>(false, executedResult);
        } catch(RepeatSeckillException e) {
            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.DUPLICATE);
            return new ResponseResult<>(false, executedResult);
        } catch(BaseSeckillException e) {
            //unknown Exception
            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.SYSTEM_ERROR);
            return new ResponseResult<>(false, executedResult);
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(SeckillController.class);

}
