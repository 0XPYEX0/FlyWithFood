package me.xpyex.plugin.flywithfood.common.types;

public enum FWFMsgType {
    CanNotFly("CannotFly"),
    EnableFly("On"),
    DisableFly("Off"),
    CanNotEnable("CannotEnable"),
    HasEffect("HasEffect"),
    NoPermission("NoPermission"),
    DisableInThisWorld("DisableInThisWorld");

    private String msgType;

    FWFMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getValue() {
        return msgType;
    }
}
