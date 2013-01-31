# Commentbox

This is a jsf component that allows quick realization of a comment functionality with many features.

![logo](https://raw.github.com/nickrussler/commentbox/master/WebContent/resources/images/banner.png)

## Screenshot

![screenshot](https://raw.github.com/nickrussler/commentbox/master/WebContent/resources/images/sample.png)

## Promo Video
https://www.youtube.com/watch?v=9ZCklu-wI90

## Features

### Replies

You can reply to comments (and reply those, and so on).

### Live Features

Other users are notified on new comments, replies and when other users type.

## Dependencies

* JSF 2.0+
* Primefaces
* Primefaces push (Athmosphere framework)

## Why should i use it, i could use some external chat service?

* You keep your data where it belongs, on your server !
* Also you can (and are allowed to) make modifications to adapt to your special needs.
* You can connect this component to your own user-system and your users don't need to have an account on your site and an additional account for the external system.

## First Steps

Create the needed datasource (commentboxDS) in the configs of your AS or rename the used datasource to one of yours (in persistence.xml).

Then simply deploy the demo project in any AS with eclipse or your IDE of choice.

In case of JBoss you need to adjust the standalone.xml to support atmosphere:

change the line:

`<subsystem xmlns="urn:jboss:domain:web:1.1" native="false" [...]>`

to:

`<subsystem xmlns="urn:jboss:domain:web:1.1" native="true" [...]>`