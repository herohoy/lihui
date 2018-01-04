package com.today36524.api.user.service

import java.sql.SQLException

import com.isuwang.dapeng.core.SoaException
import com.today36524.api.user.dao.UserDao
import com.today36524.api.user.enums.UserStatusEnum
import com.today36524.api.user.request._
import com.today36524.api.user.response._
import org.springframework.beans.factory.annotation.Autowired

import scala.util.matching.Regex

class UserServiceImpl extends UserService {
  @Autowired
  var userDao:UserDao = _

  val unReg: Regex = """^[A-Za-z]{1}[A-Za-z0-9-_.]{1,19}$""".r
  val pwdReg = """^[\S]{8,20}$""".r
  val cellReg = """^1{1}[3578]{1}(\d){9}$""".r
  val mailReg = """^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$""".r
  val qqReg = """\d{4,12}""".r
  /**
    *
    **
  ### 用户注册
    **
  #### 业务描述
    * 用户注册账户，用户密码需要加盐之后存储(加盐方案还么确定,小伙伴可以自己随意设计个简单的加解密方案)
    **
  #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
  #### 输入
    *1.user_request.RegisterUserRequest
    **
  #### 前置检查
    *1. 手机号码规则验证
    *2. 手机号未被使用验证
    *3. 密码规则,字母数字八位混合
    **
  ####  逻辑处理
    *1.密码加盐处理
    *2.新增一条user记录
    *3.返回结果 user_response.RegisterUserResponse
    **
  #### 数据库变更
    *1. insert into user() values()
    **
  ####  事务处理
    * 无
    **
  ####  输出
    *1.user_response.RegisterUserResponse
    *
    **/
  override def registerUser(request: RegisterUserRequest): RegisterUserResponse
  = {
    preCheckReg(request.userName,request.passWord,request.telephone)
    if(userDao.getUserCountByPhone(request.telephone) >= 1)
      throw new SoaException("","手机号已注册")
    try{
      val res = userDao.addUserForRegister(request)
      RegisterUserResponse(res.userName,res.telephone
        ,UserStatusEnum(res.status),res.createdAt)
    }catch {
      case se:SoaException =>
        throw se
      case _ =>
        throw new SoaException("","后台错误，请联系管理员")
    }
  }

  /**
    *
    **
  ### 用户登录
    **
  #### 业务描述
    * 用户登录
    **
  #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
  #### 输入
    *1.user_request.LoginUserRequest
    **
  #### 前置检查
    *1.手机号码规则验证
    *2.密码规则,字母数字八位混合
    **
  ####  逻辑处理
    *1. 根据手机号码和密码查询用户记录
    *2. 异常用户状态的用户登录返回 Exception
    **
  #### 数据库变更
    *1. select *  from user where telphone = ? and password = ?
    **
  ####  事务处理
    * 无
    **
  ####  输出
    *1.user_response.LoginUserResponse
    *
    **/
override def login(request: LoginUserRequest): LoginUserResponse ={
  //使用手机号判断用户是否存在
  if(userDao.getUserCountByPhone(request.telephone) < 1)
    throw new SoaException("","手机号未注册")

  //密码加密处理过程 TODO

  //登录
  val u = userDao.authUser(request)
  checkUndefinedStatus(UserStatusEnum(u.status))
  checkDeleteStatus(UserStatusEnum(u.status))
  checkBlackStatus(UserStatusEnum(u.status))
  LoginUserResponse(u.userName,u.telephone,UserStatusEnum(u.status)
  ,u.integral,u.createdAt,u.updatedAt,u.email,u.qq)

}

  /**
    *
    **
    * ### 用户修改个人资料
    **
    * #### 业务描述
    * 用户再注册之后完善个人资料,完善资料增加积分5
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.ModifyUserRequest
    **
    * #### 前置检查
    *1. 邮箱规则验证
    *2. qq 规则验证
    *3. 用户状态判断只有用户状态为
    **
    * ####  逻辑处理
    *1. 根据输入的参数计算用户积分
    *2. 修改用户 email qq
    *2. 修改完成之后调用积分action增加用户积分(完善资料增加积分5) ChangeUserIntegralAction
    **
    * #### 数据库变更
    *1. update user set email = ? , qq = ? where id = ${userId}
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1.user_response.ModifyUserAction
    *
    **/
  override def modifyUser(request: ModifyUserRequest): ModifyUserResponse =
    {
      val r = userDao.updateUserForIntegral(request)
      checkAllStatus(UserStatusEnum(r.status))
      ModifyUserResponse(r.userName,r.telephone
      ,UserStatusEnum(r.status),r.updatedAt
      ,r.email,r.qq)
    }

  /**
    *
    **
    * ### 冻结用户接口
    **
    * #### 业务描述
    * 用户因为触犯一些游戏规则,后台自检程序或者管理员会冻结该用户
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.FreezeUserRequest
    **
    * #### 前置检查
    *1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能冻结)
    **
    * ####  逻辑处理
    *1. 设置用户状态为 FREEZE
    **
    * #### 数据库变更
    *1. update user set status = ? , remark = ? where id = ${userId}
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1.user_response.FreezeUserResponse
    *
    **/
  override def freezeUser(request: FreezeUserRequest): FreezeUserResponse =
    {
      val r = userDao.updateUserFreeze(request)
      checkUndefinedStatus(UserStatusEnum(r.status))
      checkDeleteStatus(UserStatusEnum(r.status))
      checkBlackStatus(UserStatusEnum(r.status))
      checkFreezeStatus(UserStatusEnum(r.status))
      FreezeUserResponse(r.userId,UserStatusEnum(r.status),r.remark)
    }

  /**
    *
    **
    * ### 拉黑用户接口
    **
    * #### 业务描述
    * 用户因为触犯一些游戏规则,后台自检程序或者管理员会拉黑该用户,拉黑用户把用户的积分置为0
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.BlackUserRequest
    **
    * #### 前置检查
    *1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能拉黑)
    **
    * ####  逻辑处理
    *1. 设置用户状态为  BLACK
    *2. 调用积分修改接口 ChangeUserIntegralAction
    **
    * #### 数据库变更
    *1. update user set status = ? , remark = ? where id = ${userId}
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1.user_response.BlackUserResponse
    *
    **/
  override def blackUser(request: BlackUserRequest): BlackUserResponse =
    {
      val r = userDao.updateUserBlack(request)
      checkUndefinedStatus(UserStatusEnum(r.status))
      checkDeleteStatus(UserStatusEnum(r.status))
      checkBlackStatus(UserStatusEnum(r.status))
      BlackUserResponse(r.userId,UserStatusEnum(r.status),r.remark)
    }

  /**
    *
    **
    * ### 记录积分改变流水
    **
    * #### 业务描述
    * 用户因为完成一些游戏规则或者触犯游戏规则导致积分减少或者增加,调用该接口修改用户积分
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.ChangeIntegralRequest
    **
    * #### 前置检查
    *1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能冻结)
    **
    * ####  逻辑处理
    *1. 设置用户状态为 FREEZE
    **
    * #### 数据库变更
    *1. update user set integral = ?  where id = ${userId}
    *2. insert into integral_journal() values()
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1. i32 流水 Id
    *
    **/
  override def changeUserIntegral(request: ChangeIntegralRequest): Int =
    userDao.updateIntegralChange(request)

  /**
    *
    **
    * ### 解冻用户接口
    **
    * #### 业务描述
    * 用户被冻结后，由于申请恢复或申诉错误操作并得到认可，对该用户进行解冻操作
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.UnreezeUserRequest
    **
    * #### 前置检查
    *1.用户状态检查(未冻结,已拉黑,已逻辑删除的用户不能解冻)
    **
    * ####  逻辑处理
    *1. 设置用户状态为 ACTIVATED
    **
    * #### 数据库变更
    *1. update user set status = ? , remark = ? where id = ${userId}
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1.user_response.UnfreezeUserResponse
    *
    **/
  override def unfreezeUser(request: UnfreezeUserRequest): UnfreezeUserResponse =
    {
      val r = userDao.updateUserUnfreeze(request)
      checkUnfreezeStatus(UserStatusEnum(r.status))
      UnfreezeUserResponse(r.userId,UserStatusEnum(r.status),r.remark)
    }

  /**
    *
    **
  ### 用户注册
    **
  #### 业务描述
    * 用户注册账户，用户密码需要加盐之后存储(加盐方案还么确定,小伙伴可以自己随意设计个简单的加解密方案)
    **
  #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
  #### 输入
    *1.user_request.RegisterUserRequest
    **
  #### 前置检查
    *1. 手机号码规则验证
    *2. 手机号未被使用验证
    *3. 密码规则,字母数字八位混合
    **
  ####  逻辑处理
    *1.密码加盐处理
    *2.新增一条user记录
    *3.返回结果 user_response.RegisterUserResponse
    **
  #### 数据库变更
    *1. insert into user() values()
    **
  ####  事务处理
    * 无
    **
  ####  输出
    *1.user_response.RegisterUserResponse
    *
    **/
  override def registerUserSql(request: RegisterUserRequest): RegisterUserSqlResponse
  = ??? //userDao.addUserForRegister(request)

  /**
    *
    **
  ### 用户登录
    **
  #### 业务描述
    * 用户登录
    **
  #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
  #### 输入
    *1.user_request.LoginUserRequest
    **
  #### 前置检查
    *1.手机号码规则验证
    *2.密码规则,字母数字八位混合
    **
  ####  逻辑处理
    *1. 根据手机号码和密码查询用户记录
    *2. 异常用户状态的用户登录返回 Exception
    **
  #### 数据库变更
    *1. select *  from user where telphone = ? and password = ?
    **
  ####  事务处理
    * 无
    **
  ####  输出
    *1.user_response.LoginUserResponse
    *
    **/
  override def loginSql(request: LoginUserRequest): LoginUserSqlResponse
  = ???

  /**
    *
    **
    * ### 用户修改个人资料
    **
    * #### 业务描述
    * 用户再注册之后完善个人资料,完善资料增加积分5
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.ModifyUserRequest
    **
    * #### 前置检查
    *1. 邮箱规则验证
    *2. qq 规则验证
    *3. 用户状态判断只有用户状态为
    **
    * ####  逻辑处理
    *1. 根据输入的参数计算用户积分
    *2. 修改用户 email qq
    *2. 修改完成之后调用积分action增加用户积分(完善资料增加积分5) ChangeUserIntegralAction
    **
    * #### 数据库变更
    *1. update user set email = ? , qq = ? where id = ${userId}
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1.user_response.ModifyUserAction
    *
    **/
  override def modifyUserSql(request: ModifyUserRequest): ModifyUserSqlResponse =
    ???

  /**
    *
    **
    * ### 冻结用户接口
    **
    * #### 业务描述
    * 用户因为触犯一些游戏规则,后台自检程序或者管理员会冻结该用户
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.FreezeUserRequest
    **
    * #### 前置检查
    *1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能冻结)
    **
    * ####  逻辑处理
    *1. 设置用户状态为 FREEZE
    **
    * #### 数据库变更
    *1. update user set status = ? , remark = ? where id = ${userId}
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1.user_response.FreezeUserResponse
    *
    **/
  override def freezeUserSql(request: FreezeUserRequest): FreezeUserSqlResponse =
    ??? //userDao.updateUserFreeze(request)

  /**
    *
    **
    * ### 拉黑用户接口
    **
    * #### 业务描述
    * 用户因为触犯一些游戏规则,后台自检程序或者管理员会拉黑该用户,拉黑用户把用户的积分置为0
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.BlackUserRequest
    **
    * #### 前置检查
    *1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能拉黑)
    **
    * ####  逻辑处理
    *1. 设置用户状态为  BLACK
    *2. 调用积分修改接口 ChangeUserIntegralAction
    **
    * #### 数据库变更
    *1. update user set status = ? , remark = ? where id = ${userId}
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1.user_response.BlackUserResponse
    *
    **/
  override def blackUserSql(request: BlackUserRequest): BlackUserSqlResponse =
    ??? //userDao.updateUserBlack(request)

  /**
    *
    **
    * ### 解冻用户接口
    **
    * #### 业务描述
    * 用户被冻结后，由于申请恢复或申诉错误操作并得到认可，对该用户进行解冻操作
    **
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    **
    * #### 输入
    *1.user_request.UnreezeUserRequest
    **
    * #### 前置检查
    *1.用户状态检查(未冻结,已拉黑,已逻辑删除的用户不能解冻)
    **
    * ####  逻辑处理
    *1. 设置用户状态为 ACTIVATED
    **
    * #### 数据库变更
    *1. update user set status = ? , remark = ? where id = ${userId}
    **
    * ####  事务处理
    *1. 无
    **
    * ####  输出
    *1.user_response.UnfreezeUserResponse
    *
    **/
  override def unfreezeUserSql(request: UnfreezeUserRequest): UnfreezeUserSqlResponse =
    ??? //userDao.updateUserUnfreeze(request)


  @throws (classOf[SoaException])
  private def preCheckReg(username:String,password:String
                       ,telephone:String): Unit ={
    if(username==null || username.isEmpty)
      throw new SoaException("","用户名不能为空")
    if(!unReg.pattern.matcher(username).matches)
      throw new SoaException("","用户名必须为2-20位的英文字母与数字组合，且首字符必须为字母")
    if(password==null || password.isEmpty)
      throw new SoaException("","密码不能为空")
    if(!pwdReg.pattern.matcher(password).matches)
      throw new SoaException("","密码必须为8-20位的任意字符")
    if(telephone==null || telephone.isEmpty)
      throw new SoaException("","手机号不能为空")
    if(!cellReg.pattern.matcher(telephone).matches)
      throw new SoaException("","手机号不符合规范")
  }

  @throws (classOf[SoaException])
  private def checkBlackStatus(status:UserStatusEnum): Unit ={
    if(status.eq(UserStatusEnum.BLACK)){
      throw new SoaException("","该用户已被列入黑名单，请联系管理员")
    }
  }
  @throws (classOf[SoaException])
  private def checkFreezeStatus(status:UserStatusEnum): Unit ={
    if(status.eq(UserStatusEnum.FREEZED)){
      throw new SoaException("","该用户已被冻结，请联系管理员")
    }
  }
  @throws (classOf[SoaException])
  private def checkDeleteStatus(status:UserStatusEnum): Unit ={
    if(status.eq(UserStatusEnum.DELETE)){
      throw new SoaException("","该用户已被删除，请联系管理员")
    }
  }
  @throws (classOf[SoaException])
  private def checkUndefinedStatus(status:UserStatusEnum): Unit ={
    if(status.eq(UserStatusEnum.UNDEFINED)){
      throw new SoaException("","该用户已被删除，请联系管理员")
    }
  }
  @throws (classOf[SoaException])
  private def checkAllStatus(status:UserStatusEnum): Unit ={
    checkUndefinedStatus(status)
    checkDeleteStatus(status)
    checkBlackStatus(status)
    checkFreezeStatus(status)
  }
  @throws (classOf[SoaException])
  private def checkUnfreezeStatus(status:UserStatusEnum): Unit ={
    if(!status.eq(UserStatusEnum.FREEZED)){
      throw new SoaException("","该用户并非处于冻结状态")
    }
  }
}
