package com.today36524.api.user.response

        /**
         * Autogenerated by Dapeng-Code-Generator (1.2.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated

        *

 拉黑操作返回体

        **/
        case class BlackUserResponse(

         /**
        *

 用户 id

        **/
        
        userId : String, /**
        *

 用户状态

        **/
        
        status : com.today36524.api.user.enums.UserStatusEnum, /**
        *

  操作员冻结备注

        **/
        
        remark : String
        )
      