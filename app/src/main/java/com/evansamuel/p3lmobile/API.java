package com.evansamuel.p3lmobile;

public class API {
    public static final String ROOT_URL =" https://p3lakb9681.xyz/public/";
    public static final String ROOT_API = ROOT_URL+"api/";
    public static final String URL_SCAN = ROOT_API+"reservasi/scan";
    public static final String URL_SELECT = ROOT_API+"menu";
    public static final String URL_IMG="https://p3lakb9681.xyz/public/menu/";
    public static final String URL_ADD_ORDER=ROOT_API+"order/create";
    public static final String URL_GET_ORDER=ROOT_API+"order/";
    public static final String URL_PUT_ORDER=URL_GET_ORDER+"edit/";
    public static final String URL_DELETE_ORDER=URL_GET_ORDER+"delete/";
    public static final String URL_CHECKOUT_ORDER=URL_GET_ORDER+"onProcess/";
}
