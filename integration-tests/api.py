#!/bin/env python3
import requests
import json

server="http://localhost:80"

# Moderator section
def register(username, password):
    data = { "username" : username, "password" : password }
    url = server + "/register"
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return res.json()["token"]
    else:
        return { "error" : res.status_code }

def auth(username, password):
    data = { "username" : username, "password" : password }
    url = server + "/auth?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return { "idMod" : res.json()["idModerator"], "token" : res.json()["token"] }
    else:
        return { "error" : res.status_code }

# User section

def connectUser(code):
    data = { "code" : code }
    url = server + "/connect"
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return res.json()["token"]
    else:
        return { "error" : res.status_code }

# Poll section
def createPoll(token, idMod, title):
    data = { "title" : title }
    url = server + "/mod/"+ str(idMod) + "/poll?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return res.json()["idPoll"]
    else:
        return { "error" : res.status_code }

def sessionStatus(token, idPoll, status):
    data = { "status" : status }
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "/session?token=" + token
    res = requests.put(url, json = data)
    if (res.status_code == 200):
        return res.json()["code"]
    else:
        return { "error" : res.status_code }

def getSession(token):
    url = server + "/session?token=" + token
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

# Question section
def addQuestion(token, idPoll, title):
    data = { "title" : title, "details" : "This is a comment", "visibility" : "visible",
            "answersMin" : 1, "answersMax" : 10 }
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "/question?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return
    else:
        return { "error" : res.status_code }

