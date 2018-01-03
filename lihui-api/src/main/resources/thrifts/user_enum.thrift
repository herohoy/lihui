namespace java com.today36524.api.user.enums

/**
* 会员用户所在国家区域电话号码前缀
**/
enum UserCountryPhonePrefix{
    CN = 86,
    JP = 81,
    KR = 82,
    US = 1,
    UK = 44
}

/**
* 会员用户状态（是否可用及会员级别）
**/
enum UserStatusEnum {
    /**
    * 等待激活（预留）
    **/
    ACTIVATING = 0,
    /**
    * 已激活
    **/
    ACTIVATED = 1,
    /**
    * 权属会员（资料已完善）
    **/
    DATA_PERFECTED = 2,
    /**
    * 冻结用户（涉嫌违规处理冻结期）
    **/
    FREEZED = 3,
    /**
    * 黑名单用户
    **/
    BLACK = 4,
    /**
    * 逻辑删除
    **/
    DELETE = 99,
}


/**
* 积分流水类型
**/
enum IntegralTypeEnum {
    /**
    * 流水不变（预留，可用于查询）
    **/
    SHOW = 0,
    /**
    * 增加流水
    **/
    ADD = 1,
    /**
    * 减少流水
    **/
    MINUS = 2,
}



/**
* 积分来源
**/
enum IntegralSourceEnum {
    /**
    * 完善个人资料
    **/
    PREFECT_INFORMATION = 1,
    /**
    * 拉黑
    **/
    BLACK = 2,
}
