package com.today36524.api.user.response

        /**
         * Autogenerated by Dapeng-Code-Generator (1.2.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated

        *

 解冻操作返回体

        **/
        case class UnfreezeUserResponse(

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

  操作员解冻备注

        **/
        
        remark : String
        )
      