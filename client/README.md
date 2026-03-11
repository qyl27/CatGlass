# CatGlassClient

在游戏中使用命令获取Cookie。

本项目是客户端模组，安装在服务器上时会阻止服务器启动。  

会在服务器存储和请求Cookie时在游戏日志中打印其内容（Payload使用十六进制字符串编码显示）。  

catglass**c**是CatGlassClient的缩写，也是本项目的modid和客户端注册的根命令。  

## Cookies

### 列出所有Cookies的Key
```mcfunction
/catglassc cookies list
```
返回值是Cookies总数。

### 根据Key获取Cookie
```mcfunction
/catglassc cookies get <key> <format>
```
Key是一个Identifier（ResourceLocation），如果对应的Cookie不存在会报告错误，并返回0；  

Format是输出格式，由于Cookie的值是一个byte[]，无法直接输出，所以需要指定输出格式，目前支持以下几种格式：  
- `BASE64`：将byte[]编码为Base64字符串输出。
- `HEX_STRING`：将byte[]编码为十六进制字符串输出。
- `UTF8`：将byte[]解码为UTF-8字符串输出。  

成功返回1。

### 设置Cookie
```mcfunction
/catglassc cookies set <key> <format> <value>
```
Key和Format同上；

Value是要设置的Payload，格式由Format参数指定，如果不能被按所选格式解析会报告错误，并返回0。

成功返回1。

### 删除Cookie
```mcfunction
/catglassc cookies unset <key>
```
Key同上，如果对应的Cookie不存在不会报告错误。

总是返回1。

## 忽略Cookie列表

忽略列表是一个Identifier列表，存储在客户端的模组配置文件中，也可以通过命令管理。

在收到服务端存储Cookie的请求时，会检查其Key是否在忽略列表中，如果被忽略则不会实际写入serverCookie中。

```mcfunction
/catglassc ignore <list|clear>
/catglassc ignore <check|add|remove> <key>
```

## 预设Cookie值

预设Cookie值是一个Identifier到byte[]的映射，存储在客户端的模组配置文件中，可以通过命令管理。

在配置文件中，它是一个列表，每一行使用形如`examplemod:cookie_key:cafebabe`的格式表示一个预设Cookie，它使用冒号进行分割，其中第二个冒号前的字符串是Key（`examplemod:cookie_key`），后面是十六进制字符串编码的Payload（`cafebabe`）。

在客户端尝试登入服务器的握手过程中，如果是首次登入（即并非通过transfer命令切换服务器），会把预设Cookie列表的内容覆盖到客户端的serverCookie中。

命令的形式和Cookies一节中基本相同。

```mcfunction
/catglassc presets <list|clear>
/catglassc presets get <key> <format>
/catglassc presets set <key> <format> <value>
/catglassc presets unset <key>
```
