# Commentbox

This is a jsf component that allows quick realization of a comment functionality with many features.

![logo](https://raw.github.com/nickrussler/commentbox/master/misc/images/banner.png)

## Screenshot

![screenshot](https://raw.github.com/nickrussler/commentbox/master/misc/images/sample.png)

## Promo Video
https://www.youtube.com/watch?v=9ZCklu-wI90

## Features

### Replies

You can reply to comments (and reply those, and so on).

### Live Features

Other users are notified on new comments, replies and when other users type.

## Dependencies

* Primefaces
 
### Optional Dependencies

* Primefaces push (Atmosphere framework) - You need this when you want to use the live features (enabled by default)
* Add [prettyTime](http://ocpsoft.org/prettytime/) and [prettytime-integration-jsf](http://ocpsoft.org/prettytime/#section-6) to your project and commentbox will automatically use it to display time

## Download
[Here](https://github.com/nickrussler/commentbox/raw/master/demo-source/WebContent/WEB-INF/lib/commentbox-0.0.1-SNAPSHOT.jar) you can download the latest jar

## Documetation
Download the documentation [here](https://github.com/nickrussler/commentbox/raw/master/misc/documentation.pdf).

## First Steps

I recomend to take a look at the [demo](https://github.com/nickrussler/commentbox/tree/master/demo-source) at first.

Just deploy the demo project in any AS with eclipse or your IDE of choice, and try out some features!

### Extended Demo - DEPRECATED

After you played around with the demo you can start to use it immediatly or you can take a look at the [extended demo](https://github.com/nickrussler/commentbox/tree/master/extended-demo-source).

In the extended demo a real database is used via JPA.

Because of that you need to create a datasource (commentboxDS) in the configs of your AS or rename the used datasource to one of yours (in persistence.xml).

Then deploy the demo project in any AS with eclipse or your IDE of choice.

NOTE: The Extended Demo is not maintainded anymore, i will remove it in the future.


## Notice for JBoss usage
In case you use JBoss and want to use the live features you need to adjust the standalone.xml to support atmosphere:

change the line:

`<subsystem xmlns="urn:jboss:domain:web:1.1" native="false" [...]>`

to:

`<subsystem xmlns="urn:jboss:domain:web:1.1" native="true" [...]>`

## Why should i use it, i could use some external chat service?

* You keep your data where it belongs, on your server !
* Also you can (and are allowed to) make modifications to adapt to your special needs.
* You can connect this component to your own user-system and your users don't need to have an account on your site and an additional account for the external system.

##License
This project is open source and free, it is available under the [Apache v2 License](http://www.apache.org/licenses/LICENSE-2.0.html).


## Credits

* Fugue Icons - Yusuke Kamiyamane - http://p.yusukekamiyamane.com/
* CLEditor YouTube Plugin v1.0.0 - Markus Horowski - http://www.terapix.de
* TeamRot - The Group that developed the initial commentBox component at RWTH Aachen University. Members: Ahmet Yüksektepe, Arthur Otto, Kristjan Liiva, Nick Russler, Thomas Müller
