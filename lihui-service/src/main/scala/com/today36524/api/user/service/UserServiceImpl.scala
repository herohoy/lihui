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
  ### 用户注册
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
  ### 用户登录
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
    * ### 用户修改个人资料
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
    * ### 冻结用户接口
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
    * ### 拉黑用户接口
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
    * ### 记录积分改变流水
    **/
  override def changeUserIntegral(request: ChangeIntegralRequest): Int =
    userDao.updateIntegralChange(request)

  /**
    * ### 解冻用户接口
    **/
  override def unfreezeUser(request: UnfreezeUserRequest): UnfreezeUserResponse =
    {
      val r = userDao.updateUserUnfreeze(request)
      checkUnfreezeStatus(UserStatusEnum(r.status))
      UnfreezeUserResponse(r.userId,UserStatusEnum(r.status),r.remark)
    }

  /**
  ### 用户注册
    **/
  override def registerUserSql(request: RegisterUserRequest): RegisterUserSqlResponse
  = ??? //userDao.addUserForRegister(request)

  /**
  ### 用户登录
    **/
  override def loginSql(request: LoginUserRequest): LoginUserSqlResponse
  = ???

  /**
    * ### 用户修改个人资料
    **/
  override def modifyUserSql(request: ModifyUserRequest): ModifyUserSqlResponse =
    ???

  /**
    * ### 冻结用户接口
    **/
  override def freezeUserSql(request: FreezeUserRequest): FreezeUserSqlResponse =
    ??? //userDao.updateUserFreeze(request)

  /**
    * ### 拉黑用户接口
    **/
  override def blackUserSql(request: BlackUserRequest): BlackUserSqlResponse =
    ??? //userDao.updateUserBlack(request)

  /**
    * ### 解冻用户接口
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
