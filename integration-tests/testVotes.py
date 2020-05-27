import api
import sys

password = "1234"
reg = api.register("testVotes", password)

tokenMod = reg["token"]
idMod = reg["idModerator"]

pollTitle = "Test de poll"
poll = api.postPoll(tokenMod, idMod, pollTitle)
idPoll = poll["idPoll"]

q1 = api.postQuestion(tokenMod, idMod, idPoll, "Question 1", "visible", 1, 0, 0)

ans1 = [ "haha" , "Réponse 2" ]
api.postAnswer(tokenMod, idMod, idPoll, q1["idQuestion"], ans1[0], "")
api.postAnswer(tokenMod, idMod, idPoll, q1["idQuestion"], ans1[1], "")

code = api.putSessionWithId(tokenMod, idMod, idPoll, "open")["code"]

# Switch to user view

tokenUser = api.connectUser(code)["token"]

session = api.getSession(tokenUser)
user_idMod = session["idModerator"]
user_idPoll = session["idPoll"]

all_questions = api.getQuestion(tokenUser, user_idMod, user_idPoll)

idQuestion1 = all_questions[0]["idQuestion"]

answer1 = api.getAnswer(tokenUser, user_idMod, user_idPoll, idQuestion1)[0]

vote1 = api.vote(tokenUser, user_idMod, user_idPoll, idQuestion1, answer1["idAnswer"], True)

if vote1["code"] != 200:
    print("[FAILED] Should have received a 200 response")
    sys.exit(1)

api.putQuestionWithId(tokenMod, idMod, idPoll, idQuestion1, "Question 1", "hidden", 1, 0, 0)
vote2 = api.vote(tokenUser, user_idMod, user_idPoll, idQuestion1, answer1["idAnswer"], True)

if vote2["code"] != 404:
    print("[FAILED] Should have received a 404 response")
    sys.exit(1)

api.putQuestionWithId(tokenMod, idMod, idPoll, idQuestion1, "Question 1", "archived", 1, 0, 0)
vote2 = api.vote(tokenUser, user_idMod, user_idPoll, idQuestion1, answer1["idAnswer"], True)

if vote2["code"] != 404:
    print("[FAILED] Should have received a 404 response")
    sys.exit(1)

api.account_delete(tokenMod, idMod, password)
