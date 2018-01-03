namespace java com.today36524.api.user.response

include 'user_enum.thrift'

/**
* 注册用户返回体
**/
struct RegisterUserResponse {
    /**
    * 用户名称
    **/
    1:string userName,
    /**
    * 电话号码
    **/
    2: string telephone,
    /**
    * 用户状态
    **/
    3: user_enum.UserStatusEnum status,
    /**
    * 注册时间
    **/
    4: i64 createdAt,

}

/**
* 修改用户返回体
**/
struct ModifyUserResponse {
    /**
    * 用户名称
    **/
    1:string userName,
    /**
    * 电话号码
    **/
    2: string telephone,
    /**
    * 用户状态
    **/
    3: user_enum.UserStatusEnum status,
    /**
    * 用户邮箱
    **/
    4: optional string email,
    /**
    * 用户 qq
    **/
    5: optional string qq,
    /**
    * 更新时间
    **/
    6: i64 updatedAt,

}

/**
* 用户登录返回体
**/
struct LoginUserResponse {
    /**
    * 用户名称
    **/
    1:string userName,
    /**
    * 电话号码
    **/
    2: string telephone,
    /**
    * 用户状态
    **/
    3: user_enum.UserStatusEnum status,
    /**
    * 用户邮箱
    **/
    4: optional string email,
    /**
    * 用户 qq
    **/
    5: optional string qq,
    /**
    * 用户 积分
    **/
    6: i32 integral ,
    /**
    * 注册时间
    **/
    7: i64 createdAt,
    /**
    * 更新时间
    **/
    8: i64 updatedAt,
}

/**
* 冻结操作返回体
**/
struct FreezeUserResponse {
    /**
    * 用户 id
    **/
    1: string userId,
    /**
    * 用户状态
    **/
    2: user_enum.UserStatusEnum status,
    /**
    *  操作员冻结备注
    **/
    3: string remark,
}

/**
* 拉黑操作返回体
**/
struct BlackUserResponse {
    /**
    * 用户 id
    **/
    1: string userId,
    /**
    * 用户状态
    **/
    2: user_enum.UserStatusEnum status,
    /**
    *  操作员冻结备注
    **/
    3: string remark,
}


/**
* 解冻操作返回体
**/
struct UnfreezeUserResponse {
    /**
    * 用户 id
    **/
    1: string userId,
    /**
    * 用户状态
    **/
    2: user_enum.UserStatusEnum status,
    /**
    *  操作员解冻备注
    **/
    3: string remark,
}


/**
* 注册用户sql返回体
**/
struct RegisterUserSqlResponse {
    /**
    * 用户名称
    **/
    1:string userName,
    /**
    * 电话号码
    **/
    2: string telephone,
    /**
    * 用户状态
    **/
    3: i32 status,
    /**
    * 注册时间
    **/
    4: i64 createdAt,

}

/**
* 修改用户sql返回体
**/
struct ModifyUserSqlResponse {
    /**
    * 用户名称
    **/
    1:string userName,
    /**
    * 电话号码
    **/
    2: string telephone,
    /**
    * 用户状态
    **/
    3: i32 status,
    /**
    * 用户邮箱
    **/
    4: optional string email,
    /**
    * 用户 qq
    **/
    5: optional string qq,
    /**
    * 更新时间
    **/
    6: i64 updatedAt,

}

/**
* 用户登录sql返回体
**/
struct LoginUserSqlResponse {
    /**
    * 用户名称
    **/
    1:string userName,
    /**
    * 电话号码
    **/
    2: string telephone,
    /**
    * 用户状态
    **/
    3: i32 status,
    /**
    * 用户邮箱
    **/
    4: optional string email,
    /**
    * 用户 qq
    **/
    5: optional string qq,
    /**
    * 用户 积分
    **/
    6: i32 integral ,
    /**
    * 注册时间
    **/
    7: i64 createdAt,
    /**
    * 更新时间
    **/
    8: i64 updatedAt,
}

/**
* 冻结操作sql返回体
**/
struct FreezeUserSqlResponse {
    /**
    * 用户 id
    **/
    1: string userId,
    /**
    * 用户状态
    **/
    2: i32 status,
    /**
    *  操作员冻结备注
    **/
    3: string remark,
}

/**
* 拉黑操作sql返回体
**/
struct BlackUserSqlResponse {
    /**
    * 用户 id
    **/
    1: string userId,
    /**
    * 用户状态
    **/
    2: i32 status,
    /**
    *  操作员冻结备注
    **/
    3: string remark,
}


/**
* 解冻操作sql返回体
**/
struct UnfreezeUserSqlResponse {
    /**
    * 用户 id
    **/
    1: string userId,
    /**
    * 用户状态
    **/
    2: i32 status,
    /**
    *  操作员解冻备注
    **/
    3: string remark,
}
