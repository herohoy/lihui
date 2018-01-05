package com.today36524.api.user.service

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

  val USERNAME_REG: Regex = """^[A-Za-z]{1}[A-Za-z0-9-_.]{1,19}$""".r
  val PWD_REG = """^[\S]{8,20}$""".r
  val CELL_REG = """^1{1}[3578]{1}[0-9]{9}$""".r
  val MAIL_REG = """^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$""".r
  val QQ_REG = """\d{4,12}""".r
  val INT_REG = """^[0-9]{1,10}$""".r
  /**
  ### 用户注册
    **/
  override def registerUser(request: RegisterUserRequest): RegisterUserResponse
  = {
    assert(!(request.userName==null || request.userName.isEmpty),"用户名不能为空")
    assert(USERNAME_REG.pattern.matcher(request.userName).matches,
      "用户名必须为2-20位的英文字母与数字组合，且首字符必须为字母")
    assert(!(request.passWord==null || request.passWord.isEmpty),"密码不能为空")
    assert(PWD_REG.pattern.matcher(request.passWord).matches,
      "密码必须为8-20位的任意字符")
    assert(!(request.telephone==null || request.telephone.isEmpty),"手机号不能为空")
    assert(CELL_REG.pattern.matcher(request.telephone).matches,
      "手机号不符合规范")

    assert(userDao.getUserCountByPhone(request.telephone) < 1,"手机号已注册")

    //密码加密处理过程 TODO

      val res = userDao.addUserForRegister(request)
      RegisterUserResponse(res.userName,res.telephone
        ,UserStatusEnum(res.status),res.createdAt)
  }

  /**
  ### 用户登录
    **/
override def login(request: LoginUserRequest): LoginUserResponse ={
  assert(!(request.telephone==null || request.telephone.isEmpty),"手机号不能为空")
  assert(CELL_REG.pattern.matcher(request.telephone).matches,
    "手机号不符合规范")
  //使用手机号判断用户是否存在
  assert(userDao.getUserCountByPhone(request.telephone) == 1,"手机号未注册")

  //密码加密处理过程 TODO

  //登录
  val u = userDao.authUser(request).getOrElse(throw new SoaException("","密码不正确"))

  assert(!UserStatusEnum(u.status).eq(UserStatusEnum.UNDEFINED),
    "该用户状态未知，请联系管理员")
  assert(!UserStatusEnum(u.status).eq(UserStatusEnum.DELETE),
    "该用户已被删除，请联系管理员")
  assert(!UserStatusEnum(u.status).eq(UserStatusEnum.BLACK),
    "该用户已被列入黑名单，请联系管理员")

  LoginUserResponse(u.userName,u.telephone,UserStatusEnum(u.status)
  ,u.integral,u.createdAt,u.updatedAt,u.email,u.qq)

}

  /**
    * ### 用户修改个人资料
    **/
  override def modifyUser(request: ModifyUserRequest): ModifyUserResponse =
    {
      assert(!(request.telephone==null || request.telephone.isEmpty),"手机号不能为空")
      assert(CELL_REG.pattern.matcher(request.telephone).matches,
        "手机号不符合规范")
      assert(!(request.email==null || request.email.isEmpty),"邮箱不能为空")
      assert(MAIL_REG.pattern.matcher(request.email).matches,
        "邮箱不符合规范")
      assert(!(request.qq==null || request.qq.isEmpty),"QQ号不能为空")
      assert(QQ_REG.pattern.matcher(request.qq).matches,
        "QQ号不符合规范")

      val status = userDao.getUserStatusByPhone(request.telephone)
      //使用手机号判断用户是否存在
      assert(status.isInstanceOf[Some[Int]],"手机号未注册")

      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.UNDEFINED),
        "该用户状态未知，请联系管理员")
      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.DELETE),
        "该用户已被删除，请联系管理员")
      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.BLACK),
        "该用户已被列入黑名单，请联系管理员")
      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.FREEZED),
        "该用户已被冻结，请联系管理员")

      val r = userDao.updateUserForIntegral(request)
      ModifyUserResponse(r.userName,r.telephone
      ,UserStatusEnum(r.status),r.updatedAt
      ,r.email,r.qq)
    }

  /**
    * ### 冻结用户接口
    **/
  override def freezeUser(request: FreezeUserRequest): FreezeUserResponse =
    {
      val status = userDao.getUserStatus(request.userId)
      assert(status.isInstanceOf[Some[Int]],"用户不存在")

      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.UNDEFINED),
        "该用户状态未知，请联系管理员")
      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.DELETE),
        "该用户已被删除，请联系管理员")
      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.BLACK),
        "该用户已被列入黑名单，请联系管理员")
      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.FREEZED),
        "该用户已被冻结，请联系管理员")

      val r = userDao.updateUserFreeze(request)
      FreezeUserResponse(r.userId,UserStatusEnum(r.status),r.remark)
    }

  /**
    * ### 拉黑用户接口
    **/
  override def blackUser(request: BlackUserRequest): BlackUserResponse =
    {
      val status = userDao.getUserStatus(request.userId)
      assert(status.isInstanceOf[Some[Int]],"用户不存在")

      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.UNDEFINED),
        "该用户状态未知，请联系管理员")
      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.DELETE),
        "该用户已被删除，请联系管理员")
      assert(!UserStatusEnum(status.get).eq(UserStatusEnum.BLACK),
        "该用户已被列入黑名单，请联系管理员")

      val r = userDao.updateUserBlack(request)
      BlackUserResponse(r.userId,UserStatusEnum(r.status),r.remark)
    }

  /**
    * ### 记录积分改变流水
    **/
  override def changeUserIntegral(request: ChangeIntegralRequest): Int =
    {
      assert(userDao.getUserCountById(request.userId) == 1,"用户不存在")
      assert(INT_REG.pattern.matcher(request.integralPrice).matches,"数字格式不正确")
      userDao.updateIntegralChange(request)
    }

  /**
    * ### 解冻用户接口
    **/
  override def unfreezeUser(request: UnfreezeUserRequest): UnfreezeUserResponse =
    {
      val status = userDao.getUserStatus(request.userId)
      assert(status.isInstanceOf[Some[Int]],"用户不存在")

      assert(UserStatusEnum(status.get).eq(UserStatusEnum.FREEZED),
        "该用户并非处于冻结状态")

      val r = userDao.updateUserUnfreeze(request)
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


}
