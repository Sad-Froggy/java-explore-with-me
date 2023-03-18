# java-explore-with-me

[ссылка на пул-реквест фичи](https://github.com/Sad-Duck/java-explore-with-me/pull/4)

# Новые эндпоинты добавленные в ветке feature_comments:

## Public:

##### Get
###### /comments/event/{eventId} 
получение постраничного списка комментариев к событию.


##### Get
###### /comments/replies/{commentId} 
получение ветки комментария и ответов на него

## Private:

##### Post
###### /users/comments/events/{eventId} 
создание комментария к событию

##### Patch
###### /users/comments/{commentId} 
редактирование комментария создателем

##### Delete
###### /users/comments/{commentId} 
удаление комментария создателем

## Admin:

##### Get
###### /admin/comments/{commentId} 
получение одного комментария по id

##### Get
###### /admin/comments/user/{userId} 
получение постраничног списка всех комментариев созданных пользователем

##### Delete
###### /admin/comments/{commentId} 
удаление одного комментария по id администратором

##### Delete
###### /admin/comments/user/{userId} 
удаление всех комментариев пользователя

##### Delete
###### /admin/comments/events/{eventId} - удаление всех комментариев события

* при удалении комментария вся ветка его ответов также удаляется

# Старые эндпоинты, изменённые в этой ветке:

## Private:

##### Post
###### users/{userId}/events
##### Patch
###### /users/{userId}/events/{eventId}
При создании события можно установить boolean переменную "disableComments"
Если disableComments = true, то комментировать событие нельзя. 
Если у события уже есть комментарии, отключить их нельзя

## Admin:

##### Patch
###### /admin/events/{eventId}
Если у события уже есть комментарии, администратор все равно может их отключить - тогда все комментарии события 
будут удалены

##### Delete
###### /admin/users/{userId}
При удалении пользователя теперь все его комментарии будут удалены