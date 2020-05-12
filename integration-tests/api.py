#!/bin/env python3
import requests
import json

server="http://localhost:80"

def register(username, password):
    data = { "username" : username, "password" : password }
    url = server + "/register"
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return res.json()["token"]
    else:
        print (res.json()["error"])

def auth(username, password):
    data = { "username" : username, "password" : password }
    url = server + "/auth?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return { "idMod" : res.json()["idModerator"], "token" : res.json()["token"] }
    else:
        print("auth rip")

def createPoll(token, idMod, title):
    data = { "title" : title }
    url = server + "/mod/"+ str(idMod) + "/poll?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return res.json()["idPoll"]
    else:
        print(res.json())

def addQuestion(token, idPoll, title):
    data = { "title" : title, "details" : "This is a comment", "visibility" : "visible",
            "answersMin" : 1, "answersMax" : 10 }
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "/question?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        print("Added question with title: " + res.json()["title"])
    else:
        print(res.json())

def sessionStatus(token, idPoll, status):
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

