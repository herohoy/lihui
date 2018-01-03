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

        class ModifyUserSqlResponseSerializer extends TCommonBeanSerializer[com.today36524.api.user.response.ModifyUserSqlResponse]{
          
      @throws[TException]
      override def read(iprot: TProtocol): com.today36524.api.user.response.ModifyUserSqlResponse = {

        var schemeField: com.isuwang.org.apache.thrift.protocol.TField = null
        iprot.readStructBegin()

      var userName: String = null
        var telephone: String = null
        var status: Int = 0
        var email: Option[String] = None
        var qq: Option[String] = None
        var updatedAt: Long = 0
        

      while (schemeField == null || schemeField.`type` != com.isuwang.org.apache.thrift.protocol.TType.STOP) {

        schemeField = iprot.readFieldBegin

        schemeField.id match {
          
              case 1 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.STRING => userName = iprot.readString
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
              case 2 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.STRING => telephone = iprot.readString
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
              case 3 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.I32 => status = iprot.readI32
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
              case 4 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.STRING => email = Option(iprot.readString)
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
              case 5 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.STRING => qq = Option(iprot.readString)
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
              case 6 =>
                  schemeField.`type` match {
                    case com.isuwang.org.apache.thrift.protocol.TType.I64 => updatedAt = iprot.readI64
                    case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
            }
            
          case _ => com.isuwang.org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.`type`)
        }
      }

      iprot.readFieldEnd
      iprot.readStructEnd

      val bean = com.today36524.api.user.response.ModifyUserSqlResponse(userName = userName,telephone = telephone,status = status,email = email,qq = qq,updatedAt = updatedAt)
      validate(bean)

      bean
      }
    
      @throws[TException]
      override def write(bean: com.today36524.api.user.response.ModifyUserSqlResponse, oprot: TProtocol): Unit = {

      validate(bean)
      oprot.writeStructBegin(new com.isuwang.org.apache.thrift.protocol.TStruct("ModifyUserSqlResponse"))

      
            {
            val elem0 = bean.userName 
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("userName", com.isuwang.org.apache.thrift.protocol.TType.STRING, 1.asInstanceOf[Short]))
            oprot.writeString(elem0)
            oprot.writeFieldEnd
            
            }
            {
            val elem1 = bean.telephone 
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("telephone", com.isuwang.org.apache.thrift.protocol.TType.STRING, 2.asInstanceOf[Short]))
            oprot.writeString(elem1)
            oprot.writeFieldEnd
            
            }
            {
            val elem2 = bean.status 
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("status", com.isuwang.org.apache.thrift.protocol.TType.I32, 3.asInstanceOf[Short]))
            oprot.writeI32(elem2)
            oprot.writeFieldEnd
            
            }
            if(bean.email.isDefined){
            val elem3 = bean.email .get
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("email", com.isuwang.org.apache.thrift.protocol.TType.STRING, 4.asInstanceOf[Short]))
            oprot.writeString(elem3)
            oprot.writeFieldEnd
            
            }
            if(bean.qq.isDefined){
            val elem4 = bean.qq .get
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("qq", com.isuwang.org.apache.thrift.protocol.TType.STRING, 5.asInstanceOf[Short]))
            oprot.writeString(elem4)
            oprot.writeFieldEnd
            
            }
            {
            val elem5 = bean.updatedAt 
            oprot.writeFieldBegin(new com.isuwang.org.apache.thrift.protocol.TField("updatedAt", com.isuwang.org.apache.thrift.protocol.TType.I64, 6.asInstanceOf[Short]))
            oprot.writeI64(elem5)
            oprot.writeFieldEnd
            
            }
      oprot.writeFieldStop
      oprot.writeStructEnd
    }
    
      @throws[TException]
      override def validate(bean: com.today36524.api.user.response.ModifyUserSqlResponse): Unit = {
      
              if(bean.userName == null)
              throw new SoaException(SoaBaseCode.NotNull, "userName字段不允许为空")
            
              if(bean.telephone == null)
              throw new SoaException(SoaBaseCode.NotNull, "telephone字段不允许为空")
            
    }
    

          @throws[TException]
          override def toString(bean: com.today36524.api.user.response.ModifyUserSqlResponse): String = if (bean == null) "null" else bean.toString

        }
        
      