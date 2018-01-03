package com.today36524.api.user.dao

import com.alibaba.druid.pool.DruidDataSource
import com.isuwang.dapeng.core.SoaException
import com.today36524.api.user.request._
import com.today36524.api.user.response._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import wangzx.scala_commons.sql._

@Repository
class UserDao {
  @Autowired
  var dataSource : DruidDataSource = _

  /**
    * 注册时添加用户
    * @param request 注册请求中包含的用户信息
    * @return 执行结果数字
    */
  @throws(classOf[SoaException])
  def addUserForRegister(request: RegisterUserRequest)
  = {
    dataSource.executeUpdate(
      sql"""
         insert into user
         (
         user_name
         ,password
         ,telephone
         ,email
         ,qq
         ,integral
         ,created_at
         ,created_by
         ,updated_at
         ,updated_by
         ,remark
         ) values
         (
         ${request.userName}
         ,${request.passWord}
         ,${request.telephone}
         ,''
         ,''
         ,0
         ,CURRENT_TIMESTAMP
         ,2000000001
         ,CURRENT_TIMESTAMP
         ,2000000001
         ,''
         )
       """)

    dataSource.executeUpdate(
      sql"""
           insert into integral_journal
           (
              user_id,
                integral_type,
                integral_price,
                integral_source,
                integral,
                created_at,
                created_by,
                updated_at,
                updated_by,
                remark
           )
           select
              id as user_id
              ,1 as integral_type
              ,0 as integral_price
              ,1 as integral_source
              ,0 as integral
              ,CURRENT_TIMESTAMP as created_at
               ,2000000001 as created_by
               ,CURRENT_TIMESTAMP as updated_at
               ,2000000001 as updated_by
               ,'' as remark
           from user where telephone=${request.telephone}
         """)

    try{
      dataSource.row[RegisterUserSqlResponse](
        sql"""
       select
       user_name as userName,
       telephone,
       status,
       unix_timestamp(created_at) as createdAt
       from user
       where telephone=${request.telephone}
       """).get
    }catch {
      case e:NoSuchElementException =>
        throw new SoaException("","用户注册失败")
    }
  }

  /**
    * 判断用户是否存在
    * @param loginUser
    * @throws SoaException
    * @return
    */
  @throws(classOf[SoaException])
  def getUserByPhone(loginUser:LoginUserRequest) ={
    try{
      dataSource.row[LoginUserSqlResponse](
        sql"""
       select
       user_name as userName,
       telephone,
       status,
       integral,
       unix_timestamp(created_at) as createdAt,
       unix_timestamp(updated_at) as updatedAt,
       email,
       qq
       from user
       where telephone=${loginUser.telephone}
       """).get
    }catch {
      case e:NoSuchElementException =>
        throw new SoaException("","未找到当前用户")
    }
  }

  /**
    * 用户登录验证
    * @param loginUser
    * @throws SoaException
    * @return
    */
  @throws(classOf[SoaException])
  def authUser(loginUser:LoginUserRequest) ={
    try{
    dataSource.row[LoginUserSqlResponse](
      sql"""
       select
       user_name as userName,
       telephone,
       status,
       integral,
       unix_timestamp(created_at) as createdAt,
       unix_timestamp(updated_at) as updatedAt,
       email,
       qq
       from user
       where telephone=${loginUser.telephone}
         and password=${loginUser.passWord}
       """).get
    }catch {
      case e:NoSuchElementException =>
        throw new SoaException("","密码不正确")
    }
  }

  /**
    * TODO
    * @param mdRequest
    * @return
    */
  def updateUserForIntegral(mdRequest:ModifyUserRequest) = {
    dataSource.executeUpdate(
      sql"""
           update user set
           email = ${mdRequest.email},
           qq = ${mdRequest.qq}
           where id=1000000000
         """)
  }

  /**
    * 冻结用户
    * @param request
    * @throws com.isuwang.dapeng.core.SoaException
    * @return
    */
  @throws(classOf[SoaException])
  def updateUserFreeze(request: FreezeUserRequest) = {
    dataSource.executeUpdate(
      sql"""
           update user set
           status = 3,
           remark = ${request.remark}
         where id = ${request.userId}
         """)

    try{
      dataSource.row[FreezeUserSqlResponse](
        sql"""
       select
       id as userId,
       status,
       remark
       from user
       where id=${request.userId}
       """).get
    }catch {
      case e:NoSuchElementException =>
        throw new SoaException("","冻结用户失败")
    }
  }

  /**
    * 拉黑用户
    * @param request
    * @throws com.isuwang.dapeng.core.SoaException
    * @return
    */
  @throws(classOf[SoaException])
  def updateUserBlack(request:BlackUserRequest) = {
    dataSource.executeUpdate(
      sql"""
           update user set
           status = 4,
           remark = ${request.remark}
         where id = ${request.userId}
         """)

    try{
      dataSource.row[BlackUserSqlResponse](
        sql"""
       select
       id as userId,
       status,
       remark
       from user
       where id=${request.userId}
       """).get
    }catch {
      case e:NoSuchElementException =>
        throw new SoaException("","用户设置黑名单失败")
    }
  }

  /**
    * 解冻用户
    * @param request
    * @throws com.isuwang.dapeng.core.SoaException
    * @return
    */
  @throws(classOf[SoaException])
  def updateUserUnfreeze(request: UnfreezeUserRequest) = {
    dataSource.executeUpdate(
      sql"""
           update user set
           status = 1,
           remark = ${request.remark}
         where id = ${request.userId}
         """)

    try{
      dataSource.row[UnfreezeUserSqlResponse](
        sql"""
       select
       id as userId,
       status,
       remark
       from user
       where id=${request.userId}
       """).get
    }catch {
      case e:NoSuchElementException =>
        throw new SoaException("","冻结用户失败")
    }
  }


  /**
    * TODO
    * @param request
    * @throws com.isuwang.dapeng.core.SoaException
    * @return
    */
  @throws(classOf[SoaException])
  def updateIntegralChange(request:ChangeIntegralRequest) = {
    dataSource.executeUpdate(
      sql"""
           insert into integral_journal
         """)
  }

}
