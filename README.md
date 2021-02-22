# YYReader
Android小说阅读器

第一次commit
简单搭建界面，activity+fragment+bottomBar+自定义ToolBar
bottomBar关联fragment跳转

第二次commit
书架页面搭建
书籍模型定义
书籍打开和关闭动画实现，3DRotation+Scale实现

第三次commit
修改为Navigation+BottomNavigationView导航，使用nav_graph
修复小说显示的bug

第四次commit
阅读界面设置搭建
侧滑显示章节-DrawerLayout

第五次commit
阅读界面功能实现
图标优化，使用vector Asset

第六次commit
阅读界面设置功能实现----目录展开、阅读界面字体、颜色更改等
本地文件读取界面搭建----TabLayout+ViewPager实现界面切换

第七次commit
文件读取功能实现，实现Txt文件过滤
文件和文件夹item分类显示

第八次commit
使用数据结构Stack优化文件回退处理
实现选中文件的监听回调

第九次commit
修复部分bug
使用数据库记录本地小说、章节信息和记录等，使用第三方库Litepal

第十次commit
解决数据库记录错误（使用Paractable遗漏id属性，记录始终默认为0）
搭建“我的”界面，使用CollapsingToolbarLayout和NestScrollView实现滑动顶部栏

第十一次commit
添加翻页动画，实现平滑翻页效果
更改界面绘制方式，绘制分页绘制bitmap，使用scroller完成滑动效果

第十二次commit
增加覆盖、仿真翻页效果，完善取消翻页的效果

第十三次commit
沉浸式状态栏实现
阅读模式界面style完善
当前章节选中回调

第十四次commit
书城界面布局，tablayout+fragment
使用banner进行图片轮播

第十五次commit
修复书籍打开动画
抽取翻页效果的Anim类公共部分构成抽象类
图片资源美化

第十六次commit
修复读取数据库章节后，加载page时无高度导致oom
实现默认的小说，首次安装时将raw中文件写入内部存储，加入书架
