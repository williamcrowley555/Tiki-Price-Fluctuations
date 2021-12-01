package com.tiki_server.enums;

public enum MessageType {
//    Server Response
    PRODUCT_INFO,
    ERROR,

//    Client Request

    FILTER_PRODUCTS,
    GET_PRODUCT,
    GET_CONFIGURABLE_PRODUCT,
    GET_PRODUCT_HISTORIES_BY_URL,
    GET_PRODUCT_HISTORIES_BY_PRODUCT_ID,
    GET_CONFIGURABLE_PRODUCT_HISTORIES,
    GET_REVIEWS_BY_PRODUCT_ID
}
