# Feature_Comments

[ссылка на пул-реквест фичи](https://github.com/Sad-Duck/java-explore-with-me/pull/4)

# Новые эндпоинты добавленные в ветке feature_comments:

## Public:

##### <span style="color:CornflowerBlue ">Get
###### /comments/event/{eventId} 
получение постраничного списка комментариев к событию.


##### <span style="color:CornflowerBlue ">Get
###### /comments/replies/{commentId} 
получение ветки комментария и ответов на него

## Private:

##### <span style="color:DarkSeaGreen ">Post
###### /users/comments/events/{eventId} 
создание комментария к событию

##### <span style="color:Aqua">Patch
###### /users/comments/{commentId} 
редактирование комментария создателем

##### <span style="color:red">Delete
###### /users/comments/{commentId} 
удаление комментария создателем

## Admin:

##### <span style="color:CornflowerBlue ">Get
###### /admin/comments/{commentId} 
получение одного комментария по id

##### <span style="color:CornflowerBlue ">Get
###### /admin/comments/user/{userId} 
получение постраничног списка всех комментариев созданных пользователем

##### <span style="color:red">Delete
###### /admin/comments/{commentId} 
удаление одного комментария по id администратором

##### <span style="color:red">Delete
###### /admin/comments/user/{userId} 
удаление всех комментариев пользователя

##### <span style="color:red">Delete
###### /admin/comments/events/{eventId} 
удаление всех комментариев события

* при удалении комментария вся ветка его ответов также удаляется

# Старые эндпоинты, изменённые в этой ветке:

## Private:

##### <span style="color:DarkSeaGreen ">Post
###### users/{userId}/events
##### <span style="color:Aqua">Patch
###### /users/{userId}/events/{eventId}
При создании события можно установить boolean переменную "disableComments"
Если disableComments = true, то комментировать событие нельзя. 
Если у события уже есть комментарии, отключить их нельзя

## Admin:

##### <span style="color:Aqua">Patch
###### /admin/events/{eventId}
Если у события уже есть комментарии, администратор все равно может их отключить - тогда все комментарии события 
будут удалены

##### <span style="color:red">Delete
###### /admin/users/{userId}
При удалении пользователя теперь все его комментарии будут удалены
