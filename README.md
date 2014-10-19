AndroidUploader
===============

A Uploader framework for android


使用
====

Uploader
--------

一个上传任务

Bridge
-------

上传连接桥，主要用于处理以何种方式进行上传。
一般都是用Http上传，对应的生成HttpBridge。


一个上传任务对应一个Uploader。
每个Uploader中有一个上传用的连接桥。

开始上传
--------

生一个HttpBridge, 设置好相应的上传参数, httpClient, httpPost或url。
使用上面的HttpBridge生成一个HttpUploader或FileHttpUploader, 生成UploaderUiHandler, 用于上传的状态回调.
调用HttpUploader的uploader方法。