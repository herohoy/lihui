package com.today36524.api.user.enums;

      class UserStatusEnum private(val id: Int, val name: String) extends com.isuwang.dapeng.core.enums.TEnum(id,name) {}

      /**
       * Autogenerated by Dapeng-Code-Generator (1.2.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated

      * 

 会员用户状态（是否可用及会员级别）

      **/
      object UserStatusEnum {

      
            val ACTIVATING = new UserStatusEnum(0, "等待激活（预留）")
          
            val ACTIVATED = new UserStatusEnum(1, "已激活")
          
            val DATA_PERFECTED = new UserStatusEnum(2, "权属会员（资料已完善）")
          
            val FREEZED = new UserStatusEnum(3, "冻结用户（涉嫌违规处理冻结期）")
          
            val BLACK = new UserStatusEnum(4, "黑名单用户")
          
            val DELETE = new UserStatusEnum(99, "逻辑删除")
          
      val UNDEFINED = new UserStatusEnum(-1,"UNDEFINED") // undefined enum
      

      def findByValue(v: Int): UserStatusEnum = {
        v match {
          case 0 => ACTIVATING
            case 1 => ACTIVATED
            case 2 => DATA_PERFECTED
            case 3 => FREEZED
            case 4 => BLACK
            case 99 => DELETE
            
          case _ => new UserStatusEnum(v,"#"+ v)
        }
      }

      def apply(v: Int) = findByValue(v)
      def unapply(v: UserStatusEnum): Option[Int] = Some(v.id)

    }
    