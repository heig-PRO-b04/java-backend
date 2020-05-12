#!/bin/env python3
import requests
import json

server = "http://172.22.22.53:8080"
token = ""
idMod = ""

def register():
    global token
    data = { "username" : username, "password" : password }
    url = server + "/register"
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        token = res.json()["token"]
    else:
        print (res.json()["error"])

def auth():
    global token
    global idMod
    data = { "username" : username, "password" : password }
    url = server + "/auth?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        idMod = res.json()["idModerator"]
        token = res.json()["token"]
    else:
        print("auth rip")

def createPoll(title):
    data = { "title" : title }
    url = server + "/mod/"+ str(idMod) + "/poll?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        idPoll = res.json()["idPoll"]
        return idPoll
    else:
        print(res.json())

def addQuestion(idPoll, title):
    data = { "title" : title, "details" : "This is a comment", "visibility" : "visible",
            "answersMin" : 1, "answersMax" : 10 }
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "/question?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        print("Added question with title: " + res.json()["title"])
    else:
        print(res.json())

def sessionStatus(idPoll, status):
    data = { "status" : status }
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "/session?token=" + token
    res = requests.put(url, json = data)
    if (res.status_code == 200):
        print(res.json())
        return res.json()["code"]
    else:
        print(res.json())

def connectUser(code):
    data = { "code" : code }
    url = server + "/connect"
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        print(res.json())
        return res.json()["token"]
    else:
        print(res.json())

def getSession(token):
    url = server + "/session?token=" + token
    res = requests.get(url)
    if (res.status_code == 200):
        print(res.json())
        return res.json()
    else:
        print(res.json())


username="guy-laurent"
password="1234"

register()
auth()

print("Registered and authenticated")
print("Token: " + token)
print("idMod: " + str(idMod))
print()
print()

idPoll = createPoll("My poll")
print("Created poll with id " + str(idPoll))

print()
print()
# addQuestion(idPoll, "This is my first question")
# addQuestion(idPoll, "This is my second question")
# addQuestion(idPoll, "This is my third question")

code = sessionStatus(idPoll, "open")
print("Here is the code " + code)

print()
print()

#userToken = connectUser(code)
#session = getSession(userToken)

