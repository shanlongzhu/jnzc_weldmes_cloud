package com.gw.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author zhanghan
 * @Date 2021/6/17 20:35
 * @Description 将 前端请求 的 sessionId 与 用户的 shiroSession 进行绑定
 *
 * 接入 redis 后可将目前的注释放开,通过 redis 进行用户角色信息的获取
 */
@Component
public class SessionDaoConfig extends EnterpriseCacheSessionDAO {

   /* @Resource
    private RedisTemplate<Serializable, Session> redisTemplate;*/

   /**
    * @Date 2021/6/17 23:44
    * @Description  保证 当前用户 shiro会话 的一致性
    * @Params
    */
    @Override
    protected Serializable doCreate(Session session) {

        //获取 shiro会话 中的 sessionid
        Serializable sessionId = this.generateSessionId(session);

        SimpleSession simpleSession= (SimpleSession) session;

        //session 要和 sessionid 绑定
        simpleSession.setId(sessionId);

        return sessionId;
    }


    @Override
    protected Session doReadSession(Serializable sessionId) {
        //从redis中读取sessionid(先获取redis对value的操作对象，需要先设定key)
        //return redisTemplate.boundValueOps(sessionId).get();
        return null;
    }

    @Override
    protected void doUpdate(Session session) {
//        if (session instanceof ValidatingSession) {
//            ValidatingSession validatingSession = (ValidatingSession) session;
//            if (validatingSession.isValid()) {
//                redisTemplate.boundValueOps(session.getId()).set(session);
//            } else {
//                // 校验失败，说明未登录或者登录失效
//                redisTemplate.delete(session.getId());
//            }
//        } else {
//            redisTemplate.boundValueOps(session.getId()).set(session);
//        }
    }

    @Override
    protected void doDelete(Session session) {

        //redisTemplate.delete(session.getId());
    }



}
