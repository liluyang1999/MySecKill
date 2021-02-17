package com.example.lly.controller;

import com.example.lly.dto.ExecutedResult;
import com.example.lly.dto.StateExposer;
import com.example.lly.entity.rbac.User;
import com.example.lly.exception.BaseSeckillException;
import com.example.lly.exception.FailedSeckillException;
import com.example.lly.exception.RepeatSeckillException;
import com.example.lly.module.security.JwtTokenUtil;
import com.example.lly.service.JwtAuthService;
import com.example.lly.service.SeckillService;
import com.example.lly.service.UserSecurityService;
import com.example.lly.util.enumeration.SeckillStateType;
import com.example.lly.util.result.ResponseEnum;
import com.example.lly.util.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.*;

@Api(tags = "MySeckill")
@RestController
public class SeckillController {

    private static final int corePoolScale = Runtime.getRuntime().availableProcessors();   //JVM可用的核心数量
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolScale, corePoolScale + 1, 101, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1500));

    /**
     * 当前时间数值一定从服务器端获取, 防止提前参与秒杀
     *
     * @return 包含当前时间的封装类, 时间以秒数表示
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getCurrentTime")
    public ResponseResult<Long> getCurrentTime() {
        Long currentTime = System.currentTimeMillis();
        return ResponseResult.success(currentTime);
    }


    private final SeckillService seckillService;
    private final JwtAuthService jwtAuthService;
    private final UserSecurityService userSecurityService;

    @Autowired
    public SeckillController(SeckillService seckillService, JwtAuthService jwtAuthService, UserSecurityService userSecurityService) {
        this.seckillService = seckillService;
        this.jwtAuthService = jwtAuthService;
        this.userSecurityService = userSecurityService;
    }

    /**
     * 用来返回秒杀状态
     *
     * @param seckillInfoId 对应的秒杀活动
     * @return 包含秒杀状态实体的封装类  加密链接
     */
    @RequestMapping(value = "/{seckillInfoId}/showStateExposer")
    public ResponseResult<StateExposer> showStateExpoer(@PathVariable("seckillInfoId") Integer seckillInfoId,
                                                        HttpServletRequest request) {
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        if (!authenticateToken(token)) {
            logger.error("请求秒杀链接时未检测到用户的登录信息, 请重新登录");
            return ResponseResult.error(ResponseEnum.NOT_LOGIN);
        }


        ResponseResult<StateExposer> result;
        try {
            StateExposer exposer = seckillService.getCorrespondingStateExposer(seckillInfoId);
            result = ResponseResult.success(exposer);
        } catch (Exception e) {
            logger.error("**********发生异常！**********");
            e.printStackTrace();
            result = ResponseResult.error(ResponseEnum.FAILED);
        }
        return result;
    }

    /**
     * 用户点击按钮后发送到此接口，开启秒杀执行过程
     *
     * @param seckillInfoId 对应的活动Id
     * @param encodedUrl    需要检验的md5加密Url值
     * @param request       包含执行结果的封装类
     * @return 包含执行结果的封装类
     */
    @ApiOperation(value = "Lock with AOP")
    @RequestMapping(value = "/{seckillInfoId}/{encodedUrl}/executeSeckillWithAopLock")
    public ResponseResult<ExecutedResult> executeSeckillWithAopLock(@PathVariable("seckillInfoId") Integer seckillInfoId,
                                                                    @PathVariable("encodedUrl") String encodedUrl,
                                                                    HttpServletRequest request) {
        //先从缓存中查询用户, 空值则先让用户登录
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        if (!authenticateToken(token)) {
            logger.error("请求秒杀链接时未检测到用户的登录信息, 请重新登录");
            return ResponseResult.error(ResponseEnum.NOT_LOGIN);
        }

        String username = JwtTokenUtil.getUsernameFromToken(token);
        User user = userSecurityService.getUserByUsername(username);
        try {
            Callable<ExecutedResult> callable = () -> seckillService.executeSeckillTask(user.getId(), seckillInfoId, encodedUrl);
            ExecutedResult executedResult = executor.submit(callable).get();
            return ResponseResult.success(executedResult);
        } catch (FailedSeckillException e) {
            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.FINISH);
            return ResponseResult.error(ResponseEnum.FAILED);
        } catch (RepeatSeckillException e) {
            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.DUPLICATE);
            return ResponseResult.error(ResponseEnum.FAILED);
        } catch (BaseSeckillException | InterruptedException | ExecutionException e) {
            //unknown Exception
            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.SYSTEM_ERROR);
            return ResponseResult.error(ResponseEnum.FAILED);
        }
    }

    private boolean authenticateToken(String token) {
        return !StringUtils.isEmpty(token)
                && !jwtAuthService.validateExpiration(token)
                && jwtAuthService.validateUsername(token);
    }

    private static final Logger logger = LoggerFactory.getLogger(SeckillController.class);

}


//    /**
//     * 用户点击按钮后发送到此接口，开启秒杀执行过程
//     *
//     * @param seckillInfoId 对应的活动Id
//     * @param encodedUrl    需要检验的md5加密Url值
//     * @return 包含执行结果的封装类
//     */
//    @RequestMapping(value = "/{seckillInfoId}/{encodedUrl}/startExecution")
//    public ResponseResult<ExecutedResult> startExecution(@PathVariable("seckillInfoId") Integer seckillInfoId,
//                                                         @PathVariable("encodedUrl") String encodedUrl,
//                                                         HttpServletRequest request) {
//        String username = request.getParameter("user");
//        User user = (User) redisTemplate.opsForHash().get("users", username);
//        if(user == null) {
//            logger.error("用户未登录, 请先检查");
//            return ResponseResult.error(ResponseEnum.NOT_LOGIN);
//        }
//
//        try {
//            ExecutedResult executedResult = seckillService.executeSeckillTask(user.getId(), seckillInfoId, encodedUrl);
//            return ResponseResult.success(executedResult);
//        } catch (FailedSeckillException e) {
//            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.FINISH);
//            return ResponseResult.error(ResponseEnum.FAILED);
//        } catch (RepeatSeckillException e) {
//            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.DUPLICATE);
//            return ResponseResult.error(ResponseEnum.FAILED);
//        } catch (BaseSeckillException e) {
//            //unknown Exception
//            ExecutedResult executedResult = new ExecutedResult(seckillInfoId, SeckillStateType.SYSTEM_ERROR);
//            return ResponseResult.error(ResponseEnum.FAILED);
//        }
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/requestSeckillInfoInProgressList")
//    public ResponseResult<List<SeckillInfo>> requestSeckillInfoInProgressList(HttpServletRequest request) {
//        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
//
//        if (!authenticateToken(token)) {
//            return ResponseResult.error(ResponseEnum.FAILED);
//        }
//        List<SeckillInfo> seckillInfoInProgressList = seckillService.getAllSeckillInfoInProgress();
//        return ResponseResult.success(seckillInfoInProgressList);
//    }
//
//
//    @RequestMapping(value = "/requestSeckillInfoInFutureList")
//    public ResponseResult<List<SeckillInfo>> requestSeckillInfoInFutureList(HttpServletRequest request) {
//        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
//        if (StringUtils.isEmpty(token) || jwtAuthService.validateExpiration(token) || !jwtAuthService.validateUsername(token)) {
//            return ResponseResult.error(ResponseEnum.FAILED);
//        }
//        List<SeckillInfo> seckillInfoInFutureList = seckillService.getAllSeckillInfoInFuture();
//        return ResponseResult.success(seckillInfoInFutureList);
//    }
//
//
//    @RequestMapping(method = RequestMethod.GET, value = "/requestSeckillInfoDetail/{seckillInfoId}")
//    public String requestSeckillInfoDetail(Model model, @PathVariable("seckillInfoId") Integer seckillInfoId) {
//        if (seckillInfoId == null) {
//            return "redirect:/seckill/list";  //重定向，发送二次Request请求
//        }
//
//        SeckillInfo seckillInfo = seckillService.getSeckillInfoById(seckillInfoId);
//        if (seckillInfo == null) {
//            return "forward:/seckill/list";   //转发，服务器直接调用资源相应，Request资源共享
//        }
//
//        model.addAttribute("seckillInfo", seckillInfo);
//        return "detail";
//    }
