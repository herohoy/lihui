package com.today36524.api.user.response

        /**
         * Autogenerated by Dapeng-Code-Generator (1.2.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated

        *

 修改用户返回体

        **/
        case class ModifyUserResponse(

         /**
        *

 用户名称

        **/
        
        userName : String, /**
        *

 电话号码

        **/
        
        telephone : String, /**
        *

 用户状态

        **/
        
        status : com.today36524.api.user.enums.UserStatusEnum, /**
        *

 更新时间

        **/
        
        updatedAt : Long, /**
        *

 用户邮箱

        **/
        
        email : Option[String] = None, /**
        *

 用户 qq

        **/
        
        qq : Option[String] = None
        )
      