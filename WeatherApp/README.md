这个 app 是我根据《第一行代码》一书中的酷欧天气项目学习编写的。此书对安卓新手十分友好，在此感谢作者郭霖。这是一个很简单的 app，我把自己学习开发这个 app 的历程分享出来。可能写的都是一些很简单的知识点，但都是自己在开发过程中遇到的坑，希望能帮到其他初学者。天气 app 暂时就先更新到这，最近在学动画效果，又开了一个 repository: https://github.com/ye125440/AnimationTest

![地区选择](http://ww2.sinaimg.cn/large/0060lm7Tgw1f4sopjhqohj307i0dc3yn.jpg)  ![天气显示](http://ww4.sinaimg.cn/large/0060lm7Tgw1f4sor8x7i6j307i0dc0sw.jpg)         

##今天什么天儿 v2.0 更新时间 2016/6/15
一下子跨到了v2.0是因为更新加入了各式天气的图标，同时把原来的日志工具 LogUtil 更换为了开源日志工具 Logger，非常好用。
GitHub地址：https://github.com/orhanobut/logger

![各式天气图标](http://ww1.sinaimg.cn/large/0060lm7Tgw1f4w9mxvganj307i0dcwep.jpg)

P.S. 由于图标资源还不够充足，有些天气没有与之对应的图标，我都默认设置成为了 app 的图标。

##今天什么天儿 v1.1 更新时间 2016/6/14
首先最直接的更新是 app 更名了，改为**今天什么天儿**。然后，使用 ToolBar 替代了原本的 RelativeLayout。

![ToolBar效果](http://ww1.sinaimg.cn/large/0060lm7Tgw1f4utqbzbrrj307i0dcq36.jpg) 

Toolbar 是在 Android 5.0 开始推出的一个 Material Design 风格的导航控件 ，Google 非常推荐大家使用 Toolbar 来作为Android客户端的导航栏，以此来取代之前的 Actionbar 。与 Actionbar 相比，Toolbar 明显要灵活的多。它不像 Actionbar 一样，一定要固定在Activity的顶部，而是可以放到界面的任意位置。除此之外，在设计 Toolbar 的时候，Google也留给了开发者很多可定制修改的余地，这些可定制修改的属性在API文档中都有详细介绍，如：

+ 设置导航栏图标；
+ 设置App的logo；
+ 支持设置标题和子标题；
+ 支持添加一个或多个的自定义控件；
+ 支持Action Menu；

在 `weather_layout.xml` 布局文件中设置 ToolBar 时，要选择 android.support.v7.widget.Toolbar，为它设置了一个自定义主题：`android:theme="@style/Theme.ToolBar.Weather"`，主要是为了解决 ToolBar 显示不正常的问题。在不设置自定义主题是 ToolBar 上“更多”的按钮显示为黑色，如下图。估计是在定义主题时要注意父主题一定要是`.NoactionBar`的，不然会和 ToolBar 冲突。
想要了解更多关于 ToolBar 的资料，可以看[ Toolbars 官方文档](https://material.google.com/components/toolbars.html)。

![不自定义 Theme 的显示结果](http://ww1.sinaimg.cn/large/0060lm7Tgw1f4wrwmg38cj30f002qt8l.jpg) 

##什么鬼天气 v1.0 更新时间 2016/6/12
什么鬼天气 app 中大部分的代码都来自《第一行代码》一书，书中所用的查询天气的 API 接口已经失效，我采用了聚合数据的[全国天气查询API](https://www.juhe.cn/docs/api/id/39)。注册账号得到你们自己的 AppKey，点击申请数据可以得到500次的免费次数。同时由于更换了 API 接口，书中的 xml 数据解析也换为了 Json 数据解析。
分享一个写的很好的 Json 解析教程：[Json 解析教程](http://www.jianshu.com/p/cbc1aa0c7661)。

![全国天气查询API](http://ww3.sinaimg.cn/large/0060lm7Tgw1f4soruqjdnj30em06kdgr.jpg)

你们在 clone 了这个项目后需要修改 activity 包下 ChooseAreaActivity 中的 `public static final String API_KEY`，把它改为你们自己的AppKey（我的次数很可能用完了）就可以直接运行先看看效果了。
什么鬼天气 v1.0修复了原书中的一个小bug，屏幕旋转资源重载问题，如下图：

![屏幕旋转资源重载bug](http://ww2.sinaimg.cn/large/0060lm7Tgw1f4sosfot4sg30900fh0uy.gif)

在 ChooseAreaActivity 选择地区是需要经过省份、城市、地区三个选择列表。选择了省或者市后，没确定具体地区时，如果旋转屏幕，原本的选择列表由于 activity 的重新加载会回到初始的省份选择列表，用户需要再次选择，体验肯定不好。
通过复写 ChooseAreaActivity 中的 `onSaveInstanceState()` 方法保存地区选择信息，后在 `onCreate()` 方法中恢复相关数据，修复了这个 bug。具体的保存与恢复在代码中可以看到，也有相关注释。
大部分重要的代码我都加了注释，另外在布局文件中，天气信息的 TextView 数据都是通过网络请求获取到，在调整布局的时候如果能够在有数据显示的情况下调整会更准确一些，于是使用了 `tools命名空间`。
关于 tools 命名空间，推荐一篇文章：[tools 命名空间](https://mp.weixin.qq.com/s?__biz=MzA4MjU5NTY0NA==&mid=2653418742&idx=1&sn=1efe7fa5c95cbf292b43ebba365813fc&scene=1&srcid=0518umesaM4ZrcRAYcNpFNZ7&key=f5c31ae61525f82e66d0951fb781d5c0541e1aff25b7c19d52529090dc9aadd5135c16492d32df57a81580273d5ce6fc&ascene=0&uin=MTY5MDI4NDA4Mg%3D%3D&devicetype=iMac+MacBookPro11%2C3+OSX+OSX+10.11.4+build%2815E65%29&version=11020201&pass_ticket=cxx8EkEgugUPRKu5OFT3LHt%2B24fap8mmX1w%2FJYuNro%2BB1zV9bzsrmnh%2F0sNY7HC1)。
现在还只是1.0的版本，希望在后面的更新中可以加入天气的图片显示。

