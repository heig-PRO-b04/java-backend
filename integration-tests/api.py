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
def getPoll(token, idMod):
    url = server + "/mod/"+ str(idMod) + "/poll?token=" + token
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def getPollWithId(token, idMod, idPoll):
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "?token=" + token
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def postPoll(token, idMod, title):
    data = { "title" : title }
    url = server + "/mod/"+ str(idMod) + "/poll?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def putPoll(token, idMod, idPoll, title):
    data = { "title" : title }
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "?token=" + token
    res = requests.put(url, json = data)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def deletePoll(token, idMod, idPoll):
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "?token=" + token
    res = requests.delete(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

# Session section
def getSession(token):
    url = server + "/session?token=" + token
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def getSessionWithId(token, idMod, idPoll):
    url = server + "/mod/" + str(idMod) + "/poll/" + str(idPoll) + "/session?token=" + token
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def putSessionWithId(token, idPoll, status):
    data = { "status" : status }
    url = server + "/mod/" + str(idMod) + "/poll/" + str(idPoll) + "/session?token=" + token
    res = requests.put(url, json = data)
    if (res.status_code == 200):
        return res.json()["code"]
    else:
        return { "error" : res.status_code }

# Question section
def getQuestion(token, idMod, idPoll):
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "/question?token=" + token
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def getQuestionWithId(token, idMod, idPoll, idQuestion):
    url = (server
            + "/mod/"+ str(idMod)
            + "/poll/" + str(idPoll)
            + "/question/" + str(idQuestion)
            + "?token=" + token
            )
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def postQuestion(token, idPoll, title, visibility, index, qmin, qmax):
    data = { "title" : title
           , "details" : "This is a comment"
           , "visibility" : visibility
           , "indexInPoll": index
           , "answersMin" : qmin
           , "answersMax" : qmax
           }
    url = server + "/mod/"+ str(idMod) + "/poll/" + str(idPoll) + "/question?token=" + token
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return
    else:
        return { "error" : res.status_code }

def putQuestionWithId(token, idMod, idPoll, idQuestion, title, visibility, index, qmin, qmax):
    url = (server
            + "/mod/"+ str(idMod)
            + "/poll/" + str(idPoll)
            + "/question/" + str(idQuestion)
            + "?token=" + token
            )
    data = { "title" : title
           , "details" : "This is a comment"
           , "visibility" : visibility
           , "indexInPoll": index
           , "answersMin" : qmin
           , "answersMax" : qmax
           }
    res = requests.put(url, json = data)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def deleteQuestionWithId(token, idMod, idPoll, idQuestion):
    url = (server
            + "/mod/" + str(idMod)
            + "/poll/" + str(idPoll)
            + "/question/" + str(idQuestion)
            + "?token=" + token
            )
    res = requests.delete(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

# Answers section
def getAnswer(token, idMod, idPoll, idQuestion):
    url = (server
            + "/mod/" + str(idMod)
            + "/poll/" + str(idPoll)
            + "/question/" + str(idQuestion)
            + "/answer"
            + "?token=" + token
            )
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def getAnswerWithId(token, idMod, idPoll, idQuestion, idAnswer):
    url = (server
            + "/mod/" + str(idMod)
            + "/poll/" + str(idPoll)
            + "/question/" + str(idQuestion)
            + "/answer/" + str(idAnswer)
            + "?token=" + token
            )
    res = requests.get(url)
    if (res.status_code == 200):
        return res.json()
    else:
        return { "error" : res.status_code }

def postAnswer(token, idMod, idPoll, idQuestion, title, description):
    data = { "title" : title
           , "description" : description
           }
    url = (server
            + "/mod/" + str(idMod)
            + "/poll/" + str(idPoll)
            + "/question/" + str(idQuestion)
            + "/answer/"
            + "?token=" + token
            )
    res = requests.post(url, json = data)
    if (res.status_code == 200):
        return
    else:
        return { "error" : res.status_code }

def putAnswerWithId(token, idMod, idPoll, idQuestion, idAnswer):
    data = { "title" : title
           , "description" : description
           }
    url = (server
            + "/mod/" + str(idMod)
            + "/poll/" + str(idPoll)
            + "/question/" + str(idQuestion)
            + "/answer/" + str(idAnswer)
            + "?token=" + token
            )
    res = requests.put(url, json = data)
    if (res.status_code == 200):
        return
    else:
        return { "error" : res.status_code }

def deleteAnswerWithId(token, idMod, idPoll, idQuestion, idAnswer):
    url = (server
            + "/mod/" + str(idMod)
            + "/poll/" + str(idPoll)
            + "/question/" + str(idQuestion)
            + "/answer/" + str(idAnswer)
            + "?token=" + token
            )
    res = requests.delete(url)
    if (res.status_code == 200):
        return
    else:
        return { "error" : res.status_code }

def vote(token, idMod, idPoll, idQuestion, idAnswer):
    # TODO
    return
