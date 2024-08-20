# 什么是NativeLoader

`NativeLoader`是一个Java编写的Minecraft Fabric Mod，它依赖于[JNA](https://github.com/java-native-access/jna)（依赖文件位于libs文件夹下），它旨在通过提供一个兼容层，使可以与原生代码交互的语言获得编写Fabric Mod的能力

> 实际上，`NativeLoader`所做的只有提供包装类和调用动态链接库，这是为了尽量少地编写Java代码

# 为什么需要NativeLoader

使用NativeLoader，你可以使用其它能与原生代码交互的语言编写Fabric Mod

_以下是碎碎念_

我一开始创建`NativeLoader`的目的是使用纯C#写mod，<del>因为我用不惯Java，就连Java也是创建了这个项目才学的</del>，但是我其实是有一点使用Xamarin.Android（微软早期的.NET跨平台UI项目，现已停止支持）的经验的，我当时在想为什么这个项目就可以将C#对象绑定为Java对象呢，慢慢的便形成了这个项目的想法雏形，后来也就有了这个项目，<del>从新建文件夹到功能80%齐全只花了我不到两周的时间</del>

# 使用NativeLoader

要想使用`NativeLoader`，你需要在支持的版本下安装该mod，但这时只是安装了兼容层，一个独立的mod应有的逻辑、资源，它都不具有，这些都需要你提供一个支持`NativeLoader`的**资源包**来提供这些资源

## 资源包

为了加载资源，`NativeLoader`使用了一种特殊的方式——将所有外置内容组织在一个**资源包**内，在你在游戏内**手动加载该资源包**后，你的代码就可以自动获取到资源包内的模型、材质、音效等资源

资源包在作为资源的时候是必须手动加载的，这与普通的资源包无异，但`NativeLoader`会在游戏启动时扫描所有资源包中任意命名空间下的`natives`目录（如`minecraft`命名空间即为`assets/minecraft/natives`），将内部的所有文件全部复制到.minecraft/nativeloader目录，并全部执行JNA加载

**加载后是无法卸载的**，这与普通mod一样，**但如果你是第一次加载，请务必在游戏的资源包菜单中手动加载该资源包**

> nativeloader目录是以*资源包名*/*命名空间*/natives/*动态链接库文件*来组织文件的，所有在游戏启动时没有相对应资源包的目录将被删除

## 支持NativeLoader的库

C#：[Mliybs.Minecraft.Fabric](https://github.com/Mliybs/Mliybs.Minecraft.Fabric)