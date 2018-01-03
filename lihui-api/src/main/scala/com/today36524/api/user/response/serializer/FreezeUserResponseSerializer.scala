 package com.today36524.api.user.response.serializer;

        import com.today36524.api.user.request.serializer._;import com.today36524.api.user.response.serializer._;
        import com.isuwang.dapeng.core._
        import com.isuwang.org.apache.thrift._
        import com.isuwang.org.apache.thrift.protocol._

        /**
        * Autogenerated by Dapeng-Code-Generator (1.2.2)
        *
        * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
        *  @generated
        **/

        class FreezeUserResponseSerializer extends TCommonBeanSerializer[com.today36524.api.user.response.FreezeUserResponse]{
          
      @throws[TException]
      override def read(iprot: TProtocol): com.today36524.api.user.response.FreezeUserResponse = {

        var schemeField: com.isuwang.org.apache.thrift.protocol.TField = null
        iprot.readStructBegin()

      var userId: String = null
        var status: com.today36524.api.user.enums.UserStatusEnum = null
        var remark: String = null
        

      while (schemeField == null || schemeField.`type` != com.isuwang.org.apache.thrift.protocol.TType.STOP) {

        schemeField = iprot.readFieldBegin

        schemeField.id match {
          
              case 1 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.STRING => userId = iprot.readString
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
              case 2 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.I32 => status = com.today36524.api.user.enums.UserStatusEnum.findByValue(iprot.readI32)
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
              case 3 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.STRING => remark = iprot.readString
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
          case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
        }
      }

      iprot.readFieldEnd
      iprot.readStructEnd

      val bean = com.today36524.api.user.response.FreezeUserResponse(userId = userId,status = status,remark = remark)
      validate(bean)

      bean
      }
    
      @throws[TException]
      override def write(bean: com.today36524.api.user.response.FreezeUserResponse, oprot: TProtocol): Unit = {

      validate(bean)
      oprot.writeStructBegin(new com.isuwang.org.apache.thrift.protocol.TStruct("FreezeUserResponse"))

      
            {
            val elem0 = bean.userId 
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("userId", com.isuwang.org.apache.thrift.protocol.TType.STRING, 1.asInstanceOf[Short]))
            oprot.writeString(elem0)
            oprot.writeFieldEnd
            
            }
            {
            val elem1 = bean.status 
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("status", com.isuwang.org.apache.thrift.protocol.TType.I32, 2.asInstanceOf[Short]))
            oprot.writeI32(elem1.id)
            oprot.writeFieldEnd
            
            }
            {
            val elem2 = bean.remark 
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("remark", com.isuwang.org.apache.thrift.protocol.TType.STRING, 3.asInstanceOf[Short]))
            oprot.writeString(elem2)
            oprot.writeFieldEnd
            
            }
      oprot.writeFieldStop
      oprot.writeStructEnd
    }
    
      @throws[TException]
      override def validate(bean: com.today36524.api.user.response.FreezeUserResponse): Unit = {
      
              if(bean.userId == null)
              throw new SoaException(SoaBaseCode.NotNull, "userId字段不允许为空")
            
              if(bean.status == null)
              throw new SoaException(SoaBaseCode.NotNull, "status字段不允许为空")
            
              if(bean.remark == null)
              throw new SoaException(SoaBaseCode.NotNull, "remark字段不允许为空")
            
    }
    

          @throws[TException]
          override def toString(bean: com.today36524.api.user.response.FreezeUserResponse): String = if (bean == null) "null" else bean.toString

        }
        
      