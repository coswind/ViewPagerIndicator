ViewPagerIndicator
=====================

从ViewPagerIndicator剥离出来的TabPageIndicator,底部使用canvas绘制的可以随着viewpager滑动的滑动条.

##HOW TO
Simply add the repository to your build.gradle file:

    repositories {
        maven {
            url 'https://github.com/coswind/mvn-repo/raw/master/'
        }
        mavenCentral()
    }
    
And you can use the artifacts like this:

    dependencies {
        compile 'com.android.support:support-v4:18.0.0+'
        compile 'com.coswind.viewpagerindicator:viewpagerindicator:1.0-SNAPSHOT'
    }
    
Sample project here:

[Sample Project](https://github.com/coswind/TestTabViewPagerIndicator)

##APK [Android 2.3+]

[TabViewPagerIndicator.apk](https://github.com/coswind/TabViewPagerIndicator/raw/master/TabViewPagerIndicator.apk)
