package com.today36524.api.user.dao

import com.alibaba.druid.pool.DruidDataSource
import com.today36524.api.user.enums.IntegralTypeEnum
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
  def addUserForRegister(request: RegisterUserRequest)
  = {
    //添加用户表信息
    val uid = dataSource.generateKey[Int](
      sql"""
         insert into user set
         user_name = ${request.userName} ,password = ${request.passWord},
         telephone = ${request.telephone} ,email = '' ,qq = '' ,integral = 0,
         created_at = CURRENT_TIMESTAMP ,created_by = 2000000001,
         updated_at = CURRENT_TIMESTAMP ,updated_by = 2000000001 ,remark = ''
       """)

    //添加积分流水信息
    dataSource.executeUpdate(
      sql"""
           insert into integral_journal set
              user_id = $uid, integral_type = 1, integral_price = 0, integral_source = 1,
                integral = 0, created_at = CURRENT_TIMESTAMP, created_by = 2000000001,
                updated_at = CURRENT_TIMESTAMP, updated_by = 2000000001, remark = ''
         """)

    //返回数据
      dataSource.row[Row](
        sql"""
       select user_name, telephone, status, created_at from user
       where telephone=${request.telephone}
       """).get
  }

  /**
    * 判断用户是否存在，更好的方式，不会频繁抛异常
    * @param userPhone
    * @return
    */
  def getUserCountByPhone(userPhone:String) =
    dataSource.row[Int](
      sql"""
       select count(1) from user where telephone=$userPhone
       """).get

  /**
    * 通过id判断用户是否存在，更好的方式，不会频繁抛异常
    * @param id
    * @return
    */
  def getUserCountById(id:String) =
    dataSource.row[Int](
      sql"""
       select count(1) from user where id=$id
       """).get

  /**
    * 用户登录验证
    * @param loginUser
    * @return
    */
  def authUser(loginUser:LoginUserRequest) =
    dataSource.row[Row](
      sql"""
      select
       user_name, telephone, status, integral,created_at,updated_at, email, qq
      from user
       where telephone=${loginUser.telephone}
         and password=${loginUser.passWord}
       """)

  /**
    * 完善用户信息并获得积分
    * @param mdRequest
    * @return
    */
  def updateUserForIntegral(mdRequest:ModifyUserRequest) = {

    //获取用户当前积分和id
    val nowusr = dataSource.row[Row](
      sql"""
           select integral,email,qq from user where id=${mdRequest.userId}
         """).get
    val intgr = nowusr.cell("integral").getInt
    val eml = nowusr.cell("email").getString
    val qq = nowusr.cell("qq").getString

    //只有初始化（即email和qq都为空）时才会添加积分
    if(eml.isEmpty && qq.isEmpty){
      //更新用户积分
      dataSource.executeUpdate(
        sql"""
         update user set email = ${mdRequest.email}, qq = ${mdRequest.qq}, status = 2,
         integral = $intgr + 5 where id=${mdRequest.userId}
           """)

      //添加积分流水
      dataSource.executeUpdate(sql"""
         insert into integral_journal set
            user_id = ${mdRequest.userId}, integral_type = 1, integral_price = 5,
            integral_source = 1, integral = $intgr + 5,
            created_at = CURRENT_TIMESTAMP, created_by = 2000000001,
            updated_at = CURRENT_TIMESTAMP, updated_by = 2000000001, remark = ''
           """)
    }

    //返回数据
    dataSource.row[Row](
      sql"""
           select user_name, telephone, status,updated_at, email, qq
           from user where id=${mdRequest.userId}
         """).get
  }

  /**
    * 获取用户状态
    * @param id
    * @return
    */
  def getUserStatus(id:String): Option[Int] = dataSource.row[Int](
    sql"""
         select status from user where id = $id
       """)

  /**
    * 获取用户状态
    * @param telephone
    * @return
    */
  def getUserStatusByPhone(telephone:String): Option[Int] = dataSource.row[Int](
    sql"""
         select status from user where telephone = $telephone
       """)

  /**
    * 冻结用户
    * @param request
    * @return
    */
  def updateUserFreeze(request: FreezeUserRequest) = {
    //冻结用户
    dataSource.executeUpdate(sql"""
           update user set status = 3, remark = ${request.remark}
           where id = ${request.userId}
         """)

    //返回用户信息
    dataSource.row[FreezeUserSqlResponse](sql"""
       select id as userId, status, remark from user where id=${request.userId}
       """).get
  }

  /**
    * 拉黑用户
    * @param request
    * @return
    */
  def updateUserBlack(request:BlackUserRequest) = {
    //查询之前的积分，从user表获取
    val lastIntgr = dataSource.row[Int](sql"""
           select integral from user where id=${request.userId}
         """).get

    //添加一条负增长的积分流水
    dataSource.executeUpdate(sql"""
           insert into integral_journal set
              user_id = ${request.userId}, integral_type = 2, integral_price = $lastIntgr,
                integral_source = 2, integral = 0, created_at = CURRENT_TIMESTAMP,
                created_by = 2000000001, updated_at = CURRENT_TIMESTAMP,
                updated_by = 2000000001, remark = ''
         """)

    //拉黑用户
    dataSource.executeUpdate(sql"""
           update user set status = 4, integral = 0, remark = ${request.remark}
           where id = ${request.userId}
         """)

    //返回已拉黑的用户信息
    dataSource.row[BlackUserSqlResponse](sql"""
         select id as userId, status, remark from user where id=${request.userId}
       """).get
  }

  /**
    * 解冻用户
    * @param request
    * @return
    */
  def updateUserUnfreeze(request: UnfreezeUserRequest) = {
    //解冻用户，按照目前的模式也可用于恢复黑名单用户和已逻辑删除用户
    dataSource.executeUpdate(sql"""
        update user set status = 1, remark = ${request.remark}
        where id = ${request.userId}
         """)

    //返回用户信息
    dataSource.row[UnfreezeUserSqlResponse](sql"""
       select id as userId,status,remark from user where id=${request.userId}
       """).get
  }


  /**
    * 改变
    * @param request
    * @return
    */
  def updateIntegralChange(request:ChangeIntegralRequest) = {
    val integType = request.integralType.id
    val integSrc = request.integralSource.id

    //查询之前的积分，从user表获取
    val lastIntgr = dataSource.row[Int](sql"""
           select integral from user where id=${request.userId}
         """).get

    //先更新user表的积分值
    val integPrice =
      if(IntegralTypeEnum.ADD.eq(request.integralType))
        request.integralPrice.toInt
      else if (IntegralTypeEnum.MINUS.eq(request.integralType))
        -request.integralPrice.toInt
      else 0
    dataSource.executeUpdate(sql"""
           update user set
           integral = integral + $integPrice
           where id=${request.userId}
         """)

    //再添加流水
    val gid =
    dataSource.generateKey[Int](sql"""
           insert into integral_journal set
              user_id = ${request.userId}, integral_type = ${integType},
              integral_price = ${request.integralPrice}, integral_source = ${integSrc},
              integral = ${lastIntgr + integPrice}, created_at = CURRENT_TIMESTAMP,
              created_by = 2000000001, updated_at = CURRENT_TIMESTAMP,
              updated_by = 2000000001, remark = ''
         """)
    gid
  }

}
