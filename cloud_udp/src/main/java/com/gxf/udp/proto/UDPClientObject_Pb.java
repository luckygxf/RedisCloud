// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: UDPClientObject.proto

package com.gxf.udp.proto;

public final class UDPClientObject_Pb {
  private UDPClientObject_Pb() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * <pre>
   *客户端发出的请求类型
   * </pre>
   *
   * Protobuf enum {@code com.gxf.udp.proto.RequestCommand}
   */
  public enum RequestCommand
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     *web command
     * </pre>
     *
     * <code>CMD_executeShell = 0;</code>
     */
    CMD_executeShell(0),
    /**
     * <code>CMD_runInstance = 1;</code>
     */
    CMD_runInstance(1),
    /**
     * <code>CMD_runSentinel = 2;</code>
     */
    CMD_runSentinel(2),
    /**
     * <code>CMD_runRedisCommand = 3;</code>
     */
    CMD_runRedisCommand(3),
    /**
     * <code>CMD_isPortUsed = 4;</code>
     */
    CMD_isPortUsed(4),
    /**
     * <code>CMD_createRemoteFile = 5;</code>
     */
    CMD_createRemoteFile(5),
    /**
     * <code>CMD_startRedisInstanceAtPort = 6;</code>
     */
    CMD_startRedisInstanceAtPort(6),
    /**
     * <code>CMD_reportData = 7;</code>
     */
    CMD_reportData(7),
    /**
     * <pre>
     *client command
     * </pre>
     *
     * <code>CMD_CLIENT_reportData = 8;</code>
     */
    CMD_CLIENT_reportData(8),
    /**
     * <code>CMD_CLIENT_getAppInfoByKey = 9;</code>
     */
    CMD_CLIENT_getAppInfoByKey(9),
    /**
     * <pre>
     *monitor command
     * </pre>
     *
     * <code>CMD_MONITOR_getMachineInfo = 10;</code>
     */
    CMD_MONITOR_getMachineInfo(10),
    UNRECOGNIZED(-1),
    ;

    /**
     * <pre>
     *web command
     * </pre>
     *
     * <code>CMD_executeShell = 0;</code>
     */
    public static final int CMD_executeShell_VALUE = 0;
    /**
     * <code>CMD_runInstance = 1;</code>
     */
    public static final int CMD_runInstance_VALUE = 1;
    /**
     * <code>CMD_runSentinel = 2;</code>
     */
    public static final int CMD_runSentinel_VALUE = 2;
    /**
     * <code>CMD_runRedisCommand = 3;</code>
     */
    public static final int CMD_runRedisCommand_VALUE = 3;
    /**
     * <code>CMD_isPortUsed = 4;</code>
     */
    public static final int CMD_isPortUsed_VALUE = 4;
    /**
     * <code>CMD_createRemoteFile = 5;</code>
     */
    public static final int CMD_createRemoteFile_VALUE = 5;
    /**
     * <code>CMD_startRedisInstanceAtPort = 6;</code>
     */
    public static final int CMD_startRedisInstanceAtPort_VALUE = 6;
    /**
     * <code>CMD_reportData = 7;</code>
     */
    public static final int CMD_reportData_VALUE = 7;
    /**
     * <pre>
     *client command
     * </pre>
     *
     * <code>CMD_CLIENT_reportData = 8;</code>
     */
    public static final int CMD_CLIENT_reportData_VALUE = 8;
    /**
     * <code>CMD_CLIENT_getAppInfoByKey = 9;</code>
     */
    public static final int CMD_CLIENT_getAppInfoByKey_VALUE = 9;
    /**
     * <pre>
     *monitor command
     * </pre>
     *
     * <code>CMD_MONITOR_getMachineInfo = 10;</code>
     */
    public static final int CMD_MONITOR_getMachineInfo_VALUE = 10;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @Deprecated
    public static RequestCommand valueOf(int value) {
      return forNumber(value);
    }

    public static RequestCommand forNumber(int value) {
      switch (value) {
        case 0: return CMD_executeShell;
        case 1: return CMD_runInstance;
        case 2: return CMD_runSentinel;
        case 3: return CMD_runRedisCommand;
        case 4: return CMD_isPortUsed;
        case 5: return CMD_createRemoteFile;
        case 6: return CMD_startRedisInstanceAtPort;
        case 7: return CMD_reportData;
        case 8: return CMD_CLIENT_reportData;
        case 9: return CMD_CLIENT_getAppInfoByKey;
        case 10: return CMD_MONITOR_getMachineInfo;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<RequestCommand>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        RequestCommand> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<RequestCommand>() {
            public RequestCommand findValueByNumber(int number) {
              return RequestCommand.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return UDPClientObject_Pb.getDescriptor().getEnumTypes().get(0);
    }

    private static final RequestCommand[] VALUES = values();

    public static RequestCommand valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private RequestCommand(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.gxf.udp.proto.RequestCommand)
  }

  public interface UDPClientObjectOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.gxf.udp.proto.UDPClientObject)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 sessionID = 1;</code>
     */
    int getSessionID();

    /**
     * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
     */
    int getCommandValue();
    /**
     * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
     */
    RequestCommand getCommand();

    /**
     * <code>bytes params = 3;</code>
     */
    com.google.protobuf.ByteString getParams();
  }
  /**
   * Protobuf type {@code com.gxf.udp.proto.UDPClientObject}
   */
  public  static final class UDPClientObject extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.gxf.udp.proto.UDPClientObject)
      UDPClientObjectOrBuilder {
    // Use UDPClientObject.newBuilder() to construct.
    private UDPClientObject(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private UDPClientObject() {
      sessionID_ = 0;
      command_ = 0;
      params_ = com.google.protobuf.ByteString.EMPTY;
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private UDPClientObject(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              sessionID_ = input.readInt32();
              break;
            }
            case 16: {
              int rawValue = input.readEnum();

              command_ = rawValue;
              break;
            }
            case 26: {

              params_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return UDPClientObject_Pb.internal_static_com_gxf_udp_proto_UDPClientObject_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return UDPClientObject_Pb.internal_static_com_gxf_udp_proto_UDPClientObject_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              UDPClientObject.class, Builder.class);
    }

    public static final int SESSIONID_FIELD_NUMBER = 1;
    private int sessionID_;
    /**
     * <code>int32 sessionID = 1;</code>
     */
    public int getSessionID() {
      return sessionID_;
    }

    public static final int COMMAND_FIELD_NUMBER = 2;
    private int command_;
    /**
     * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
     */
    public int getCommandValue() {
      return command_;
    }
    /**
     * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
     */
    public RequestCommand getCommand() {
      RequestCommand result = RequestCommand.valueOf(command_);
      return result == null ? RequestCommand.UNRECOGNIZED : result;
    }

    public static final int PARAMS_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString params_;
    /**
     * <code>bytes params = 3;</code>
     */
    public com.google.protobuf.ByteString getParams() {
      return params_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (sessionID_ != 0) {
        output.writeInt32(1, sessionID_);
      }
      if (command_ != RequestCommand.CMD_executeShell.getNumber()) {
        output.writeEnum(2, command_);
      }
      if (!params_.isEmpty()) {
        output.writeBytes(3, params_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (sessionID_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, sessionID_);
      }
      if (command_ != RequestCommand.CMD_executeShell.getNumber()) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(2, command_);
      }
      if (!params_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, params_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof UDPClientObject)) {
        return super.equals(obj);
      }
      UDPClientObject other = (UDPClientObject) obj;

      boolean result = true;
      result = result && (getSessionID()
          == other.getSessionID());
      result = result && command_ == other.command_;
      result = result && getParams()
          .equals(other.getParams());
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + SESSIONID_FIELD_NUMBER;
      hash = (53 * hash) + getSessionID();
      hash = (37 * hash) + COMMAND_FIELD_NUMBER;
      hash = (53 * hash) + command_;
      hash = (37 * hash) + PARAMS_FIELD_NUMBER;
      hash = (53 * hash) + getParams().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static UDPClientObject parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static UDPClientObject parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static UDPClientObject parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static UDPClientObject parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static UDPClientObject parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static UDPClientObject parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static UDPClientObject parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static UDPClientObject parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static UDPClientObject parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static UDPClientObject parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static UDPClientObject parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static UDPClientObject parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(UDPClientObject prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.gxf.udp.proto.UDPClientObject}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.gxf.udp.proto.UDPClientObject)
        UDPClientObjectOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return UDPClientObject_Pb.internal_static_com_gxf_udp_proto_UDPClientObject_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return UDPClientObject_Pb.internal_static_com_gxf_udp_proto_UDPClientObject_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                UDPClientObject.class, Builder.class);
      }

      // Construct using com.gxf.udp.proto.UDPClientObject_Pb.UDPClientObject.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        sessionID_ = 0;

        command_ = 0;

        params_ = com.google.protobuf.ByteString.EMPTY;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return UDPClientObject_Pb.internal_static_com_gxf_udp_proto_UDPClientObject_descriptor;
      }

      public UDPClientObject getDefaultInstanceForType() {
        return UDPClientObject.getDefaultInstance();
      }

      public UDPClientObject build() {
        UDPClientObject result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public UDPClientObject buildPartial() {
        UDPClientObject result = new UDPClientObject(this);
        result.sessionID_ = sessionID_;
        result.command_ = command_;
        result.params_ = params_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof UDPClientObject) {
          return mergeFrom((UDPClientObject)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(UDPClientObject other) {
        if (other == UDPClientObject.getDefaultInstance()) return this;
        if (other.getSessionID() != 0) {
          setSessionID(other.getSessionID());
        }
        if (other.command_ != 0) {
          setCommandValue(other.getCommandValue());
        }
        if (other.getParams() != com.google.protobuf.ByteString.EMPTY) {
          setParams(other.getParams());
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        UDPClientObject parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (UDPClientObject) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int sessionID_ ;
      /**
       * <code>int32 sessionID = 1;</code>
       */
      public int getSessionID() {
        return sessionID_;
      }
      /**
       * <code>int32 sessionID = 1;</code>
       */
      public Builder setSessionID(int value) {
        
        sessionID_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 sessionID = 1;</code>
       */
      public Builder clearSessionID() {
        
        sessionID_ = 0;
        onChanged();
        return this;
      }

      private int command_ = 0;
      /**
       * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
       */
      public int getCommandValue() {
        return command_;
      }
      /**
       * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
       */
      public Builder setCommandValue(int value) {
        command_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
       */
      public RequestCommand getCommand() {
        RequestCommand result = RequestCommand.valueOf(command_);
        return result == null ? RequestCommand.UNRECOGNIZED : result;
      }
      /**
       * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
       */
      public Builder setCommand(RequestCommand value) {
        if (value == null) {
          throw new NullPointerException();
        }
        
        command_ = value.getNumber();
        onChanged();
        return this;
      }
      /**
       * <code>.com.gxf.udp.proto.RequestCommand command = 2;</code>
       */
      public Builder clearCommand() {
        
        command_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString params_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>bytes params = 3;</code>
       */
      public com.google.protobuf.ByteString getParams() {
        return params_;
      }
      /**
       * <code>bytes params = 3;</code>
       */
      public Builder setParams(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        params_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>bytes params = 3;</code>
       */
      public Builder clearParams() {
        
        params_ = getDefaultInstance().getParams();
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:com.gxf.udp.proto.UDPClientObject)
    }

    // @@protoc_insertion_point(class_scope:com.gxf.udp.proto.UDPClientObject)
    private static final UDPClientObject DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new UDPClientObject();
    }

    public static UDPClientObject getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<UDPClientObject>
        PARSER = new com.google.protobuf.AbstractParser<UDPClientObject>() {
      public UDPClientObject parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new UDPClientObject(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<UDPClientObject> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<UDPClientObject> getParserForType() {
      return PARSER;
    }

    public UDPClientObject getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gxf_udp_proto_UDPClientObject_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gxf_udp_proto_UDPClientObject_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\025UDPClientObject.proto\022\021com.gxf.udp.pro" +
      "to\"h\n\017UDPClientObject\022\021\n\tsessionID\030\001 \001(\005" +
      "\0222\n\007command\030\002 \001(\0162!.com.gxf.udp.proto.Re" +
      "questCommand\022\016\n\006params\030\003 \001(\014*\250\002\n\016Request" +
      "Command\022\024\n\020CMD_executeShell\020\000\022\023\n\017CMD_run" +
      "Instance\020\001\022\023\n\017CMD_runSentinel\020\002\022\027\n\023CMD_r" +
      "unRedisCommand\020\003\022\022\n\016CMD_isPortUsed\020\004\022\030\n\024" +
      "CMD_createRemoteFile\020\005\022 \n\034CMD_startRedis" +
      "InstanceAtPort\020\006\022\022\n\016CMD_reportData\020\007\022\031\n\025" +
      "CMD_CLIENT_reportData\020\010\022\036\n\032CMD_CLIENT_ge",
      "tAppInfoByKey\020\t\022\036\n\032CMD_MONITOR_getMachin" +
      "eInfo\020\nB\024B\022UDPClientObject_Pbb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_gxf_udp_proto_UDPClientObject_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_gxf_udp_proto_UDPClientObject_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gxf_udp_proto_UDPClientObject_descriptor,
        new String[] { "SessionID", "Command", "Params", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
